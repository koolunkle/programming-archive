# 01. Language — 언어 설계와 컴파일러

Kotlin 언어 자체의 진화 방향을 다루는 발표 모음. 대부분 **KotlinConf 2025(2025.05) 시점에 설계·실험 단계**인 기능이므로, 학습 전에 링크된 KEEP / YouTrack 이슈에서 현재 상태를 먼저 확인할 것.

## 학습 순서

1. **Smart Casts** — 이후 모든 발표가 전제로 하는 data-flow 분석의 기본기
2. **Rich Errors** → **Return Values** — 에러를 값으로 다루는 설계와, 그 값을 버리지 못하게 하는 검사기. 한 쌍으로 볼 것
3. **Immutability** — value class 확장과 `copy var`. 스마트 캐스트·동시성과의 관계까지
4. **Type Checking** — 위 기능들(union, local, 동등성)을 떠받칠 타입 시스템의 장기 비전
5. **Nullness (JSpecify)** — Java interop 경계의 null-safety
6. **Compiler Plugins** — 언어 기능을 직접 확장하는 방법 (K2 FIR/IR)

발표 간 연결:

- Rich Errors의 `T | Error` ↔ Type Checking의 "categorized union" — 같은 문제를 언어 설계자와 타입 이론 관점에서 본 것
- Return Values의 `@MustUseReturnValue` ↔ JSpecify의 차기 과제(반환값 검사) — Kotlin·Java 생태계가 같은 방향으로 수렴 중
- Immutability ↔ [Data-Oriented Design](<../08-design-practices/Data-Oriented Design.pdf>) — 기능(how)과 설계 철학(why)

---

## Smart Casts - How They Work

