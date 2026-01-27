import java.math.BigInteger

/*
   함수형 프로그래밍

   함수형 프로그래밍이라는 용어는 불변성을 선호하고, 순수 함수를 사용하는 경우에
   동시성을 쉽게 구현할 수 있으며, 반복보다는 변형을 사용하고, 조건문보다는
   필터를 사용하는 코딩 스타일을 지칭
*/

/*
    알고리즘에서 fold 사용하기

    반복 알고리즘을 함수형 방식으로 구현하고 싶을 때
    fold 함수를 사용해 시퀀스나 컬렉션을 하나의 값으로 축약할 수 있다

    inline fun <R> Iterable<T>.fold(initial: R, operation: (acc: R, T) -> R): R
*/

// fold를 사용해 정수의 합 계산하기
fun sum(vararg nums: Int) = nums.fold(0) { acc, n -> acc + n }

fun sumWithTrace(vararg nums: Int) =
        nums.fold(0) { acc, n ->
            println("acc = $acc, n = $n")
            acc + n
        }

// fold를 사용한 반복 연산(정수의 팩토리얼)
fun recursiveFactorial(n: Long): BigInteger =
        when (n) {
            0L, 1L -> BigInteger.ONE
            else -> BigInteger.valueOf(n) * recursiveFactorial(n - 1)
        }

fun factorialFold(n: Long): BigInteger =
        when (n) {
            0L, 1L -> BigInteger.ONE
            else -> (2..n).fold(BigInteger.ONE) { acc, i -> acc * BigInteger.valueOf(i) }
        }

// fold를 사용해서 피보나치 수 계산하기
fun fibonacciFold(n: Int) =
        (2 until n).fold(1 to 1) { (prev, curr), _ -> curr to (prev + curr) }.second

/*
    sum 연산 테스트

    @Test
    fun `sum using fold`() {
        val numbers = intArrayOf(3, 1, 4, 1, 5, 9)
        assertEquals(numbers.sum(), sum(*numbers))
    }
*/

fun main() {
    println(sum(1, 2, 3))
    println(sumWithTrace(1, 2, 3))
    println(factorialFold(2L))
}
