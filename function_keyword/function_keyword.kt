// 1. infix 정의
//     해당 수정자를 사용하면 점과 괄호 없이 연산자처럼 중위 표기법을 사용하여 함수를 호출할 수 있음
//     중위 함수는 멤버 함수이거나 확장 함수여야 하며, 단일 매개변수를 가져야 하고 infix 키워드로 표시되어야 함
//         infix fun Int.`**`(x: Int) = toDouble().pow(x).toInt()
//         infix fun Long.`**`(x: Int) = toDouble().pow(x).toLong()
//         infix fun Float.`**`(x: Int) = pow(x)
//         infix fun Double.`**`(x: Int) = pow(x)

//         @Test
//         fun `raise to power`() {
//             assertAll(
//                 { assertThat(1, equalTo(2 `**` 0)) },
//                 { assertThat(1L, equalTo(2L `**` 0)) },
//                 { assertThat(1F, equalTo(2F `**` 0)) },
//                 double(또는 float)에서의 동치 비교를 피하기 위한 햄크레스트 논리 판정 함수(closeTo)
//                 { assertThat(1.0, closeTo(2.0 `**` 0, 1e-6)) }
//             )
//         }

//     2. inline 정의
//         해당 수정자는 컴파일러에게 별도의 함수 호출을 생성하는 대신 함수의 바이트코드를 호출 사이트에 직접 삽입하도록 제안
//         주로 고차 함수(다른 함수를 매개변수로 받거나 반환하는 함수)와 관련된 오버헤드를 줄이는 데 사용
//         특히 람다를 처리할 때 람다에 대한 익명 클래스 객체 생성을 방지하여 오버헤드를 줄임
//         inline 함수가 크고 자주 호출되는 경우 inline으로 인해 컴파일된 코드의 크기가 커질 수 있음
//         그외 noinline, crossline, reified 등의 키워드가 있음

//     3. internal 정의
//         해당 수정자는 선언의 가시성을 현재 모듈로 제한
//         같은 모듈 내의 어디에서나 멤버에 접근할 수 있도록 허용하지만, 모듈 외부에서는 접근할 수 없도록 허용

//     4. tailrec 정의
//         꼬리 호출 (Tail Call)은 함수 안에서 함수의 호출이 발생할 때, 해당 호출이 함수의 가장 마지막 순서에 위치하는 것을 말함
//         이런 형식으로 작성된 꼬리 재귀(Tail Recursion) 함수는 프로그램의 컴파일러(인터프리터)에 의해 다음 재귀 함수 호출 시
//         기존 함수의 저장되어있던 정보를 메모리에서 제거함으로써(기존 함수의 Stack frame을 pop함으로써) 재귀 함수의 동작을 최적화
//             tailrec fun recursive(x: Int, y: Int): Int {
//                 return if (x % 2 == 0) y else recursive(x+1, y+1)
//             }

//             println(recursive(1,2))
//             >> result: 3
