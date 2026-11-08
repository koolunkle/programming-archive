# 02. Coroutines — 코루틴과 동시성

코루틴을 "쓸 줄 아는" 단계 다음: 관측·디버깅·성능 측정, 라이프사이클과 맞물린 Flow 수집, 서버에서의 structured concurrency, 그리고 Swift concurrency와의 비교.

## 학습 순서

1. **Structured Concurrency - in Ktor** — `launch`/`async`의 에러 전파와 scope 관리 기본기를 서버 맥락에서 재점검
2. **Flow - Lifecycle-aware Collection** — `repeatOnLifecycle` 등을 직접 구현하며 `suspendCancellableCoroutine`·`callbackFlow`·`produceState` 이해
3. **Debugging - Performance Tuning** — 로깅·컨텍스트·메트릭·dispatcher 튜닝
4. **Debugging - IntelliJ Coroutine Debugger** — 도구 쪽 지원
5. **Swift Concurrency** — 같은 개념의 Swift판과, Kotlin에 없는 컴파일 타임 data-race 검사

발표 간 연결:

- 모든 발표의 공통 기반은 **structured concurrency**: Ktor의 `call` scope, Android의 lifecycle scope, Swift의 task group이 같은 규칙(부모는 자식을 기다리고, 취소는 자식부터)
- 좋은 코루틴 API의 조건 — Flow 발표의 `addRepeatingJob` 폐기 사례와 Ktor 발표의 "scope를 감싸서 주입" 원칙
- Swift의 `Sendable`·actor 격리 ↔ [01 Immutability](<../01-language/README.md>)의 "`copy var`는 경쟁 가능한 람다에 캡처 금지" — Kotlin도 같은 방향을 검토 중

---

## Structured Concurrency - in Ktor

