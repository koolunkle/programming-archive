package codes;

import java.util.Scanner;

public class MainDrive {

	public static void main(String[] args) {

//		컴퓨터가 3자리 숫자 문제 출제.
//		[7,4,1] 처럼 3칸의 배열로 만들자.

		int[] cpuNumbers = new int[3];

//		각 칸에 하나씩 (1~9) 랜덤 숫자를 채우자.

		for (int i = 0; i < cpuNumbers.length; i++) {

//			써도 되는 숫자가 나올때까지 무한 반복.

			while (true) {

//				1~9 중 하나를 랜덤으로 추출.
				int randomNum = (int) (Math.random() * 9 + 1);

//				중복 검사.  cpuNumbers에 같은 값이 있나? 확인.

				boolean isDuplOk = true;

				for (int cpuNum : cpuNumbers) {

					if (randomNum == cpuNum) {

//						중복 숫자 발견! => 사용하면 안됨!
						isDuplOk = false;

					}

				}

//				써도 되는 숫자면, cpuNumber의 위치에 맞게 집어넣고, 다음숫자로 넘어가자.

				if (isDuplOk) {

					cpuNumbers[i] = randomNum;

//					무한 반복 탈출
					break;

				}

			}

		}

//		나온 문제가 어떤 값인지 출력
//		for (int cpuNum : cpuNumbers) {
//			System.out.println("문제 숫자 : " + cpuNum);
//		}

//		사용자에게 3자리 숫자 입력 -> 3S 맞출때까지.

		Scanner myScanner = new Scanner(System.in);

//		입력 시도 횟수 기록 변수
		int tryCount = 0;

		while (true) {

			System.out.print("정답 입력 (3자리 정수) : ");
			int inputNum = myScanner.nextInt();

//			시도 횟수 1회 증가.
			tryCount++;

//			723 => [7, 2, 3] 배열형태로 분리하자. => 문제 배열과 비교하기 편해짐.

			int[] userNumbers = new int[3];

//			userNumbers[0] = 입력 숫자의 100의 자리?

			userNumbers[0] = inputNum / 100;

//			userNumbers[1] = 입력 숫자의 10의 자리? => 723 => 72 의 1의 자리?

			userNumbers[1] = inputNum / 10 % 10;

//			userNumbers[2] = 입력 숫자의 1의 자리?

			userNumbers[2] = inputNum % 10;

//			?S / ?B 값을 저장할 변수.
			int strikeCount = 0;
			int ballCount = 0;

//			cpuNumbers / userNumbers 두 배열을 비교.
//			같은숫자 + 위치도 같으면? S 하나 발견.
//			같은숫자 but, 위치가 다르면?  B 하나 발견.

//			내 숫자를 바꿔주는 반복
			for (int i = 0; i < userNumbers.length; i++) {

//				컴퓨터가 낸 숫자를 둘러보는 반복

				for (int j = 0; j < cpuNumbers.length; j++) {

//					내 숫자 / 컴퓨터 숫자 같은가?

					if (userNumbers[i] == cpuNumbers[j]) {

//						두 숫자의 위치가 같은가? => 두 배열의 index인 i, j의 값이 같은가?
						if (i == j) {

//							숫자도 같고, 위치도 같다. -> S 발견
							strikeCount++;

						} else {

//							숫자는 같지만, 위치가 다르다. -> B 발견
							ballCount++;

						}

					}

				}

			}

//			?S ?B인지 변수에 저장되어있다.

			System.out.println(strikeCount + "S / " + ballCount + "B 입니다.");

//			3S라면? 정답 맞췄다. => 무한 반복 탈출
			if (strikeCount == 3) {

				System.out.println("축하합니다. 정답을 맞췄습니다.");

				System.out.println(tryCount + "회 만에 맞췄습니다.");

				break;
			}

		}

	}

}
