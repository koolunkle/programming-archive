/*
    사용자 정의 이터레이터 정의하기

    컬렉션을 감싼 클래스를 손쉽게 순회하고 싶은 경우
    next와 hasNext 함수를 모두 구현한 이터레이터를 리턴하는 연산자 함수 정의

    kotlin.collections의 Iterator 인터페이스
    interface Iterator<out T> {
        operator fun next() : T
        operator fun hasNext(): Boolean
    }
*/

data class Player(val name: String)

class Team(val name: String, val players: MutableList<Player> = mutableListOf()) {
    fun addPlayers(vararg people: Player) = players.addAll(people)
    /// ... 필요한 다른 함수들 ...
}

// 직접적으로 순회하기
operator fun Team.iterator(): Iterator<Player> = players.iterator()

class Teams(val name: String, val players: MutableList<Player> = mutableListOf()) :
        Iterable<Player> {

    override fun iterator(): Iterator<Player> {
        return players.iterator()
    }
}

// Team 클래스의 Iterator 확장 함수 사용하기
// assertEquals("Cousins, Curry, Durant, Green, Thompson", team.map { it.name }.joinToString())

fun main() {
    // 리스트 순회하기
    val team = Team("Warriors")
    team.addPlayers(
            Player("Curry"),
            Player("Thompson"),
            Player("Durant"),
            Player("Green"),
            Player("Cousins"),
    )

    for (player in team.players) {
        println(player)
    }

    println("********************")

    // 직접적으로 순회하기
    for (player in team) {
        println(player)
    }

    val teams = Teams("Warriors")
    teams.players.addAll(
            mutableListOf(
                    Player("Curry"),
                    Player("Thompson"),
                    Player("Durant"),
                    Player("Green"),
                    Player("Cousins"),
            )
    )
    val result = teams.map { it.name }.joinToString()
    println(result)
}
