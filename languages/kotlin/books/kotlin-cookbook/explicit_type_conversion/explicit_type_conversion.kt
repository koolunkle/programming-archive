fun main() {
    val intVar: Int = 3

    // 컴파일 되지 않음
    // val longVar : Long = intVar

    // 명시적 타입 변환 (byte, short, char, int, long, float, double)
    val longVar: Long = intVar.toLong()
}
