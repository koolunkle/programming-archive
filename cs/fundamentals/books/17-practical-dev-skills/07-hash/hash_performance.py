import hashlib
import time


def computeMD5(str):
    hasher = hashlib.md5()
    hasher.update(str.encode("utf-8"))
    return hasher.hexdigest()


def computeSHA512(str):
    hasher = hashlib.sha512()
    hasher.update(str.encode("utf-8"))
    return hasher.hexdigest()


# 백만 개의 MD5 해시 생성 속도 측정
md5_t1 = time.monotonic()

for i in range(1, 1000001):
    computeMD5(str(f"hash_test_key_{i}"))

md5_t2 = time.monotonic()

# 백만 개의 SHA-512 해시 생성 속도 측정
sha2_t1 = time.monotonic()

for i in range(1, 1000001):
    computeSHA512(f"hash_test_key_{i}")

sha2_t2 = time.monotonic()

print(f"Elapsed time(MD5)={md5_t2-md5_t1}")
print(f"Elapsed time(SHA512)={sha2_t2-sha2_t1}")
