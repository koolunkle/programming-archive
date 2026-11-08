# 13. 데이터 압축(zlib)

> 압축은 저장 공간과 네트워크 트래픽을 줄여 준다. 대신 CPU를 쓴다. zip 파일과 웹의 gzip 응답이 쓰는 DEFLATE 알고리즘과, 이를 쉽게 쓰게 해 주는 zlib 라이브러리를 다룬다.

## 핵심 요약

- zip 파일과 HTTP gzip은 **DEFLATE** 알고리즘으로 압축한다. **zlib** 은 DEFLATE 압축과 해제(INFLATE)를 제공하는 사실상 표준 라이브러리다.
- 텍스트·JSON처럼 반복이 많은 데이터는 잘 줄어든다. 이미 압축된 데이터(jpg, mp4, zip)나 암호화된 데이터는 거의 줄지 않는다.
- 압축 레벨은 특별한 이유가 없으면 **기본값(6)** 을 쓴다. 작은 데이터(약 1KB 미만)는 압축하지 않는다.
- 외부에서 받은 압축 데이터를 풀 때는 **해제 후 최대 크기를 제한** 한다(압축 폭탄 방지).

## zip, zlib, DEFLATE, INFLATE

| 이름 | 종류 | 설명 |
| --- | --- | --- |
| DEFLATE | 알고리즘(RFC 1951) | LZ77과 허프만 코딩을 결합한 무손실 압축 알고리즘 |
| INFLATE | 알고리즘 | DEFLATE로 압축한 데이터를 푸는 과정 |
| zlib | 라이브러리 / 형식(RFC 1950) | DEFLATE를 구현한 무료 라이브러리. DEFLATE 데이터에 2바이트 헤더와 Adler-32 체크섬을 붙인 형식의 이름이기도 하다 |
| gzip | 파일 형식(RFC 1952) | DEFLATE 데이터 + 파일 이름·시간 헤더 + CRC32. `.gz` 파일과 HTTP 압축에 쓴다 |
| zip | 아카이브 형식 | 여러 파일을 묶는 형식. 파일마다 주로 DEFLATE로 압축한다 |

- zlib 자체는 표준 기관이 정한 규격은 아니다. 그러나 상용 프로그램에서도 무료로 쓸 수 있고 C, 자바, 파이썬 등 거의 모든 환경에 들어 있어 사실상 표준이 되었다.
- 웹 브라우저와 Nginx 같은 웹 서버는 HTTP 응답과 웹소켓 메시지를 자동으로 압축하고 푼다. 애플리케이션이 직접 다룰 일은 생각보다 적다.

## 압축 원리

- 압축은 데이터에서 반복되는 부분을 찾아 더 짧은 표현으로 바꾸는 것이다. 예를 들어 `AAAAABBBCC` 는 "A 5개, B 3개, C 2개"라는 뜻으로 `A5B3C2` 라고만 적어도 원래대로 되돌릴 수 있다.
- DEFLATE는 두 가지 기법을 함께 쓴다.
  - **LZ77**: 앞에서 나온 문자열이 다시 나오면, 문자열 대신 "몇 바이트 앞에서 몇 바이트만큼"이라는 (거리, 길이) 정보로 기록한다.
  - **허프만 코딩**: 자주 나오는 기호에는 짧은 비트를, 드물게 나오는 기호에는 긴 비트를 배정한다.
- DEFLATE는 원본을 완전히 되돌릴 수 있는 **무손실 압축** 이다. JPEG, MP3처럼 사람이 알아채기 어려운 정보를 버려 크기를 줄이는 방식은 **손실 압축** 이라고 한다.

## 압축할 때 고려할 점

### 압축 레벨

- `0`(압축 안 함)부터 `9` 까지 지정한다. `1` 은 가장 빠르지만 덜 줄어들고, `9` 는 가장 많이 줄지만 느리다. 기본값 `-1` 은 레벨 `6` 과 같다.
- 레벨을 높여도 크기는 조금밖에 줄지 않는데 CPU 사용량은 크게 늘 수 있다. 실시간으로 메시지를 주고받는 서버는 낮은 레벨을, 한 번 압축해서 오래 보관하는 파일은 높은 레벨을 고려한다.

### 데이터 크기와 종류

- 압축 결과에는 헤더와 체크섬이 붙는다. 수십 바이트짜리 데이터는 압축하면 오히려 커질 수 있다. 그래서 일정 크기 이상만 압축하는 기준을 둔다(Nginx의 `gzip_min_length` 설정).
- 이미 압축된 형식이나 암호화된 데이터, 난수는 반복 패턴이 없어 압축 효과가 없다. 이런 데이터는 CPU만 낭비하므로 압축하지 않는다.

