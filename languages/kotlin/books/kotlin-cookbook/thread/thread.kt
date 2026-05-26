import kotlin.concurrent.thread
import kotlin.random.Random

/*
    스레드 시작하기

    코드 블록을 동시적 스레드에서 실행하고 싶은 경우
    kotlin.concurrent 패키지의 thread 함수를 사용한다

    코틀린은 스레드를 쉽게 생성하고 시작할 수 있는 확장 함수 thread를 제공한다
    thread 확장 함수의 시그니처는 다음과 같다

    thread 확장 함수가 요구하는 코드 블록은 인자가 없고 Unit을 리턴하는 람다이다
    이는 Runnable 인터페이스 또는 Thread의 run 메소드 시그니처와 일치한다

    thread 확장 함수는 자신의 본문에서 생성한 스레드를 리턴하기 때문에
    스레드의 join 메소드를 사용해서 모든 스레드를 순차적으로 호출하게 만들 수 있다
*/

fun thread(
        start: Boolean = true,
        isDaemon: Boolean = true,
        contextClassLoader: ClassLoader? = null,
        name: String? = null,
        priority: Int = -1,
        block: () -> Unit
): Thread {
    val thread =
            object : Thread() {
                public override fun run() {
                    block()
                }
            }
    if (isDaemon) thread.isDaemon = true
    if (priority > 0) thread.priority = priority
    if (name != null) thread.name = name
    if (contextClassLoader != null) thread.contextClassLoader = contextClassLoader
    if (start) thread.start()
    return thread
}

fun main() {
    // 데몬 스레드 시작하기
    (0..5).forEach { n ->
        val sleepTime = Random.nextLong(1000L)
        /*
            데몬 스레드
            백그라운드에서 동작하며, 주로 서비스 스레드의 보조 역할을 수행하거나 특정 작업을 주기적으로 처리하기 위한 목적으로 사용
        */
        thread(isDaemon = true) { // 스레드가 데몬 스레드이기 때문에 스레드 실행이 끝나기 전에 프로그램이 종료됨
            Thread.sleep(sleepTime)
            println("${Thread.currentThread().name} for $n after ${sleepTime}ms")
        }
    }

    // 여러 스레드 조인하기
    (0..5).forEach { n ->
        val sleepTime = Random.nextLong(1_000L)
        thread {
                    Thread.sleep(sleepTime)
                    println("${Thread.currentThread().name} for $n after ${sleepTime}ms")
                }
                .join()
    }
}
