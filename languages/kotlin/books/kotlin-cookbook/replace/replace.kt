/*
    정규표현식과 함께 replace 함수 사용하기

    부분 문자열의 모든 인스턴스를 어떤 값으로 교체하고 싶은 경우
    문자열 인자 또는 정규식을 받도록 중복된 String의 replace 함수를 사용
*/

// 두 개가 중복된 replace 함수
// fun String.replace(oldValue: String, newValue: String, ignoreCase: Boolean = false): String = ""
// fun CharSequence.replace(regex: Regex, replacement: String): String = ""

// 자바 스타일로 작성한 회문 확인 함수
fun isPal(string: String): Boolean {
    // 단어가 아닌 문자를 모두 빈 문자열로 변경
    // \w: 소문자 a-z, 대문자 A-Z, 숫자 0-9, 밑줄을 의미하는 모든 단어 문자
    // \W: \w의 반대 문자
    val testString = string.lowercase().replace("""[\W+]""".toRegex(), "")
    return testString == testString.reversed()
}

// 코틀린 스타일로 작성한 회문 확인 함수
fun String.isPalindrome() =
        this.lowercase().replace("""[\W+]""".toRegex(), "").let { it == it.reversed() }

fun main() {
    // 중복된 두 가지 replace 사용하기
    println("one*two*" == "one.two.".replace(".", "*")) // 2개의 마침표를 별표로 교체
    println(
            "********" == "one.two.".replace(".".toRegex(), "*")
    ) // 각각의 모든 문자를 별표로 교체 (마침표를 모든 단일 글자를 의미하는 정규표현식으로 취급)
    println(isPal("???"))
    println("???".isPalindrome())
}
