import time
import uuid

now = str(int(time.time()))
print(f"현재 시간(Unix epoch time): {now}")

uuid_str = str(uuid.uuid4())
print(f"생성된 UUID={uuid_str}")

new_uuid_str = now[0:8] + "-" + now[8:10] + "00-" + uuid_str[14:]
print(f"새로 만든 UUID={new_uuid_str}")
