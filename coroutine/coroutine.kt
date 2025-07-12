/*
    코루틴과 구조적 동시성

    코루틴은 개발자가 동시성 코드를 마치 동기 코드처럼 작성할 수 있게 한다
    코루틴 지원으로 콜백 메소드 또는 리액티브 스트림과 같은 다르 방법들보다
    훨씬 쉽게 동시적 코드를 작성할 수 있다
    * 동시성 관리: 독립된 다수의 작업을 조율하고, 취소와 예외를 다루는 등의 작업이 주된 요소

    연관된 주제로는 코루틴 영역과 코루틴 컨텍스트 사용하기,
    적절한 코루틴 빌더와 디스패처 선택하기, 코루틴 동작 조정하기가 있으며
    코루틴을 일시정지(suspend)하고 재개(resume)할 수 있다는 것이 코루틴의 개념이다

    suspend 키워드와 함께 함수를 생성하면 복잡한 멀티스레딩 코드를
    직접 작성하지 않고도 함수를 임시로 정지하고 나중에 다른 스레드에서
    이 정지된 함수를 다시 재개할 수도 있다는 것을 시스템에게 알려준다
*/

/*
    코루틴 빌더 선택하기

    코루틴을 생성하는 올바른 함수를 선택해야 한다
    사용 가능한 빌더 함수 중에 선택한다

    새 코루틴을 생성하려면 빌더 함수 runBlocking, launch, async 중 하나를 사용할 수 있다
    runBlocking 빌더 함수는 최상위 함수인 반면 launch와 async는 CoroutineScope의 확장 함수이다

    GlobalScope에 정의된 launch와 async가 완전하게 제거될 것이 아니라면 이를 사용하지 말 것을 권장한다
    시작하는 코루틴이 특정 코루틴 잡에도 할당되지 않고 영구적으로 취소되지 않으면
    애플리케이션의 전체 수명주기에 걸쳐 실행되는 문제점이 있기 때문이다
    반드시 사용해야 할 이유가 없다면 GlobalScope의 launch와 async는 사용하지 않는다
*/

/*
    runBlocking 빌더

    runBlocking은 명령줄 검증 또는 테스트에 유용하다
    runBlocking은 현재 스레드를 블록하고 모든 내부 코루틴이 종료될 때까지 블록한다
    runBlocking 함수의 시그니처는 다음과 같다

    fun <T> runBlocking(block: suspend CoroutineScope.() -> T): T

    runBlocking 함수 자체는 suspend 함수가 아니므로 보통 함수에서 호출할 수 있다
    runBlocking 함수는 인자로써 CoroutineScope에 확장 함수로 추가될 suspend 함수를 받고
    이 인자로 받은 함수를 실행하고, 실행한 함수가 리턴하는 값을 리턴한다
*/

/*
    launch 빌더

    독립된 프로세스를 실행하는 코루틴을 시작하고, 해당 코루틴에서
    리턴값을 받을 필요가 없다면 launch 코루틴 빌더를 사용한다

    launch 함수는 CoroutineScope의 확장 함수이기 때문에
    CoroutineScope를 사용 가능한 경우에만 사용할 수 있다

    launch 함수는 코루틴 취소가 필요하면 사용할 수 있는
    Job 인스턴스를 리턴하며 함수 시그니처는 다음과 같다

    fun CoroutineScope.launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ): Job

    CoroutineContext는 다른 코루틴과 상태를 공유하기 위해 사용한다
    CoroutineStart 파라미터는 DEFAULT, LAZY, ATOMIC 또는 UNDISPATCHED 값으로 된 열거형 클래스이다

    launch 함수는 마지막 파라미터로 제공된 람다는 반드시 인자가 없는
    일시 중단 함수이고 아무것도 리턴하지 않아야 한다
*/

/*
    async 빌더

    값을 리턴해야 하는 경우에는 일반적으로 async 빌더를 사용한다
    async 빌더도 CoroutineScope의 확장 함수이며 시그니처는 다음과 같다

    fun <T> CoroutineScope.async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T
    ): Deffered<T>

    launch 빌더처럼 async의 CoroutineContext와 CoroutineStart 파라미터도 적절한 기본값이 있다
    async 빌더는 파라미터로써 제공한 일시 중단 함수는 값을 리턴하면 지연된(Deffered) 인스턴스로 해당 값을 감싼다
    지연된 인스턴스는 자바스크립트의 Promies 또는 자바의 Future와 비슷한 느낌을 준다

    Deffered에서 알아야 할 중요한 함수가 생산된 값을 리턴하기 전에
    코루틴이 완료될 때까지 기다리는 await이다
*/