> *Coroutines and Structured Concurrency in Ktor* — Simon Vergauwen · [영상](https://www.youtube.com/watch?v=JxTIZAEos8Y)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.4–10, 16–22

**요약** — Ktor의 `Application`과 `RoutingCall`이 각각 `CoroutineScope`라는 사실에서 출발해, 요청 처리 중 병렬 작업과 백그라운드 작업을 안전하게 다루는 법.

**핵심 내용**

- `RoutingCall : CoroutineScope` → 핸들러 안에서 `call.async { }`로 병렬 실행(LLM 질의 + 메트릭 추적)
- `call.launch`의 세 경우: 정상 / 응답 후 실패(200 응답 + "Unhandled exception" 로그) / 응답 전 실패(형제 작업 취소 → **500**). fire-and-forget은 에러 처리가 필수
- 응답 전에 모두 끝나야 하면 `coroutineScope { launch {...}; launch {...} }` 로 묶기
- `Application : CoroutineScope` → 서버 수명 동안 도는 구독(`PubSub.subscribe(...).collect`)은 `launch`로. Flow의 `catch`(에러별 재throw·emit)와 `onCompletion`(`CancellationException` = 서버 종료) 구분
- 서비스에는 scope를 **주입**(`class MyService(scope: CoroutineScope)` 또는 위임 `CoroutineScope by scope`) — 상속보다 합성
- 원칙: `Deferred`는 반드시 `await`, `launch` 안의 에러는 모두 처리, `CoroutineScope`는 캡슐화

**학습 포인트**

- 세 가지 `launch` 시나리오를 직접 실행해 HTTP 상태 코드와 로그 확인
- 서버 종료 시 `Application` scope의 작업이 어떻게 취소되는지 — graceful shutdown 설계에 반영

---

## Flow - Lifecycle-aware Collection on Android

> *Collect Like a Pro: a deep dive on the Android lifecycle-aware coroutines APIs* — Manuel Vivo · [영상](https://www.youtube.com/watch?v=qJB5iX2cOu0)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.25–26, 39–56, 70–90, 100–114 · KMP 사용 예 [joreilly/Confetti](https://github.com/joreilly/Confetti), [joreilly/PeopleInSpace](https://github.com/joreilly/PeopleInSpace)

**요약** — 앱이 백그라운드일 때 Flow 수집을 멈추는 API 세 가지를 쓰는 법과, 그 **구현을 직접 따라가며** 코루틴 빌더를 익히는 발표.

**핵심 내용**

- cold flow(수집할 때만 생산, 수집자마다 새로 시작) vs hot flow(`SharedFlow`/`StateFlow`, `shareIn`/`stateIn`). `lifecycleScope.launch { collect }`는 STOPPED에서도 계속 수집 → 자원 낭비·위험
- API: `repeatOnLifecycle(STARTED)`, `Flow.flowWithLifecycle`(lifecycle-runtime-ktx), `collectAsStateWithLifecycle`(lifecycle-runtime-compose). 모두 **KMP 호환**(JetBrains 포크 artifact)
- channel이나 buffer를 쓰는 cold flow(`buffer`, `conflate`, `flowOn`, `shareIn`)는 수집이 멈춰도 생산자가 즉시 멈추지 않을 수 있음
- 구현 해부
  - `repeatOnLifecycle` = `suspendCancellableCoroutine` + `LifecycleEventObserver`: `upTo(state)`에서 launch, `downFrom(state)`에서 cancel, `ON_DESTROY`에서 resume. `Dispatchers.Main.immediate`, `Mutex`, `finally`에서 observer 제거
  - `flowWithLifecycle` = `callbackFlow { lifecycle.repeatOnLifecycle { collect { send(it) } }; close() }`
  - `collectAsStateWithLifecycle` = `produceState` + `repeatOnLifecycle`
- 나쁜 API 사례: 폐기된 `addRepeatingJob` — 호출한 코루틴의 context를 보존하지 않아 부모가 취소돼도 취소되지 않음

**학습 포인트**

- `suspendCancellableCoroutine`과 `callbackFlow`로 콜백 API 하나씩 감싸 보기 (`invokeOnCancellation`, `awaitClose` 필수)
- 자기 코드의 "scope를 받아 내부에서 launch하는" API가 structured concurrency를 깨지 않는지 점검

---

## Debugging - Performance Tuning

> *Taming Asynchronous Beasts: Debugging and Performance Tuning in a Coroutine World* — Marcin Moskała · [영상](https://www.youtube.com/watch?v=QTX5_JV4TVU)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.5–13, 20–30 · 도구 [reactor/BlockHound](https://github.com/reactor/BlockHound)

**요약** — 코루틴 환경에서의 관측(로그·컨텍스트) → 디버깅 → 측정·최적화(dispatcher, 동시성 제한, 취소) 체크리스트.

**핵심 내용**

- **관측**: 예외에 맥락 담기, 코루틴 시작·종료 로깅(비용 고려), `CoroutineName`, 프로덕션에서도 `kotlinx.coroutines.debug=on`(코루틴 ID와 **stack trace 복원**)
- 요청 맥락 전파: 커스텀 `CoroutineContext.Element`(`RequestContext`) 또는 SLF4J **MDC + `MDCContext`**(kotlinx-coroutines-slf4j). `CoroutineExceptionHandler`에 로깅 훅
- 디버깅: IntelliJ의 코루틴 parallel stacks, debug agent(Android 런타임 제외)
- **측정**: 기본 지표 외에 큐 길이·캐시 적중·열린 연결 수를 직접 계측 — Flow `buffer` 앞뒤 `onEach`로 대기 원소 수, `shareIn` 구독자 수를 `onSubscription`/`onCompletion`으로 gauge에 기록. Android는 vitals, Firebase Performance, JankStats
- **Dispatcher**: 고정 스레드풀의 대기 작업 수 확인, blocking 호출만 `Dispatchers.IO`로(이미 suspend인 클라이언트를 IO로 감싸지 말 것), **BlockHound**로 blocking 탐지, `Dispatchers.IO.limitedParallelism(20)`로 독립 한도, `Semaphore`/resilience4j `RateLimiter`로 동시 요청 제한
- 취소 범위 구분: `coroutineScope` 안 `launch`(함께 취소) / 외부 `scope.launch`(scope 취소 시만) / `launch(Job())`(절대 취소 안 됨 — 주의)
- 미시 최적화: Regex를 상수로, `List` 포함 검사는 `Set`으로, `@JvmInline value class`, `inline` 고차 함수

**학습 포인트**

- 서버 하나에 `RequestContext` 또는 MDC 전파를 적용해 로그 한 줄로 요청을 추적할 수 있게 만들기
- 테스트에 BlockHound를 붙여 숨은 blocking 호출 찾기
- 외부 API 호출부마다 `limitedParallelism` 또는 `Semaphore`가 필요한지 점검

---

## Debugging - IntelliJ Coroutine Debugger

> *Coroutine Debugging in IntelliJ IDEA* — Alexey Merkulov · [영상](https://www.youtube.com/watch?v=3vaAVtnrzAM)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.2–8, 12–53 (IDE 화면 캡처)

**요약** — 2023년까지 사실상 `println`밖에 없던 코루틴 디버깅이 IntelliJ에서 어떻게 개선됐는지와 stepping 구현 원리.

**핵심 내용**

- 과거의 고통: suspend 함수 step over가 거의 불가능, 일반 breakpoint가 아무 코루틴에서나 멈춤, 최적화로 사라진 변수 때문에 조건부 breakpoint 설정 곤란
- **코루틴 stepping**: 디버거가 코루틴의 시작·suspend·resume 이벤트를 추적해, 스레드가 바뀌어도 **stepping을 시작한 코루틴 안에 머묾**. Step Over / Step Out / Smart Step Into / Run-to-Cursor / launch 안으로 Smart Step Into
- 최적화로 사라지던 지역 변수: Kotlin 언어·API 레벨 2.2에서 개선
- 전체 코루틴 분석(교착·성능·"지금 뭘 하고 있나"): **Coroutines view**, async stack trace, thread dump에 코루틴 dump 포함

**학습 포인트**

- 여러 dispatcher를 오가는 코드에서 step over가 같은 코루틴을 따라가는지 직접 확인
- 교착 상태 재현 코드를 만들어 Coroutines view와 thread dump로 원인 찾기

---

## Swift Concurrency - Compared to Coroutines

> *Swift concurrency for curious Kotliners* — Nikita Bobko · [영상](https://www.youtube.com/watch?v=Jyj4kdK8a6o)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.13–56 (Kotlin ↔ Swift 대조 코드)

**요약** — Swift concurrency는 Kotlin 코루틴과 놀랄 만큼 비슷하지만, Swift 6 strict mode는 **data race를 컴파일 타임에 막는다**는 결정적 차이가 있다.

**핵심 내용**

- 대응 관계: `suspend fun` ↔ `func async`(호출부에 `await` 필수), `suspendCoroutine` ↔ `withCheckedContinuation`, `ensureActive()` ↔ `Task.checkCancellation()`, `coroutineScope { launch }` ↔ `withThrowingTaskGroup { addTask }`(부모는 가장 긴 자식을 기다리고, 취소는 자식부터)
- Swift 6 strict mode
  - 전역 `var`는 오류 → `let`, `@MainActor`, 또는 `nonisolated(unsafe)`(책임은 개발자)
  - 가변 상태를 가진 non-`Sendable` 타입은 공유 불가. `Sendable` 클래스는 가변 프로퍼티 금지. `@Sendable` 클로저가 non-Sendable 값을 캡처하면 경고
- **actor**: 단일 스레드 격리 도메인, 외부 접근은 `await`(메시지). non-Sendable 상태는 actor 밖으로 나갈 수 없음. 메시지는 불변이거나 **`sending`**(이전 격리 도메인을 떠남)이어야 함
- Kotlin은 현재 ObjC를 거쳐 Swift와 interop하며, 앞으로 직접 interop 예정(→ [03 Swift Export](<../03-multiplatform/README.md>))

**학습 포인트**

- Kotlin 코드에서 전역 가변 상태와 스레드 간 공유 객체를 찾아, Swift 6 규칙이라면 어떻게 고쳐야 할지 적용해 보기
- KMP에서 Kotlin 코루틴을 Swift async로 노출할 때(SKIE 등) 취소가 양방향으로 전파되는지 확인
