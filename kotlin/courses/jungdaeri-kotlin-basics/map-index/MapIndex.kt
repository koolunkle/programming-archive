fun main() {

    val friends = listOf("철수", "제임스", "영희", "제시카")
    // val results = friends.map { name -> "frined : $name" }
    val results = friends.mapIndexed { index, name -> "frined${index} : $name" }

    println(results)
}
