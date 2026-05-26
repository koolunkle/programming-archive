fun main() {

    // val numbers = mutableSetOf<Int>(1, 3, 3, 3, 3, 3, 5)
    val numbers = setOf<Int>(1, 3, 3, 3, 3, 3, 5)
    val numbersToMutable = numbers.toMutableSet()

    // numbers.add(10)
    numbersToMutable.add(10)
    numbersToMutable.remove(1)

    println(numbersToMutable)

    val results = numbersToMutable.withIndex()

    results.forEach { result -> println("index : ${result.index}, value : ${result.value}") }
}
