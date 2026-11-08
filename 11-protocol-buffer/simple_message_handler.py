import json
import uuid

import simple_message_pb2
from google.protobuf import json_format, text_format


def create_new_msg():
    new_msg = simple_message_pb2.SimpleMessage()  # type: ignore

    new_msg.name = "문자열"
    new_msg.num64 = 12345
    new_msg.float64 = 12345.6

    new_uuid = uuid.uuid4()
    print(f"new_uuid={new_uuid}")
    new_msg.uuid = new_uuid.bytes

    # enum type
    new_msg.type = simple_message_pb2.SimpleMessage.Ping  # type: ignore

    # number list
    new_msg.num64_list.append(1)
    new_msg.num64_list.append(2)

    # string list
    new_msg.name_list.append("one")
    new_msg.name_list.append("two")

    # map
    new_msg.map_field["key1"] = "value1"
    new_msg.map_field["key2"] = "value2"

    # another message
    new_msg.another_msg.name = "문자열2"
    new_msg.another_msg.num64 = 56789

    for i in range(5):
        another_msg2 = simple_message_pb2.AnotherMessage()  # type: ignore
        another_msg2.name = f"문자열-{i}"
        another_msg2.num64 = i
        new_msg.another_msg2.append(another_msg2)

    return new_msg


simple_message = create_new_msg()

print("--------------------------------------------------")

# 빈 문자열은 ' ', 정수나 실수는 0으로 표기된다.
print(f"name={simple_message.name}")
print(f"num64={simple_message.num64}")
print(f"float64={simple_message.float64}")
print(f"uuid={uuid.UUID(bytes=simple_message.uuid)!s}")

# index = 0
# for num64 in simple_message.num64_list:
#     print(f"num64_list[{index}].num64={num64}")
#     index += 1

for index, num64 in enumerate(simple_message.num64_list):
    print(f"num64_list[{index}].num64={num64}")

for index, name in enumerate(simple_message.name_list):
    print(f"name_list[{index}].num64={name}")

print(f"type={simple_message.type}")

for key in simple_message.map_field:
    print(f"map_field[{key}]={simple_message.map_field[key]}")

another_msg = simple_message.another_msg
print(f"another_msg.name={another_msg.name}")
print(f"another_msg.num64={another_msg.num64}")

for index, msg2 in enumerate(simple_message.another_msg2):
    print(f"another_msg[{index}].name={msg2.name}, num64={msg2.num64}")

# UTF-8 텍스트 변환
print("===============UTF-8 텍스트 변환===============")
text_message = text_format.MessageToString(simple_message, as_utf8=True)
print(text_message)

# JSON 객체 변환
print("===============JSON 객체 변환===============")
json_str = json_format.MessageToJson(simple_message)
print(json.loads(json_str))
