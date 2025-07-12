fun main() {
    val point = Point(10, 20)
    println(-point)
    println(point.unaryMinus())
}

data class Point(val x: Int, val y: Int)

// 연산자 오버로딩
operator fun Point.unaryMinus() = Point(-x, -y)

/*
   복소수
   import org.apache.commons.math3.complex.Complex
   operator fun Complex.plus(c: Complex) = this.add(c)
   ... after ...
*/
