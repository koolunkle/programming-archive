import re

import bitstring
from bitstring import BitArray


def open_json_file(filename):
    with open(filename, mode="rb") as file:
        return file.read()


data = open_json_file("../08-json/message1.json")
bit_str = BitArray(data).bin  # 비트 코드를 문자열로 변환한다.

print(bit_str)

pad_count = 0

while len(bit_str) % 24 != 0:
    bit_str += "00000000"
    pad_count += 1

b64_str = ""
b64_chs = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
bit_list = re.findall(R"(\d{6})", bit_str)  # 6개씩 비트 문자열을 쪼갠다.

if pad_count > 0:
    # 마지막에 추가된 0은 "A"가 아니라 "=" 이어야 한다.
    # 그래서 여기서 제외한 후 나중에 다시 추가한다.
    bit_list = bit_list[:-pad_count]

for bit in bit_list:
    v = int(bit, 2)
    b64_str += b64_chs[v]

b64_str += "=" * pad_count

print("=========================")
print(f"my_base64={b64_str}")

bit_str = ""

for ch in b64_str:
    # 패딩 문자를 제외한 모든 문자를 살펴본다.
    if ch in b64_chs:
        # 6자리 비트 문자열로 전환한 후 버퍼에 그대로 넣는다.
        bit_str += format(b64_chs.index(ch), "06b")

# 패딩 때문에 채웠던 0 비트(8비트 미만)는 원본 데이터가 아니므로 버린다.
bit_str = bit_str[: len(bit_str) // 8 * 8]

with open("message2.json", "wb") as file:
    # 비트 문자열을 바이트(8비트)로 변환한 후 비트로 바꾸고 파일에 쓴다.
    file.write(bytes(int(bit_str[i : i + 8], 2) for i in range(0, len(bit_str), 8)))
