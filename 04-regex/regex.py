import re


def find_pattern(pattern, string):
    match = re.findall(pattern, string)
    if not match:
        print("일치하는 데이터가 없습니다")
        return

    print(f"일치하는 데이터를 찾았습니다: {match}")


find_pattern("o", "Hello World")
find_pattern("[a-z]", "Hello World, 1,2,3,4,5")
find_pattern("[A-Z]", "Hello World, 1,2,3,4,5")
find_pattern("[a-zA-Z]", "Hello World, 1,2,3,4,5")
find_pattern("[0-9]", "Hello World, 1,2,3,4,5")
find_pattern("[a-zA-Z0-9]", "Hello World, 1,2,3,4,5")

# 파이썬은 문자열 앞에 r을 붙여 \ 파싱을 피할 수 있다.
find_pattern(r"[\\\[\]]", r"!@#$%^&*()?><\[]")
find_pattern("[\\\\\\[\\]]", "!@#$%^&*()?><\\[]")

find_pattern("[a-z]+", r"Hello World, 1,2,3,4,5, !@#$%^&*()?></\[]")
find_pattern("[a-z0-9,]{3}", r"Hello World, 1,2,3,4,5, !@#$%^&*()?></\[]")
find_pattern("[a-z0-9,]{3,5}", r"Hello World, 1,2,3,4,5, !@#$%^&*()?></\[]")
find_pattern("^[a-zA-Z]{3}", r"Hello World, 1,2,3,4,5, !@#$%^&*()?></\[]")
find_pattern("[^a-z^A-Z^0-9]{3}$", r"Hello World, 1,2,3,4,5, !@#$%^&*()?></\[]")
