/*
    완벽한 when 강제하기

    코틀린 컴파일러가 when 문에서 가능한 모든 절을 가지도록 강제하게 만들고 싶은 경우
    값을 리턴하는 제네릭 타입에 exhaustive라는 간단한 확장 속성을 추가하고
    when block에 이를 연결한다
*/

// 숫자를 3으로 나눈 나머지
fun printMod3(n: Int) {
    when (n % 3) {
        0 -> println("$n % 3 == 0")
        1 -> println("$n % 3 == 1")
        2 -> println("$n % 3 == 2")
    }
}

/*
    when을 사용해서 값을 리턴하기

    코틀린은 리턴을 else 블록을 강요하는 것으로 해석하므로
    이 점을 이용해 값을 자동으로 리턴하도록 when 블록을 만들면
    모든 when 블록이 완벽해지도록 강제할 수 있다
    * 완벽해지도록 강제하기 위해서는 exhaustive라는 확장 속성을 만든다
*/

val <T> T.exhaustive: T
    get() = this

fun printMod3SingleStatement(n: Int) =
        when (n % 3) {
            0 -> println("$n % 3 == 0")
            1 -> println("$n % 3 == 1")
            2 -> println("$n % 3 == 2")
            else -> println("Houston, we have a problem...") // else 문 없이는 컴파일되지 않음
        }.exhaustive // 속성이 컴파일러에게 else 절을 요구하도록 강요 (현재 객체를 리턴하기 떄문)

fun main() {
    // 숫자를 3으로 나눈 나머지
    printMod3(10)
}
