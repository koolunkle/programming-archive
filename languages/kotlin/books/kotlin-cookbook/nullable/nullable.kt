fun main() {
    var name: String

    // ... after ...

    name = "Dolly" // null이 아닌 문자열 할당
    // name = null // null을 할당하면 컴파일되지 않음

    class Person(val first: String, val middle: String?, val last: String)

    val jkRowling = Person("Joanne", null, "Rowling")
    val northwest = Person("North", null, "West")

    val p = Person(first = "North", middle = null, last = "West")

    if (p.middle != null) {
        // null 할당이 불가능한 문자열 타입으로 영리한 타입 변환(smart cast) 수행
        // val middleNameLength = p.middle.length

        // null이 아님을 단언(assert)(꼭 필요한 경우가 아니라면 사용하지 말 것)
        // val middleNameLength = p.middle!!.length

        // 안전 호출, 결과 타입은 Int?
        // val middleNameLength : Int? = p.middle?.length

        // null일 경우 엘비스 연산자는 0을 리턴
        val middleNameLength = p.middle?.length ?: 0
    }

    // 안전 타입 변환 연산자
    val p1 = p as? Person
}
