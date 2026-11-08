# 11. 프로토콜 버퍼

> 프로토콜 버퍼(Protobuf)는 구글이 만든 바이너리 직렬화 형식이다. 메시지 구조를 스키마로 먼저 정의하고, 그 스키마에서 각 언어용 코드를 생성해 사용한다.

## 핵심 요약

- 텍스트 형식(JSON, XML)보다 **메시지가 작고 처리가 빠르다.** 짧은 시간에 많은 요청을 처리해야 하는 게임 서버, 서버 간 통신(gRPC)에 적합하다.
- 사용 순서: **`.proto` 스키마 작성 → `protoc` 로 인터페이스 코드 생성 → 생성된 클래스로 직렬화/역직렬화**.
- 메시지 형식이 코드로 고정되므로 형식이 바뀌면 빌드나 테스트 단계에서 바로 드러난다.
- 호환성의 핵심은 **필드 번호** 다. 한 번 쓴 번호는 바꾸거나 다른 용도로 재사용하지 않는다.
- 바이너리라서 사람이 바로 읽을 수 없지만, **암호화된 것은 아니다.** 보안은 HTTPS 같은 별도 수단으로 지킨다.

## 특징

### 바이너리 형식

- 키 이름 대신 필드 번호와 타입 정보를 짧은 바이트로 저장하고, 정수는 값 크기에 따라 길이가 달라지는 방식(varint)으로 저장한다. 그래서 같은 데이터를 XML이나 JSON보다 훨씬 작게 표현한다.
- 받는 쪽도 같은 스키마로 만든 코드가 있어야 내용을 해석할 수 있다. 디버깅할 때는 텍스트나 JSON으로 변환해 확인한다.
- 직렬화한 데이터를 파일로 저장할 때 정해진 확장자는 없다. `.bin`, `.pb`, `.dat` 등을 쓴다.

### 스키마 기반 코드 생성

```text
simple_message.proto ──protoc──> simple_message_pb2.py ──import──> 프로젝트 코드
      (스키마)                      (인터페이스 코드)
```

- JSON은 형식에 대한 약속이 문서나 사람의 기억에 의존하지만, 프로토콜 버퍼는 스키마가 곧 코드가 된다. 필드 이름을 잘못 쓰면 바로 오류가 난다.
- 같은 스키마로 C++, 자바, C#, Go, 파이썬 등 여러 언어의 코드를 만들 수 있어서, 서로 다른 언어로 만든 서버끼리도 같은 메시지를 주고받을 수 있다.

## 설치

