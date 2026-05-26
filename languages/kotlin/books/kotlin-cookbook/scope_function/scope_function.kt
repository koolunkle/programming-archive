import java.net.URL

/*
    영역 함수

    코틀린 표준 라이브러리에는 객체 컨텍스트 안에서 코드 블록을 실행할 목적으로
    만든 다수의 함수가 포함되어 있다ㅏ
    * apply, also, run, let
*/

/*
apply로 객체 생성 후에 초기화하기

객체를 사용하기 전에 생성자 인자만으로는 할 수 없는 초기화 작업이 필요하다
inline fun <T> T.apply(block: T.() -> Unit): T

apply 함수는 모든 제네릭 타입 T에 존재하는 확장 함수
명시된 블록을 수신자인 this와 함께 호출하고 해당 블록이 완료되면 this를 리턴
이미 인스턴스화된 객체의 추가 설정을 위해 사용하는 가장 일반적인 방법
(run 호출을 apply로 바꿔서 쓸 수 있음)
*/

// 도메인 객체를 삽입하고 생성된 키로 갱신하기
// @Repository
// class JdbcOfficerDAO(private val jdbcTemplate: JdbcTemplate) {
//     private val insertOfficer =
//
// SimpleJdbcInesrt(jdbcTemplate).withTableName("OFFICERS").usingGeneratedKeyColumns("id")
//     fun save(officer: Officer) =
//             officer.apply {
//                 id =
//                         insertOfficer.executeAndReturnKey(
//                                 mapOf("rank" to rank, "first_name" to first, "last_name" to last)
//                         )
//             }
//     // ...
// }

/*
    부수 효과를 위해 also 사용하기

    코드 흐름을 방해하지 않고 메시지를 출력하거나 다르 부수 효과를 생성하고 싶은 경우
    also 함수를 사용해 부수 효과를 생성하는 동작을 수행한다

    public inline fun <T> T.also(block: (T) -> Unit) : T

    also 함수는 모든 제너릭 타입 T에 추가되고 block 인자를 실행시킨 후에 자신을 리턴
    also는 컨텍스트 객체를 리턴하기 때문에 추가 호출을 함께 연쇄시키기에 용이함
    다수의 also 호출을 함께 연쇄할 수는 있지만 일반적으로 연속 비즈니스 로직 호출은 함수의 일부분으로 추가
*/

/*
    let 함수와 엘비스 연산자 사용하기

    오직 널이 아닌 레퍼런스의 코드 블록을 실행하고 싶지만 레퍼런스가 널이라면 기본값을
    리턴하고 싶은 경우 엘비스 연산자를 결합한 안전 호출 연산자와 함께 let 영역 함수를 사용

    let 함수는 모든 제네릭 타입 T의 확장 함수이며
    컨텍스트 객체가 아닌 블록의 결과를 리턴
    public inline fun <T, R> T.let(block: (T) -> R): R
    (let 함수는 also로 바꿔 쓸 수 있음)
*/

// 문자열 대문자 변경과 특수한 입력 처리
fun processString(str: String) =
        str.let {
            when {
                it.isEmpty() -> "Empty"
                it.isBlank() -> "Blank"
                else -> it.uppercase()
            }
        }

// 널이 될 수 있는 문자열 대문자 변경과 특수한 입력 처리
fun processNullableString(str: String?) =
        str?.let {
            when {
                it.isEmpty() -> "Empty"
                it.isBlank() -> "Blank"
                else -> it.uppercase()
            }
        }
                ?: "Null"

/*
    임시 변수로 let 사용하기

    연산 결과를 임시 변수에 할당하지 않고 처리하고 싶은 경우
    연산에 let 호출을 연쇄하고 let에 제공한 람다 또는 함수 레퍼런스 안에서 결과 처리
*/

fun main() {
    // also로 콘솔 출력과 로그 기록하기
    // val book =
    //         createBook()
    //         .also { println(it) }
    //         .also { Logger.getAnonymousLogger().info(it.toString()) }

    // 서비스의 테스트 (also)
    val site = Site("koolunkle", 37.0, 127.0).also { println(it.name) }
    site

    // 리팩토링 이전 let 예제
    val numbers = mutableListOf("one", "two", "three", "four", "five")
    val resultList = numbers.map { it.length }.filter { it > 3 }
    println(resultList)

    // 리팩토링 이후 let 예제
    numbers.map { it.length }.filter { it > 3 }.let { println(it) }

    // let 블록에서 함수 레퍼런스 사용하기
    numbers.map { it.length }.filter { it > 3 }.let(::println)

    // 예시: 우주에 있는 우주 비행사의 이름 출력하기
    Gson().fromJson(
                    URL("http://api.open-notify.org/astros.json").readText(),
                    AstroResut::class.java
            )
            .people
            .map { it.name }
            .let(::println)
}

class Site(val name: String, val latitude: Double, val longitude: Double)

data class AstroResut(val message: String, val number: Number, val people: List<Assignment>)

data class Assignment(val craft: String, val anme: String)
