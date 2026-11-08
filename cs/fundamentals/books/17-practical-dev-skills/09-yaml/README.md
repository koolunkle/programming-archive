# 09. YAML

> YAML은 사람이 읽고 고치기 쉽도록 만든 데이터 형식이다. 주석을 달 수 있고 같은 값을 재사용할 수 있어서, 데이터 교환보다는 **설정 파일** 에 많이 쓴다.

## 핵심 요약

- YAML은 "YAML Ain't Markup Language"의 약자다. 들여쓰기로 구조를 표현하고, JSON에 없는 **주석** 과 **앵커·별칭**(값 재사용)을 지원한다.
- YAML 1.2는 JSON을 거의 그대로 포함하는 상위 집합이라, 파싱 결과는 JSON과 같은 `dict`/`list` 다.
- 신뢰할 수 없는 YAML은 반드시 **`yaml.safe_load()`** 로 읽는다. 일반 `load` 는 임의의 파이썬 객체를 만들 수 있어 코드 실행 공격에 노출된다.
- `yes`, `no`, `on`, `off` 같은 값은 파서에 따라 불리언으로 해석되므로, 문자열은 **따옴표로 감싸는 습관** 을 들인다.
- 데이터 직렬화에는 JSON이 더 범용적이다. YAML은 주석과 재사용이 필요한 설정 파일에 쓴다.

## YAML 문법

```yaml
number: &num 12345      # &num: 앵커
pi: 3.14
str: 문자열 값
null_key: ~             # null
object:                 # 들여쓰기로 중첩 객체 표현
  str2: 문자열 값2
  object2:
    number2: *num       # *num: 별칭(12345를 참조)
num_array:              # 하이픈으로 배열 요소 구분
  - 1
  - 2
str_array: [one, two]   # JSON처럼 한 줄로도 쓸 수 있다
```

| 요소 | YAML | JSON |
| --- | --- | --- |
| 객체 | 들여쓰기 | `{ }` |
| 배열 | `- 항목` | `[ ]` |
| 문자열 | 따옴표 생략 가능 | 큰따옴표 필수 |
| null | `~`, `null`, 빈 값 | `null` |
| 주석 | `# 주석` | 없음 |

- 들여쓰기는 **스페이스만** 쓴다. 탭은 허용되지 않는다.
- 문자 인코딩은 UTF-8, UTF-16, UTF-32를 지원한다. 그래도 특별한 이유가 없다면 UTF-8로 통일한다([1장](../01-text-encoding/README.md)).

### 주석

- `#` 뒤의 내용은 주석이다. 설정 값마다 의미와 허용 범위를 적어 둘 수 있다는 것이 JSON 대비 가장 큰 장점이다.
- 주석으로 구역을 나누면 긴 설정 파일도 읽기 쉬워진다.

### 앵커와 별칭

- **앵커**(`&이름`)로 값에 이름을 붙이고, **별칭**(`*이름`)으로 그 값을 다른 곳에서 참조한다. 같은 값을 여러 곳에 복사하지 않아도 된다.
- 객체에도 쓸 수 있다. **병합 키** `<<:` 를 쓰면 앵커의 내용을 가져온 뒤 일부만 덮어쓸 수 있다. 개발·QA·운영 환경 설정처럼 대부분 같고 일부만 다를 때 유용하다.

```yaml
definitions:
  default: &default
    min_log_level: info
    app_name: realapp
    secure_mode: false
  configurations:
    dev:
      <<: *default              # default의 값을 모두 가져온 뒤
      min_log_level: verbose    # 이 값만 덮어쓴다
      server_url: http://dev.realapp.com
    production:
      <<: *default
      min_log_level: warning
      secure_mode: true
      server_url: https://www.realapp.com
```

> **참고** 병합 키(`<<`)는 YAML 1.1의 기능으로 PyYAML 등 주요 파서가 지원하지만, YAML 1.2 핵심 규격에는 포함되지 않는다. 사용하는 파서가 지원하는지 확인한다.

## YAML 읽고 쓰기

