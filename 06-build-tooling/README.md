# 06. Build & Tooling — 빌드 도구와 코드 품질

Gradle의 성능·구성 방법, 새 빌드 도구(Amper, Buck2), JVM·Kotlin 버전 호환성, 정적 분석, 라이브러리 생태계 인덱스. 2025.05 기준으로 Gradle 9.0(configuration cache 기본 권장)은 출시 직전, Amper는 0.7 단계였다.

## 학습 순서

1. **Compatibility** — `jvmTarget`, `apiVersion`, `-Xjdk-release` 등 모든 버전 플래그의 의미. 라이브러리 작성자라면 필수
2. **Gradle - Scalable Setup** → **Gradle - Fast Inner Dev Loops** — 언제 무엇을 도입할지, configuration cache의 현재
3. **Static Analysis** — detekt 커스텀 룰(타입 해석 포함)로 아키텍처 규칙 자동화
4. **Amper**, **Buck2** — Gradle의 대안이 무엇을 다르게 설계했는지
5. **klibs.io** — KMP 라이브러리 인덱스를 만들며 드러난 생태계의 정의 문제

발표 간 연결:

- "빌드를 이해 가능하게(toolable)" — Gradle의 Declarative Gradle, Amper의 선언적 구성 + 추적 가능한 변환, Buck2의 Starlark 규칙. 같은 문제의식의 세 가지 답
- 빌드 성능의 두 층: 그래프 수준(Gradle configuration cache·build cache, Buck2 원격 실행) + 컴파일러 수준 증분(Buck2가 Kotlin Build Tools API 사용)
- Compatibility의 바이너리 호환 ↔ [03 AWS](<../03-multiplatform/README.md>)의 public API 설계 원칙

---

## Compatibility - JVM Target & Library Versions