### CPU와 메모리

- 메시지 하나를 압축하는 부담은 작아도, 초당 수만 건을 처리하는 서버에서는 무시할 수 없는 비용이 된다. 적용 전에 서버 한 대가 감당할 수 있는 처리량을 측정한다.
- 압축된 데이터만 보고는 풀었을 때의 크기를 알 수 없다. 몇 KB짜리 데이터가 수 GB로 풀리도록 만든 **압축 폭탄(zip bomb)** 으로 서버 메모리를 고갈시키는 공격이 있다.

```python
d = zlib.decompressobj()
data = d.decompress(compressed, 10 * 1024 * 1024)  # 최대 10MB까지만 해제
if not d.eof:  # 끝까지 풀지 못했다면 제한을 넘은 것
    raise ValueError("압축 해제 크기 제한 초과")
```

### 무결성 검증

- zlib 형식에는 원본 데이터의 **Adler-32** 체크섬이, gzip과 zip에는 **CRC32** 체크섬이 들어 있다. `zlib.decompress()` 는 해제하면서 체크섬을 자동으로 확인하고, 맞지 않으면 오류를 낸다.
- TCP나 HTTP처럼 전송 계층이 오류를 잡아 주는 환경이라면 따로 확인할 필요가 거의 없다. UDP처럼 보장이 없는 환경이나, 압축 없이 저장한 데이터의 손상을 확인할 때는 `zlib.crc32()` 로 직접 체크섬을 계산해 함께 저장한다.

> **참고** CRC32와 Adler-32는 우연한 손상을 찾기 위한 체크섬이다. 누군가 의도적으로 바꾼 데이터는 막지 못한다. 위변조를 막아야 한다면 SHA-256이나 HMAC을 쓴다([7장](../07-hash/README.md)).

## 압축과 해제

```python
import json
import zlib

json_str = json.dumps(json_object, ensure_ascii=False)
json_byte_data = json_str.encode("utf8")          # 압축은 bytes만 받는다

compressed_data = zlib.compress(json_byte_data, level=-1)
decompressed_data = zlib.decompress(compressed_data)
print(decompressed_data.decode("utf8"))           # 같은 인코딩으로 되돌린다
```

- `zlib.compress()` 는 문자열이 아니라 바이트를 받는다. 문자열은 `encode("utf8")` 로 인코딩한 뒤 압축하고, 해제한 뒤에는 같은 인코딩으로 디코딩한다([1장](../01-text-encoding/README.md)).
- 예제를 실행하면 224바이트였던 `message1.json` 이 156바이트로 약 30% 줄어든다. 데이터가 크고 반복되는 키가 많을수록 효과가 커진다.

```text
'json_str' 데이터 길이=224
압축된 'compressed_json' 데이터 길이=156
압축 해제된 'decompressed_json' 길이=224
```

- 압축한 데이터를 JSON 같은 텍스트 메시지에 넣을 때는 Base64로 인코딩한다([12장](../12-base64/README.md)). 받는 쪽이 버퍼를 미리 잡을 수 있게 원본 크기도 함께 보내면 좋다.

```json
{
    "compressed": true,
    "original_size": 224,
    "data": "eJyrVspLzE1V..."
}
```

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `comp.py` | 8장의 `message1.json` 을 압축하고 압축 전후 길이와 CRC32 출력 |
| `comp_then_decomp.py` | 압축한 데이터를 다시 해제해 원본과 같은지 확인 |

```sh
cd 13-zlib
../.venv/Scripts/python.exe comp_then_decomp.py
```

> **참고** 두 예제는 입력 파일 경로를 윈도우 형식(`..\\08-json\\message1.json`)으로 적었으므로 맥·리눅스에서는 경로를 `../08-json/message1.json` 으로 바꿔야 한다. `pathlib.Path` 를 쓰면 운영체제와 상관없이 동작한다.

## 더 알아보기

- 파이썬 `gzip`, `zipfile`, `tarfile` 모듈: 파일 단위 압축과 아카이브
- **Brotli**(구글)와 **Zstandard(zstd)**(메타): gzip보다 압축률이 좋거나 빠른 알고리즘. 최신 브라우저와 CDN이 지원한다.
- HTTP는 요청의 `Accept-Encoding` 과 응답의 `Content-Encoding` 헤더로 압축 방식을 정한다([14장](../14-http/README.md)).

---

> 이 장의 "압축 원리" 이후 내용은 책 원문이 아니라 목차를 바탕으로 일반 기술 자료와 예제 코드를 참고해 작성했다.

[← 이전: 12. Base64](../12-base64/README.md) · [목차](../README.md) · [다음: 14. HTTP →](../14-http/README.md)
