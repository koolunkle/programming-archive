/*
    디스패처 사용하기

    I/O 또는 다른 작업을 위한 전용 스레드 풀을 사용해야 한다
    Dispatchers 클래스에서 적당한 디스패처를 사용한다

    코루틴은 CoroutineContext 타입의 컨텍스트 내에서 실행된다
    코루틴 컨텍스트에는 CoroutineDispatcher 클래스의 인스턴스에 해당하는 코루틴 디스패처가 포함되어 있다

    디스패처는 코루틴이 어떤 스레드 또는 스레드 풀에서 코루틴을 실행할 지 결정한다
    launch 또는 async 같은 빌더를 사용할 때 CoroutineContext 선택 파라미터를 통해
    사용하고 싶은 디스패처를 명시할 수 있으며 코틀린 표준 라이브러리에는
    다음과 가은 내장 디스패처가 들어 있다
        1. Dispatchers.Default
        2. Dispatchers.IO
        3. Dispatchers.Unconfined

    Dispatchers.Unconfined는 일반적으로 애플리케이션 코드에서 사용해서는 안 된다
    Dispatchers.Default는 평범한 공유 백그라운드 스레드 풀을 사용한다
    (코루틴이 대규모의 계산 리소스를 소모하는 경우에 적합하다)
    Dispatchers.IO는 파일 I/O 또는 블록킹 네트워크 I/O와 같은
    I/O 집약적인 블록킹 작업을 제거하기 위해 디자인되어 생성된
    스레드 주문식(on-demand) 공유 풀을 사용한다

    기본 디스패처와 I/O 디스패처 사용하기

    fun main() = runBlocking<Unit> {
        launchWithIO()
        launchWithDefault()
    }

    suspend fun launcchWithIO() {
        withContext(Dispatchers.IO) {
            delay(100L)
            println("Using Dispatchers.IO")
            println(Thread.currentThread().name)
        }
    }


    suspend fun launcchWithIO() {
        withContext(Dispatchers.Default) {
            delay(100L)
            println("Using Dispatchers.Default")
            println(Thread.currentThread().name)
        }
    }
    *출력: (워커 번호는 다를 수 있음)
        Using Dispatchers.IO
        DefaultDispatcher-worker-3
        Using Dispatchers.Default
        DefaultDispatcher-worker-2

    몇몇 튜토리얼은 newSingleThreadContext와 newFixedThreadPoolContext 함수를 디스패처 생성 함수로 설명한다
    두 함수 모두 구식이며 장차 다르 함수로 대체될 것이다. 비슷한 기능을 사용하려면
    자바 ExecutorService와 asCoroutineDispatcher 함수를 사용한다

    안드로이드 API에는 앞서 설명한 디스패처 외에도 Dispatchers.Main이라는 디스패처가 있다
    Main에서 UI를 갱신하는 모든 작업을 하길 바라는 일반적인 UI 툴킷이지만
    모든 작업에 추가 시간이 필요하거나 Main을 지연시킨다

    안드로이드 Main 디스패처를 사용하려면 kotlinx-coroutines-android 의존성을 추가해야 한다
    gradle 빌드 파일은 다음과 비슷하다

    dependencies {
        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:x.x.x"
        implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:x.x.x"
    }

    안드로이드 컴포넌트 라이브러리에는 수명주기 디스패처도 추가되어 있다
    안드로이드 KTX 라이브러리와 lifecycle-viewmodel 구현에 대한 내용을 확인할 수 있다
    안드로이드는 KTX 라이브러리에 속한 viewModelScope에서 코루틴을 시작할 것을 추천한다
*/

fun main() {}
