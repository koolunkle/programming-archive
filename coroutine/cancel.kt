/*
    코루틴 취소하기

    코루틴 내의 비동기 처리를 취소하고 싶은 경우
    launch 빌더 또는 withTimeout 이나 withTimeoutOrNull 같은 함수가
    리턴하는 Job 레퍼런스를 사용한다

    잡 취소하기

    fun main() = runBlocking {
        val job = launch {
            repeat(100) { i ->
                println("job: I'm waiting $i...")
                delay(100L)
            }
        }
        delay(500L)
        println("main: That's enough waiting")
        job.cancel()
        job.join()
        println("main: Done")
    }

    launch 빌더는 Job 인스턴스를 리턴하고 이를 지역 변수에 할당한다
    그다음 repeat 함수를 사용해서 100개의 코루틴을 시작한다

    launch 블록의 밖에서 main 함수는 이 모든 코루틴을 기다리기 지루해 해당 잡을 취소했다
    join 함수는 해당 잡이 완료될 때까지 기다린 다음 프로그램을 종료한다
    (cancel과 join 호출을 결합한 cancelAndJoin 함수도 있다)
    * 출력:
        job: I'm waiting 0...
        job: I'm waiting 1...
        job: I'm waiting 2...
        job: I'm waiting 3...
        job: I'm waiting 4...
        main: That's enough waiting
        main: Done

    잡을 취소하려는 이유가 시간이 너무 오래 걸려서라면
    withTimeout 함수를 사용할 수 있으며 시그니처는 다음과 같다

    suspend fun <T> withTimeout(
        timeMillis: Long,
        block: suspend CoroutineScope.() -> T
    ): T

    withTimeout 함수는 코루틴 안의 일시 중단 블록의 코드를 실행하고
    만약 타임아웃을 초과하면 TimeoutCancellationException을 던진다

    withTimeout 사용하기
    fun main() = runBlocking {
        withTimeout(1000L) {
            repeat(50) { i ->
                println("job: I'm waiting $i...")
                delay(100L)
            }
        }
    }
    * 출력:
        job: I'm waiting 0...
        job: I'm waiting 1...
        job: I'm waiting 2...
        job: I'm waiting 3...
        job: I'm waiting 4...
        job: I'm waiting 5...
        job: I'm waiting 6...
        job: I'm waiting 7...
        job: I'm waiting 8...
        job: I'm waiting 9...
        Exception in thread "main"
            kotlinx.coroutines.TimeoutCancellationException:
        Timed out waiting for 1000 ms
        at kotlinx.coroutines.TimeoutKt.TimeoutCancellationException(Timeout.kt:126)

        원한다면 TimeoutCancellationException을 캐치하거나 타임 아웃시에
        예외를 던지는 대신 null을 리턴하는 withTimeoutOrNull을 사용할 수 있다
*/

/*
    안드로이드에서 잡 취소하기

    안드로이드는 UI 스레드에서 동작하는 Dispatchers.Main이라는 추가 디스패치를 제공한다
    일반적인 구현 패턴은 MainActivity가 CoroutineScope를 구현하고,
    컨텍스트가 필요할 때 컨텍스트를 제공하고 필요하면 컨텍스트를 닫는다

    안드로이드에서 디스패처 사용하기

    class MainActivity : AppCompatActivity(), CoroutineScope {
        override val coroutineContext: CoroutineContext // 중복된 + 연산자를 사용해서 컨텍스트를 생성
            get() = Dispatchers.Main + job

        private lateinit var job: Job // 준비 되면 속성을 초기화

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState) // 이제 준비됨
            job = Job()
        }

        override fun onDestroy() {
            job.cancel() // 액티비티가 소멸되면 잡을 취소
            super.onDestroy()
        }
    }
*/

/*
    안드로이드에서 코루틴 시작하기

    fun displayData() {
        launch { // coroutineContext 속성을 사용해서 시작
            val data = async(Dispatchers.IO) { // 네트워크 호출을 위해 Dispatchers.IO로 변경
                ... 네트워크로 데이터 얻기 ...
            }
            updateDisplay(data.await()) // UI 갱신을 위해 Dispatchers.Main으로 돌아가기
        }
    }
*/

fun main() {}
