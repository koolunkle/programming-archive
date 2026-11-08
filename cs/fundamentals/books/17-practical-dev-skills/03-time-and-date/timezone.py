import datetime

# 시스템 기본 시간
t1 = datetime.datetime.now()

# UTC 시간
t2 = datetime.datetime.now(tz=datetime.timezone.utc)

print(f"시스템 기본 시간: {t1}")
print(f"UTC 시간: {t2}")
