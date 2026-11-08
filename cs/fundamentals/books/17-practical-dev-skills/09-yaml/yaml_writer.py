import yaml

message2 = {
    "number": 12345,
    "pi": 3.14,
    "str": "문자열 값",
    "null_key": None,
    "object": {"str2": "문자열 값2", "object2": {"number2": 12345}},
    "num_array": [1, 2, 3, 4, 5],
    "str_array": ["one", "two", "three", "four", "five"],
}

# ensure_ascii=True 인 경우에는 아스키 코드가 아닌 모든 문자열을 \uXXXX 로 표기한다.
with open("message2.yaml", "w", encoding="UTF-8") as file:
    yaml.dump(message2, file, allow_unicode=True)
