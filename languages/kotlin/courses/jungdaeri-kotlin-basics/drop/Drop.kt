fun main() {

    val numbers: List<Int> = (1..100).toList()

    val result1 = numbers.drop(10)
    println("result1: ${result1}")

    val result2 = numbers.dropLast(10)
    println("result2: ${result2}")

    val result3 = numbers.dropWhile { it < 30 }
    println("result3: ${result3}")

    val result4 = numbers.dropLastWhile { it > 80 }
    println("result4: ${result4}")

    val result5 = numbers.filter { it < 80 }
    println("result5: ${result5}")
}
