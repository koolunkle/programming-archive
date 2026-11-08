import hashlib


def computeSHA1(str):
    hasher = hashlib.sha1()
    hasher.update(str.encode("utf-8"))
    return hasher.hexdigest()


hash1 = computeSHA1("해시 값1")
hash2 = computeSHA1("해시 값2")

print(f"해시 값1={hash1} / 길이={len(hash1)}")
print(f"해시 값2={hash2} / 길이={len(hash2)}")
