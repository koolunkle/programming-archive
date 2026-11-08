# 시간 기록 (현재)
import time

t1 = time.monotonic()

while True:
    # t2 시간 기록
    t2 = time.monotonic()
    # 루프가 3초 이상 실행된 경우 종료
    if t2 >= t1 + 3:
        break

    time.sleep(0.1)

# 실제 시간 차이 출력
print(f"t1={t1}")
print(f"t2={t2}")
print(f"diff={t2-t1}")