```python
import yaml

with open("message1.yaml", encoding="utf-8") as file:
    data = yaml.safe_load(file)        # JSON을 읽을 때와 같은 dict

with open("message2.yaml", "w", encoding="utf-8") as file:
    yaml.dump(data, file, allow_unicode=True)  # 한글을 그대로 저장
```

- 읽은 결과는 `dict` 와 `list` 이므로, 사용법은 JSON과 같다([8장](../08-json/README.md)).
- 파일은 텍스트 모드로 열고 `encoding="utf-8"` 을 명시한다. 바이너리 모드(`"wb"`)로 쓴다면 문자열을 바이트로 **인코딩** 해서 써야 한다.
- 저장할 때 키 순서는 라이브러리와 옵션에 따라 달라질 수 있다(PyYAML은 기본으로 키를 정렬한다. `sort_keys=False` 로 끌 수 있다).

## 실무에서 주의할 점

### 안전하게 읽기

- `yaml.load(file, Loader=yaml.Loader)` 는 `!!python/object` 같은 태그로 임의의 파이썬 객체를 만들 수 있다. 공격자가 만든 YAML을 읽으면 서버에서 코드가 실행될 수 있다.
- 항상 `yaml.safe_load()`(또는 `Loader=yaml.SafeLoader`)를 쓴다.

### 암시적 타입 변환

- 따옴표가 없는 값은 파서가 타입을 추측한다. PyYAML(YAML 1.1 규칙)에서 생기는 대표적인 예다.

| YAML 값 | 의도 | 실제 해석 |
| --- | --- | --- |
| `country: NO` | 노르웨이 국가 코드 | `False` |
| `answer: yes` | 문자열 | `True` |
| `version: 1.10` | 버전 문자열 | 실수 `1.1` |
| `mode: 0700` | 문자열 `"0700"` | 8진수로 해석된 정수 `448` |

- 문자열로 써야 하는 값은 `"NO"`, `"1.10"` 처럼 따옴표로 감싼다.

### 설정 파일 관리

- 키 이름에 오타가 있어도 YAML은 오류를 내지 않는다. 예를 들어 운영 환경에서 `secure_mode` 를 `sercure_mode` 로 잘못 쓰면, 병합된 기본값 `false` 가 그대로 남아 보안 모드가 꺼진 채 배포된다. 설정을 읽은 뒤 **스키마로 검증**(필수 키, 허용 값, 알 수 없는 키 거부)한다.
- 비밀번호, API 키, 솔트 같은 **비밀 값은 YAML 파일과 저장소에 넣지 않는다.** 환경 변수나 비밀 관리 서비스(Vault, AWS Secrets Manager 등)에서 실행할 때 주입한다.
- 운영 서버와 개발 서버는 포트처럼 같아도 되는 설정과 도메인, DB 주소처럼 달라야 하는 설정을 구분해 관리한다. 설정 간의 의존 관계를 먼저 그려 보고 파일 구조를 정하면 좋다.

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `message1.yaml` | 8장 `message1.json` 과 같은 내용을 YAML로 표현(앵커·별칭 포함) |
| `yaml_reader.py` | YAML을 읽어 숫자, 문자열, null, 객체, 배열 값 출력 |
| `yaml_writer.py` | `dict` 를 `message2.yaml` 로 저장 |
| `realapp_config.yaml` | 객체 앵커로 환경별 설정 공유 |
| `realapp_config2.yaml` | 병합 키(`<<`)로 공통 설정을 상속하고 일부 덮어쓰기 |
| `open_yaml_file.py` | `SafeLoader` 로 설정 파일을 읽어 병합 결과 확인 |

```sh
cd 09-yaml
../.venv/Scripts/python.exe open_yaml_file.py
```

## 더 알아보기

- `ruamel.yaml`: 주석과 키 순서를 보존한 채 YAML을 수정할 수 있는 라이브러리. 설정 파일을 프로그램으로 고칠 때 유용하다.
- 쿠버네티스, GitHub Actions, Docker Compose 등 많은 도구가 YAML 설정을 쓴다. 들여쓰기 실수를 막으려면 `yamllint` 나 편집기 스키마 검증을 활용한다.

---

[← 이전: 08. JSON](../08-json/README.md) · [목차](../README.md) · [다음: 10. XML →](../10-xml/README.md)
