fun main() {

    val friends = listOf<String>("철수", "존시나", "에이미", "영희", "제임스")

    for ((index, friend) in friends.withIndex()) {
        println("index: $index, friend: $friend")
    }
}