- 파이썬 런타임 라이브러리: `pip install protobuf`(이 프로젝트의 `requirements.txt` 에 포함)
- 컴파일러(`protoc`)는 둘 중 하나를 쓴다.
  - **pip로 설치**(운영체제 공통): `pip install grpcio-tools` 후 `python -m grpc_tools.protoc` 로 실행
  - **바이너리 설치**: [GitHub 릴리스](https://github.com/protocolbuffers/protobuf/releases)에서 `protoc` 를 받거나, 맥은 `brew install protobuf`

> **주의** 생성된 코드에는 생성에 쓴 protobuf 버전이 기록된다(이 프로젝트는 7.35.1). 실행 환경의 `protobuf` 런타임은 이 버전과 같거나 더 높아야 한다. 런타임 버전을 올리지 않은 채 최신 `protoc` 로 다시 생성하면 import할 때 오류가 날 수 있다.

## 스키마 정의

```proto
syntax = "proto3";

message SimpleMessage {
    string name = 1;          // <타입> <필드 이름> = <필드 번호>;
    int64 num64 = 2;
    double float64 = 3;
    bytes uuid = 4;

    enum Type {
        Ping = 0;             // 이넘의 첫 값은 반드시 0
        Urgent = 1;
    }
    Type type = 5;

    repeated string name_list = 6;       // 리스트
    map<string, string> map_field = 8;   // 맵
    AnotherMessage another_msg = 9;      // 다른 메시지를 필드로 사용
}
```

- 파일 첫 줄에 문법 버전을 적는다. 새로 시작한다면 `proto3` 를 쓴다.
- `message` 하나가 생성될 클래스 하나가 된다.

### 필드 번호

- 필드 번호는 직렬화된 데이터에서 필드를 구분하는 식별자다. 필드 이름은 데이터에 저장되지 않는다.
- 한 메시지 안에서 번호가 겹치면 안 된다. 다른 메시지끼리는 같은 번호를 써도 된다.
- 필드 번호 1\~15는 1바이트, 16\~2047은 2바이트로 저장된다. 자주 쓰는 필드에 작은 번호를 준다.

### 이넘

- 첫 번째 값은 반드시 `0` 이며, 값을 설정하지 않았을 때의 기본값이 된다. 그래서 `TYPE_UNSPECIFIED = 0` 처럼 "지정 안 됨"을 0번으로 두는 것이 관례다.
- 한 이넘 안에서는 값이 겹치면 안 된다(`allow_alias` 옵션 제외). 서로 다른 이넘끼리는 같은 값을 써도 된다.

### 리스트와 맵

- 리스트는 타입 앞에 `repeated` 를 붙인다.
- 맵은 `map<키 타입, 값 타입>` 으로 정의한다.

### 기본값과 필드 존재 여부

- 값을 넣지 않은 필드는 기본값을 가진다. 문자열은 `""`, 숫자는 `0`, `bytes` 는 `b""`, `repeated` 와 `map` 은 빈 컨테이너다(`None` 이 아니다).
- proto3에서 숫자·문자열 필드는 "0을 넣었는지"와 "값을 넣지 않았는지"를 구분하지 못한다. 구분이 필요하면 `optional int64 num64 = 2;` 처럼 `optional` 을 붙이고 `HasField("num64")` 로 확인한다.

## 인터페이스 코드 생성과 사용

```sh
cd 11-protocol-buffer
python -m grpc_tools.protoc -I. --python_out=. simple_message.proto   # grpcio-tools 사용
protoc --proto_path=. --python_out=. simple_message.proto              # protoc 바이너리 사용
```

```python
import simple_message_pb2

msg = simple_message_pb2.SimpleMessage()
msg.name = "문자열"
msg.num64_list.append(1)
msg.map_field["key1"] = "value1"

data = msg.SerializeToString()                         # 직렬화(bytes)
restored = simple_message_pb2.SimpleMessage.FromString(data)  # 역직렬화
```

### 텍스트·JSON으로 변환

- 디버깅이나 로그 출력용으로 `text_format.MessageToString(msg, as_utf8=True)` 와 `json_format.MessageToJson(msg)` 을 쓸 수 있다.
- JSON으로 바꾸면 `bytes` 필드는 Base64 문자열이 된다([12장](../12-base64/README.md)).

### oneof

- `oneof` 는 여러 필드 중 **하나만** 값을 가질 수 있게 한다. 요청 종류에 따라 내용이 다른 메시지를 하나의 공통 메시지로 감쌀 때 유용하다.

```proto
message RequestMsg {
  oneof msg {
    Login login = 1;
    Order order = 2;
    Refund refund = 3;
  }
}
```

- 네트워크 모듈은 `WhichOneof("msg")` 로 요청 종류만 확인해 담당 모듈로 넘기고, 로그인 ID나 주문 내용 같은 세부 내용은 알 필요가 없다.
- `oneof` 안의 필드 하나에 값을 넣으면 나머지 필드는 자동으로 지워진다. `refund` 를 설정한 뒤 `order` 를 설정하면 `refund` 는 사라진다(`create_refund()` 예제). 한 메시지에 여러 필드를 차례로 설정하는 코드는 버그의 원인이 되므로 피한다.
- 메시지 타입 자체를 감추고 싶다면 `google.protobuf.Any` 를 쓴다.

## 스키마를 바꿀 때 지킬 규칙

| 변경 | 안전한가 | 설명 |
| --- | --- | --- |
| 새 필드 추가(새 번호) | 안전 | 예전 코드는 모르는 필드를 무시한다 |
| 필드 이름 변경 | 바이너리는 안전 | 데이터에 이름이 없기 때문. 단, JSON 변환 결과와 코드는 바뀐다 |
| 필드 삭제 | 조건부 | 번호와 이름을 `reserved` 로 막아 재사용을 방지한다 |
| 필드 번호 변경 | **위험** | 기존 데이터를 엉뚱한 필드로 해석한다 |
| 필드 타입 변경 | **대부분 위험** | 호환되는 일부 타입(`int32` ↔ `int64` 등)만 가능하다 |

```proto
message SimpleMessage {
    reserved 4;          // 삭제한 uuid 필드의 번호
    reserved "uuid";     // 이름도 재사용 금지
}
```

- 빌드나 테스트 전에 항상 최신 스키마로 코드를 다시 생성하는 스크립트를 두면, 호환성이 깨지는 변경을 일찍 발견할 수 있다.

## 예제 코드

| 파일 | 내용 |
| --- | --- |
| `simple_message.proto` | 기본 타입, 이넘, 리스트, 맵, 중첩 메시지를 정의한 스키마 |
| `simple_message_pb2.py` | 위 스키마로 생성한 인터페이스 코드(직접 수정 금지) |
| `simple_message_handler.py` | 메시지를 만들고 값 읽기, 텍스트·JSON 변환 |
| `oneof_message.proto` | 로그인·주문·환불 요청을 `oneof` 로 감싼 스키마 |
| `oneof_message_pb2.py` | 위 스키마로 생성한 인터페이스 코드 |
| `oneof_message_handler.py` | `WhichOneof` 로 요청 종류를 판별하고 처리 |

```sh
cd 11-protocol-buffer
../.venv/Scripts/python.exe simple_message_handler.py
```

## 더 알아보기

- **gRPC**: 프로토콜 버퍼로 서비스(RPC)와 메시지를 정의하고 HTTP/2로 통신하는 프레임워크. 서버 간 통신에 많이 쓴다.
- **Buf**: 스키마 린트와 호환성이 깨지는 변경을 자동으로 검사하는 도구
- **Editions**: `proto2`/`proto3` 를 대체하는 새로운 문법 버전 체계(`edition = "2023";`)
- 비슷한 바이너리 형식: FlatBuffers(역직렬화 없이 바로 읽기), MessagePack(스키마 없는 바이너리 JSON), Avro

---

[← 이전: 10. XML](../10-xml/README.md) · [목차](../README.md) · [다음: 12. Base64 →](../12-base64/README.md)
