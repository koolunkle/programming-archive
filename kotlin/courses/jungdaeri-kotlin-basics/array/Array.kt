fun main() {

    // val friends = listOf<String>("철수", "영희", "제임스")
    val friends = mutableListOf<String>("철수", "영희", "제임스")
    friends[0] = "Test"

    // val firstFriend = friends.get(0)
    // val firstFriend = friends[0]
    val firstFriend = friends.first()

    // val numbers = arrayOf<Int>(1, 2)
    val numbers = arrayListOf<Int>(1, 2)
    numbers[0] = 100

    val firstNumber = numbers.first()

    for (number in numbers) {
        println(number)
    }

    println(firstFriend)
    println(firstNumber)
}
