# 04. Compose & Android — UI와 Android 아키텍처

Compose UI의 표현력(커스텀 레이아웃·도형·애니메이션), 개발 도구(Hot Reload, Notebooks, IDE 자체의 Compose 전환), 접근성, 새로운 폼팩터(VR), 그리고 AndroidX 라이브러리의 KMP 전환. 2025.05 기준으로 Compose Hot Reload는 공개 직후, Navigation3·NavigationEvent는 alpha01, Kotlin Notebooks의 Compose 지원은 개발 중이었다.

## 학습 순서

1. **Custom UI** — `Layout`, `GenericShape`, `drawWithCache`, frame 단위 애니메이션까지 Compose UI 도구 상자 전체를 한 화면으로
2. **Animation** — `AnimationSpec`·`Easing`의 정의와 직접 구현
3. **Accessibility** — semantics tree를 의도대로 구성하는 법
4. **Architecture Components** — AndroidX가 플랫폼 타입(`Bundle`)을 KMP로 떼어낸 방법과 Navigation3
5. **Hot Reload** — 도구의 내부. JVM·Compose 컴파일러·런타임 지식을 한 번에 엮는 발표
6. **Compose for Desktop**, **Prototyping**, **VR** — Compose가 쓰이는 다른 자리들

발표 간 연결:

- 애니메이션 API 두 층위: **Animation**(선언적 `animate*AsState` + 스펙) ↔ **Custom UI**(`Animatable` + 코루틴으로 직접 시퀀싱, `withFrameMillis` 루프)
- 빠른 UI 반복의 선택지: Preview → **Prototyping**(Notebooks) → **Hot Reload**. 각각 무엇을 포기하는지가 다름
- KMP 라이브러리 설계: **Architecture Components**(`expect class` + `actual typealias`로 플랫폼 타입 보존) ↔ [03 RevenueCat](<../03-multiplatform/README.md>)(typealias 대신 wrapper 선택) — 같은 선택지에서 다른 결론
- Compose stability와 불변 데이터 ↔ [01 Immutability](<../01-language/README.md>), [08 Data-Oriented Design](<../08-design-practices/README.md>)

---

## Custom UI - Building Complex Layouts

