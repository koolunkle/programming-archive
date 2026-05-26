/*
    컬렉션을 윈도우로 처리하기

    컬렉션을 같은 크기로 나누고 싶다면 chunked 함수를 사용한다
    컬렉션을 정해진 간격으로 컬렉션을 따라 움직이는 블록을 원한다면 windowed 함수를 사용한다

    Iterable 컬렉션에서 chunked 함수는 컬렉션을 주어진 크기 또는 그보다 더 작게 리스트의 리스트로 분할한다
    chunked 함수는 리스트의 리스트를 리턴할 수 있고 결과 리스트에 적용할 변환 람다를 제공할 수도 있다.

    fun <T> Iterable<T>.chunked(size: Int): List<List<T>>

    fun <T, R> Iterable<T>.chunked(
        size: Int,
        transform: (List<T>) -> R
    ): List<R>
*/

// 코틀린 표준 라이브러리의 chunked 구현
public fun <T> Iterable<T>.chunked(size: Int): List<List<T>> {
    // size: 각 윈도우에 포함될 원소의 개수
    // step: 각 단계마다 전진할 원소의 개수 (기본 1개)
    /*
       partialWindows
       나뉘어 있는 마지막 부분이 윈도우에 필요한 만큼의 원소 개수를 갖지 못한 경우,
       해당(마지막) 부분을 그대로 유지할 지 여부를 알려주는 값 (기본 false)
    */
    return windowed(size = size, step = size, partialWindows = true)
}

fun main() {
    val range = 0..10

    val chunked = range.chunked(3)
    val chunkedSum = range.chunked(3) { it.sum() }
    val chunkedAvg = range.chunked(3) { it.average() }

    val window = range.windowed(3, 3)
    val windowAvg = range.windowed(3, 3) { it.average() }
    val windowStepOne = range.windowed(3, 1)

    println("***** chuncked example *****")
    println(chunked)
    println(chunkedSum)
    println(chunkedAvg)

    println("***** window example *****")
    println(window)
    println(windowAvg)
    println(windowStepOne)
}
