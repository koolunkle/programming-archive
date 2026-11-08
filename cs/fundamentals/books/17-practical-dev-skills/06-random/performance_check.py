import os
import random
import time

# 유사 난수 값을 백만 번 생성한 후, 성능 측정
random.seed()  # 운영체제 난수(os.urandom)로 시드 설정

prng_t1 = time.monotonic()

"""
유사 난수 생성 시 random.randrange() 함수를 사용했다면
암호학적으로 안전한 난수 생성보다 더 느린 결과가 나올 수 있다.

randrange() 함수는 파이썬 인터프리터를 통해 동작하고 
다른 함수(random/urandom)들은 C 코드로 작성된 모듈만 호출하여 더 빠르기 때문이다.
"""
for i in range(1000000):
    random.random() 

prng_t2 = time.monotonic()

# 암호학적으로 안전한 난수 값을 백만 번 생성한 후, 성능 측정
srng_t1 = time.monotonic()

for i in range(1000000):
    random_four_byte = os.urandom(4)

srng_t2 = time.monotonic()

print(f"Elapsed time(PRNG)={prng_t2 - prng_t1}")
print(f"Elapsed time(SRNG)={srng_t2 - srng_t1}")
