data class Friend(val name: String, val age: Int)

enum class FriendType {
    OLD,
    YOUNG;

    fun filter(friend: Friend): Boolean {
        return when (this) {
            OLD -> friend.age >= 20
            YOUNG -> friend.age < 20
        }
    }
}

fun filterYoung(friend: Friend): Boolean {
    return friend.age < 20
}

fun main() {

    val friends = mutableListOf<Friend>()

    friends.add(Friend("Bob1", 23))
    friends.add(Friend("Bob2", 21))
    friends.add(Friend("Bob3", 22))
    friends.add(Friend("Bob4", 23))
    friends.add(Friend("Bob5", 24))
    friends.add(Friend("Bob6", 100))
    friends.add(Friend("Bob6", 10))
    friends.add(Friend("Bob6", 102))
    friends.add(Friend("Bob6", 103))
    friends.add(Friend("Bob6", 104))

    // val filteredFriends = friends.filter { friend -> friend.age < 20 }
    // val filteredFriends = friends.filter(::filterYoung)
    val filteredFriends = friends.filter(FriendType.YOUNG::filter)

    println("filterd : $filteredFriends")

    val foundFriends = friends.filter { friend -> friend.name == "Bob7" }.firstOrNull()

    println(foundFriends)
}

data class Todo(val title: String, val isDone: Boolean)

enum class TodoType {
    ON_PROGRESS,
    DONE;

    fun filter(todo: Todo): Boolean {
        return when (this) {
            ON_PROGRESS -> todo.isDone == false
            DONE -> todo.isDone == true
        }
    }
}
