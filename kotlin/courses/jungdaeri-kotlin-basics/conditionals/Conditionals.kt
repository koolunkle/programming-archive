fun main() {

    var isDarkMode: Boolean = true

    if (!isDarkMode) {
        println("Dark Mode : $isDarkMode")
    }

    val result: String = if (isDarkMode) "Dark Mode" else "Light Mode"

    println(result)

    when (isDarkMode) {
        true -> println("Dark Mode : On")
        false -> println("Dark Mode : Off")
    }

    val price: Int = 100

    if (price in arrayOf(100, 200, 300)) {
        println("contain")
    } else {
        println("not contained")
    }
}
