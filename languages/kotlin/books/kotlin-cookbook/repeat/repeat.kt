/*
    반복적으로 람다 실행하기

    주어진 람다 식을 여러 번 실행하고 싶은 경우
    코틀린 내장 repeat 함수를 사용한다

    @kotlin.internal.InlineOnly
    public inline fun repeat(times: Int, action: (Int) -> Unit) {
        contract { callsInPlace(action) }

        for (index in 0 until times) {
            action(index)
        }
    }
*/

fun main() {
    repeat(5) { println("Counting: $it") }
}
