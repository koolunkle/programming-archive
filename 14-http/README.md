# 14. HTTP

> HTTP는 웹 브라우저와 서버가 데이터를 주고받는 프로토콜이다. 지금은 웹 페이지뿐 아니라 모바일 앱, 서버 간 통신 대부분이 HTTP 위에서 동작한다. 이 장부터 17장까지는 웹을 지탱하는 기술을 다룬다.

## 핵심 요약

- HTTP는 클라이언트가 **요청** 을 보내면 서버가 **응답** 하는 텍스트 기반 프로토콜이다. 메서드, 상태 코드, 헤더의 의미를 정확히 아는 것이 웹 개발의 기본이다.
- HTTP는 **무상태** 다. 서버는 이전 요청을 기억하지 않으므로, 로그인 같은 상태는 **쿠키와 세션** 으로 유지한다.
- 세션 ID는 추측할 수 없는 값으로 만들고, 쿠키에는 `HttpOnly`, `Secure`, `SameSite` 속성을 붙인다.
- 서버를 여러 대 운영한다면 스티키 세션보다 **공유 세션 저장소**(Redis 등)나 토큰 방식을 쓴다.
- 실제 서비스에서는 애플리케이션 서버 앞에 **Nginx** 같은 웹 서버를 두고 HTTPS, 압축, 정적 파일을 맡긴다.

## HTTP 개요

- HTTP는 TCP 위에서 동작한다. 요청과 응답이 사람이 읽을 수 있는 문자열이라 디버깅하기 쉽다.

| 버전 | 특징 |
| --- | --- |
| HTTP/1.1 | 지금도 널리 쓰는 기본 버전. 연결을 재사용하는 Keep-Alive가 기본 |
| HTTP/2 | 메시지를 바이너리 프레임으로 나누고, 한 연결에서 여러 요청을 동시에 처리(멀티플렉싱). 헤더 압축 |
| HTTP/3 | TCP 대신 UDP 기반 QUIC을 사용해 연결 수립을 빠르게 하고 패킷 손실의 영향을 줄임 |

- 버전이 바뀌어도 메서드, 상태 코드, 헤더의 의미는 같다. HTTP/1.1을 기준으로 이해하면 충분하다.

> **참고** TCP 소켓으로 직접 통신하면 HTTP보다 가벼울 수 있다. 하지만 한 연결에서 여러 요청을 주고받을 때 어느 응답이 어느 요청의 것인지 구분할 요청 ID, 타임아웃, 재연결 같은 처리를 모두 직접 만들어야 한다. HTTP는 이런 규칙이 이미 표준으로 정해져 있어 개발 부담이 훨씬 적다.

## 무상태성

- 서버는 각 요청을 독립적으로 처리한다. 프로토콜만으로는 두 요청이 같은 사용자의 것인지 알 수 없다.
- 장점: 어느 서버가 요청을 처리해도 결과가 같으므로 서버를 늘리고 로드 밸런서로 나누기 쉽다.
- 단점: 로그인 상태, 장바구니처럼 이어지는 상태는 쿠키·세션·토큰 같은 별도 방법으로 유지해야 한다.

## 간단한 HTTP 서버

```python
from flask import Flask

app = Flask(__name__)


@app.route("/")
def hello_world():
    return "Hello, World!"


app.run()
```

- 실행 후 브라우저에서 `http://localhost:5000` 에 접속하면 `Hello, World!` 가 보인다. Flask 개발 서버는 기본으로 5000번 포트를 쓴다.
- `@app.route("/")` 처럼 URL 경로와 처리 함수를 연결하는 것을 **라우팅** 이라고 한다.

> **주의** `app.run()` 은 개발용 서버다. 실제 서비스에서는 gunicorn, uWSGI 같은 WSGI 서버로 실행하고, 앞에 Nginx 같은 웹 서버를 둔다.

## HTTP 요청

```text
POST /v1/posts HTTP/1.1                    ← 요청 라인: 메서드, 경로, 버전
Host: localhost:5000                       ← 헤더
User-Agent: Mozilla/5.0 ...
Content-Type: application/json
Content-Length: 41
                                           ← 빈 줄
{"title": "제목", "contents": "내용"}       ← 바디
```

| 메서드 | 의미 | 바디 | 멱등성 |
| --- | --- | --- | --- |
| `GET` | 리소스 조회. 필요한 값은 쿼리 문자열(`?size=10`)로 전달 | 없음 | O |
| `POST` | 리소스 생성, 데이터 전송 | 있음 | X |
| `PUT` | 리소스 전체 교체 | 있음 | O |
| `PATCH` | 리소스 일부 수정 | 있음 | X(설계에 따라 다름) |
| `DELETE` | 리소스 삭제 | 보통 없음 | O |

