import kotlin.random.Random

/*
    Random의 무작위 동작 이해하기

    난수를 생성하고 싶은 경우 Random 클래스에 있는 함수 중 하나를 사용한다

    추상 Random 클래스 내의 선언
    open fun nextInt(): Int

    open fun nextInt(until: Int): Int

    open fun nextInt(from: Int, until: Int): Int
*/

/*
    Random 내부의 동반 객체

    해당 동반 객체는 기본 구현을 가져오고 추상 클래스에 선언된
    모든 메소드를 획득한 기본 구현에 위임하기 위해 재정의한다

    nextInt와 동일한 패턴으로 Boolean, Byte, Float, Long, Double뿐만 아니라
    부호가 없는 UBytes, UInt, Ulong과 같은 여러 타입이 동반 객체에 포함되어 있다

    companion object Default : Random() {
        private val defaultRandom: Random = defaultPlatformRandom()

        override fun nextInt(): Int = defaultRandom.nextInt()
        override fun nextInt(until: Int): Int = defaultPlatformRandom().nextInt(until)
        override fun nextInt(from: Int, until: Int): Int = defaultPlatformRandom().nextInt(from, until)
    }
*/

fun main() {
    // nextInt 함수 중복
    val value = Random.nextInt()
    println(value in Int.MIN_VALUE..Int.MAX_VALUE)

    val value1 = Random.nextInt(10)
    println(value1 in 0..10)

    val value2 = Random.nextInt(5, 10)
    println(value2 in 5..10)

    /*
        시드 값과 함께 난수 생성기 사용하기

        Int나 Long 타입의 시드 값을 받는 Random 함수가 있다
        Random 함수는 인자로 받은 시드 값을 바탕으로  반복 가능한 난수 생성기를 리턴한다
        시드 값이 같은 경우 nextInt 호출은 동일한 순서의 난수 값을 제공한다
    */
    val r1 = Random(12345)
    val num1 = (1..10).map { r1.nextInt() }
    println(num1)

    val r2 = Random(12345)
    val num2 = (1..10).map { r2.nextInt() }
    println(num1 == num2)
}
