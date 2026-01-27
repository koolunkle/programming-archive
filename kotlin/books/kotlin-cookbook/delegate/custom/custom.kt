import kotlin.reflect.KProperty

/*
    사용자 정의 대리자 만들기

    어떤 클래스의 속성이 다른 클래스의 획득자와 설정자를 사용하게끔 만들고 싶은 경우
    ReadOnlyProperty 또는 ReadWriteProperty를 구현하는 클래스를 생성함으로써 직접 속성 대리자를 작성한다
*/

// ReadOnlyProperty와 ReadWriteProperty의 인터페이스
interface ReadOnlyProperty<in R, out T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
}

interface ReadWriteProperty<in R, T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}

// 코틀린 표준 문서의 Delegate (예제) 클래스
// (대리자를 만들려고 ReadOnlyProperty나 ReadWriteProperty 인터페이스를 구현할 필요가 없음)
// (시그니처와 동일한 getValue와 setValue 함수만으로도 충분하다)
class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating ${property.name} in $thisRef"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to ${property.name} in $thisRef")
    }
}

// Delegate 사용하기
class Example {
    var p: String by Delegate()
}

fun main() {
    // Delegate 사용하기
    val e = Example()
    println(e.p)
    e.p = "NEW"
}
