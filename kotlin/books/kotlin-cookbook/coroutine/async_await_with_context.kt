/*
    async/await을 withContext 변경하기

    async로 코루틴을 시작하고 바로 다음에 코루틴이 완료될 동안 기다리는
    await 코드를 간소화하고 싶은 경우 async/await 조합을 withContext로 변경한다

    CoroutineScope 클래스에는 withContext라는 확장 함수도 정의되어 있다
    withContext 확장 함수의 시그니처는 다음과 같다

    suspend fun <T> withContext(
        context: CoroutineContext,
        block: suspend CoroutineScope.() -> T
    ): T

    코틀린 공식 문서에서 withContext는 "주어진 코루틴 컨텍스트와 함께 명시한 일시정지 블록을 호출하고
    완료될 때까지 일시정지한 후에 그 결과를 리턴한다" 라고 나와 있다
    실제로 withContext는 async와 바로 다음에 있는 await의 조합을 대체하기 위해 사용한다

    async와 await을 withContext로 대체하기

    suspend fun retrieve1(url: String) = coroutineScope {
        async(Dispatchers.IO) {
            println("Retrieving data on ${Thread.currentThread().name}")
            delay(100L)
            "asyncResults"
        }.await()
    }

    suspend fun retrieve2(url: String) = coroutineScope {
        withContext(Dispatchers.IO) {
            println("Retrieving data on ${Thread.currentThread().name}")
            delay(100L)
            "withContextResults"
        }
    }

    fun main() = runBlocking<Unit> {
        val result1 = retrieve1("www.mysite.com")
        val result2 = retrieve2("www.mysite.com")
        println("printing result on ${Thread.currentThread().name} $result1")
        println("printing result on ${Thread.currentThread().name} $result2")
    }
    * 출력: (순서는 다를 수 있음)
        Retrieving data on DefaultDispatcher-worker-2
        Retrieving data on DefaultDispatcher-worker-2
        printing result on main withContextResults
        printing result on main asyncResults
*/
