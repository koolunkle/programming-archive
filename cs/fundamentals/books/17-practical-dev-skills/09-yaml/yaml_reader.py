import sys

import yaml
import yaml.parser


def open_yaml_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return yaml.load(file, Loader=yaml.SafeLoader)
    except yaml.parser.ParserError as e:
        print(f"YAML 데이터를 파싱하는 데 실패했습니다. 사유: {e}")
        return None


# message1.yaml 파일은 같은 디렉터리에 있어야 한다.
yaml_data = open_yaml_file("message1.yaml")

if not yaml_data:
    sys.exit(0)

# 정수
num_value = yaml_data["number"]

# 실수
float_value = yaml_data["pi"]

# 문자열
str_value = yaml_data["str"]

# 빈 키
empty_value = yaml_data["null_key"]

print(f"num_value={num_value}")
print(f"float_value={float_value}")
print(f"str_value={str_value}")
print(f"empty_value={empty_value}")

# 객체 안 객체 접근
yaml_data2 = yaml_data["object"]
print(f'yaml_data["object"]["str2"]={yaml_data2["str2"]}')

# 배열 접근
yaml_array = yaml_data["num_array"]
for n in yaml_array:
    print(f"n={n}")
