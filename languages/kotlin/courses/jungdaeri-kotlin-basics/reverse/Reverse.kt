fun main() {

    val pets1: List<String> = listOf<String>("고양이", "강아지", "소")

    val reversed1 = pets1.reversed()
    val reversed2 = pets1.asReversed()

    println(reversed1)
    println(reversed2)

    val pets2: MutableList<String> = mutableListOf<String>("고양이", "강아지", "소")

    // println("original: ${pets2}")
    println("reverse: ${pets2.reverse()}")
    // println("reversed: ${pets2.reversed()}")
    // println("asReversed: ${pets2.asReversed()}")

    // asReversed: Affects original data (Reflected in original)
    val result1: MutableList<String> = pets2.asReversed()

    pets2.add("토끼")
    println(result1)

    result1.add("제비")
    println(pets2)

    println("==========")

    // reversed: Does not affect original data (Independent copy)
    val result2: List<String> = pets2.reversed()

    println(pets2)
    println(result2)

    pets2.add("토끼")

    println(pets2)
    println(result2)
}
