# 02. 다국어 처리(i18n)

> 화면에 보여줄 문장을 코드에서 분리하면, 코드를 고치지 않고도 사용자의 언어에 맞는 문장을 보여줄 수 있다.

## 핵심 요약

- **i18n** 은 internationalization의 첫 글자 i와 마지막 글자 n 사이에 18글자가 있다는 뜻의 약어로, 프로그램이 여러 언어를 지원하도록 만드는 작업이다.
- 코드에는 원문(`msgid`)만 두고, 언어별 번역문은 별도 파일(PO → MO)로 관리한다. 파이썬은 표준 라이브러리 `gettext` 를 사용한다.
- 원문(`msgid`)에는 `TEST_MESSAGE_1` 같은 식별자보다 **영어 문장** 을 쓴다. 번역이 없는 언어에서는 원문이 그대로 출력되기 때문이다.
- 언어를 무엇으로 결정할지(OS 설정, 브라우저 `Accept-Language`, 사용자 설정) **기준을 먼저 정한다.**

## gettext 동작 방식

```text
코드(_("...") 표시) ──xgettext──> PO 파일(번역 작성) ──msgfmt──> MO 파일 ──gettext──> 실행 시 번역문 출력
```

| 파일 | 형식 | 역할 |
| --- | --- | --- |
| `.py` | 소스 코드 | 번역할 문장을 `_("...")` 로 감싼다 |
| `.po` | 텍스트 | `msgid`(원문)와 `msgstr`(번역문) 쌍. 사람이 편집한다 |
| `.mo` | 바이너리 | PO를 컴파일한 파일. 프로그램이 실행 중에 읽는다 |

## 1단계: 코드에 번역할 문장 표시

```python
import gettext

translation_ko = gettext.translation(
    domain="i18n_test", localedir="./locale", languages=["ko_KR"]
)
_ = translation_ko.gettext

print(_("Test message 1"))
```

- `gettext.translation()` 은 `<localedir>/<language>/LC_MESSAGES/<domain>.mo` 파일을 찾는다. 위 코드는 `./locale/ko_KR/LC_MESSAGES/i18n_test.mo` 를 읽는다.
  - `domain`: MO 파일 이름. 보통 프로젝트 이름을 쓴다.
  - `localedir`: MO 파일들이 있는 최상위 디렉터리.
  - `languages`: 찾을 언어 코드 목록. **앞에서부터 차례로 찾아 처음 발견한 파일을 사용한다.** 하나도 없으면 예외가 발생하므로, 원문을 대신 출력하려면 `fallback=True` 를 지정한다.
- `_` 로 감싼 문장만 번역 대상으로 추출된다. `translation_ko.install()` 을 호출하면 `_` 함수가 전역(builtins)에 등록되어 모든 모듈에서 쓸 수 있다.

## 2단계: PO 파일 만들기

```sh
# 윈도우(gettext-iconv 설치 시)와 맥/리눅스 모두 같은 명령을 사용한다.
xgettext -d i18n_test_ko i18n_test.py        # i18n_test_ko.po 생성
xgettext -j -o i18n_test_ko.po i18n_test.py   # 코드가 바뀌었을 때 기존 PO에 병합
```

- 생성된 PO 파일에서 다음을 채운다.
  - 헤더의 `Content-Type: text/plain; charset=UTF-8` — 문자 인코딩은 UTF-8로 지정한다([1장](../01-text-encoding/README.md)).
  - 각 항목의 `msgstr` 에 번역문을 쓴다. `msgid` 는 코드의 원문이므로 PO 파일에서 수정하지 않는다. 원문을 바꾸려면 코드를 고친 뒤 다시 추출한다.
  - 번역을 마치면 헤더의 `#, fuzzy` 표시를 지운다. fuzzy로 표시된 항목은 `msgfmt` 가 컴파일에서 제외할 수 있다.

```po
#: i18n_test.py:11
msgid "Test message 1"
msgstr "한국어 메시지 1"
```

> **참고** 맥은 `brew install gettext`, 윈도우는 gettext-iconv 같은 배포판을 설치하면 `xgettext`, `msgfmt` 를 쓸 수 있다. 파이썬 패키지 `Babel`(`pybabel extract/compile`)로도 같은 작업을 할 수 있다.

## 3단계: MO 파일 생성과 실행

```sh
msgfmt i18n_test_ko.po -o locale/ko_KR/LC_MESSAGES/i18n_test.mo
```

- `msgfmt` 는 출력 디렉터리를 만들어 주지 않으므로 `locale/ko_KR/LC_MESSAGES` 를 먼저 만든다.
- `localedir` 와 `domain` 은 바꿀 수 있지만, 언어 코드 폴더와 `LC_MESSAGES` 라는 이름은 정해진 규칙이다.

## 여러 언어 지원

- 언어마다 PO/MO 파일을 만든다(중국어 `zh_CN`, 일본어 `ja_JP` 등).

```text
locale/
├── ko_KR/LC_MESSAGES/i18n_test.mo
├── zh_CN/LC_MESSAGES/i18n_test.mo
└── ja_JP/LC_MESSAGES/i18n_test.mo
```

- 실행 중에 언어를 바꿔야 한다면 언어별 `translation` 객체를 만들어 두고, 설정이 바뀔 때 해당 객체의 `gettext`(또는 `install()`)로 교체한다.
- 웹 서비스는 요청마다 사용자가 다르므로, 전역 `_` 대신 요청의 `Accept-Language` 헤더나 사용자 설정에 맞는 `translation` 을 골라 쓴다.

## 실무에서 정해야 할 기준

- **언어 결정 기준**: OS 언어, 브라우저 `Accept-Language`, 접속 IP의 국가, 사용자가 직접 고른 설정 중 무엇을 우선할지 정한다. 보통 사용자 설정 → `Accept-Language` → 기본 언어 순으로 쓴다.
- **메시지 관리**: 더 이상 쓰지 않는 문장과 번역이 빠진 문장을 주기적으로 찾아 정리한다. 문장은 코드와 함께 추가·삭제되는 것이 이상적이다.
- **일관성**: 같은 의미의 문장을 여러 번 만들지 않고 재사용한다. 단, 단어를 이어 붙여 문장을 만들면 언어마다 어순이 달라 어색해지므로 문장 단위로 번역한다.
- **레이아웃**: 언어마다 길이가 다르다. 독일어처럼 긴 언어로 바꾸면 버튼이나 폼이 깨질 수 있다.

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `i18n_test.py` | 한국어 MO 파일을 불러와 번역문 출력 |
| `i18n_multilang.py` | 한국어·중국어·일본어 `translation` 객체를 만들고 골라서 사용 |
| `i18n_test_ko.po` 외 | 언어별 PO 파일(번역 원본) |
| `locale/` | 언어별로 컴파일된 MO 파일 |

```sh
cd 02-i18n
../.venv/Scripts/python.exe i18n_multilang.py
```

## 더 알아보기

- 언어 외에도 날짜 표기, 통화 기호, 숫자 구분 기호, 길이·무게 단위가 나라마다 다르다. 이를 함께 다루는 작업을 **l10n(localization)** 이라고 한다. 파이썬은 `Babel` 패키지로 다룰 수 있다.
- 단수/복수 형태가 다른 문장은 `ngettext` 를 사용한다.
- 안드로이드(`strings.xml`)와 iOS(`Localizable.strings`)도 같은 원리로 다국어를 지원한다.

---

[← 이전: 01. 문자열 인코딩](../01-text-encoding/README.md) · [목차](../README.md) · [다음: 03. 날짜와 시간 →](../03-time-and-date/README.md)
