/*
    지연 스퀀스 사용하기 (특정 조건을 만족시키는 데 필요한 최소량의 데이터만 처리하고 싶은 경우)

    자바 1.8에서 등장한 스트림과 비슷한 코틀린 시퀀스 살펴보기
    컬렉션에서 처리는 즉시 발생하지만, 시퀀스는 지연 처리된다
    데이터를 처리하기 위해 시퀀스를 사용하면 각각의 원소는 자신의
    다음의 원소가 처리되기 전에 전체 파이프라인을 완료한다

    지연 처리 방식은 데이터가 많거나 first 같은 short-curcuit 연산의 경우에 도움이 되며
    원하는 값을 찾았을 때 시퀀스를 종료할 수 있게 도와준다
    (코틀린 시퀀스를 쇼트 서킷 함수와 함께 사용)

    * 특정 조건에 다다를 때까지 오직 필요한 데이터만을 처리하는 방식을 '쇼트 서킷'이라고 한다
    * 컬렉션의 원소를 인자로 받고 불리언을 리턴하는 람다를 '서술 조건자'라고 한다
    * 중간 연산(intermediate operation) / 최종 연산(terminal operation)
*/
import kotlin.math.ceil
import kotlin.math.sqrt

/*
    먼저 주어진 수가 2인지 확인
    2가 아니면 2부터 해당 수의 제곱근 값을 반올림한 수까지를 범위로 생성
    none 함수는 주어진 수를 이 범위의 각자의 수로 나누어 정확히 떨어지는 수를
    이 범위에서 찾을 수 없다면 true를 리턴
*/
fun Int.isPrime() =
        this == 2 ||
                (2..ceil(sqrt(this.toDouble())).toInt()).none { divisor -> this % divisor == 0 }

// 주어진 정수 다음에 나오는 소수 찾기
fun nextPrime(num: Int) = generateSequence(num + 1) { it + 1 }.first(Int::isPrime)

// 초기값과 시퀀스에 속하게 될 다음 값을 생산하는 함수를 인자로 받는다
// fun <T : Any> generateSequence(seed: T?, nextFunction: (T) -> T?): Sequence<T>

/*
    무한 시퀀스 다루기

    무한대의 원소를 갖는 시퀀스의 일부분이 필요할 때, 널을 리턴하는 시퀀스 생성기를 사용하거나
    시퀀스 확장 함수 중에서 takeWhile 같은 함수를 사용하자
*/

// 처음 N개의 소수 찾기
fun firstNPrimes(count: Int) = generateSequence(2, ::nextPrime).take(count).toList()

// 주어진 수보다 작은 모든 소수 (버전 1)
fun primesLessThan(max: Int): List<Int> =
        generateSequence(2) { n -> if (n < max) nextPrime(n) else null }.toList().dropLast(1)

// 주어진 수보다 작은 모든 소수 (버전 2)
fun primesLessThans(max: Int): List<Int> =
        generateSequence(2, ::nextPrime).takeWhile { it < max }.toList()

/*
    시퀀스에서 yield하기

    구간을 지정해 시퀀스에서 값을 생성하고 싶을 때,
    yield 중단 함수와 함께 sequence 함수를 사용한다

    yield 함수는 이터레이터에 값을 제공하고 다음 값을 요청할 때까지 값 생성을 중단한다

    yield가 중단 함수라는 사실은 코루틴과도 잘 동작한다는 의미, 다시 말해 코틀린 런타임은
    코루틴에 값을 제공한 후에 다음 값을 요청할 때까지 해당 코루틴을 중단시킬 수 있다
*/

// sequence 함수의 시그니처
// fun <T> sequence(block: suspend SequenceScope<T>.() -> Unit): Sequence<T>

// sequence를 사용해 피보나치 수 생성하기
fun fibonacciSequence() = sequence {
    var terms = Pair(0, 1)
    while (true) {
        yield(terms.first)
        terms = terms.second to terms.first + terms.second
    }
}

// SequenceScope의 yield와 yieldAll 함수
// abstract suspend fun yield(value: T)

// abstract suspend fun yieldAll(iterator: Iterator<T>)

// suspend fun yieldAll(elements: Iterable<T>)

// suspend fun yieldAll(sequence : Sequence<T>)

fun main() {
    // 3으로 나누어지는 첫 번째 배수 찾기 (버전 1)
    (100 until 200).map { it * 2 }.filter { it % 3 == 0 }.first()

    // 3으로 나누어지는 첫 번째 배수 찾기 (버전 2)
    (100 until 200).map { it * 2 }.first { it % 3 == 0 }

    /*
        3으로 나누어지는 첫 번째 배수 찾기 (최적)
        map, filter와 같은 중간 연산은 새로운 시퀀스 리턴
        first와 같은 최종 연산은 시퀀스가 아닌 다르 것을 리턴
        최종 연산 없이는 시퀀스가 데이터를 처리하지 않음
    */
    (100 until 2_000_000).asSequence().map { it * 2 }.filter { it % 3 == 0 }.first()

    /*
        시퀀스 생성하기

        값으로 이뤄진 시퀀스를 생성하고 싶을 때, 이미 원소가 있다면 sequenceOf를 사용하고
        Iterable이 있다면 asSequence를 사용한다
    */
    val numSequence1 = sequenceOf(3, 1, 4, 1, 5, 9)
    val numSequence2 = listOf(3, 1, 4, 1, 5, 9).asSequence()

    numSequence1.forEach { println(it) }
    println("********************")
    numSequence2.forEach { println(it) }
    println("********************")
    println(10.isPrime())
    println(nextPrime(1_000_000))
    println(firstNPrimes(10))

    val fibonacci = fibonacciSequence().take(10).toList()
    fibonacci.forEach(::println)

    // sequence 안의 yieldAll 함수
    val seq = sequence {
        val start = 0
        yield(start)
        yieldAll(1..5 step 2)
        yieldAll(generateSequence(8) { it * 3 })
    }
    seq.take(7).forEach(::println)
}
