/*
    코틀린 버전 알아내기

    코드를 작성해 현재 사용 중인 코틀린의 버전을 알고 싶은 경우
    kotlinVersion 클래스 동반 객체의 CURRENT 속성을 사용한다 (주(Major), 부(Minor), 패치(Patch))
*/

fun main() {
    // 현재 코틀린 버전 출력하기
    println("The current Kotlin version is ${KotlinVersion.CURRENT}")

    val v12 = KotlinVersion(major = 1, minor = 2)
    val v1341 = KotlinVersion(major = 1, minor = 3, patch = 41)

    println(v12 < KotlinVersion.CURRENT)
    println(v1341 <= KotlinVersion.CURRENT)
    println(KotlinVersion(1, 3, 41) == KotlinVersion(major = 1, minor = 3, patch = 41))
}
