# 15. RESTful API

> REST는 HTTP를 이용해 리소스를 주고받는 API 설계 원칙이다. 블로그 글을 쓰고, 읽고, 고치고, 지우는 CRUD API를 Flask로 직접 만들면서 설계 방법을 익힌다.

## 핵심 요약

- **리소스는 URL로, 행위는 HTTP 메서드로, 결과는 상태 코드로** 표현한다. URL에는 동사 대신 명사(복수형)를 쓴다.
- `/v1` 처럼 URL에 **버전** 을 넣어 메시지 형식이 바뀌어도 기존 클라이언트가 계속 동작하게 한다.
- 클라이언트가 보낸 값은 **항상 검증** 하고, 상태 코드는 의미에 맞게(`201`, `400`, `404` 등) 쓴다.
- 목록 조회에는 **페이지네이션** 을, 수정과 삭제에는 **권한 검사** 를 넣는다.

## REST 원칙

- **REST(REpresentational State Transfer)** 는 로이 필딩(Roy Fielding)이 2000년 박사 논문에서 정리한 설계 원칙이다. 이 원칙을 잘 따르는 API를 RESTful API라고 한다.

| 구성 | 표현 방법 | 예 |
| --- | --- | --- |
| 리소스(무엇을) | URL 경로 | `/posts`(글 목록), `/posts/1`(1번 글) |
| 행위(어떻게) | HTTP 메서드 | `GET`, `POST`, `PUT`, `PATCH`, `DELETE` |
| 표현(어떤 형태로) | 바디 형식 | JSON(`Content-Type: application/json`) |
| 결과 | 상태 코드 | `200`, `201`, `400`, `404` |

```text
나쁜 예: POST /createPost, GET /getPost?id=1, POST /deletePost/1
좋은 예: POST /posts,      GET /posts/1,       DELETE /posts/1
```

- REST의 주요 제약 조건
  - **클라이언트-서버**: 화면(클라이언트)과 데이터 처리(서버)의 역할을 나눈다.
  - **무상태**: 요청 하나에 처리에 필요한 정보가 모두 들어 있어야 한다([14장](../14-http/README.md)).
  - **캐시 가능**: 응답을 캐시해도 되는지 명시한다.
  - **균일한 인터페이스**: 리소스를 URL로 식별하고 정해진 메서드로 다룬다.
  - **계층화**: 클라이언트는 중간에 프록시나 로드 밸런서가 있는지 몰라도 된다.

## 이 장에서 만드는 API

| 기능 | 메서드와 URL | 요청 바디 | 성공 응답 |
| --- | --- | --- | --- |
| 글쓰기 | `POST /v1/posts` | `{"title", "contents"}` | `200 OK` |
| 글 목록 읽기 | `GET /v1/posts?size=N` | 없음 | `200 OK` + 글 목록 JSON |
| 글 하나 읽기 | `GET /v1/posts/<number>` | 없음 | `200 OK` + 글 JSON |
| 글 업데이트 | `PUT /v1/posts/<number>` | `{"title", "contents"}` | `200 OK` |
| 글 삭제 | `DELETE /v1/posts/<number>` | 없음 | `200 OK` |

```python
bp = Blueprint("v1", __name__, url_prefix="/v1")
...
app = Flask(__name__)
app.register_blueprint(bp)
app.url_map.strict_slashes = False   # /v1/posts 와 /v1/posts/ 를 같은 경로로 처리
```

- `Blueprint` 로 모든 경로 앞에 `/v1` 을 붙인다. 형식이 크게 바뀌면 `/v2` 를 새로 만들고, 기존 클라이언트는 `/v1` 을 계속 쓰게 한다([8장](../08-json/README.md)).

## 글쓰기 API

```text
POST http://localhost:5000/v1/posts
Content-Type: application/json

{"title": "첫 번째 글", "contents": "안녕하세요"}
```

```python
@bp.route("/posts", methods=["POST"])
def write_post():
    request_json = request.get_json()
    title = request_json.get("title", "")
    contents = request_json.get("contents", "")

    if len(title) == 0 or len(contents) == 0:
        return "Bad request", 400

    global post_number
    now = datetime.datetime.now(tz=datetime.timezone.utc).strftime("%Y-%m-%d %H:%M:%S")
    posts[post_number] = BlogPost(title=title, contents=contents, date=now)
    post_number = post_number + 1
    return "OK", 200
```

- 제목이나 내용이 비어 있으면 `400 Bad Request` 를 응답한다.
- 작성 시각은 서버의 시간대 설정에 영향받지 않도록 **UTC** 로 저장한다([3장](../03-time-and-date/README.md)).
- 예제는 메모리의 딕셔너리에 저장하므로 서버를 다시 시작하면 글이 사라진다. 실무에서는 SQLite, MySQL, PostgreSQL 같은 DB에 저장한다.

> **실무 팁** 새 리소스를 만들었다면 `200 OK` 보다 `201 Created` 와 함께 `Location: /v1/posts/1` 헤더나 생성된 글 번호를 돌려주는 것이 RESTful API의 일반적인 방식이다. 클라이언트가 방금 쓴 글을 바로 조회할 수 있다.

## 글 읽기 API

