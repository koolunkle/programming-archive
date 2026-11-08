import json
import zlib


def open_json_file(filename):
    try:
        with open(filename, encoding="UTF8") as file:
            return json.load(file)
    except ValueError as e:
        print(f"JSON 데이터를 파싱하는 데 실패했습니다. 사유={e}")
        return None


json_object = open_json_file("..\\08-json\\message1.json")
json_str = json.dumps(json_object, ensure_ascii=False)
json_byte_data = json_str.encode("utf8")

# level은 1-9까지 지정이 가능하다. 1은 가장 빠르나 압축률이 낮다.
# 9는 가장 느리나 압축률이 높다. 기본값 -1은 6과 동일하다.
compressed_data = zlib.compress(json_byte_data, level=-1)

# 압축된 데이터의 CRC32 체크섬 계산
crc32 = zlib.crc32(compressed_data)

print(f"'json_str' 데이터 길이={len(json_byte_data)}")
print(f"압축된 'compressed_json' 데이터 길이={len(compressed_data)}")
print(f"압축된 'compressed_json' 데이터 CRC2={crc32}")

decompressed_data = zlib.decompress(compressed_data)
print(f"압축 해제된 'decompressed_json' 길이={len(decompressed_data)}")
print("decompressed_data(UTF8)={}".format(decompressed_data.decode("utf8")))
