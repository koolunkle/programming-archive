fun main() {

    val firstFriends = listOf("철수", "제임스", "영희", "제시카")
    val secondFriends = listOf("에이미", "마이클잭슨", "웡", "세바스찬")
    val totalFriends: List<List<String>> = listOf(firstFriends, secondFriends)

    println(totalFriends)

    val flattenFriends = totalFriends.flatten()

    println(flattenFriends)
}