```python
@bp.route("/posts", methods=["GET"])
def get_posts():
    posts_size = int(request.args.get("size", "-1"))   # -1이면 전체
    posts_json = []

    for number, post in posts.items():
        posts_json.append({"title": post.title, "contents": post.contents,
                           "date": post.date, "number": number})
        if 0 <= posts_size <= len(posts_json):
            break

    return json.dumps({"posts": posts_json}, ensure_ascii=False)
```

```text
GET http://localhost:5000/v1/posts?size=10
GET http://localhost:5000/v1/posts/1

{"post": [{"title": "첫 번째 글", "contents": "안녕하세요", "date": "2026-10-09 12:38:18", "number": "1"}]}
```

- `ensure_ascii=False` 를 지정하면 한글이 `\uXXXX` 로 바뀌지 않고 그대로 응답된다([8장](../08-json/README.md)). Flask의 `jsonify()` 를 쓰면 `Content-Type: application/json` 헤더도 자동으로 붙는다.
- 글이 수천 개를 넘으면 한 번에 다 내려주면 안 된다. 개수(`size`)와 함께 `page` 나 마지막으로 받은 글 번호(`cursor`)를 받아 나눠서 조회하는 **페이지네이션** 을 구현한다.
- 없는 글을 요청하면 예제는 `400` 을 응답하지만, 의미상으로는 `404 Not Found` 가 더 정확하다. 상태 코드만 보고도 클라이언트가 오류 원인을 구분할 수 있다.

## 글 업데이트 API

```python
@bp.route("/posts/<number>", methods=["PUT"])
def update_post(number):
    number = int(number)
    if number not in posts:
        return "Bad Request", 400
    ...
    posts[number] = BlogPost(title=title, contents=contents, date=now)  # 전체 덮어쓰기
    return "OK", 200
```

- `PUT` 은 리소스 **전체** 를 새 값으로 바꾼다. 제목만 바꾸려 해도 내용까지 모두 보내야 한다. 일부만 바꾸려면 `PATCH` 를 쓴다.
- `PUT` 은 **멱등** 해야 한다. 같은 요청을 여러 번 보내도 결과가 같으므로, 네트워크 오류로 재시도해도 안전하다. `GET`, `PUT`, `DELETE` 는 멱등하고 `POST` 는 멱등하지 않다(두 번 보내면 글이 두 개 생긴다).
- 실무에서는 수정하기 전에 요청한 사용자가 글 작성자인지 확인하는 **권한 검사** 가 반드시 필요하다([17장](../17-oauth/README.md)).

## 글 삭제 API

```python
@bp.route("/posts/<number>", methods=["DELETE"])
def delete_post(number):
    if int(number) not in posts:
        return "Bad Request", 400
    del posts[int(number)]
    return "OK", 200
```

- 응답 바디가 필요 없다면 `204 No Content` 를 쓰기도 한다.
- 실무에서는 데이터를 실제로 지우지 않고 `deleted_at` 같은 표시만 남기는 **소프트 삭제** 를 많이 쓴다. 실수로 지운 데이터를 복구하거나, 법에 따라 일정 기간 보관해야 하는 데이터를 다룰 수 있다.

## API 테스트

```sh
curl -X POST http://localhost:5000/v1/posts -H "Content-Type: application/json" -d "{\"title\":\"t\",\"contents\":\"c\"}"
curl http://localhost:5000/v1/posts
curl -X PUT http://localhost:5000/v1/posts/1 -H "Content-Type: application/json" -d "{\"title\":\"t2\",\"contents\":\"c2\"}"
curl -X DELETE http://localhost:5000/v1/posts/1
```

- VS Code(REST Client 확장)나 JetBrains IDE에서는 `post.http` 같은 `.http` 파일로 요청을 저장해 두고 바로 보낼 수 있다. Postman 같은 도구도 많이 쓴다.

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `restful_api.py` | 글쓰기·목록·조회·수정·삭제 API(`/v1` Blueprint) |
| `post.http` | IDE에서 바로 보낼 수 있는 요청 예시(1번 글 조회) |

```sh
cd 15-restful-api
../.venv/Scripts/python.exe restful_api.py
```

## 더 알아보기

- **OpenAPI(Swagger)**: API 명세를 작성하고 문서와 클라이언트 코드를 자동으로 만든다. FastAPI는 이를 기본으로 지원한다.
- **GraphQL**: 클라이언트가 필요한 필드만 골라서 요청한다. **gRPC** 는 프로토콜 버퍼([11장](../11-protocol-buffer/README.md))로 서버 간 고성능 통신을 한다.
- **RFC 9457(Problem Details)**: 오류 응답 바디의 표준 형식(`type`, `title`, `status`, `detail`)
- **멱등성 키**: 결제처럼 중복되면 안 되는 `POST` 요청에 `Idempotency-Key` 헤더를 붙여 재시도를 안전하게 만든다.

---

> 이 장은 책 원문이 아니라 목차를 바탕으로 일반 기술 자료와 예제 코드를 참고해 작성했다.

[← 이전: 14. HTTP](../14-http/README.md) · [목차](../README.md) · [다음: 16. HTTPS →](../16-https/README.md)
