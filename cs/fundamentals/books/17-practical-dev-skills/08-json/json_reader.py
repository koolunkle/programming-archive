import json
import sys


def open_json_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return json.load(file)
    except FileNotFoundError as e:
        print(f"JSON 데이터를 파싱하는 데 실패했습니다. 사유={e}")
        return None


# message1.json 파일은 같은 디렉터리에 있어야 한다.
json_data = open_json_file("message1.json")

if not json_data:
    # 더 이상 로직을 진행할 수 없으므로 종료한다.
    sys.exit(0)

# 정수
num_value = json_data["number"]

# 실수
float_value = json_data["pi"]

# 문자열
str_value = json_data["str"]

# 빈 키(None)
empty_value = json_data["null_key"]

print(f"num_value={num_value}")
print(f"float_value={float_value}")
print(f"str_value={str_value}")
print(f"empty_value={empty_value}")

# 객체 안 객체 접근
json_data2 = json_data["object"]
print(f'json_data["object"]["str2"]={json_data2["str2"]}')

# 배열 접근
json_array = json_data["num_array"]

for n in json_array:
    print(f"n={n}")

# 존재하지 않는 키에 접근
# unknown_value = json_data["unknown_key"]
# print(f"unknown_value={unknown_value}")

# 존재하지 않는 키에 접근할 때 발생하는 예외 제어
try:
    unknown_value = json_data["unknown_key"]
    print(f"unknown_value={unknown_value}")
except KeyError:
    print('"unknown_key"는 존재하지 않습니다')

# 존재하지 않는 키에 접근 시 키 존재 여부 검사
if "unknown_key" in json_data:
    unknown_value = json_data["unknown_key"]
    print(f"unknown_value={unknown_value}")
else:
    print('"unknown_key"는 존재하지 않습니다')

# float_value가 3 이상 3.2 미만인지 검사하는 코드
assert 3 <= float_value <= 3.2

# str_value가 null이 아니고 문자열 길이가 0 이상인지 검사하는 코드
assert str_value and len(str_value) > 0
