/*
   함수형 프로그래밍

   함수형 프로그래밍이라는 용어는 불변성을 선호하고, 순수 함수를 사용하는 경우에
   동시성을 쉽게 구현할 수 있으며, 반복보다는 변형을 사용하고, 조건문보다는
   필터를 사용하는 코딩 스타일을 지칭
*/

/*
    알고리즘에서 reduce 사용하기

    비어 있지 않는 컬렉션의 값을 축약하고 싶지만 누적자의 초기값을 설정하고 싶지 않은 경우

    inline fun <S, T : S> Iterable<T>.reduce(operation: (acc: S, T) -> S): S
*/

public inline fun IntArray.reduce(operation: (acc: Int, Int) -> Int): Int {
    if (isEmpty()) {
        throw UnsupportedOperationException("Empty array can't be reduced")
    }
    var accumulator = this[0]
    for (index in 1..lastIndex) {
        accumulator = operation(accumulator, this[index])
    }
    return accumulator
}

// reduce를 이용한 합(sum) 구현
fun sumReduce(vararg nums: Int) = nums.reduce { acc, i -> acc + i }

/*
    @Test
    fun `sum using reduce`() {
        val numbers = intArrayOf(3, 1, 4, 1, 5, 9)
        assertAll(
            { assertEquals(numbers.sum(), sumReduce(*numbers)) },
            { assertThrows<UnsupportedOperationException> { sumReduce() } },
        )
    }
*/

/*
    reduce를 잘못된 방법으로 사용하는 상황

    모든 입력 값을 서로 더하기 전에 모든 입력 값을 수정하고 싶을 경우(더하기 전에 값을 두배 증가시키기)

    컬렉션의 첫 번째 값으로 누적자를 초기화하고 컬렉션의 다른 값에
    추가 연산을 필요로 하지 않은 경우에만 reduce 사용
*/

// 더하기 전에 값을 두배 증가시키기
fun sumReduceDoubles(vararg nums: Int) =
        nums.reduce { acc, i ->
            println("acc = $acc\ni = $i")
            acc + 2 * i
        }

fun main() {
    println(sumReduce(1, 2, 3))
    /*
        Expected: 46
        Actual: 43
        reason: 리스트의 첫 번째 값 3은 누적자를 초기화하는 데 사용됐기 때문에 값이 2배가 되지 않음
    */
    println(sumReduceDoubles(3, 1, 4, 1, 5, 9))
}