- **멱등성** 은 같은 요청을 여러 번 보내도 결과가 같다는 뜻이다. 네트워크 오류로 재시도해도 안전한지 판단하는 기준이 된다([15장](../15-restful-api/README.md)).
- 자주 쓰는 요청 헤더: `Host`, `User-Agent`(클라이언트 정보), `Content-Type`(바디 형식), `Accept`(원하는 응답 형식), `Accept-Language`(원하는 언어, [2장](../02-i18n/README.md)), `Accept-Encoding`(지원하는 압축 방식, [13장](../13-zlib/README.md)), `Authorization`(인증 정보), `Cookie`

> **주의** `User-Agent` 를 포함한 모든 요청 헤더와 바디는 클라이언트가 마음대로 바꿔 보낼 수 있다. 통계나 화면 분기에만 쓰고, 보안 판단의 근거로 삼지 않는다.

## HTTP 응답

```text
HTTP/1.1 200 OK                            ← 상태 라인: 버전, 상태 코드, 설명
Content-Type: text/html; charset=utf-8     ← 헤더
Content-Length: 13
Set-Cookie: sessionId=...; Path=/; HttpOnly
                                           ← 빈 줄
Hello, World!                              ← 바디
```

| 상태 코드 | 분류 | 자주 쓰는 코드 |
| --- | --- | --- |
| 1xx | 정보 | `101 Switching Protocols`(웹소켓 전환) |
| 2xx | 성공 | `200 OK`, `201 Created`, `204 No Content` |
| 3xx | 리다이렉션 | `301 Moved Permanently`, `302 Found`, `304 Not Modified` |
| 4xx | 클라이언트 오류 | `400 Bad Request`, `401 Unauthorized`(인증 필요), `403 Forbidden`(권한 없음), `404 Not Found`, `429 Too Many Requests` |
| 5xx | 서버 오류 | `500 Internal Server Error`, `502 Bad Gateway`, `503 Service Unavailable` |

- 자주 쓰는 응답 헤더: `Content-Type`, `Content-Length`, `Cache-Control`(캐시 정책), `Set-Cookie`, `Content-Encoding`(압축 방식), `Location`(리다이렉트·생성된 리소스 주소)
- `Content-Type` 에 `charset=utf-8` 을 명시하면 클라이언트에서 글자가 깨지는 것을 막을 수 있다([1장](../01-text-encoding/README.md)).

## 쿠키와 세션

- **쿠키** 는 서버가 브라우저에 저장하도록 보내는 작은 데이터다. 서버가 `Set-Cookie` 헤더로 보내면 브라우저는 이후 같은 서버에 요청할 때마다 `Cookie` 헤더에 담아 보낸다.
- **세션** 은 사용자 상태를 서버에 저장하고, 브라우저에는 그 상태를 찾을 **세션 ID** 만 쿠키로 주는 방식이다.

```python
@app.route("/")
def hello_world():
    if "sessionId" in request.cookies:
        return f"기존 연결입니다: sessionId={request.cookies['sessionId']}"

    new_session_id = str(uuid.uuid4())
    response = make_response(f"새 연결입니다: sessionId={new_session_id}")
    response.set_cookie("sessionId", new_session_id, max_age=5)  # 5초 뒤 만료
    return response
```

- 세션 ID는 다른 사람이 추측할 수 없어야 한다. UUID 버전 4나 `secrets.token_urlsafe()` 로 만든다([5장](../05-uuid/README.md), [6장](../06-random/README.md)).
- `max_age`(또는 `expires`)를 지정하지 않으면 브라우저를 닫을 때 지워지는 세션 쿠키가 된다.

| 쿠키 속성 | 역할 |
| --- | --- |
| `HttpOnly` | 자바스크립트에서 쿠키를 읽지 못하게 해 XSS 공격으로 세션 ID가 탈취되는 것을 막는다 |
| `Secure` | HTTPS 연결에서만 쿠키를 보낸다([16장](../16-https/README.md)) |
| `SameSite` | 다른 사이트에서 시작된 요청에 쿠키를 보낼지 정해 CSRF 공격을 줄인다(`Lax` 권장) |
| `Max-Age` / `Expires` | 만료 시간 |

- 쿠키는 사용자가 직접 보고 고칠 수 있다. 권한, 금액 같은 중요한 정보를 그대로 넣지 않는다.

## 스티키 세션

