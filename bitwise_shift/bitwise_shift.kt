// 비트 시프트 연산자 사용하기
//     1. 코틀린에는 비트 시프트를 위한 'shr', 'shl', 'ushr' 같은 비트 중위 연산자(bitwise inflix)가 있음
//     2. 비트 연산은 리스트 접근 제어 및 통신 프로토콜, 압축, 암호화 알고리즘, 컴퓨터 그래픽을 비롯한 수많은 애플리케이션에서 사용
//     3. 코틀린은 시프트 연산을 위해 특정 연산자 심볼을 사용하지는 않지만 확장함수로써 시프트 연산자 정의
//      가. shl: 부호 있는 왼쪽 시프트
//      나. shr: 부호 있는 오르쪽 시프트
//      다. ushr: 부호 없는 오르쪽 시프트
//     4. 비트 연산은 2의 보수 연산, 비트를 왼쪽 또는 오른쪽으로 시프트하는 것은 2를 곱하거나 나눈 것과 같음
//         가. 2를 곱하거나 나누기
//             @Test
//             fun `doubling and halving`() {
//                 assertAll("left shifts doubling from 1", // 0000_0001
//                     { assertThat(2, equalTo(1 shl 1)) }, // 0000_0010
//                     { assertThat(4, equalTo(1 shl 2)) }, // 0000_0100
//                     { assertThat(8, equalTo(1 shl 3)) }, // 0000_1000
//                     { assertThat(16, equalTo(1 shl 4)) }, // 0001_0000
//                     { assertThat(32, equalTo(1 shl 5)) }, // 0010_0000
//                     { assertThat(64, equalTo(1 shl 6)) }, // 0100_0000
//                     { assertThat(128, equalTo(1 shl 7)) }, // 1000_0000
//                 )

//                 assertAll("right shifts halving from 235", // 1110_1011
//                     { assertThat(117, equalTo(235 shl 1)) }, // 0111_0101
//                     { assertThat(58, equalTo(235 shl 2)) }, // 0011_1010
//                     { assertThat(29, equalTo(235 shl 3)) }, // 0001_1101
//                     { assertThat(14, equalTo(235 shl 4)) }, // 0000_1110
//                     { assertThat(7, equalTo(235 shl 5)) }, // 0000_0111
//                     { assertThat(3, equalTo(235 shl 6)) }, // 0000_0011
//                     { assertThat(1, equalTo(235 shl 7)) }, // 0000_0001
//                 )
//             }

//         나. ushr 함수 사용과 shr 함수 사용 비교
//             부호를 보존하지 않고 값을 시프트하려는 경우 ushr 함수 사용
//             양수의 경우 shr 함수와 똑같은 동작을 하지만, 음수의 경우에는 shr은 왼쪽에서부터 1을 채우기 때문에 결과는 여전히 음수
//             val n1 = 5
//             val n2 = -5
//             println(n1.toString(2)) // 0b0101
//             println(n2.toString(2)) // -0b0101

//             assertThat(n1 shr 1, equalTo(0b0010)) // 2
//             assertThat(n1 ushr 1, equalTo(0b0010)) // 2

//             assertThat(n2 shr 1, equalTo(-0b0011)) // -3

//              2의 보수 연산, ushr은 왼쪽을 0으로 채움
//             assertThat(n2 ushr 1, equalTo(0x7fff_fffd)) // 2_147_483_645

//         다. 큰 정수 2개의 중간 값 찾기
//             ushr 함수는 많은 곳에서 사용되는데, 흥미로운 예제 하나는 큰 정수 2개의 중간 값을 찾는 경우이다.
//             두 값이 모두 큰 값이라면 두 값을 더한 결과는 Int.MAX_VALUE 값보다 큰 값이 될 것이기 때문에 덧셈의 결과는 음수
//             2로 나누려는 부호 없는 오른쪽 시프트의 결과는 low와 high 값의 사이에 위치
//             이진 검색 또는 정렬 같은 많은 알고리즘은 어쩌면 각각의 값이 매우 큰 두 정수 값의 평균 계산이 요구됨
//             해당 경우 ushr을 사용하면 평균 값 계산이 원하는 경계에 위치하게 할 수 있음

//             val high = (0.99 * Int.MAX_VALUE).toInt()
//             val low = (0.75 * Int.MAX_VALUE).toInt()

//             val mid1 = (high + low) / 2
//             val mid2 = (high + low) ushr 1

//             두 큰 수의 합은 Int의 최댓값보다 크기 때문에 결과는 음수
//             assertTrue(mid1 !in low..high)

//              부호 없는 시프트는 원하는 범위 내에서 결과 값을 얻는 것을 보장
//             assertTrue(mid2 in low..high)

//         라. 비트 불리언 연산자
//             비트 불리언 연산을 위해 코틀린이 제공하는 and, or, xor, inv(not 대신) 비트 연산자를 사용
//             inv 함수는 숫자의 모든 비트를 뒤집는다 (0b00000100 > 0b11111011)

//             1. 숫자 4비트 반전
//                 4 == 0b0000_0100 (이진수)
//                 주어진 비트 보수 (모든 비트를 뒤집는다)
//                 0b1111_1011 == 251 (십진수)
//                 assertEquals(-5, 4.inv())

//             2. and, or, xor의 간단한 예
//                 @Test
//                 fun `and, or, xor`() {
//                     val n1 = 0b0000_1100 // 십진수 12
//                     val n2 = 0b0001_1001 // 십진수 25

//                     val n1_and_n2= n1 and n2
//                     val n1_or_n2 = n1 or n2
//                     val n1_xor_n2 = n1 xor n2

//                     assertThat(n1_and_n2, equalTo(0b0000_1000)) // 8
//                     assertThat(n1_or_n2, equalTo(0b0001_1101)) // 29
//                     assertThat(n1_xor_n2, equalTo(0b0001_0101)) // 21
//                 }

//             3. 정수 값을 각각의 RGB 값으로 변환하기
//                 fun intsFromColor(color: Color) : List<Int> {
//                     val rgb = color.rgb
//                     val alpha = rgb shr 24 and 0xff
//                     val red = rgb shr 16 and 0xff
//                     val green = rgb shr 8 and 0xff
//                     val blue = rgb and 0xff
//                     return listOf(alpha, red, green, blue)
//                 }

//                 3-1. 리스트 분해 및 테스트
//                     @Test
//                     fun `colors as ints`() {
//                         val color = Color.MAGENTA
//                         val (a, r, g, b) = intsFromColor(color)

//                         assertThat(color.alpha, equalTo(a))
//                         assertThat(color.red, equalTo(r))
//                         assertThat(color.green, equalTo(g))
//                         assertThat(color.blue, equalTo(b))
//                     }

//                 3-2. 각각의 RGB 값과 알파 값으로 종합한 정수 만들기
//                     fun colorFromInts(alpha: Int, red: Int, green: Int, blue: Int) =
//                         (alpha and 0xff shl 24) or
//                         (red and 0xff shl 16) or
//                         (green and 0xff shl 8) or
//                         (blue and 0xff)

//                 3-3. RGB와 알파 값을 정수로 변환하기
//                     @Test
//                     fun `ints as colors`() {
//                         val colo = Color.MAGENTA
//                         val intColor = colorFromInts(color.alpha, color.red, color.green, color.blue)
//                         val color1 = Color(intColor, true)
//                         assertThat(color1, equalTo(color))
//                     }
