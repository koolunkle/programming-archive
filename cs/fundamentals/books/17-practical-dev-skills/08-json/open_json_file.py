import json


def open_json_file(filename):
    try:
        with open(filename, encoding="UTF-8") as file:
            return json.load(file)
    except FileNotFoundError as e:
        print(f"JSON 데이터를 파싱하는 데 실패했습니다. 사유={e}")
        return None


# message1.json 파일은 같은 디렉터리에 있어야 한다.
json_data = open_json_file("message11.json")

if json_data:
    print(json_data)
