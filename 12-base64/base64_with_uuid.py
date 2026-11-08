import base64
import uuid

my_uuid = uuid.uuid4()
print(f"원본 UUID={my_uuid!s}, 바이트 길이={len(my_uuid.bytes)}")

b64_encoded_str = base64.b64encode(my_uuid.bytes)
print(
    f"base64 인코딩 문자열={b64_encoded_str.decode("UTF-8")}, 바이트 길이={b64_encoded_str}"
)

decoded_uuid = uuid.UUID(bytes=base64.b64decode(b64_encoded_str))
print(f"base64 디코딩 된 UUID={decoded_uuid}")
