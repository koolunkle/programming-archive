fun main() {
    // 값을 범위로 지정하기
    val range = 3..8
    println(range.start)
    println(range.endInclusive)
    println(5.coerceIn(range))
    println(1.coerceIn(range))
    println(9.coerceIn(range))

    val min = 2
    val max = 6
    println(1.coerceIn(min, max))
}
