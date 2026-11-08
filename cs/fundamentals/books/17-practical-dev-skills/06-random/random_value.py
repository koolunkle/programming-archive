# 랜덤값을 열 번 출력
import random

for i in range(10):
    # 인자 없이 호출하면 운영체제 난수(os.urandom)로 시드를 설정한다.
    # os.urandom을 사용할 수 없는 환경에서만 현재 시간을 사용한다.
    random.seed()
    print(random.randrange(1, 10))
