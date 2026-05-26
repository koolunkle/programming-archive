fun main() {

    // val numbers = listOf(1, 3, 5, 7, 9)
    // numbers.toMutableList()

    val numbers = mutableListOf(1, 3, 5, 7, 7, 7, 9)

    numbers.toList()

    println(numbers)

    val numbersToSet = numbers.toSet()

    println(numbersToSet)

    // val numbersToSetDescending = numbersToSet.toSortedSet(comparator = Comparator.reverseOrder())
    val numbersToSetDescending = numbersToSet.sortedDescending()

    println(numbersToSetDescending)
}
