def print_text(text, encoding_type):
    byte_data = text.encode(encoding_type)
    hex_data_as_str = " ".join(f"{hex(c)}" for c in byte_data)
    int_data_as_str = " ".join(f"{int(c)}" for c in byte_data)

    print("'" + text + f"' 문자열 길이: {len(text)}")
    print(
        "'"
        + text
        + f"' 전체 문자를 표현하는 데 사용한 바이트 수: {len(byte_data)} 바이트"
    )
    print("'" + text + f"' 16진수 값: {hex_data_as_str}")
    print("'" + text + f"' 10진수 값: {int_data_as_str}")


print_text("Hello", "euc-kr")
print_text("안녕하세요", "euc-kr")
