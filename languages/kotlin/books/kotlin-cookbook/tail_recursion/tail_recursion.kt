import java.math.BigInteger
import java.math.BigDecimal

/*
    꼬리 재귀 적용하기

    재귀 프로세스를 실행하는 데 필요한 메모리를 최소화(최적화)하고 싶은 경우
    꼬리 재귀를 사용해 프로세스 알고리즘을 표현하고 해당 함수에 tailrec 키워드 추가
    tailrec 변경자를 적용할 수 있는 함수의 자격 요건은 다음과 같다
        1. 해당 함수는 반드시 수행하는 마지막 연산으로써 자신을 호출해야 한다
        2. try/catch/finally 블록 안에서는 tailrec을 사용할 수 없다
        3. 오직 JVM 백엔드에서만 꼬리 재귀가 지원된다
*/

// 재귀 함수로 팩토리얼 구현
fun recFactorial(n: Long): BigInteger =
        when (n) {
            0L, 1L -> BigInteger.ONE
            else -> BigInteger.valueOf(n) * recursiveFactorial(n - 1)
        }

/*
        재귀 팩토리얼 구현 테스트
        @Test
        fun `check recursive factorial`() {
            assertAll(
                { assertThat(recursiveFactorial(0), `is`(BigInteger.ONE)) },
                { assertThat(recursiveFactorial(1), `is`(BigInteger.ONE)) },
                { assertThat(recursiveFactorial(2), `is`(BigInteger.valueOf(2))) },
                { assertThat(recursiveFactorial(5), `is`(BigInteger.valueOf(120))) },
                { assertThrows<StackOverflowError> { recursiveFactorial(10_000) } } // 너무 큰 숫자는 StackOverflowError 발생
            )
        }
*/

// 꼬리 호출 알고리즘을 사용하는 팩토리얼 구현
@JvmOverloads // 어노테이션이 자바에서 오직 1개의 인자로 호출할 수 있게 도와줌
tailrec fun factorial(n : Long, acc : BigInteger = BigInteger.ONE) : BigInteger = // tailrec 키워드 사용 
when(n) {
    0L -> BigInteger.ONE,
    1L -> acc,
    else -> factorial(n - 1, acc * BigInteger.valueOf(n)) // 꼬리 재귀 호출
}

/*
    꼬리 재귀 구현 테스트
    @Test
    fun `factorial tests`() {
        assertAll(
            { assertThat(factorial(0), `is`(BigInteger.ONE)) },
            { assertThat(factorial(1), `is`(BigInteger.ONE)) },
            { assertThat(factorial(2), `is`(BigInteger.valueOf(2))) },
            { assertThat(factorial(5), `is`(BigInteger.valueOf(120))) },
             // ...
            { assertThat(factorial(15000).toString().length, `is`(56130)) },
            { assertThat(factorial(15000).toString().length, `is`(333061)) },
        )
    }
*/

/*
    꼬리 재귀 코틀린 바이트코드를 자바로 디컴파일
    
    public static final BigInteger factorial(long n, BigInteger acc) {
    while(true) {
        BigInteger result;
        if(n == 0L) {
            result = BigDecimal.ONE;
        } else {
            if(n != 1L) {
                result = result.multiply(BigInteger.valueOf(n));
                n = n - 1L;
                continue;
            }
        }
        return result;
    }
}
*/ 

