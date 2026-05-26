import kotlin.random.Random

/*
    Nothing 클래스를 사용법에 맞게 적절하게 사용하기 (절대 리턴하지 않는 함수에 Nothing 사용하기)
    Nothing은 클래스 안밖으로 인스턴스할 수 없음

    public class Nothing private constructor()

    Nothing 클래스의 사용은 2가지 상황에서 자연스럽게 발생
    1. 함수 몸체가 전적으로 예외를 던지는 코드로 구성
    2. 변수에 널을 할당할 때 구체적인 타입을 명시하지 않은 경우
*/

// 함수 몸체가 전적으로 예외를 던질 때
fun doNothing(): Nothing = throw Exception("Nothing at all")

// 변수에 널을 할당할 때 구체적인 타입을 명시하지 않음
// val x: Nothing? = null
val x = if (Random.nextBoolean()) "True" else throw Exception("nope")

fun main() {
    for (n in 1..10) {
        val x =
                when (n % 3) {
                    0 -> "$n % 3 == 0"
                    1 -> "$n % 3 == 1"
                    2 -> "$n % 3 == 2"
                    else -> throw Exception("Houston, we have a problem...")
                }
        println(x)
        // assertTrue(x is string)
    }
}
