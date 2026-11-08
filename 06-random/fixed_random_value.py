# 랜덤값을 열 번 출력
import random

for i in range(10):
    # seed 값을 0으로 설정
    random.seed(0)
    print(random.randrange(1, 10))