> *Creative UIs with Compose* — Chris Horner · [영상](https://www.youtube.com/watch?v=9KdP2idt6LE)
>
> 예제 코드: [chris-horner/persona-im](https://github.com/chris-horner/persona-im)

**요약** — 평면 디자인의 획일성에 대한 문제 제기와 함께, 게임(Persona 5) 메신저 UI를 Compose로 그대로 재현하는 과정.

**핵심 내용**

- 비정형 도형: `GenericShape { moveTo; lineTo }`(dp를 px로 바꾸려면 `Density` 확장으로), `drawBehind`로 텍스트 뒤에 겹친 도형 그리기. Figma 도형을 코드로 옮기는 도구 언급
- 겹치는 배치: `Row` + `offset`의 한계 → **`Layout { measurables, constraints -> }`**로 아바타와 말풍선을 직접 측정·배치
- 리스트를 가로지르는 배경 선: `LazyListState.layoutInfo.visibleItemsInfo`의 offset으로 보이는 항목 사이를 연결, `drawWithCache`로 Path 재사용, `BlurMaskFilter`로 그림자
- 상태 위치: 메시지 목록은 ViewModel, 선 좌표·애니메이션 진행도는 **UI 상태 홀더**(`@Stable class TranscriptState` + `rememberTranscriptState()`)
- 애니메이션: 항목마다 `Animatable`들을 코루틴으로 시퀀싱(`delay` → `snapTo` → `animateTo(EaseOutBack)`), 선 길이는 `lerp`로 진행도 반영
- 파티클: `withFrameMillis` 루프로 frame 시간 제공, 객체 **pool**로 할당 없이 재사용

**학습 포인트**

- [persona-im](https://github.com/chris-horner/persona-im) 코드로 `Layout` 측정·배치 흐름과 `TranscriptState` 설계 따라가기
- `drawBehind` vs `drawWithCache` 차이 — Path 객체를 매 프레임 새로 만들지 않는 이유
- 앱에서 "지루해도 되는" 화면과 표현력이 필요한 화면을 구분해 보기

## Animation - AnimationSpec & Easing

> *The Easing Symphony: AnimationSpec in detail!* — Nicole Terc · [영상](https://www.youtube.com/watch?v=clDGqPfaIto)
>
> 예제 코드: [nicole-terc/easing-symphony](https://github.com/nicole-terc/easing-symphony)

**요약** — 음악 시각화(파형·진폭·주파수)를 예제로 `AnimationSpec`과 `Easing`의 역할을 구분하고, 둘 다 직접 만들어 보는 발표. 슬라이드에 없는 데모 18개는 발표 영상에서 볼 수 있다.

**핵심 내용**

- 예제 기반: KMP `MusicReader`(Android `MediaPlayer`, iOS `AVAudioPlayer`, Desktop `Clip`)가 `StateFlow`로 파형·진폭·주파수 제공 → `Canvas`에 그리기
- **AnimationSpec** = 애니메이션 명세(데이터 타입 + 설정). 내부는 `vectorize(converter)` → `VectorizedAnimationSpec`의 `getValueFromNanos`/`getVelocityFromNanos`/`getDurationNanos`. 기본 제공: `spring`(dampingRatio, stiffness), `tween`(duration, easing), `keyframes`
- **Easing** = 시간 비율 → 진행 비율 함수(`fun interface Easing { fun transform(fraction: Float): Float }`). 시간 기반 스펙(tween, keyframes)에만 존재. "스펙은 경로, easing은 속도"
- 커스텀 easing: `CubicBezierEasing(0.68f, -0.55f, 0.27f, 1.55f)`, 수식(`x²`, 사인파 `fraction + a·sin(2π·n·fraction)`). 영감은 easings.net
- 커스텀 AnimationSpec: `VectorizedAnimationSpec<AnimationVector2D>`를 구현해 시작→끝 직선에 수직으로 사인파 오프셋을 더하는 `SineAnimationSpec` → `animateOffsetAsState`에 적용

**학습 포인트**

- [easing-symphony](https://github.com/nicole-terc/easing-symphony)에서 같은 진폭 입력에 spring·tween·keyframes·커스텀 스펙을 바꿔 끼우며 비교
- spring이 easing을 갖지 않는 이유(물리 기반이라 duration이 결과로 정해짐) 설명해 보기
- 커스텀 `VectorizedAnimationSpec`을 만들 때 velocity를 올바르게 계산해야 하는 이유 — 애니메이션이 중간에 대상 값이 바뀌어 이어질 때

---

## Accessibility - Semantics & Accessibility Scanner

> *Building Inclusive Jetpack Compose Apps* — Vanessa Johnson · [영상](https://www.youtube.com/watch?v=PW_qc6EUBQE)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.27–44

**요약** — TalkBack 사용자 관점에서 Compose의 semantics tree를 의도대로 구성하는 법과 흔한 실수.

**핵심 내용**

- 원칙: WCAG의 POUR(Perceivable, Operable, Understandable, Robust). 전 세계 인구 15%(10억 명)가 장애를 가짐
- navigational cue: 일관된 색·명확한 계층·설명적 레이블·익숙한 컨트롤
- `Modifier.semantics { }`로 노출 정보 조정: `role`(Button, Checkbox…), `contentDescription`, `stateDescription`, accessibility action
- 흔한 실수: 낮은 대비, 작은 터치 영역(30dp), 빈 `contentDescription`, **같은 정보를 이미지·체크박스·행에 중복 서술**해 TalkBack이 여러 번 읽음
- 해법: 행 단위로 `semantics(mergeDescendants = true) { stateDescription = ... }` + `clickable`로 한 번에 읽히게. 수동 테스트(TalkBack) + 자동 테스트 + **Accessibility Scanner**

**학습 포인트**

- 자기 앱의 목록 항목 하나를 TalkBack으로 탐색해 몇 번 멈추고 무엇을 읽는지 기록 → `mergeDescendants`로 정리
- Accessibility Scanner로 대비·터치 영역 문제 일괄 점검

---

## Architecture Components - Decoupling for KMP

> *Lessons learned decoupling Architecture Components from platform specific code* — Jeremy Woods, Marcello Galhardo · [영상](https://www.youtube.com/watch?v=jDz_yNZkEzk)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.30–36, 55–79, 90–107, 122–130, 148–170

**요약** — AndroidX Architecture Components 팀이 기존 라이브러리(SavedState)를 KMP로 옮긴 방법과 새 라이브러리(NavigationEvent)를 KMP로 새로 만든 방법의 비교.

**핵심 내용**

- 6명이 14개 라이브러리(Activity, Lifecycle, Navigation, Room, SavedState, ViewModel…)를 담당. Android는 2019년부터 Kotlin-first
- **SavedState 이전**(1.3.0, 2025.05): 핵심 API가 `android.os.Bundle`에 의존 — Bundle은 플랫폼 팀 소유라 확장 함수가 멤버에 가려짐 → `expect class SavedState` + Android에서는 **`actual typealias SavedState = Bundle`**(비용 없는 추상화), 그 외 플랫폼은 Map 기반. 읽기·쓰기는 `@JvmInline value class SavedStateReader/Writer`
- kotlinx.serialization 통합: `encodeToSavedState`/`decodeFromSavedState`, `SavedStateHandle`·Activity·Fragment·Nav 백스택에서 `by saved { }` 위임, `rememberSaveable(serializer = ...)`
- **NavigationEvent 신규 작성**: 시스템 뒤로가기(predictive back)·키보드·앞으로 가기를 플랫폼 공통 이벤트로. `NavigationEventDispatcher`/`NavigationEventCallback`, 플랫폼별 `NavigationInputHandler`. 검증으로 `OnBackPressedDispatcher`를 그 위에 재작성하고 Navigation3와 통합
- 이전 vs 재작성: 이전은 검증된 동작 유지·점진적·사용자 코드 변경 없음 / 재작성은 깔끔한 설계지만 기능 동등성 확보가 느리고 사용자 마이그레이션 필요

**학습 포인트**

- `expect class` + `actual typealias` 패턴이 "기존 Android API 호환 + KMP 지원"을 동시에 달성하는 방식 이해 — 단, 플랫폼 API가 common 사용자에게 새지 않게 하는 경계 설계 필요
- 프로세스 종료 후 상태 복원을 `@Serializable` 클래스 + `by saved { }`로 바꿔 보기
- [Navigation3](https://goo.gle/nav3)의 백스택 직접 소유 모델

---

## Hot Reload - How It Was Built

> *Implementing Compose Hot Reload* — Sebastian Sellmair · [영상](https://www.youtube.com/watch?v=UebhKUdO2sQ)
>
> 예제 코드: 슬라이드 p.26–56 · 도구 저장소 [JetBrains/compose-hot-reload](https://github.com/JetBrains/compose-hot-reload)

**요약** — "Kotlin에선 불가능"으로 여겨지던 Compose Hot Reload를 ClassLoader 실험 → JetBrains Runtime → Compose 런타임 → 컴파일러 순서로 풀어낸 엔지니어링 여정.

**핵심 내용**

- 목표: 실제 프로젝트에서 함수·클래스 추가·삭제·시그니처 변경까지 허용, 단 "복잡도 예산" 안에서
- 1차 시도 **ClassLoader**: UI 코드만 별도 로더에 올려 교체 → 데모는 되지만 UI가 순수 함수가 아님(side effect, remember, 앱과의 상호작용), 사용자가 코드를 분리하고 JVM 개념을 알아야 함 → 폐기
- 2차 **JetBrains Runtime + DCEVM**: 새 코드를 "평행 우주"에 로드·검증 → 모든 객체 migration → JIT 리셋 → 승격. 필드가 추가된 객체는 **특수 GC**가 새 메모리로 옮겨 해결
- 보스전 1 **Compose 런타임**: 코드만 바꿔서는 다시 실행되지 않고, remember된 상태는 slot table의 group에 있음 → **bytecode 분석**으로 함수·group별 해시를 추적해 바뀐 group만 무효화(상태 보존)
- 보스전 2 **컴파일러**: `ComposableSingletons$...$lambda-1$1`처럼 순번으로 이름 붙은 composable 람다는 순서가 바뀌면 다른 클래스가 됨(심지어 supertype도 바뀜) → Kotlin 2.1.10+ 에서 **구조 해시 기반 이름**으로 변경
- 결과: Kotlin·Compose 컴파일러, Compose 런타임, Gradle·Amper, bytecode 추적까지 전 스택에 걸친 변경. Desktop JVM 타깃에서 동작

**학습 포인트**

- [compose-hot-reload](https://github.com/JetBrains/compose-hot-reload)를 KMP 프로젝트의 desktop 타깃에 적용해 보고 상태가 유지되는 변경/유지되지 않는 변경 구분
- slot table과 group의 개념 — recomposition·remember가 어떻게 동작하는지 이해하는 가장 구체적인 사례
- ClassLoader 방식이 실패한 이유를 "추상화 누수" 관점에서 정리

---

## Compose for Desktop - Inside Android Studio & IntelliJ

> *Project Sparkles: How Compose for Desktop is changing Android Studio and IntelliJ* — Chris Sinco, Sebastiano Poggi · [영상](https://www.youtube.com/watch?v=lcqtRQ5Fou8)
>
> 예제 코드: 없음 (코드 없이 제품 개발 사례 중심)

**요약** — Android Studio의 Swing UI를 Compose for Desktop + **Jewel**(IntelliJ 테마 컴포넌트)로 바꿔 가는 Google·JetBrains 공동 프로젝트.

**핵심 내용**

- 동기: 더 나은 개발자 경험 → 더 나은 사용자 경험, 출시 속도, IDE의 완성도
- 도입 이력: Koala(task-based profiler, 첫 시험장, Compose 버그 보고 없음) → Ladybug(기기 추가 플로우, 첫 Compose 다이얼로그) → Narwhal(Journeys 스크립트 편집기: 자연어 UI 테스트, 에디터 안 첫 Compose UI) → Meerkat+(Gemini 채팅 UI 재작성)
- JCEF 대신 Compose로 Markdown 렌더링(Gemini 응답), 이미지·각주·코드 실행까지 지원하는 Markdown preview 작업 중
- 방향: 새 기능은 **Compose First**, 필요하면 기존 UI 교체. Swing interop, 학습 기간이 주→일 단위로. 마이크로 인터랙션·적응형 레이아웃·Material Expressive 모션

**학습 포인트**

- IntelliJ 플러그인에서 Jewel로 Compose UI를 만드는 방법 — 플러그인 개발자라면 Swing 대신 선택지가 생김

## Prototyping - Compose in Kotlin Notebooks

> *Compose Prototyping in Kotlin Notebooks* — Christian Melchior · [영상](https://www.youtube.com/watch?v=bkd6EAPIVe0)
>
> 예제 코드: [cmelchior/notebook-compose-kotlinconf2025](https://github.com/cmelchior/notebook-compose-kotlinconf2025) (발표 노트북 포함)

**요약** — Kotlin Notebook을 Compose UI 놀이터로 쓰는 시도. 데모 중심이며 발표 시점엔 개발 중인 기능.

**핵심 내용**

- 비교: 앱 실행(설정 없음, 느린 반복, 깊은 화면은 도달이 오래 걸림) / `@Preview`(정적 데이터만) / Hot Reload(Desktop만, 기존 앱에서 반복) / **Notebook**(Desktop만, 프로젝트 설정 없이 실데이터로 대화형 실험)
- 상태: Compose 지원(KTNB-650) 개발 중, K2 REPL(KTNB-891) 필요해 지연, 하드웨어 아키텍처별 배포 문제 미해결
- Kotlin Notebook 자체는 IntelliJ(Community·Ultimate)와 Android Studio에서 무료

**학습 포인트**

- KTNB-650 진행 상황 확인 후, 디자인 시스템 컴포넌트 카탈로그를 Notebook으로 만드는 용도 검토

## VR - Compose on Meta Quest

> *Building immersive VR apps for Meta Quest with Jetpack Compose* — Samuel Gebre Yohannes · [영상](https://www.youtube.com/watch?v=l7alNC819MU)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.34–53, 60–80, 96–112

**요약** — 기존 Android 앱(KotlinConf 앱)을 Meta Spatial SDK로 Meta Quest용 몰입형 앱으로 확장하기.

**핵심 내용**

- Meta Horizon OS는 AOSP 기반 → Android 앱이 그대로 동작. Spatial SDK는 **ECS**(Entity–Component–System: entity는 ID, component는 데이터, system이 query로 매 프레임 처리)
- 적용: Gradle product flavor(`mobile`/`metaquest`) + `AppSystemActivity`에서 VR·Compose·패스스루 기능 등록
- **Panel** = 2D 콘텐츠 컨테이너(Activity 또는 composable). 3D 공간 위치(`Transform(Pose(Vector3, Quaternion))`)에 배치, `Grabbable`. 공통 코드에 `ComposableVrPanelProvider` interface를 두고 Quest flavor만 구현을 Koin으로 주입 → 화면 이동 대신 **새 패널 생성**
- 문제: 핀치 줌 제스처가 VR 컨트롤러에서 동작하지 않음 → 조이스틱·마우스휠 줌으로 대체
- 3D: GLTF 모델(Kodee)을 `Mesh`로 배치, 커스텀 component + system으로 컨트롤러 버튼에 반응해 애니메이션 트랙 재생

**학습 포인트**

- "interface는 common, 구현은 특정 flavor에만, DI로 선택적 주입"이라는 패턴 — VR이 아니어도 폴더블·태블릿 분기에 그대로 적용 가능
- ECS 모델을 Compose의 상태 기반 모델과 비교
