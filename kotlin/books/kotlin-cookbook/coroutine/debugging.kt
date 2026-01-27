/*
    코루틴 디버깅

    코루틴의 실행 정보가 더 많이 필요한 경우
    JVM에서 -Dkotlinx.coroutines.debug 플래그를 사용해서 실행한다

    비동기 프로그램의 디버깅은 다수의 작업이 동시에 일어나기 때문에 항상 어렵다
    다행히 코틀린 라이브러리는 쉬운 디버깅 기능이 포함되어 있다
    JVM에서 코루틴을 디버그 모드로 실행하려면 kotlinx.coroutines.debug 시스템 속성을 사용한다

    디버그 모드는 실행된 모든 코루틴에 고유한 이름을 부여한다
    메인 스레드에서 두 개의 코루틴을 시작하는 작업을 보여주는 예제는 다음과 같다

    suspend fun retrieve1(url: String) = coroutineScope {
        async(Dispatchers.IO) {
            println("Retrieving data on ${Thread.currentThread().name}")
            delay(100L)
            "asyncResults"
        }.await()
    }

    suspend fun retrieve2(url: String) =
        withContext(Dispatchers.IO) {
            println("Retrieving data on ${Thread.currentThread().name}")
            delay(100L)
            "withContextResults"
        }

    fun main() = runBlocking<Unit> {
        val result1 = retrieve1("www.mysite.com")
        val result2 = retrieve2("www.mysite.com")
        println("printing result on ${Thread.currentThread().name} $result1")
        println("printing result on ${Thread.currentThread().name} $result2")
    }

    이 프로그램을 -Dkotlinx.coroutines.debug 플래그와 함께 실행하면 출력은 다음과 같다
    Retrieving data on DefaultDispatcher-worker-1 @coroutine#1
    Retrieving data on DefaultDispatcher-worker-1 @coroutine#2
    printing result on main @coroutine#1 withContextResults
    printing result on main @coroutine#1 asyncResults

    각 코루틴에는 스레드 이름의 일부로 보이는 고유한 이름(@coroutine#1)이 있다
    이는 도움이 되긴 하지만 때때로 생성되는 이름을
    사용하기보다는 코루틴에 직접 이름을 제공하고 싶을 수도 있다

    코틀린 라이브러리에는 코루틴에 이름을 부여할 목적으로 만든 CoroutineName이라는 클래스가 있다
    CoroutineName 생성자는 스레드 이름으로 사용할 수 있는 컨텍스트 원소를 생성한다

    코루틴에 이름 부여하기

    suspend fun retrieve1(url: String) = coroutineScope {
        async(Dispatchers.IO + CoroutineName("async")) { // (문자 그대로) 코루틴 이름 추가
            ... 전과 동일 ...
        }.await()
    }

    suspend fun retrieve2(url: String) =
        withContext(Dispatchers.IO + CoroutineName("withContext")) { // (문자 그대로) 코루틴 이름 추가
            ... 전과 동일 ...
        }

    이제 출력 결과는 다음과 같다
    Retrieving data on DefaultDispatcher-worker-1 @withContext#1
    Retrieving data on DefaultDispatcher-worker-1 @async#2
    printing result on main @coroutine#1 withContextResults
    printing result on main @coroutine#1 asyncResults

    이제 "async"와 "withContext" 라는 단어가 코루틴의 이름으로써 출력에 나타난다
*/

fun main() {}
