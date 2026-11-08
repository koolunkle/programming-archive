# 당첨 확률: 30%
import random

WIN_RATE = 0.3

# 뽑기 횟수: 10개
NUMBER_OF_DRAWS = 10

# 뽑기 컨테이너 승/패 개수
draws = []
win_draws = int(NUMBER_OF_DRAWS * WIN_RATE)
loss_draws = NUMBER_OF_DRAWS - win_draws

print(f"win={win_draws} / loss={loss_draws}")

# 당첨 제비
for i in range(win_draws):
    draws.append(1)

# 꽝 제비
for i in range(loss_draws):
    draws.append(0)

print(draws)

# 제비 섞기
random.seed()
random.shuffle(draws)

# 제비 출력
print(draws)