/*

    async로 코루틴 생성하기

    suspend fun add(x: Int, y: Int): Int {
        delay(Random.nextLong(1000L)) // 최대 1000 밀리초의 랜덤 지연
        return x + y
    }

    suspend fun main() = coroutineScope { // 또 다른 코루틴 빌더
        val firstSum = async { // async를 사용해서 코루틴 시작하기
            println(Thread.currentThread().name)
            add(2, 2)
        }

        val secondSum = async { // async를 사용해서 코루틴 시작하기
            println(Thread.currentThread().name)
            add(3, 4)
        }

        println("Awaiting concurrent sums...")
        val total = firstSum.await() + secondSum.await() // 코루틴이 종료될 때까지 블록하기 위해서 await 호출
        println("Total is $total")
    }

    * 출력: DefaultDispatcher-worker-2
            Awaiting concurrent sums...
            DefaultDispatcher-worker-1
            Total is 11

    delay 함수는 코루틴을 실행하고 있는 스레드를 블록하지 않고
    코루틴을 대기 상태로 만드는 일시 중단 함수이다
    반면 runBlocking 호출은 프로그램이 종료되기 전에 모든 것이 완료될 때까지 기다린다

    출력되는 줄의 순서는 생성된 임의의 지연 값에 따라 달라진다
*/

/*
    coroutineScope 빌더

    coroutineScope 함수는 종료 전에 포함된 모든 코루틴이
    종료될 때까지 기다리는 일시 중단 함수이다

    runBlocking과는 다르게 메인 스레드를 블록하지 않는 것은 장점이지만
    반드시 일시 중단 함수의 일부로써 호출되어야 한다

    정의된 영역 안에 코루틴을 사용해야 한다는 점은 코루틴 사용 기본 원칙 중 하나이다
    coroutineScope의 이점은 코루틴 완료 여부를 확인하기 위해
    코루틴을 조사해야 할 필요가 없다는 것이다

    coroutineScope는 자동으로 모든 자식 코루틴이 완료될 때까지 기다리며
    함수 시그니처는 다음과 같다

    suspend fun <R> coroutineScope(
        block: suspend CoroutineScope.() -> R
    ): R

    coroutineScope 함수는 리시버를 이용해서 인자가 없고 제네릭 값을 리턴하는 람다를 받는다
    일시 중단 함수이기 때문에 반드시 일시 중단 함수 또는 다르 코루틴에서 호출돼야 한다
    간단한 사용 예제를 코틀린 공식 홈페이지에서 볼 수 있다

    coroutineScope 빌더 사용하기

    suspend fun main() = coroutineScope { // coroutineScope 빌더
        for (i in 0 until 10) { // 10개의 코루틴 시작
            launch {
                delay(1000L - i * 10) { // 줄어드는 시간으로 각각을 지연
                    print("$i ")
                }
            }
        }
    }
    * 출력:
        9 8 7 6 5 4 3 2 1 0

    상기 예제는 코루틴의 일반적인 패턴을 보여준다
    coroutineScope로 시작해서 코루틴이 모두 포함된 영역을 설정하고
    결과 블록 안에서 개별 작업을 다루기 위해 launch 또는 async를 사용할 수 있다

    이후 해당 영역은 프로그램 종료 전에 모든 코루틴이 완료될 때까지 기다리고
    만약 코루틴이 하나라도 실패하면 나머지 코루틴을 취소한다 (구조화된 동시성)
    이를 통해 루틴의 완료 여부를 조사하지 않고도 균형 있는 제어와
    에러 처리를 달성하고 루틴이 실패하는 경우를 처리하지 않는 것을 방지한다
*/

fun main() {
    /*
        runBlocking 함수 사용하기

        println("Before creating coroutine")
        runBlocking {
            print("Hello, ")
            delay(200L)
            println("World!")
        }
        println("After coroutine is finished")

        * 출력:
            Before creating coroutine
            Hello, World!
            After coroutine finished
    */

    /*
        launch 함수 사용하기

        println("Before runBlocking")
        runBlocking {
            println("Before launch")
            launch {
                print("Hello, ")
                delay(200L)
                println("World!")
            }
            println("After launch")
        }
        println("After runBlocking")

        * 출력:
            Before runBlocking
            Before launch
            After launch
            Hello, World!
            After runBlocking
    */
}
