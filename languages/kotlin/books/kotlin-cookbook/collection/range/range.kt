import java.time.LocalDate

/*
    범위를 수열로 만들기

    범위를 순회하고 싶지만 범위가 간단한 정수(Integer, Long) 또는 문자로
    구성되어 있지 않은 경우 사용자 정의 수열을 생성한다.
*/

// operator fun <T : Comparable<T>> T.rangeTo(that: T): ClosedRange<T> = ComparableRange(this, that)

interface ClosedRange<T : Comparable<T>> {
    val start: T
    val endInclusive: T
    operator fun contains(value: T): Boolean = value >= start && value <= endInclusive
    fun isEmpty(): Boolean = start > endInclusive
}

// keypoint 1
class LocalDateProgression(
        override val start: LocalDate,
        override val endInclusive: LocalDate,
        val step: Long = 1L
) : Iterable<LocalDate>, ClosedRange<LocalDate> {

    // Iterator 인터페이스에서 반드시 구현해야 하는 함수
    override fun iterator(): Iterator<LocalDate> =
            LocalDateProgressionIterator(start, endInclusive, step)

    infix fun step(days: Long) = LocalDateProgression(start, endInclusive, days)
}

// keypoint 2
internal class LocalDateProgressionIterator(
        start: LocalDate,
        val endInclusive: LocalDate,
        val step: Long
) : Iterator<LocalDate> {

    private var current = start

    override fun hasNext() = current <= endInclusive

    override fun next(): LocalDate {
        val next = current
        current = current.plusDays(step)
        return next
    }
}

// keypoint 3
operator fun LocalDate.rangeTo(other: LocalDate) = LocalDateProgression(this, other)

fun main() {
    // 범위 안에서 LocalDate 사용하기
    val startDate = LocalDate.now()
    val midDate = startDate.plusDays(3L)
    val endDate = startDate.plusDays(5L)
    val dateRange = startDate..endDate

    println(startDate in dateRange)
    println(midDate in dateRange)
    println(endDate in dateRange)
    println(startDate.minusDays(1L) !in dateRange)
    println(endDate.plusDays(1L) !in dateRange)

    /*
        컴파일 에러 발생

        범위가 수열이 아니기 때문, 수열은 순서 있는 값의 연속이다
        사용자 정의 수열은 코틀린 표준 라이브러리인 IntProgression, LongProgression, CharProgression 처럼
        Iterable 인터페이스를 구현해야 한다

        for (date in dateRange) println(it)

        (startDate..endDate).forEach { println(it) }
    */

    // LocalDate 수열의 테스트
    val startDates = LocalDate.now()
    val endDates = startDate.plusDays(5L)

    val dateRanges = startDate..endDate
    dateRanges.forEachIndexed { index, localDate ->
        println(
                """
            localDate: $localDate
            startDate_plusDays: ${startDate.plusDays(index.toLong())}
            ******************************
            """.trimIndent()
        )
    }

    val dateList = dateRanges.map { it.toString() }
    println(dateList)

    val dateRangess = startDates..endDates step 2
    dateRangess.forEachIndexed { index, localDate ->
        println(
                """
        ********************************
        localDate: $localDate
        startDate_plushDate: ${startDate.plusDays(index.toLong() * 2)}
        ********************************
        """.trimIndent()
        )
    }

    val dateLists = dateRangess.map { it.toString() }
    println(dateLists)
}
