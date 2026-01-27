/*
    함수 이름에 특수 문자 사용하기

    함수 이름을 읽기 쉽게 작성하고 싶은 경우, 밑줄을 사용하거나
    함수 이름을 백틱으로 감싸 읽기 쉽게 만들 수 있다

    * 하지만 이 기법은 테스트 코드에서만 사용하는 것을 권장한다
    * 테스트 함수 이름은 백틱과 밑줄 기법을 둘 다 사용할 수 있다
*/

// 백틱으로 함수 이름을 감싸기
fun `only use backticks on test functions`() {
    println("only use backticks on test functions")
}

// 함수 이름에 밑줄 사용하기
fun underscores_are_also_okay_only_on_tests() {
    println("underscores_are_also_okay_only_on_tests")
}

fun main() {
    `only use backticks on test functions`()
    underscores_are_also_okay_only_on_tests()
}