- 세션을 각 서버의 메모리에 저장하면, 1번 서버에서 로그인한 사용자의 다음 요청이 2번 서버로 갔을 때 세션을 찾을 수 없다.
- **스티키 세션** 은 로드 밸런서가 같은 사용자의 요청을 항상 같은 서버로 보내는 방식이다. 쿠키나 클라이언트 IP로 서버를 고정한다.
- 구현은 간단하지만 단점이 크다.
  - 특정 서버에 사용자가 몰리면 부하가 고르게 나뉘지 않는다.
  - 서버 한 대가 멈추면 그 서버에 붙어 있던 사용자의 세션이 모두 사라진다.
  - 배포나 서버 증설 때 기존 사용자를 다른 서버로 옮기기 어렵다.
- 그래서 실무에서는 **Redis 같은 공유 세션 저장소** 를 두거나, 서버에 상태를 두지 않는 **토큰**(JWT 등) 방식을 많이 쓴다.

## CORS

- 브라우저는 **동일 출처 정책** 을 따른다. 출처(origin)는 **프로토콜 + 도메인 + 포트** 의 조합이고, 하나라도 다르면 다른 출처다.
  - `https://example.com` 과 `https://api.example.com`, `http://localhost:3000` 과 `http://localhost:5000` 은 서로 다른 출처다.
- 웹 페이지의 자바스크립트가 다른 출처의 API를 호출하면, 서버가 허락하지 않는 한 브라우저가 응답을 쓰지 못하게 막는다.
- **CORS(Cross-Origin Resource Sharing)** 는 서버가 응답 헤더로 허용할 출처를 알려주는 규약이다.

```text
Access-Control-Allow-Origin: https://www.example.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
```

- `PUT`, `DELETE` 나 JSON 바디처럼 단순하지 않은 요청은, 브라우저가 실제 요청 전에 `OPTIONS` 메서드로 **프리플라이트** 요청을 보내 허용 여부를 먼저 확인한다.
- `Access-Control-Allow-Origin: *` 은 모든 출처를 허용한다. 공개 API가 아니라면 허용할 출처를 구체적으로 적는다. 쿠키를 함께 보내는 요청에는 `*` 를 쓸 수 없다.

> **주의** CORS는 브라우저가 지키는 규칙일 뿐 서버를 보호하는 장치가 아니다. `curl` 이나 서버 간 통신에는 적용되지 않으므로 API 인증과 권한 검사는 따로 구현한다.

## 아파치와 Nginx

| 웹 서버 | 구조 | 특징 |
| --- | --- | --- |
| 아파치(Apache HTTP Server) | 요청마다 프로세스나 스레드를 배정 | 오래되고 안정적. `.htaccess` 와 모듈로 기능을 확장하기 쉽다 |
| Nginx | 적은 수의 프로세스가 이벤트 기반으로 많은 연결을 처리 | 동시 접속이 많을 때 메모리를 적게 쓰고 빠르다. 지금 더 많이 쓴다 |

- Flask 같은 애플리케이션 서버를 바로 외부에 노출하지 않고, 앞에 Nginx를 **리버스 프록시** 로 둔다.

```text
클라이언트 ──HTTPS──> Nginx ──HTTP──> 애플리케이션 서버(gunicorn + Flask) 1..N
                       │
                       └─ 정적 파일(이미지, CSS, JS)은 직접 응답
```

- 웹 서버가 HTTPS 인증서, gzip 압축, 정적 파일 캐시, 요청 크기 제한, 로드 밸런싱 같은 공통 기능을 맡고, 애플리케이션은 비즈니스 로직에 집중한다.

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `simple_server.py` | `Hello, World!` 를 응답하는 가장 단순한 서버 |
| `simple_server2.py` | 요청 헤더에서 `User-Agent` 를 읽어 출력 |
| `cookie_and_session.py` | 세션 ID 쿠키가 없으면 새로 발급하고, 있으면 기존 연결로 판단 |
| `cookie_expiration.py` | `max_age=5` 로 5초 뒤 만료되는 쿠키 |

```sh
cd 14-http
../.venv/Scripts/python.exe cookie_and_session.py   # 브라우저에서 http://localhost:5000 접속
```

## 더 알아보기

- 브라우저 개발자 도구의 **네트워크** 탭에서 실제 요청·응답 헤더를 확인해 보면 이해가 빠르다.
- **HTTP 캐시**: `Cache-Control`, `ETag`, `304 Not Modified`
- **웹소켓**: HTTP 연결을 업그레이드(`101 Switching Protocols`)해 양방향 실시간 통신을 한다.
- [MDN HTTP 문서](https://developer.mozilla.org/ko/docs/Web/HTTP): 헤더와 상태 코드의 상세 설명

---

> 이 장은 책 원문이 아니라 목차를 바탕으로 일반 기술 자료와 예제 코드를 참고해 작성했다.

[← 이전: 13. 데이터 압축](../13-zlib/README.md) · [목차](../README.md) · [다음: 15. RESTful API →](../15-restful-api/README.md)
