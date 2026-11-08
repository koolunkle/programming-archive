import datetime
import time

# t1 시간 기록
# t1 = datetime.datetime(year=2026, month=9, day=28, hour=17, minute=21, second=00)

# t1 시간 기록 (특정 날짜를 현재 시간을 기준으로 할 경우)
t1 = datetime.datetime.now() + datetime.timedelta(minutes=1)

while True:
    now = datetime.datetime.now()
    print(f"현재 시간: {now}")
    print(f"루프 만료 시간: {t1}")
    if t1 <= now:
        break

    time.sleep(1)
