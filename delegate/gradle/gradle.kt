/*
코틀린 DSL에서 project와 extra 속성을 만들고 접근하기

val myProperty: String by project // project 속성 myProperty를 사용 가능하게 만듦
val myNullableProperty: String? by project // null이 될 수 있는 속성을 사용 가능하게 만듦

val myNewProperty by extra("initial value") // extra 속성 myNewProperty를 만들고 초기화
val myOtherNewProperty by extra { "lazy initial value" } // 처음 접근이 일어날 때 초기화되는 속성을 생성

-PmyProperty=value 문법을 사용해 명령줄에서 프로젝트 속성을 설정하거나
gradle.properties 파일에서 프로젝트 속성을 설정할 수 있다
* extra 속성은 인자를 사용하거나 처음 접근 시에 평가되는 람다를 사용해 정의할 수 있다
*/

fun main() {}
