/*
    바이너리 문자열로 변환하고 되돌리기

    숫자를 바이너리 문자열로 변환하거나 바이너리(또는 다르 기저) 문자열을
    정수로 파싱하고 싶은 경우, 기저(radix)를 인자로 받는 toString 또는 toInt 함수 중복을 사용한다

    toString(Int)이 생성한 문자열은 앞에 나오는 0을 잘라낸다
    0을 잘라내는 것을 원치 않는다면 padStart 함수를 사용해서 결과 문자열을 후처리할 수 있다
*/

/*
    단일 바이너리 속성으로 데이터를 인코딩해야 한다고 가정
    e.g. 빨간색과 검정색으로 이뤄진 놀이용 카드가 있고, 카드 4장을 연속해서 받는 경우의
         모든 조합을 알고 싶다면 바이너리 0부터 15까지 세면 된다
*/

fun main() {
    // 정수를 바이너리 문자열로 변환하기
    val str = 42.toString(radix = 2)
    println(str)
    val num = "101010".toInt(radix = 2)
    println(num == 42)

    // 바이너리 문자열 패딩
    val strings = (0..15).map { it.toString(radix = 2).padStart(4, '0') }
    // "0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010",
    // "1011", "1100", "1101", "1110", "1111"
    println(strings.contains("0000"))

    val nums = strings.map { it.toInt(radix = 2) }
    println(nums.containsAll((0..15).toList()))
}
