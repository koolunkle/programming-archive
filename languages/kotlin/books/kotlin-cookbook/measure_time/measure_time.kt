import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

/*
    경과 시간 측정하기

    코드 블록이 실행되는 데 걸린 시간을 알고 싶은 경우 코틀린 표준 라이브러리의
    measureTimeMillis 또는 measureNanoTime 함수를 사용한다
    보다 정확하게 성능을 측정하려면 오픈 JDK의 자바 마이크로벤치마크 도구(JMH) 프로젝트를 사용
*/

// 코드 블록의 경과 시간 측정하기
fun doubleIt(x: Int): Int {
    Thread.sleep(100L)
    println("doubling $x with on thread ${Thread.currentThread().name}")
    return x * 2
}

// measureTimeMillis 함수의 구현
public inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}

fun main() {
    // 코드 블록의 경과 시간 측정하기
    println("${Runtime.getRuntime().availableProcessors()} processors")

    /*
        [IntStream]
        원시 타입 int 값들의 시퀀스를 다루는 데 특화된 스트림
        Integer 객체 대신 int 원시 타입을 직접 사용하기 때문에 오토박싱/언박싱으로 인한 성능 오버헤드를 줄일 수 있음

        [rangeClosed]
        IntStream의 정적 팩토리 메소드 중 하나, 지정된 범위의 정수 시퀀스 생성
        rangeClosed(startInclusive, endInclusive)는 startInclusive부터 endInclusive까지의 모든 정수를 포함하는 IntStream 생성
        range(startInclusive, endExclusive)는 endExclusive은 포함하지 않는 모두 정수의 IntStream 생성

        [Parallel]
        스트림을 병렬 스트림으로 전환하는 중간 연산자
        해당 메소드를 호출하면 후속 스트림 연산들이 병렬로 실행될 수 있도록 스트림 속성 변경
        메소드 호출 시 JVM은 Fork/Join 프레임워크를 사용하여 스트림의 요소를 여러 청크로 분할하고, 이 청크들을 별도의 스레드에서 동시에 처리
        병렬 처리에는 데이터 분할, 스레드 관리, 결과 병합 등의 추가적인 오버헤드가 발생
        데이터의 양이 적거나 연산이 매우 간단할 경우에는 오히려 순차 스트림보다 성능이 저하될 수 있음
    */
    var time = measureTimeMillis { IntStream.rangeClosed(1, 6).map { doubleIt(it) }.sum() }
    println("Sequential stream took ${time}ms")

    time = measureTimeMillis { IntStream.rangeClosed(1, 6).parallel().map { doubleIt(it) }.sum() }
    println("Parallel stream took ${time}ms")
}
