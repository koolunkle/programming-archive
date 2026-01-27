/*
    자바 스레드 풀에서 코루틴 실행하기

    코루틴을 사용하는 사용자 정의 스레드 풀을 제공하고 싶은 경우
    자바 ExecutorService의 asCoroutineDispatcher 함수를 사용한다 (newSingleThreadContext, newFixedThreadPoolContext 관련)

    코틀린 라이브러리에는 ExecutorService에 asCoroutineDispatcher 라는 확장 메소드 추가
    asCoroutineDispatcher 함수는 ExecutorService의 인스턴스를 ExecutorCoroutineDispatcher의 구현으로 변환된다

    asCoroutineDispatcher 함수를 사용하려면 Executors 클래스로
    사용자 정의 스레드 풀을 정의한 다음 디스패처로 사용할 수 있게 변환한다

    코루틴 디스패처로써 스레드 풀 사용하기

    fun main() = runBlocking<Unit> {
        val dispatcher = Executors.newFixedThreadPool(10) // 크기가 10인 스레드 풀 생성
            .asCoroutineDispatcher()

        withContext(dispatcher) { // 생성한 스레드 풀을 코루틴을 위한 디스패처로 사용
            delay(100L)
            println(Thread.currentThread().name)
        }

        dispatcher.close() // 스레드 풀 종료
    }

    실행 결과는 시스템이 코루틴을 실행하기 위해 스레드 풀 1의 스레드 2를
    선택했음을 나타내는 poo1-thread-2를 출력한다

    디스패처의 close 함수를 호출하지 않으면 계속 실행된다
    즉, main 함수가 절대 종료되지 않으므로 close 함수 호출은 필수다

    close 함수 호출로도 잘 동작하지만 ExecutorService를 중단시키기 위해
    shutdown 또는 shutdownNow를 호출한다

    스레드 풀 종료하기

    val pool = ExecutorService.newFixedThreadPool(10)
    withContext(pool.asCoroutineDispatcher()) {
        ...
    }
    pool.shutdown()

    상기 접근 방식의 문제점은 개발자가 shutdown 메소드를 호출해야 한다는 사실을 잊어버릴 수도 있다는 것이다
    자바에서는 해당 문제를 close 메소드가 있는 AutoCloseable 인터페이스를 구현함으로써 해결하고
    따라서 코드를 try-with-resources 블록으로 감쌀 수 있다

    그러나 여기서 호출해야 하는 메소드는 close가 아니라 shutdown인데
    코틀린 라이브러리 개발자들은 이전 코드에서 생성한 인스턴스인
    Executor CoroutineDispatcher 클래스의 구현을 변경하였다

    ExecutorCoroutineDispatcher가 Closeable 인터페이스를 구현하도록 리팩토링했기 때문애
    새 추상 클래스의 이름을 CloseableCoroutineDispatcher라고 명명하였으며 close 메소드는 다음과 같다

    abstract class ExecutorCoroutineDispatcher: CoroutineDispatcher(), Closeable {
        abstract override fun close()
        abstract val executor: Executor
    }

    하위 클래스 안에서
    override fun close() {
        (executor as? ExecutorService)?.shutdown()
    }

    자바 ExecutorService를 사용해서 생성한 디스패처는 기저에 있는
    ExcutorService 종료할 close 함수를 가지고 있다

    코틀린은 자바와 비슷한 try-with-resources 구문을 지원하지 않지만
    자바 Closeable 인터페이스에 확장 함수로 정의된 use 함수를 사용하여
    자바 ExecutorService를 종료하는 쉬운 해결책을 제공한다

    use를 사용해서 디스패처 닫기

    Executors.newFixedThreadPool(10).asCoroutineDispatcher().use {
        withContext(it) {
            delay(100L)
            println(Thread.currentThread().name)
        }
    }
*/