> *Kotlin compatibility crash course* — Martin Bonnin · [PDF](<Compatibility - JVM Target & Library Versions.pdf>)
>
> 예제 코드: 슬라이드 p.8–32, 47–74 · 발표자 플러그인 [GradleUp/compat-patrouille](https://github.com/GradleUp/compat-patrouille)

**요약** — "컴파일될 때까지 Java 버전을 바꿔 붙인다"를 끝내기 위한 Java·Kotlin·Android 버전 플래그 총정리.

**핵심 내용**

- Java: `sourceCompatibility`/`targetCompatibility`만 맞추면 최신 JDK API(`ArrayList.removeFirst`) 호출이 컴파일되고 구 런타임에서 `NoSuchMethodError`. **`release`** = source + target + bootstrapClasspath를 한 번에 → 최신 JDK + `options.release = N`
- **Java toolchain은 쓰지 말 것**: 모두 같은 JVM을 설치하게 하거나 Gradle 8.13의 daemon JVM 자동 프로비저닝 사용
- Kotlin: `languageVersion`(metadata 버전), `apiVersion`(컴파일 시 stdlib API), **`coreLibrariesVersion`**(stdlib 등 의존성 버전). kotlinc는 **n+1 best-effort forward compatibility**(1.9가 2.0 metadata를 읽음), 하위 호환은 영구적
- JVM: `jvmTarget` + `-Xjdk-release` + Java 쪽 `options.release`를 함께 설정해야 Gradle의 target 검증이 통과
- Android: `compileSdk`가 사실상 `release`. R8/D8이 최신 bytecode를 desugar하므로 **`jvmTarget`은 거의 무관**(같은 dex 생성) — 대신 Lint의 `NewApi`가 진짜 안전장치
- KMP의 klib에는 이런 하위 호환 설정이 없음(KT-66755) → KGP 업그레이드를 서두르지 말 것
- 권장 기본값: Java 23 + `--release 8|17`, Kotlin은 n-3(약 2년 지원), `minSdk 21`/`targetSdk 36`. 설정 자동화 [compat-patrouille](https://github.com/GradleUp/compat-patrouille)

**학습 포인트**

- 자기 라이브러리의 `.class`를 `javap -v`로 열어 major version과 `kotlin.Metadata(mv=...)` 확인
- 구버전 Kotlin 사용자를 지원해야 한다면 `languageVersion`/`apiVersion`/`coreLibrariesVersion` 조합을 정해 CI에서 검증
- 참고 글: Jake Wharton "Gradle toolchains are rarely a good idea", Aurimas Liutikas "Conservative Librarian"

---

## Gradle - Scalable Setup Practices

> *Getting your Gradle setup right, at the right time* — Marcin Mycek · [PDF](<Gradle - Scalable Setup Practices.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.5–12 (빌드 스크립트·version catalog)

**요약** — 좋은 Gradle 구성 요소를 "언제" 도입할지에 대한 JetBrains의 권장 시점.

**핵심 내용**

- version catalog(+bundles) → **처음부터**
- build cache·configuration cache, 모듈화 → **프로젝트가 커지기 시작하면** (패키지 경계를 Gradle 모듈로)
- convention plugin으로 빌드 로직 공유 → **모듈 3개 이상**부터
- 팀원의 Gradle 숙련도를 고려할 것 — 과한 추상화는 비용

**학습 포인트**

- 자기 프로젝트를 이 기준에 대입해 다음에 도입할 것 하나를 정하기
- [jb.gg/gradle-best-practices](https://jb.gg/gradle-best-practices)

## Gradle - Fast Inner Dev Loops

> *Fast inner dev loops for Kotlin Gradle builds* — Alex Semin, Rodrigo Oliveira · [PDF](<Gradle - Fast Inner Dev Loops.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.10, 14, 21, 31–43

**요약** — Gradle 팀이 말하는 빠른 반복 개발의 핵심: configuration cache와 그 다음 단계인 isolated projects.

**핵심 내용**

- 흐름의 시간 척도: 0.1s 즉시, 1s 끊김 없음, 10s 주의 분산, 1m+ 계획이 필요. 원칙은 "오버헤드는 변경 크기에 비례해야 한다"
- 빌드 = initialization → configuration → execution. 실행은 up-to-date·build cache·병렬화로 이미 최적화 → 남은 병목은 **configuration**
- **Configuration cache**: 6.6 실험 → 8.1 stable → **9.0 preferred mode**. 데이터 중복 제거로 대형 빌드에서 캐시 크기 3.75배 감소, 8.11부터 work graph 병렬 로드, `org.gradle.configuration-cache.parallel=true`로 병렬 저장
- 적중률 높이기: 최신 도구, configuration 입력 최소화(빌드 타임스탬프 같은 env 변수 금지), Develocity로 추적. 비호환 task는 `notCompatibleWithConfigurationCache()`
- **Isolated Projects**(pre-alpha): 프로젝트별 구성을 독립·병렬화, IDE sync에도 적용 예정. [Declarative Gradle](https://declarative.gradle.org) 실험(`build.gradle.dcl`)

**학습 포인트**

- `--configuration-cache` 로 빌드하고 리포트에서 문제 목록 정리 → 해결 가능한 것부터 처리
- 두 번째 실행에서 configuration 시간이 얼마나 줄어드는지, 어떤 입력이 캐시를 무효화하는지 측정

---

## Static Analysis - KtLint & Detekt

> *Code Quality at Scale: Future-Proof Your Android Codebase with KtLint and Detekt* — Tristan Hamilton · [PDF](<Static Analysis - KtLint & Detekt.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.24–36, 52–78, 83–122, 136–143 (커스텀 detekt 룰은 p.86–122)

**요약** — 포맷팅·정적 분석을 넘어, **타입 해석을 쓰는 detekt 커스텀 룰**로 R8 full mode를 안전하게 켜고 아키텍처 규칙을 강제한 HubSpot 사례.

**핵심 내용**

- 기반: KtLint(kotlinter 플러그인, 단순함), detekt(순수 Kotlin, baseline, 서드파티 룰셋, 커스텀 룰), 커버리지, 의존성 관리
- R8 full mode(AGP 8+)의 위험: Retrofit + Gson이 리플렉션으로 쓰는 data class 2,000여 개가 제거·난독화됨
- 해법: **PSI + `bindingContext`(타입 해석)** 를 쓰는 detekt 룰 `RetrofitGsonR8KeepRule` — Retrofit 인터페이스에서 쓰이는 모델에 keep 규칙이 있는지 검사. 커스텀 serializer·기타 리플렉션 호출도 같은 방식으로 정리
- 결과: 앱 크기 ~33% 감소(60→40MB), 시작 시간 ~12% 단축(654→576ms)
- 확장: "인프라 → 아키텍처" — 아키텍처 규칙(예: 특정 API 사용 위치 제한)을 detekt 룰로 자동화, LLM 활용 언급

**학습 포인트**

- 타입 해석이 필요한 룰과 필요 없는 룰의 차이(syntactic vs semantic) 이해 후 간단한 커스텀 룰 작성
- 리플렉션 의존 코드(직렬화, DI)가 R8 full mode에서 깨지는 지점을 정적으로 찾는 방법 정리
- 발표자의 [Medium 시리즈](https://medium.com/@thamilton_82161)

---

## Amper - Latest Updates

> *What's new in Amper* — Dmitry Savvinov · [PDF](<Amper - Latest Updates.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.33–39, 50–60

**요약** — JetBrains의 실험적 빌드 도구 Amper가 추구하는 비전과, 이를 가능하게 하는 "toolable" 빌드 설계.

**핵심 내용**

- Amper: 빌드 구성·의존성 관리·컴파일러 호출·캐싱. 독립 실행형과 Gradle 플러그인 두 형태, JVM 서버(Ktor·Spring)와 KMP(Android·iOS·Compose Desktop) 지원. YAML은 최종 선택이 아님
- 비전: 빌드 도구를 만지지 않게(템플릿·플랫폼 기본 지원), 오류를 빨리 발견, **구성이 실제와 일치** — IDE가 각 설정값이 어디서 왔는지(`[default]`, `[template]`, `[inherited]`) 보여줌
- 추적 가능성(traceability): 선언적이라고 항상 추적 가능한 것도(Maven 플러그인), 명령형이라고 항상 불가능한 것도 아님 → **선언적 구성 + 명확한 입력 + 예측 가능한 변환**
- 과제: 실제 빌드 작업용 명령형 코드·환경 변수·플러그인. 확장성(Gradle·Maven)과 toolability 사이에서 위치를 찾는 중 — 0.8에서 확장성 추가 예정

**학습 포인트**

- 작은 KMP 프로젝트를 Amper `module.yaml`로 구성해 보고 Gradle 대비 무엇이 단순해지고 무엇을 할 수 없는지 정리
- 자기 Gradle 빌드에서 "추적 불가능한" 부분(조건부 의존성, 런타임 계산) 찾기

## Buck2 - Building Kotlin & Android

> *Build your Kotlin and Android apps with Buck2* — Sergei Rybalkin · [PDF](<Buck2 - Building Kotlin & Android.pdf>)
>
> 예제 코드: 슬라이드 p.5–6, 17–18 · 도구 [facebook/buck2](https://github.com/facebook/buck2)

**요약** — Meta의 Android 앱을 빌드하는 오픈소스 빌드 시스템 Buck2의 구조와 Kotlin 지원.

**핵심 내용**

- 구조: Rust 코어(언어 무관, 병렬성·증분·I/O·원격 실행) + Starlark 규칙(Kotlin·Android·Java·C++, ksp2·kapt4·d8·redex) + 대부분 선언적인 target 정의(`android_library(...)`)
- 성능: 단일 lazy 빌드 그래프, GC 없음, repo 전체 작업 없음, **전역 캐시**(동료가 이미 빌드한 결과 재사용), 원격 실행
- Kotlin 최적화: **source-only ABI(KOSABI)**, smart deps tracking(실제 사용한 클래스만 의존으로 추적), 2단계 증분 — 그래프 수준(Buck2) + target 내부 클래스 수준(Kotlin Build Tools API)
- 도입 판단: 강력하고 빠르며 내부 버전과 동일한 오픈소스. 하지만 빌드 시스템 교체는 어렵고 Kotlin/Android 지원은 외부 사용자가 아직 없음

**학습 포인트**

- Gradle의 모듈 단위 증분과 Buck2의 2단계 증분·ABI 기반 재빌드 회피 비교
- [buck2.build getting started](https://buck2.build/docs/about/getting_started/)로 작은 Android 앱 빌드

---

## klibs.io - Kotlin Package Index

> *Klibs.io — the dream of creating a Kotlin Package Index* — Ignat Beresnev · [PDF](<klibs.io - Kotlin Package Index.pdf>)
>
> 예제 코드: 없음 (코드 없이 서비스·데이터 모델 소개 중심)

**요약** — KMP 라이브러리 검색 사이트 klibs.io를 만들며 마주친 "라이브러리란 무엇인가" 문제.

**핵심 내용**

- 수집: Maven Central 전체 스크래핑은 약관 위반 → `kotlin-tooling-metadata.json`이 있는 아티팩트로 KMP 라이브러리 식별
- "라이브러리"의 정의가 없음: 의존성 하나? group ID 하나(Koin은 여러 개)? Git 저장소 하나(kotlinx는 저장소마다 다름)? → GitHub 저장소를 "프로젝트" 단위로 채택
- 교훈: 인덱스는 저장소가 아니다, 생태계에 공통으로 강제되는 정의가 없다, 수많은 예외 케이스

**학습 포인트**

- 라이브러리 작성자라면 klibs.io에서 자기 라이브러리가 어떻게 보이는지 확인하고 메타데이터(README, 플랫폼 정보) 정비
