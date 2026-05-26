fun main() {

    infix fun Int.multiplyBy(x: Int): Int = this * x

    val result1 = 1 multiplyBy 2
    println(result1)

    val result2 = 1.multiplyBy(2)
    println(result2)
}
