import hashlib


def computeSHA256(str):
    hasher = hashlib.sha256()
    hasher.update(str.encode("utf-8"))
    return hasher.hexdigest()


def computeSHA512(str):
    hasher = hashlib.sha512()
    hasher.update(str.encode("utf-8"))
    return hasher.hexdigest()


hash1 = computeSHA256("해시 값1")
hash2 = computeSHA256("해시 값2")
hash3 = computeSHA512("해시 값1")
hash4 = computeSHA512("해시 값2")

print(f"해시 값1={hash1} / 길이={len(hash1)}")
print(f"해시 값2={hash2} / 길이={len(hash2)}")
print(f"해시 값3={hash3} / 길이={len(hash3)}")
print(f"해시 값4={hash4} / 길이={len(hash4)}")
