import re


def split_with_regex(pattern, string):
    result = re.split(pattern, string)
    if not result:
        print("일치하는 데이터가 없습니다.")
        return

    print(f"일치하는 데이터를 찾았습니다: {result}")


split_with_regex("@", "gigone.lee@gmail.com")
