# 08. Design & Practices — API·시스템 설계와 안티패턴

언어 기능이 아닌 **설계 판단**을 다루는 발표 모음: 라이브러리 API 호환성, 데이터 중심 설계, 조직과 시스템의 관계, Kotlin 관용구의 함정.

## 학습 순서

1. **API Design** — 소스·바이너리·계약 호환성의 구분과 자동 검사 도구
2. **Anti-patterns** — 관용구를 잘못 썼을 때 생기는 버그(가볍게 읽기 좋음)
3. **Data-Oriented Design** — "모든 것을 데이터로"라는 설계 철학과 Kotlin 구현
4. **Systems Thinking** — 코드 밖, 시스템과 조직을 보는 관점

발표 간 연결:

- 바이너리 호환성 3부작: **API Design**(원칙과 도구) ↔ [03 AWS](<../03-multiplatform/README.md>)(public API에서 data class·enum 금지) ↔ [06 Compatibility](<../06-build-tooling/README.md>)(버전 플래그)
- 불변성: **Data-Oriented Design**(왜) ↔ [01 Immutability](<../01-language/README.md>)(언어 기능, how) ↔ **Anti-patterns**(mutable data class + 구조분해 버그)

---

## API Design - Lessons from Android API Council

> *APIs: How Hard Can They Be?* — Aurimas Liutikas, Alan Viverette · [PDF](<API Design - Lessons from Android API Council.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.17–45, 54–83, 91–92

**요약** — Android API Council에서 10년 가까이 API를 심사한 두 사람이 정리한 좋은 API의 원칙, 실제 사고 사례, 자동화 도구.

**핵심 내용**

- API 등급: public(문서, 강한 호환성 보장) / experimental(문서, 보장 없음) / restricted(문서 없음, 최소 보장 — 그래도 깨지면 사고)
- 원칙: **일관성**(익숙한 이름, 공유 개념), **사용성**(언어 관례, 문서, 샘플), **호환성** — 계약(문서대로 동작하는가) / 소스(코드를 고쳐야 하는가) / 바이너리(재컴파일이 필요한가)
- 깨지는 예: Kotlin 파일 이름 변경(`Example1Kt` 클래스명 변경 → Java 사용자 소스·바이너리 깨짐), 파라미터 타입 변경(`String` → `CharSequence`는 Kotlin 소스는 호환이지만 바이너리는 깨짐)
- 실제 사고 1: `@RestrictTo(LIBRARY_GROUP)` API를 다른 라이브러리 그룹(room → sqlite)이 사용 → 사용자가 sqlite만 올리자 크래시. Lint 검사로 재발 방지
- 실제 사고 2: `list.removeLast()` 가 `--release 21`로 컴파일하면 Kotlin stdlib 확장이 아닌 JDK 21의 `List.removeLast()`로 해석 → API 33 이하 기기에서 크래시. **남의 인기 타입에 확장 함수를 만들 때 주의**
- 도구: **Metalava**(Google, API 텍스트 파일·호환성 검사·API lint, JVM/Android), **BCV**(JetBrains binary-compatibility-validator, `apiDump`/`apiCheck`, klib 지원은 실험적). Jetpack은 API 변경을 presubmit 검사 + 승인 절차로 관리
- 교훈: 수동 리뷰는 선형으로만 확장된다, 개발자는 확정(stable)을 미룬다(alpha11…), 모범 사례는 시간이 지나며 바뀐다

**학습 포인트**

- 자기 라이브러리에 BCV를 붙여 `apiDump`를 커밋하고, 의도치 않은 API 변경이 CI에서 걸리게 하기
- 위의 `removeLast()` 사례를 직접 재현 — 컴파일 대상 JDK에 따라 같은 소스가 다른 메서드로 링크되는 것 확인
- 공개 API 변경 시 소스·바이너리·계약 호환성을 각각 체크하는 습관

---

## Anti-patterns - Kotlin Worst Practices

> *Kotlin Worst Practices — How to Maximize Your Hassle* — Liliia Abdulina · [PDF](<Anti-patterns - Kotlin Worst Practices.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.15–25, 37–51, 59–75

**요약** — 가상의 개발자 "Katlin"이 Kotlin 관용구를 열정적으로 잘못 쓰며 겪는 버그들.

**핵심 내용**

- **mutable data class + 구조분해**: `val (name, age) = user` 이후 `user.age += 1` 하면 구조분해한 값은 갱신되지 않음 → data class 프로퍼티는 불변으로. 가변이면 `hashCode`/`equals`가 바뀌어 HashMap·HashSet에서도 문제
- **엘비스 + `return`의 남용**: `user?.context ?: return` 을 연달아 쓰면 폼 제출 시 아무 일도 일어나지 않는 조용한 실패 → `requireNotNull(...) { "명확한 메시지" }`, 로깅, 의도가 드러나는 설계
- **public inline 함수가 internal을 호출**: inline 본문은 호출 측에 복사되므로 internal 함수를 public으로 바꾸게 됨 → `@PublishedApi internal`로 명시. non-local return은 문서화. **Explicit API mode** 권장
- 덤: KMP 프로젝트에 모든 타깃을 다 켜면 빌드가 끝나지 않음

**학습 포인트**

- 코드베이스에서 `?: return` 연쇄와 `var`를 가진 data class 검색 → 실패를 드러내도록 정리
- 라이브러리 모듈에 Explicit API mode를 켜서 의도치 않은 public 선언 찾기

---

## Data-Oriented Design

> *Good Old Data* — Andrey Zaytsev · [PDF](<Data-Oriented Design.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.11–13, 19–38, 42, 49–52

**요약** — Fleet 아키텍트가 Clojure 경험에서 얻은 데이터 중심 설계를 Kotlin으로 구현하는 방법. 데이터의 정의 → Kotlin 구현 → 그로부터 나오는 능력(emergence).

**핵심 내용**

- 데이터는 시스템의 언어(캐시, 와이어 프로토콜, DB 스키마, 이벤트, 큐, 설정). 데이터의 조건: **불변**, **값 의미론**(같은 입력 → 같은 값), **직렬화 가능**(`x == deserialize(serialize(x))`)
- Kotlin에서: `var` 없는 data class, 실질적으로 불변인 컬렉션, 필드도 모두 데이터, kotlinx.serialization. 곧 나올 `immutable value class`. 직접 정의할 땐 equals/hashCode·직렬화를 값 의미론과 일치시킬 것
- **데이터와 동작 분리**: 데이터는 동작이 없고 임의 인터페이스를 구현하지 않음. 열린 다형성은 `WidgetKind<T>("editor")` 같은 **명시적 kind 값**(이것도 데이터)으로 dispatch하고 동작은 registrar에 등록
- **State as Data**: identity(`UserId`, 변하지 않음)와 state(스냅샷)를 분리. 상태 전이 = `(State, Event) -> State'`, 이벤트도 데이터. `kotlinx.collections.immutable`의 persistent 자료구조(구조 공유) → 시간 여행, 이벤트 기록·재생, 복제, 낙관적 업데이트, 협업
- **Effects as Data**: LLM 호출·파일 편집 같은 부수 효과를 데이터로 표현하고 `EffectHandler`가 처리 → 기록, 쉬운 테스트, 어디서든 재시작. 계산을 데이터 가까이 옮기는 예로 Rama 프레임워크
- Compose의 recomposition skip도 같은 원리(불변 + 값 의미론)

**학습 포인트**

- 앱 상태 하나를 `(State, Event) -> State` 리듀서 + persistent 컬렉션으로 재구성해 보고, undo/재생이 얼마나 쉬워지는지 확인
- 도메인 모델에서 identity와 state가 섞인 클래스 찾기
- [01 Immutability](<../01-language/Immutability - Upcoming Language Features.pdf>)와 함께 읽기 — 이 발표가 그 기능의 동기

---

## Systems Thinking - Well-Functioning Systems

> *Qualities of a Well-Functioning System* — Diana Montalion · [PDF](<Systems Thinking - Well-Functioning Systems.pdf>)
>
> 예제 코드: 없음 (코드 없이 개념 중심)

**요약** — 『Learning Systems Thinking』 저자가 말하는 잘 동작하는 시스템의 세 가지 성질과, 지식이 흐르는 조직.

**핵심 내용**

- 시스템 = 관계가 결과를 만드는 상호 연결된 구성 요소. "부분의 합이 아니라 상호작용의 곱"(Ackoff). 시스템 맥락: **움직이는 데이터**, **일관성(coherence)** 설계, 그리고 **잘못된 것을 최적화**하고 있음
- 흔한 최적화 대상(효율, 속도, 통제) — "효율은 회복력의 적". 무엇을 최적화하는지 모르면 아마 통제를 최적화하고 있는 것
- 세 성질: **Resilience**(다양성·중복·피드백 루프·잘 설계된 상호의존으로 충격 흡수) / **Self-organizing**(스스로 구조를 복잡하게 만들어 적응) / **Hierarchy**(하위 시스템으로 나눠 정보 흐름을 구조화해 복잡도 감소)
- 지식: 저장(stock, 효율을 높임)보다 **흐름(flow, 효과를 높임)**이 중요. SECI 모델(사회화 → 외재화 → 결합 → 내재화)

**학습 포인트**

- 자기 팀·시스템이 실제로 무엇을 최적화하고 있는지(효율? 통제? 회복력?) 한 문장으로 써 보기
- 장애 사후 분석에서 "부분의 결함"이 아니라 "상호작용"을 원인으로 보는 연습
- [learningsystemsthinking.com](https://learningsystemsthinking.com)
