# 03. Multiplatform — KMP, Compose Multiplatform, Kotlin/Native

Kotlin Multiplatform(KMP)의 실제 도입 사례, iOS·Swift와의 경계, Kotlin/Native 런타임과 링크, 라이브러리 설계를 다룬다. 2025.05 기준으로 Compose Multiplatform 1.8에서 iOS가 stable, Swift Export는 2.2.20에서 실험적 출시 예정, Kotlin/Wasm·Compose for Web은 Beta 진입 직전이었다.

## 구성

| 위치 | 내용 |
| --- | --- |
| `case-studies/` | AWS, Google Workspace, X, McDonald's, RevenueCat, Posten Bring, Stone의 도입 경험 |
| `ios/` | Compose Multiplatform iOS, Swift interop 패턴, Swift Export 내부 구조 |
| `native/` | Kotlin/Native 메모리·GC, 네이티브 의존성과 링크, AppKit interop 예제 |
| (루트) | DI로 규모 키우기, 멀티플랫폼 라이브러리 개발, Wasm |

## 학습 순서

1. **case-studies/** — 먼저 "어디까지 공유하는가"의 스펙트럼을 파악: 데이터 계층만(X) → 비즈니스 규칙(Logistics) → 내비게이션까지(McDonald's) → UI까지(Stone)
2. **ios/Interop Patterns** → **ios/Swift Export** — 실무에서 가장 마찰이 큰 경계
3. **native/Runtime** → **native/Dependency Management** — 문제가 생겼을 때 이해해야 할 바닥
4. **Dependency Injection** — 모듈이 늘어날 때의 구조
5. **Library Development** + **case-studies/AWS**, **RevenueCat** — 라이브러리 작성자 관점
6. **ios/Compose Multiplatform**, **Wasm** — UI 공유의 현재 상태

발표 간 연결:

- 공통으로 지적된 고통: Swift interop(X, Interop Patterns), iOS 빌드 시간·CI 비용(Stone), 전이 iOS 의존성 처리(RevenueCat, Dependency Management)
- Google Workspace의 GC 튜닝 경험 ↔ **native/Runtime**의 GC 구조 설명 — 같은 Google K/N 팀(Troels Lund)
- AWS의 "public API에 data class·enum 금지" ↔ [06 Compatibility](<../06-build-tooling/README.md>) — 바이너리 호환성

---

## case-studies/

### AWS - Scaling a Massive KMP SDK

> *Blueprints for Scale: What AWS Learned Building a Massive Multiplatform Project* — Ian Botsford, Matas Lauzadis · [영상](https://www.youtube.com/watch?v=fv-vw1huJ-4)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.6–8, 21–49

**요약** — 499개 모듈, 860만 줄(98% 생성 코드), 8개 타깃, 주 5회 릴리스하는 AWS SDK for Kotlin의 설계 원칙 다섯 가지.

**핵심 내용**

- **의존성을 소유하라**: OkHttp·CRT를 공통 인터페이스로 감싸되 엔진 고유 설정은 상속한 config builder로 노출. 직렬화는 kotlinx.serialization 대신 자체 구현(생성자 제약, 스트리밍·XML·HTTP 바인딩 부족) — kotlinx 설계를 참고해 필드(포맷 무관)와 토큰(포맷별) 분리, 빌드 타임 코드 생성, 리플렉션 없음
- **확장을 위한 구조**: 단일 목적 모듈로 dependency closure 최소화, 빌드 스크립트·lint·CI를 별도 저장소로 공유
- **처음부터 KMP로 설계**: 두 번째 타깃 추가가 가장 어렵다 — `java.*` 대신 `kotlin.*`(`Clock.System`, `kotlin.random`, `Regex`, atomicfu)
- **하위 호환성**: public API에 `data class` 금지(생성자·`copy`·구조분해가 파라미터 추가 시 깨짐, 기본값 추가도 바이너리 호환 깨짐 → builder + DSL `invoke`). `enum` 대신 **sealed class + `SdkUnknown(value)`** (구버전 클라이언트가 새 값을 역직렬화 가능)
- **유지보수자 경험**: changelog 검증, 아티팩트 크기 diff, binary-compatibility-validator 등 정보가 풍부하고 override 가능한 CI 검사

**학습 포인트**

- 자기 라이브러리의 public API에서 `data class`/`enum` 사용처를 찾아 이 기준으로 위험도 평가
- 서버 응답에 새 enum 값이 추가됐을 때 구버전 클라이언트에서 실제로 무슨 일이 생기는지 재현

### Google Workspace - Deploying KMP

> *Deploying Kotlin Multiplatform at Google Workspace* — Jason Parachoniak, Troels Lund · [영상](https://www.youtube.com/watch?v=5lkZj4v4-ks)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.16, 23–24

**요약** — J2ObjC로 10년 넘게 로직을 공유해 온 Google Docs가 Kotlin/Native로 전환한 1년간의 A/B 실험 결과.

**핵심 내용**

- 경로: Xplat Java → Java to Kotlin → KMP(JVM·Closure JS·Native). Docs 사용자 상당 비율에 KMP 변형이 배포됨
- 성능: tight loop(10~50ms)는 별도 작업 없이 ~20% 개선, 앱 수준 지표(500ms+)는 평균 동등하지만 상위 백분위에서 25% 빠름 — 직선 실행 속도 이득과 GC 비용의 교환
- **GC 튜닝**: 수동 GC 호출 → CMS(concurrent mark & sweep)로 전환(빠르지만 메모리 증가) → `targetHeapUtilization` 75%, `minHeapBytes` 25MB로 조정. 사용자 입력 대기 중 GC 유도
- **String 개선**: UTF-16 배열이라 Latin-1 문자열은 절반이 0 → JEP 254처럼 첫 char를 인코딩 헤더로, 패딩에 hashCode 캐시 (IPA 크기에 결정적)
- IPA 크기 J2ObjC 수준 — `objcExportEntryPointsPath`로 ObjC/Swift 진입점을 명시해 tree shaking (위험하지만 효과 큼). GC 초기화 data race(KT-70364, 앱 시작의 0.001%) 발견
- 보너스: `kotlin.native.runtime.Debugging.dumpMemory` → kdump → **hprof 변환** 후 IntelliJ로 힙 분석

**학습 포인트**

- iOS 앱에서 heap dump를 떠서 hprof로 변환해 보는 절차를 한 번 실습해 둘 것
- K/N GC 옵션별(기본 vs CMS, heap utilization) 메모리·지연 트레이드오프를 측정 가능한 지표로 정리

### X - Rewriting Android with KMP

> *One Codebase, Three Platforms: X's Experience with Kotlin Multiplatform* — Arkadii Ivanov, Eric Denman · [영상](https://www.youtube.com/watch?v=z-u99yZFn5o)
>
> 예제 코드: 없음 (코드 없이 아키텍처·사례 중심)

**요약** — 축소된 팀이 X Android를 전면 재작성(X-Lite)하면서 KMP를 데이터 계층부터 적용한 경험.

**핵심 내용**

- 구조: 기능별 Gradle 모듈, 일부만 KMP(Android·iOS·Web), **UI는 네이티브**(Compose Multiplatform 미사용). 스택: Ktor+OkHttp, Apollo GraphQL, SQLDelight / Android: Compose, Dagger+Anvil, Coil
- 공유 범위: iOS는 데이터 계층만, presentation·내비게이션은 플랫폼별, 도메인은 부분 공유
- **Circuit → Decompose 전환**: presenter 테스트의 인지 부하·취약성, 다른 플랫폼과 공유 불가, state 안의 콜백·Flow, Parcelize 의존. Decompose 컴포넌트는 Web으로 export해 React가 관리
- E2E 암호화 DM 재작성: 1명(Android/KMP)이 iOS 2·웹 1·백엔드 1과 협업 — 버그 수정 한 곳, KMP 단위 테스트, 로그·DB·파일 구조 표준화
- 단점: Kotlin 버전 호환성, SKIE를 써도 거친 Swift interop, `@JsExport` 난잡함, 디버깅, 웹의 SQLDelight(WebWorkerDriver 포크, OPFS)

**학습 포인트**

- "UI는 네이티브, 로직은 공유" 전략에서 presenter/컴포넌트 계층을 어디에 둘지 — Circuit과 Decompose의 공유 가능성 차이 정리

### McDonalds - Shared Navigation

> *Leveraging KMP for navigation in the McDonalds App* — Cas van Luijtelaar, Anthony Bassey · [영상](https://www.youtube.com/watch?v=HSIhkB5bGJs)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.7–21

**요약** — 62개 시장, 코드베이스 75%가 KMP인 McDonald's 앱에서 **내비게이션 결정까지 공유**하되 네이티브 스택은 건드리지 않는 방법.

**핵심 내용**

- 원칙: KMP는 네이티브 내비게이션 스택·뒤로가기 제스처를 소유하지 않고 "꼭두각시처럼 조종"만. 네이티브 UI는 로직 없는 얇은 클라이언트
- KMP 쪽: 화면당 하나의 `Route`(파라미터·serializer·route guard), 필요한 데이터를 모두 담은 `NavigationEvent`를 클라이언트로 emit → 각 플랫폼 `NavigationHandler`가 실행
- **Route guard**: 모든 내비게이션 이벤트에서 조건을 평가해 목적지를 바꿈(로그인 필요 등). 딥링크도 KMP의 link mapper가 route로 변환
- 교훈: 기존 앱에 끼워 넣지 말고 이 구조를 중심으로 앱을 설계할 것

**학습 포인트**

- 자기 앱의 화면 전이 규칙(인증, 온보딩, 딥링크) 중 플랫폼 간에 중복된 것을 찾아 공통 route guard로 옮길 수 있는지 검토

### RevenueCat - Wrapping Native SDKs

> *Making native SDKs Multiplatform at RevenueCat* — Joop Korteweg · [영상](https://www.youtube.com/watch?v=5Sc3Qdb0XoQ)
>
> 예제 코드: 슬라이드 p.9, 13–26 · 발표 대상 SDK [RevenueCat/purchases-kmp](https://github.com/RevenueCat/purchases-kmp)

**요약** — 기존 Android·iOS SDK를 다시 쓰지 않고 위에서부터 감싸 KMP SDK를 만들고, 전이 iOS 의존성과 리소스 문제를 푼 과정.

**핵심 내용**

- 보통 KMP는 하위 계층(네트워크·저장소)부터 도입하지만, 목표가 "기존 SDK를 KMP에서 쓰기 쉽게"였으므로 **top-down**: 새 KMP 라이브러리가 기존 SDK에 의존
- `expect class` + `actual typealias` 는 오버헤드는 없지만 플랫폼 API를 노출 → **wrapper 클래스** 채택
- 문제: KMP 라이브러리는 전이 iOS 의존성을 자동 해결하지 못함 → 사용자가 SPM/CocoaPods로 따로 추가해야 하고 버전이 `1.7.8+13.32.0` 형태
- 해법: iOS SDK를 **정적 라이브러리로 klib에 포함**(cinterop `.def` + `xcrun swift build`), [swift-klib](https://github.com/ttypic/swift-klib-plugin)으로 자동화하고 의존성 선언을 위해 플러그인 수정
- 정적 라이브러리엔 리소스가 없음 → 리소스를 Kotlin 코드로 생성하고 Swift 쪽 `ResourceProvider` 프로토콜을 Kotlin에서 구현해 주입. 대안은 Gradle 플러그인이 되는 것([sentry-kotlin-multiplatform](https://github.com/getsentry/sentry-kotlin-multiplatform) 참고)

**학습 포인트**

- [purchases-kmp](https://github.com/RevenueCat/purchases-kmp)에서 wrapper 계층 구조 확인
- 정적 포함 방식의 단점(사용자가 전이 의존성을 별도로 쓸 수 없음, 의존성 그래프가 커지면 관리 곤란)을 **native/Dependency Management**의 링크 규칙과 연결해 이해

### Logistics - Sharing Business Rules

> *Kotlin Multiplatform's Cross-Platform Brilliance* — Anshika Koul · [영상](https://www.youtube.com/watch?v=OuX5325yq_I)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.14–18

**요약** — 물류 회사(Posten Bring)에서 흩어진 배송 규칙(위험물, 부재 배송 등)을 KMP 라이브러리로 모아 JVM 백엔드와 JS 프론트엔드에 배포.

**핵심 내용**

- 같은 규칙(`ShippingRules.isShippedByBoat`)을 `jvmMain`에서는 `@JvmStatic`, `jsMain`에서는 `@JsExport`로 노출 → Maven 아티팩트와 **npm 패키지**로 동시 배포
- 효과: 신뢰성·배포 위험·릴리스 주기 개선. 프로덕트 오너가 코드를 읽고 PR을 올릴 정도로 규칙이 한곳에 정리됨
- 과제: 팀의 도입 망설임, 기술 사일로 위험, TypeScript 정의 생성 같은 실험적 기능

**학습 포인트**

- 모바일이 아닌 **백엔드 + 웹 프론트** 조합에서의 KMP 활용 — 검증 규칙·요금 계산처럼 양쪽에서 중복 구현된 로직 찾기

### Fintech - Two Years to 55% Shared Code

> *Two Years with Kotlin Multiplatform: From Zero to 55% Shared Code* — Rodrigo Sicarelli · [영상](https://www.youtube.com/watch?v=M9Nni0Qywyo)
>
> 예제 코드: 없음 (코드 없이 조직·도입 사례 중심)

**요약** — 브라질 핀테크 Stone이 세 앱의 기능 75%가 같은데 전 앱 배포에 평균 두 달 걸리던 문제를 KMP + Compose Multiplatform 플랫폼 팀으로 해결한 2년.

**핵심 내용**

- 기술 평가: Flutter·React Native 등과 코드 재사용, 시장 채택, 팀 지식, iOS 지식 필요량, 성숙도로 비교 → KMP 선택(조직 상황에 따라 다를 수 있다는 단서)
- 플랫폼 구성: 사내 Gradle 플러그인, Jade 디자인 시스템, 기존 Android/iOS 저장소와 Gradle interop, Compose Multiplatform
- 교육: 학습 트랙, 내부 커뮤니티, 문서, 학습 지원금. DORA 지표와 PR 지표(코딩 시간, merge까지 시간)로 생산성 측정
- 마찰: XCFramework release 빌드 2시간 이상(Kotlin 2.1 새 링커·LLVM 16으로 1시간 단축), `@HiddenFromObjC` 오용이 링크 속도에 영향, CI 비용 증가(klib은 macOS 필요했으나 2.1부터 Linux에서 publish 가능)

**학습 포인트**

- KMP 도입 효과를 DORA·PR 지표로 측정하는 방법을 자기 조직에 맞게 설계
- iOS 빌드 시간 줄이기: 링커 버전, export 범위(`@HiddenFromObjC`), Linux에서 klib publish

---

## ios/

### Compose Multiplatform - iOS Stable Release

> *Compose Multiplatform for iOS* — Sebastian Aigner · [영상](https://www.youtube.com/watch?v=YKTlW8Qkj0w)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.6, 11, 24, 47–52

**요약** — Compose Multiplatform 1.8에서 iOS가 stable(API 안정·호환성 보장)이 된 것과 그 생태계.

**핵심 내용**

- 원하는 만큼만 도입: SwiftUI 안에 Compose를, Compose 안에 SwiftUI를 넣을 수 있고 Android에는 오버헤드 없음
- 체감 품질: 60/120Hz, 실험적 **parallel rendering**(`ComposeUIViewController(configure = { parallelRendering = true })`로 렌더링을 별도 스레드로), VoiceOver·AssistiveTouch·Full Keyboard Access
- 도구: KMP IDE 플러그인(마법사, preflight check, preview, 실행·디버그), **Compose Hot Reload**(Desktop JVM + JetBrains Runtime/DCEVM, 모바일은 desktop 타깃으로 UI 작업)
- 공통화된 컴포넌트: ViewModel(beta), 타입 안전 Navigation(딥링크, 뒤로 제스처), 이미지 로딩·DI·저장소 생태계. Navigation 3 예정, Web은 연내 beta
- `UIKitViewController`로 AVPlayer 같은 네이티브 뷰를 Kotlin에서 직접 사용

**학습 포인트**

- 기존 SwiftUI 앱에 Compose 화면 하나를 끼워 넣는 최소 구성 만들기
- parallel rendering을 켰을 때 메인 스레드 부하 변화 측정

### Interop Patterns - Swift, SKIE, Compose

> *Kotlin and Compose Multiplatform patterns for iOS interop* — John O'Reilly · [영상](https://www.youtube.com/watch?v=O0BqoLcRuJI)
>
> 예제 코드: 발표자 샘플 [joreilly/Confetti](https://github.com/joreilly/Confetti), [joreilly/PeopleInSpace](https://github.com/joreilly/PeopleInSpace), [joreilly/BikeShare](https://github.com/joreilly/BikeShare) 외 다수 · 슬라이드 p.14–43, 48–113, 119–138

**요약** — Swift↔Kotlin interop의 모든 경로를 실제 오픈소스 앱(PeopleInSpace, Confetti 등) 예제로 정리한 카탈로그.

**핵심 내용**

- 기본 interop은 Objective-C 경유라 enum·sealed class·기본 인자·오버로드·coroutine이 손실 → **SKIE**(sealed class를 Swift `switch onEnum(of:)`로, suspend→`async`, Flow→`AsyncSequence`, SwiftUI `Observing`), **KMP-NativeCoroutines**, **KMP-ObservableViewModel**(공유 ViewModel을 `@StateViewModel`로)
- **Swift 코드를 KMP에서 호출**: Kotlin interface를 Swift가 구현해 DI로 주입(Koin, kotlin-inject) 또는 Decompose에 Swift 클로저 전달 — Firebase Vertex AI·Analytics 예제
- Compose UI interop: `ComposeUIViewController`(Compose→UIKit), `UIKitViewController`(UIKit→Compose), SwiftUI 쪽 `UIViewControllerRepresentable`/`UIHostingController`. 네이티브 뷰 팩토리를 `CompositionLocal`로 전달
- 그 외: iOS background task(`BGTaskScheduler`)를 KMP에서 예약, Maestro UI 테스트, `-Xexport-kdoc`, 배포(KMMBridge, multiplatform-swiftpackage, umbrella framework, spmForKmp), fastlane

**학습 포인트**

- 같은 sealed class를 기본 interop / SKIE로 각각 노출해 Swift 쪽 사용 코드 비교
- "Swift 구현을 Kotlin interface로 주입" 패턴을 하나 구현 — 플랫폼 SDK를 공유 코드에서 쓰는 가장 범용적인 방법

### Swift Export - Internals

> *Swift Export - a peek under the hood* — Artem Olkov · [영상](https://www.youtube.com/watch?v=KEsVNrzPf24)
>
> 예제 코드: 공식 샘플 [Kotlin/swift-export-sample](https://github.com/Kotlin/swift-export-sample) · 슬라이드 p.14–47, 75–85, 96–141

**요약** — Objective-C를 거치지 않고 Kotlin을 Swift에서 직접 호출하게 하는 Swift Export의 사용법과 내부 구조. 2.2.20 실험적 출시.

**핵심 내용**

- ObjC export의 문제: 모듈마다 같은 이름의 최상위 함수가 `UtilsKt`, `UtilsKt_`, `UtilsKt__`로 뭉개짐 → Swift Export는 **모듈 단위**(`LoginModule.initialize()`)
- 새 기능: 멀티 모듈, 패키지, typealias, primitive nullability, 오버로드. 설정은 `swiftExport { moduleName; export(lib) { flattenPackage } }`, Xcode에서 `embedSwiftExportForXcode`
- 설계 결정: Swift 컴파일러 수정이나 Kotlin→Swift 트랜스파일 대신 **Kotlin은 평소처럼 바이너리로 컴파일하고 Swift wrapper 소스를 생성**
- 파이프라인: K2 **Analysis API**(KSP2·Dokka도 사용)로 Kotlin 선언 읽기 → Swift IR(SIR) → 세 계층 생성: Kotlin bridge(타입→포인터) / C 헤더 / Swift wrapper(호출부 정리)
- 런타임: `KotlinBase` 상속 + `externalRcRef`로 두 GC(Kotlin tracing, Swift ARC)를 연결. 언어 비호환(예: 클래스와 같은 이름의 함수) 처리
- ObjC interop은 폐기하지 않음

**학습 포인트**

- [swift-export-sample](https://github.com/Kotlin/swift-export-sample)로 ObjC export 결과와 생성된 Swift API 비교
- 현재 지원 범위는 [기능 현황 문서](https://kotl.in/swift-export-feature-status)에서 확인 — 발표 시점엔 coroutine·generic 등 미지원 항목이 많았음

---

## native/

### Runtime - Object Lifecycle & Memory

> *The Life and Death of a Kotlin/Native Object* — Troels Lund · [영상](https://www.youtube.com/watch?v=ufQvrZmQw_I)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.5–30, 59–74, 118–134

**요약** — Kotlin/Native 객체가 할당되고, GC에 의해 표시·회수되고, ObjC/Swift 경계를 넘을 때 일어나는 일.

**핵심 내용**

- 레이아웃: 헤더 + 필드, 정렬을 위해 필드 재배치. 크기는 상속 구조와 무관하게 필드 크기 합을 8바이트 cell로 올림
- 할당: 크기별 **FixedBlockPage**에서 빈 블록을 찾고, 가득 차면 새 페이지
- GC: shadow stack에서 루트 수집 → stop-the-world mark(TypeInfo의 `objOffsets`로 참조 탐색) → sweep(Kotlin 스레드가 할당 시 필요한 페이지를 직접 sweep하며 협력). 살아남은 양만큼 다시 할당되면 다음 GC
- **Concurrent mark**(`-Xgc=cms`): snapshot-at-the-beginning — 시작 시 살아 있던 객체와 새 객체는 생존, **write barrier**로 마킹 중 참조 변경 추적
- interop: Kotlin 객체가 ObjC 객체를 잡으면 refcount 증가(finalizer에서 감소), 반대는 global root. **Kotlin GC는 ObjC 쪽 순환 참조를 볼 수 없음**. weak reference는 read barrier로 처리

**학습 포인트**

- Kotlin↔Swift 사이 순환 참조(클로저 캡처, delegate)로 누수가 생기는 시나리오를 만들어 보고 weak로 끊기
- Google Workspace 발표의 CMS 전환 결과를 이 GC 구조로 설명해 보기

### Dependency Management

> *Dependencies and Kotlin/Native* — Tadeas Kriz · [영상](https://www.youtube.com/watch?v=n0LpCCv3VEY)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.7–9, 16 (의존성 선언 조각, 나머지는 다이어그램)

**요약** — JVM의 jar/classpath 모델과 Darwin 네이티브의 정적·동적 라이브러리 링크 모델을 비교하고, Kotlin/Native가 그 사이에서 겪는 문제.

**핵심 내용**

- JVM: jar = 클래스 파일 zip, 클래스→멤버 2단계 심볼, 실행 시 classpath. 합쳐도 문제없음
- 네이티브: 심볼은 **단일 수준 텍스트 이름**(ObjC 메타데이터는 `OBJC_CLASS_$_` 접두어로 인코딩). `.a`(오브젝트 파일 묶음)는 링크 시 결과물에 포함, `.dylib`은 load command로 참조되어 실행 시 dyld가 로드
- K/N은 klib(Kotlin IR)로 JVM처럼 의존성을 다루지만 결과물은 네이티브 바이너리 + cinterop으로 순수 네이티브 라이브러리 사용 + CocoaPods 플러그인 → 두 세계가 섞임
- **정의되지 않은 동작**: 같은 정적 라이브러리를 여러 동적 바이너리에 링크하면 심볼이 중복되고 어느 쪽이 쓰일지 정해지지 않음 → 앱 시작 시 경고를 무시하지 말 것
- 시스템 라이브러리 미해결(`ld: Undeclared symbol sqlite3_...`)은 `--linker-option=-lsqlite3`(LC_LINKER_OPTION)로

**학습 포인트**

- KMP framework를 static/dynamic으로 바꿔 빌드하고 `otool -L`, `nm`으로 결과 비교
- "정적 링크가 친구"라는 결론이 RevenueCat의 정적 포함 전략과 어떻게 맞물리는지 정리

### macOS Screen Saver - Native App Example

> *Building a macOS screen saver with Kotlin* — Márton Braun · [영상](https://www.youtube.com/watch?v=Zw7eJFciwXg)
>
> 예제 코드: [zsmb13/KotlinLogo-ScreenSaver](https://github.com/zsmb13/KotlinLogo-ScreenSaver) · 슬라이드 p.10–25, 32–41, 49–55

**요약** — Swift 셸 하나에 로직은 모두 Kotlin/Native로 쓴 macOS 화면 보호기. 플랫폼 API interop 실전 예제.

**핵심 내용**

- 구성: Xcode의 `ScreenSaverView` 서브클래스가 Gradle로 만든 static XCFramework의 Kotlin 뷰에 위임
- AppKit·Foundation을 Kotlin에서 그대로: `NSBundle`, `NSImageView`, `NSUserDefaults`(property delegate로 감싸 설정 저장), `NSNotificationCenter`, `NSOpenPanel`, `NSFileManager`
- macOS 함정: `legacyScreenSaver` 프로세스가 뷰를 계속 생성하며 종료되지 않음 → 개발 중 재설치 7단계, `willStop` 알림에서 직접 terminate

**학습 포인트**

- [KotlinLogo-ScreenSaver](https://github.com/zsmb13/KotlinLogo-ScreenSaver/)를 읽으며 Swift API 문서를 Kotlin 호출로 옮기는 감각 익히기 (예: `bundle.image(forResource:)` → `imageForResource`)

---

## Dependency Injection - Scaling KMP Projects

> *Scale your Kotlin Multiplatform projects using dependency injection* — Ralf Wondratschek · [영상](https://www.youtube.com/watch?v=oRKytkr2FOY)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.13–209 전반 · 라이브러리 [evant/kotlin-inject](https://github.com/evant/kotlin-inject), [amzn/kotlin-inject-anvil](https://github.com/amzn/kotlin-inject-anvil)

**요약** — Amazon에서 Dagger+Anvil을 KMP로 옮길 수 없어 **kotlin-inject-anvil**을 만든 배경과, 수동 DI에서 컴파일 타임 DI까지의 단계별 진화.

**핵심 내용**

- 단계: 수동 생성 → 수동 Component → service locator(컴파일 타임 안전성·순환 탐지 없음) → 리플렉션 기반 → 컴파일 타임 생성. 각 단계에서 테스트 교체, 짧은 수명 scope, 외부 클래스, 보일러플레이트 문제가 어떻게 드러나는지
- **kotlin-inject-anvil**: `@Inject @SingleIn(AppScope::class) @ContributesBinding(AppScope::class)` 만 붙이면 `@MergeComponent`가 모듈 간 바인딩을 모음. KSP로 플랫폼별 컴포넌트 생성, common에서는 `expect fun KClass<AppComponent>.createComponent()`
- **expect/actual 대체**: common의 기본 구현을 플랫폼 source set에서 `@ContributesBinding(replaces = [...])`로 교체 → Gradle 의존성 그래프와 DI 그래프 정렬
- 플랫폼 객체 주입: Android는 `Application`을 컴포넌트 파라미터로, iOS는 `IosNativeImplementations` interface로 Swift 구현 주입
- 장단점: KMP 지원·컴파일 타임 안전성·적은 보일러플레이트 vs 낮은 채택률·KSP 빌드 시간·불명확한 에러 메시지. 대안으로 [Metro](https://zacsweers.github.io/metro)(컴파일러 플러그인) 언급

**학습 포인트**

- 발표의 단계를 직접 따라 작성해 보며 각 DI 방식이 해결하는 문제 정리
- KSP 기반(kotlin-inject-anvil) vs 컴파일러 플러그인 기반(Metro) — [01 Compiler Plugins](<../01-language/README.md#compiler-plugins---writing-your-own>) 발표에서 Metro가 거의 모든 확장 지점을 쓰는 사례로 소개됨
- [App Platform](https://amzn.github.io/app-platform)(발표자의 Amazon 아키텍처 프레임워크)

## Library Development - Multiplatform Settings

> *Multiplatform Settings: A case study in Multiplatform library development* — Russell Wolf · [영상](https://www.youtube.com/watch?v=vMNCAryfJys)
>
> 예제 코드: [russhwolf/multiplatform-settings](https://github.com/russhwolf/multiplatform-settings) (발표 대상 라이브러리) · 슬라이드 p.13–23, 26–27, 33–39

**요약** — 2018년 Kotlin/Native 기술 프리뷰 시절부터 지금까지 가장 오래된 KMP 라이브러리 중 하나를 유지해 온 설계 결정의 역사.

**핵심 내용**

- 처음의 `expect class Settings` → **interface + 플랫폼별 구현**(`SharedPreferencesSettings`, `NSUserDefaultsSettings`, `StorageSettings`, `KeychainSettings`, `RegistrySettings`…)으로 전환. `expect` 클래스는 진화시키기 어렵고 호환성을 깨기 쉬움
- 기능을 모든 구현이 지원할 수 없어 `Settings` / **`ObservableSettings`** 분리. Android는 키를 주고 iOS는 변경만 알려 주므로 이전 값 캐시로 동작 통일
- coroutine 지원: `SuspendSettings` / `FlowSettings`(DataStore가 유일한 구현), 동기↔비동기 변환은 `runBlocking`이라 불만족스럽다고 인정
- kotlinx.serialization 통합: 객체를 속성별 여러 키로 저장 — nullability·리스트 처리, 트랜잭션이 없어 일관성 문제, 회귀 버그가 가장 많은 영역
- 저장소: [russhwolf/multiplatform-settings](https://github.com/russhwolf/multiplatform-settings)
- 유지보수 현실: 외부 기여 PR 리뷰, 미완성 기여를 직접 마무리할지, Keychain 기본 접근성 변경이 기존 데이터 호환을 깨는 문제

**학습 포인트**

- 라이브러리 API를 `expect class`로 할지 interface로 할지 판단 기준 정리 (AWS 발표의 호환성 원칙과 함께)
- 플랫폼마다 기능이 다를 때 interface를 어떻게 나눌지 — `Settings`/`ObservableSettings` 분리를 다른 도메인에 적용

## Wasm & Compose for Web - Current State

> *State of Kotlin/Wasm and Compose Multiplatform for Web on Modern Browsers* — Pamela Hill · [영상](https://www.youtube.com/watch?v=kIEBQ_czdxs)
>
> 예제 코드: 슬라이드 p.3–35 · 템플릿 [Kotlin/kotlin-wasm-browser-template](https://github.com/Kotlin/kotlin-wasm-browser-template), 실제 앱 [JetBrains/kotlinconf-app](https://github.com/JetBrains/kotlinconf-app)

**요약** — Kotlin/Wasm과 Compose for Web의 Beta 진입 준비 상태와 사용 시점 판단.

**핵심 내용**

- Kotlin/Wasm: 브라우저 프론트엔드(DOM 직접 조작 또는 Compose)와 서버(Node.js 등) 모두 대상
- 개선: 증분 컴파일 2배(2.1.0 opt-in), 브라우저 디버깅 시 변수 보기(Chromium custom formatter), 바이너리 30% 감소(2.1.0 기본)
- Compose for Web: drag-and-drop(1.8), HTML 요소 interop 진행 중, 접근성 예정. JetBrains는 **canvas 기반** 렌더링에 집중하고 HTML 기반 Compose는 커뮤니티(Kobweb)에 맡김
- 언제 쓰나: 웹을 포함해 로직과 UI를 모두 공유하고 싶을 때, 플랫폼 간 일관된 UI가 중요할 때

**학습 포인트**

- [kotlinconf-app](https://github.com/JetBrains/kotlinconf-app)의 Wasm 타깃을 빌드해 번들 크기·초기 로딩 측정
- canvas 기반 UI의 한계(SEO, 텍스트 선택, 접근성)가 자기 서비스에 문제가 되는지 판단
- 라이브러리 작성자라면 Wasm 타깃 지원 추가 검토
