fun main() {

    val numbers: List<Int> = (1..100).toList()

    val result1 = numbers.take(10)
    println("result1: ${result1}")

    val result2 = numbers.takeLast(10)
    println("result2: ${result2}")

    val result3 = numbers.takeWhile { it < 30 }
    println("result3: ${result3}")

    val result4 = numbers.takeLastWhile { it > 80 }
    println("result4: ${result4}")

    val result5 = numbers.filter { it < 80 }
    println("result5: ${result5}")

    // val result6 = numbers.takeIf { it.contains(101) }
    val result6 = numbers.takeIf { it.size > 100 } ?: emptyList()
    println(result6)

    val result7 = numbers.takeUnless { it.contains(101) }
    println(result7)

    val hello: String? = "Hello"
    val result8 = hello.takeIf { it == "Hello" } ?: "None"
    println(result8)
}
