/*
   컴파일 타임 상수는 반드시 객체나 동반 객체(companion object) 선언의 최상위 속성 또는 멤버여야 한다
   컴파일 타임 상수는 문자열 또는 기본 타입의 래퍼 클래스(Byte, Short, Int, Long, Float, Double, Char, Boolean)이며
   사용자 정의 획득자(getter)를 가질 수 없고, 컴파일 시점에 값을 사용할 수 있도록 main 함수를 포함한
   모든 함수의 바깥쪽에서 할당되어야 한다
*/

fun main() {
    val task = Task("koolunkle", 10)
    println("name: ${task.name}\n priority: ${task.priority}")
}

class Task(val name: String, _priority: Int = DEFAULT_PRIORITY) {

    // 컴파일 타임 상수
    companion object {
        const val MIN_RIOIRTY = 1
        const val MAX_RIOIRTY = 5
        const val DEFAULT_PRIORITY = 3
    }

    // 사용자 정의 설정자(setter)를 사용하는 속성
    var priority = validPriority(`_priority`)
        set(value) {
            field = validPriority(value)
        }

    // privagte 검증 함수
    private fun validPriority(p: Int) = p.coerceIn(MIN_RIOIRTY, MAX_RIOIRTY)
}
