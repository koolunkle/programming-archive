# 유니코드 문자열을 명시하기 위해 u를 붙임
import json

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
with open("message2.json", "w", encoding="UTF-8") as file:
    # json.dump(message2, file, ensure_ascii=False)
    # 들여쓰기 추가
    json.dump(message2, file, ensure_ascii=False, indent=2)
    # 키 정렬까지 필요한 경우
    # json.dump(message2, file, ensure_ascii=False, indent=2, sort_keys=True)