> *The wonders of smart casting* — Alejandro Serrano Mena · [PDF](<Smart Casts - How They Work.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.4–23

**요약** — 컴파일러가 data-flow로 축적하는 facts와 implications, 그리고 그것이 적용되는 안정성 조건.

**핵심 내용**

- 컴파일러는 **facts**(타입, nullability, 값)와 **implications**(조건부 지식)를 추적한다. K2(2.0)부터 `val isKing = this is King` 처럼 **지역 변수에 담긴 조건**도 이후 `if (isKing)`에서 스마트 캐스트로 이어진다
- `contract { returns(true) implies (this is King) }` 로 implication을 **함수 경계 너머로** 전달. `require`, `check`, `isNullOrEmpty`가 모두 이 방식
- `(player as? Sorcerer)?.element == Fire` 가 참이면 `player is Sorcerer` 까지 역으로 추론된다 — nullability 정보는 양방향으로 전파
- 스마트 캐스트는 **안정적인 값**에만 적용: `val`, custom getter 없음, 같은 모듈, `open`/`expect` 아님. 불안정하면 `when (val x = ...)` 로 지역 변수에 고정

**학습 포인트**

- 다른 모듈의 `data class` 프로퍼티가 왜 스마트 캐스트되지 않는지 설명할 수 있는가 (→ Immutability 발표의 value class가 이 제약을 푸는 방법)
- 커스텀 `contract`를 하나 작성해 보고, `callsInPlace`와 `returns implies`의 차이를 정리
- 예정된 개선: 더 많은 contract 종류, exhaustiveness 연동, 타입 변수 전파([KEEP-409](https://github.com/Kotlin/KEEP/issues/409) subtyping reconstruction)

---

## Rich Errors - Union Error Types

> *Rich Errors in Kotlin* — Michail Zarečenskij · [PDF](<Rich Errors - Union Error Types.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.6–33, 36–60, 63–100

**요약** — 복구 가능한 실패를 `T | Error` 형태의 반환 타입으로 표현하는 새 에러 모델. nulls-as-errors 패턴의 일반화.

**핵심 내용**

- 기존 방식의 한계: `null`(원인 불명) / sealed hierarchy(장황, 확장 어려움, 중첩) / `Result<T>`(`Throwable` 고정, non-exhaustive `when`) / Java checked exception(고차 함수와 합성 불가)
- **`error class` / `error object`** — 새 classifier. `Any?`가 아닌 별도 최상위 `Error` 계층에 속하므로 `T | Error`가 항상 서로소(disjoint)
- 제약: `open error class`, 제네릭 error class 불가. **값 타입 하나 + 에러 여러 개**만 허용 (`Int | String` 같은 일반 union은 불가 — 타입 검사를 polynomial로 유지하기 위함)
- 설계 원칙: 에러는 **값**(특수 제어 흐름 없음), **복구 가능한 경우 전용**(unrecoverable은 여전히 exception), **exhaustiveness 필수**, chain-call 편의성
- 연산자: `?.` 는 에러를 누적 전파(`A | E1` → `B | E1 | E2`), `!!` 는 `throw()` operator 호출. `?:` 는 없고 `ifError { }` 같은 함수로 대체
- 부가 효과: `T | NotFound` 로 Optional 대체 (`listOf(null, 42).firstOrNull()`의 모호성 해소), `runCatchingAsError {}` 로 exception → error 변환

**학습 포인트**

- 현재 코드의 sealed `Result` 계층을 rich error로 옮기면 무엇이 줄고 무엇을 잃는지 비교 (발표의 `FetchingResult` 예제)
- Rust `Result<T, E>` + `?`, Zig error union, Swift typed throws와 비교해 Kotlin이 "main actor + error attributes" 모델을 택한 이유
- 상태: 발표 시점 설계 중, KEEP 공개 예정이었음 → 현재 KEEP 진행 상황 확인

---

## Return Values - Unused Result Checker

> *Don't forget your values!* — Leonid Startsev · [PDF](<Return Values - Unused Result Checker.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.13–17, 34–56

**요약** — 사용하지 않은 반환값을 경고하는 언어 수준 검사기. Rich Errors의 에러가 조용히 버려지는 것을 막는 짝.

**핵심 내용**

- 함수의 최대 85%는 반환값을 써야 하는 함수(JetBrains·Google 조사) → **must-use를 기본값**으로, 예외만 `@IgnorableReturnValue`로 표시
- 무시해도 되는 값: `Unit`/`Nothing` 반환, ignorable로 표시된 호출, `i++` 같은 compound assignment
- **propagation**: `if`/`when`/`try`/`?:`/`as`/`!!`/`let` 은 자체로 판단하지 않고 하위 식에 "사용 여부"를 전파. 본문 마지막이 아닌 `if` 분기의 값은 미사용으로 판정
- 의도적으로 버릴 땐 `val _ = saveToDb(user)` (unnamed local variable)
- 생태계 마이그레이션: 라이브러리가 `@MustUseReturnValue`(또는 full mode 컴파일)로 "checker-ready"를 선언 → 사용자는 그 라이브러리에 대해서만 경고를 받음. Error Prone 어노테이션 인식, JSpecify와 표준화 논의 중
- 컴파일러 옵션: `-Xreturn-value-checker=check` (ready인 선언만) / `full` (내 코드 전체를 must-use로)

**학습 포인트**

- 기존 프로젝트에 `-Xreturn-value-checker=check` → `full` 순으로 켜보고 경고 유형 분류. builder/fluent API(`StringBuilder.append`)가 어떻게 처리되는지 확인
- 라이브러리 작성자라면: 공개 API 중 ignorable이어야 할 함수 목록 작성
- [KEEP-412](https://github.com/Kotlin/KEEP/issues/412) 원문에서 propagation 규칙 전체 확인

---

## Immutability - Upcoming Language Features

> *A (deep) dive into (shallow) immutability* — Marat Akhin · [PDF](<Immutability - Upcoming Language Features.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.9–10, 20–37, 43–77, 81–92

**요약** — 다중 프로퍼티 value class, 컴파일러가 강제하는 깊은 불변성(`immutable`), 불변 데이터를 편하게 갱신하는 `copy var`.

**핵심 내용**

- 현재 Kotlin은 **불변 참조(`val`)**만 강제하고 **불변 값/타입**은 강제하지 못함. concrete state(비트) vs abstract state(관찰 가능한 상태), shallow vs deep 구분
- 왜 중요한가: Compose stability(깊게 불변이면 recomposition skip), 캐시 무효화 방지, 데이터 경쟁 방지
- **value class 확장**: 프로퍼티 여러 개 허용, identity 없음, inlining 안 함(최적화가 아닌 의미론이 목적). `immutable value class` 는 모든 프로퍼티가 깊게 불변이어야 함
- 참조 타입은 `@Immutable` 어노테이션(검증 없는 계약), 제네릭은 `<immutable E>` 로 인스턴스화 시점 검사 (`ImmutableList<MutableList<Int>>` 거부)
- **`copy var`**: `book.isbn.eanPrefix = 979` 처럼 쓰면 컴파일러가 `copy()` 사다리로 변환. **`copy fun`**: 여러 필드를 "제자리에서" 바꾸는 함수가 fluent 스타일로 컴파일됨
- 부수 효과: 다른 모듈의 value class 프로퍼티도 스마트 캐스트 가능, `copy var` 는 경쟁 가능한 람다에 캡처 금지
- JVM의 [JEP 401](https://openjdk.org/jeps/401)(Valhalla value class)은 shallow 불변 기반으로 활용, 런타임 최적화는 보너스

**학습 포인트**

- Compose compiler report로 `data class`(var/val 혼합)의 stability를 직접 확인하고, 불변으로 바꿨을 때 변화 관찰
- 현재의 `@JvmInline value class` 제약(단일 프로퍼티, shallow)과 제안된 기능의 차이 정리
- 설계 중 쟁점(value interface, 람다 캡처, value array) — [KT-77734](https://youtrack.jetbrains.com/issue/KT-77734)에서 진행 상황 추적

---

## Type Checking - Design Beyond Inference

> *Designing Kotlin Beyond Type-Inference* — Ross Tate · [PDF](<Type Checking - Design Beyond Inference.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.3–14, 20–36, 41–42

**요약** — 타입 *추론* 대신 *outference*(타입 존재성만 검증)로 전환하면 가능해지는 기능들. 발표자 스스로 "brainstorm" 단계라고 명시한 장기 비전.

**핵심 내용**

- 문제: 현행 추론 알고리즘은 최악의 경우 지수 시간이고, 명세가 어렵고(Java 명세는 추론만 37쪽), 알고리즘 변경이 프로그램의 유효성을 바꿀 수 있다
- **Type-outference**: 제약 집합이 consistent한지만 검사하고 구체 타입은 몰라도 됨 → polynomial, 기능 추가가 쉬움
- **categorized union**: 같은 카테고리(예: `Any` vs `Null` vs `Error`)끼리는 union 불가 → 서브타이핑 검사에서 "or" 분기를 제거 (Rich Errors 설계의 이론적 근거)
- **`local` 파라미터/객체**: 참조가 호출 밖으로 escape하지 않음을 보장 → 동기 인터페이스 구현체 안에서도 `suspend` 호출 가능. 내부적으로 lifetime polymorphism이지만 사용자에게는 숨겨짐
- **`Any?` 가정의 한계**: error, local, unboxed value class는 `Any?`가 아님. `Any.equals(Any?)` 는 사실상 동적 타입 → **`shape interface`**(타입클래스 유사)로 타입 안전한 동등성·비교·산술 제안

**학습 포인트**

- Rich Errors의 `Int | String` 금지 규칙을 이 발표의 categorized union으로 설명해 보기
- `shape interface` 를 Haskell type class / Scala given / Rust trait과 비교
- 확정된 기능이 아니므로 "Kotlin이 어떤 제약 아래서 진화하는가"를 이해하는 용도로 볼 것

---

## Nullness - JSpecify Annotations for Java Interop

> *JSpecify: Java Nullness Annotations and Kotlin* — David Baker · [PDF](<Nullness - JSpecify Annotations for Java Interop.pdf>)
>
> 예제 코드: 슬라이드 p.4–14 · 라이브러리 [jspecify/jspecify](https://github.com/jspecify/jspecify)

**요약** — Java 라이브러리의 platform type(`String!`)을 없애는 표준 nullness 어노테이션.

**핵심 내용**

- 기존 `@Nullable` 은 10종 가까이 난립(JSR-305, JetBrains, AndroidX, Checker Framework…), 도구마다 의미가 미묘하게 다르고 일부는 type-use(`List<@Nullable String>`) 불가
- JSpecify: Google·JetBrains·Oracle·Meta·Uber·Spring 등이 합의한 **명세가 있는** 표준. 1.0 출시(2024.07)
- `@Nullable` / `@NonNull` / **`@NullMarked`**(범위 내 기본 non-null) / `@NullUnmarked`, 그리고 **parametric nullness**(`T extends @Nullable Object` → 인스턴스화에 따라 `T` 또는 `T?`)
- Kotlin 컴파일러는 **2.1.0부터** JSpecify 어노테이션을 null-safe 타입으로 취급. Guava, Spring 7, Gradle 9 API, Jetpack 등이 채택
- 다음 과제: 반환값 검사, 불변성 (→ Return Values, Immutability 발표와 연결)

**학습 포인트**

- 사용 중인 Java 의존성이 JSpecify를 쓰는지 확인하고, 업그레이드 시 Kotlin 쪽에서 새로 발생할 컴파일 오류 유형 예상(`String!` → `String?`)
- `@NullMarked` 패키지에서 `Parameterized<T extends @Nullable Object>` 의 `T`, `@Nullable T`, `@NonNull T` 가 Kotlin에서 각각 어떤 타입이 되는지 정리
- [jspecify.dev](https://jspecify.dev) 사용자 가이드

---

## Compiler Plugins - Writing Your Own

> *Writing Your Third Kotlin Compiler Plugin* — Brian Norman · [PDF](<Compiler Plugins - Writing Your Own.pdf>)
>
> 예제 코드: [bnorm/buildable](https://github.com/bnorm/buildable) (발표의 `@Buildable` 플러그인 전체 코드) · 슬라이드 p.39–242

**요약** — K2 컴파일러 파이프라인과 FIR/IR 확장 지점을 `@Buildable`(빌더 클래스 생성) 예제로 따라가는 실전 가이드.

**핵심 내용**

- 언제 쓰나: **가능하면 KSP**(컴파일 전 실행, 새 코드 생성만 가능). 기존 코드 변경·언어 의미 변경·새 경고/에러가 필요하면 컴파일러 플러그인
- 파이프라인: **Parse → Resolve → Analyze → Transform → Generate**
  - FIR(Frontend IR): 파싱 결과 트리. `SUPER_TYPES` → `TYPES` → `IMPLICIT_TYPES_BODY_RESOLVE` → `BODY_RESOLVE` 순서로 단계별 resolve — 순서가 중요
  - 대부분의 에러는 resolve에서 발생하지만, 대부분의 에러·경고 *종류*는 analyze(checker)에서 보고
  - Backend IR: 변환·코드 생성 단계
- 등록 구조: `CompilerPluginRegistrar` → `FirExtensionRegistrar`(`FirDeclarationGenerationExtension`, `FirStatusTransformerExtension`, `FirSupertypeGenerationExtension`, `FirAdditionalCheckersExtension`) + `IrGenerationExtension`
- FIR에서는 선언의 **모양만** 생성(`getNestedClassifiersNames`, `generateNestedClassLikeDeclaration`, predicate로 대상 선별), 본문은 IR 단계에서 `IrVisitor`로 채움. 진단은 `BaseDiagnosticRendererFactory`로 정의
- 플러그인 API는 **안정화되지 않음**. 현 API는 앞으로도 stable이 되지 않으며 별도의 안정 API를 개발 중([KT-49508](https://youtrack.jetbrains.com/issue/KT-49508))

**학습 포인트**

- [bnorm/buildable](https://github.com/bnorm/buildable) 클론 → FIR 생성과 IR 본문 채우기가 어떻게 나뉘는지 추적
- 템플릿 [demiurg906/kotlin-compiler-plugin-template](https://github.com/demiurg906/kotlin-compiler-plugin-template) 으로 간단한 checker(경고 하나 추가)부터 작성
- 실전 사례 읽기: [cashapp/burst](https://github.com/cashapp/burst)(변환), [drewhamilton/Poko](https://github.com/drewhamilton/Poko)(변환+분석), [ZacSweers/metro](https://github.com/ZacSweers/metro)(거의 모든 확장 지점 사용)

---

## 예제 코드: New Features Survey

> *Dissecting Kotlin: Surveying the Latest Stable and Experimental* — Huyen Tue Dao · [코드](<New Features Survey - Example Code/Dissecting Kotlin IV/dk4/src/main/kotlin>)
>
> 예제 코드: 이 폴더의 로컬 프로젝트 [dk4](<New Features Survey - Example Code/Dissecting Kotlin IV/dk4>)(발표 예제, Gradle 멀티모듈) · [Test](<New Features Survey - Example Code/Dissecting Kotlin IV/Test>)(`when`·sealed 실습)

발표 슬라이드 없이 예제 프로젝트만 있음. Kotlin `2.2.0-RC`, JVM 17, `-Xcontext-parameters` 사용.

| 파일 | 주제 |
| --- | --- |
| `01_EnumValuesToEnumEntries.kt` | `values()` → `entries` (할당 없는 대체) |
| `02a`~`02d` | scope function, extension, receiver, extension member — context parameter의 선행 개념 |
| `02e_ContextReceivers.kt` → `02f_ContextParameters.kt` | context receiver(폐기)에서 context parameter로의 전환, 사용 지침 |
| `03_GuardConditions.kt` | `when` 분기의 guard condition (`is X if cond ->`) |
| `10_QuickHits.kt` | non-local `break`/`continue`, sealed class exhaustiveness 개선 (2.1.0) |
| `99_References.md` | 각 기능의 KEEP·릴리스 노트 링크 |

**학습 포인트**

- `02e` → `02f` 순으로 읽으며 context receiver가 폐기된 이유와 context parameter의 해석 규칙 차이 정리
- `02f` 하단의 스타일 가이드("단순히 인자를 넘기는 용도로 쓰지 말 것", "작은 context 여러 개보다 상위 context로 묶을 것")를 기존 코드의 DI·로깅 패턴에 적용해 보기
