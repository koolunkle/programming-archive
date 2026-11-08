# 05. Server — Ktor, Spring, 스트리밍

Kotlin 서버 개발의 세 축: Spring 생태계의 Kotlin 지원 강화, Ktor 중심의 경량 스택과 HTML-first UI, 그리고 Kafka·Flink 기반 스트림 처리. 2025.05 기준으로 Spring Boot 4는 출시 전(2025.11 예정), Exposed 1.0은 beta 단계였다.

## 학습 순서

1. **Spring Boot 4** — JSpecify null-safety, 새 bean 등록 DSL, AOT/CDS. Spring 사용자라면 가장 먼저
2. **Modern Server Stack** — Kotlin+Spring 조합의 실전 관용구와, 그 위에서 만든 에이전트 프레임워크(Embabel)
3. **Serverless** — 클라우드 중립 FaaS를 위한 모듈 구조
4. **Exposed 1.0** — R2DBC 지원과 안정화 약속. 서버 스택 공통의 데이터 계층
5. **ktor/** — Ktor 플러그인 작성(**Plugins**), HTML-first 프론트엔드(**HTMX**)
6. **Full-Stack Webapp** · **streaming/http4k** — JS 없이 서버 주도 UI를 만드는 다른 두 방식
7. **streaming/** — Kafka Streams·RocksDB → Flink. 상태 있는 스트림 처리

발표 간 연결:

- 서버 주도 UI 세 가지 — HTMX(HTML 조각 교체), Datastar(SSE로 DOM 병합, http4k), Vaadin/Karibu(서버 컴포넌트, Full-Stack Webapp). 모두 "SPA의 복잡도를 백엔드로 되돌린다"는 같은 동기
- Spring의 JSpecify 채택 ↔ [01 Nullness](<../01-language/README.md#nullness---jspecify-annotations-for-java-interop>) — 플랫폼 타입이 사라지는 실제 사례
- 상태 저장소 RocksDB가 Kafka Streams(서비스 내장 캐시)와 Flink(operator state) 양쪽에 등장 — 같은 기술의 다른 쓰임
- 에이전트 프레임워크 비교: Embabel(목표 기반 planning) ↔ [07 Koog](<../07-ai-data/README.md>)(그래프 전략)

---

## Spring Boot 4 - Kotlin Support

> *Next level Kotlin support in Spring Boot 4* — Sébastien Deleuze · [영상](https://www.youtube.com/watch?v=QExksqeNWbY)
>
> 예제 코드: [sdeleuze/spring-petklinik](https://github.com/sdeleuze/spring-petklinik) · 슬라이드 p.8–21, 28, 34–37, 50

**요약** — JetBrains–Spring 전략적 파트너십 발표와 함께 Spring Framework 7 / Boot 4(Kotlin 2.2+ 기준)의 Kotlin 개선점.

**핵심 내용**

- **JSpecify null-safety**: 패키지에 `@NullMarked`, 필요한 곳만 `@Nullable` → Kotlin에서 `String!` 대신 `String`/`T?` 로 보임. Framework에 이어 Boot·Data·Security·AI까지 포트폴리오 전체로 확대 예정
- **`BeanRegistrarDsl`**: 기존 `beans { }` DSL을 대체. Java `BeanRegistrar` 기반이라 Spring AOT(람다 포함)를 완전 지원, `@Import`로 `@Configuration`과 혼용, 라이브러리가 자체 설정 DSL(예: `SecurityRegistrar`)을 만드는 토대
- kotlin-reflect 사용 효율 개선으로 시작 시간·메모리 감소
- **CDS + AOT cache**: Buildpacks(`bootBuildImage`)가 이미지 빌드 시 training run을 자동 수행 → Petclinic 기준 시작 최대 4.4배 빠름, RSS 최대 22.7% 감소, warmup 단축
- Kotlin 2.2 `-Xannotation-default-target=param-property`: `@get:JsonProperty` 없이 `@JsonProperty` 만으로 Jackson 매핑 (Spring AI structured output 예제)
- 레퍼런스 앱 [spring-petklinik](https://github.com/sdeleuze/spring-petklinik): Boot 4 백엔드 + KMP 공유 코드(템플릿·검증·DTO) + Kotlin/Wasm 프론트 + Spring AI

**학습 포인트**

- 기존 프로젝트를 Boot 4로 올릴 때 JSpecify로 바뀌는 시그니처 때문에 생길 컴파일 오류 유형 예측 (`-Xjsr305=strict` 사용 여부에 따라 다름)
- `@Configuration` 클래스 하나를 `BeanRegistrarDsl` 로 옮겨 보고 AOT 처리 결과 비교
- CDS/AOT cache를 켠 이미지와 끈 이미지의 시작 시간 직접 측정

---

## Modern Server Stack - Kotlin & Spring

> *Kotlin and Spring: The Modern Server Side Stack* — Rod Johnson · [영상](https://www.youtube.com/watch?v=NcAW-FZtpzk)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 9–13, 20 · 프레임워크 [embabel/embabel-agent](https://github.com/embabel/embabel-agent)

**요약** — Spring 창시자가 말하는 Kotlin+Spring의 장점과, 이 슬라이드 자체를 생성한 Kotlin 에이전트 프레임워크 **Embabel**.

**핵심 내용**

- 관용구: `kotlin-spring` 컴파일러 플러그인(all-open)으로 `open` 불필요, `data class` + `@ConfigurationProperties` 로 불변·타입 안전 설정, `reified` 확장 함수로 Java API의 `Class<T>` 인자 제거
- 프레임워크 작성자 조언: Kotlin 친화 API를 만들더라도 Java가 호출할 수 있는 `Class<O>` 형태를 항상 함께 제공
- 에이전트에 대한 관점: 더 좋은 모델, MCP, Spring AI/LangChain 수준 프레임워크(서블릿 API에 비유)는 필요하지만 충분하지 않음 — **orchestration**이 핵심이고 단순 상태 머신으로는 부족
- **Embabel**: `@Agent`/`@Action`/`@Condition` 어노테이션, Planner가 현재 상태에서 목표까지 경로를 계획(코드 수정 없이 action 추가로 확장), 도메인 객체 메서드를 LLM tool로 노출. 이 HTML 슬라이드도 YAML 서사 입력 → 조사 → Marp 생성까지 에이전트가 만듦

**학습 포인트**

- 같은 에이전트 시나리오를 Embabel(목표 기반 planning)과 Koog(명시적 그래프)로 설계해 보고, 예측 가능성과 확장성 비교
- 사내 Kotlin 라이브러리에 Java 호출자를 위한 API가 필요한지 점검

---

## Serverless - Clean Architecture with Spring Cloud Function

> *Kotlin Clean Architecture for Serverless: Business Logic You Can Take Anywhere* — Elena van Engelen · [영상](https://www.youtube.com/watch?v=6jZa4B-If-I)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.9–15, 19–20, 23

**요약** — 서버리스의 lock-in은 플랫폼이 아니라 코드 구조의 문제. Clean Architecture + Spring Cloud Function + Gradle 모듈로 AWS Lambda와 Azure Functions에 같은 비즈니스 로직을 배포.

**핵심 내용**

- 모듈: `software/{domain, application, infrastructure}` + `aws/`, `azure/`(플랫폼 어댑터) + `cdk/`(인프라 코드). Gradle 모듈 의존성으로 계층 분리를 **강제**
- Spring Cloud Function: 비즈니스 로직은 `Function<I, O>` bean, 플랫폼별 adapter(`spring-cloud-function-adapter-aws` / `-azure`)가 이벤트 변환. AWS는 `FunctionInvoker` + `SPRING_CLOUD_FUNCTION_DEFINITION`, Azure는 `@FunctionName` 핸들러
- 인프라도 Kotlin: **Terraform CDK**로 Lambda·API Gateway·Azure Function App 정의 → `cdktf synth` 후 `terraform apply`

**학습 포인트**

- 기존 Lambda 함수 하나를 domain/application과 adapter로 분리하고, 같은 로직을 로컬 Spring Boot로도 실행해 보기
- Spring Cloud Function 사용 시 cold start 비용 — Spring Boot 4의 CDS/AOT와 조합 가능성 검토

---

## Exposed 1.0 - SQL Library

> *Exposed 1.0: Stable, Scalable, and Looking Forward* — Chantal Loncle · [영상](https://www.youtube.com/watch?v=xgfeqj8UyVA)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.40–62, 70–134, 138–155

**요약** — JetBrains의 Kotlin SQL 라이브러리 Exposed의 1.0 안정화. 가장 큰 변화는 **R2DBC(비동기) 지원**.

**핵심 내용**

- 사용자 설문 1순위 요구(비동기·non-blocking 드라이버·R2DBC·coroutine 한계)에 대응: `exposed-r2dbc` 모듈, `R2dbcDatabase.connect`, `suspendTransaction`, CRUD·`SchemaUtils`가 `suspend` 함수
- JDBC와 쿼리 빌드 API는 동일하고 terminal 연산(`count()`, `empty()`)만 suspend. JDBC의 `SizedIterable`은 `Iterable`, R2DBC는 **`Flow`**
- DSL 기능 총정리: `IdTable`/`CompositeIdTable`, `transform`으로 커스텀 ID 타입, `reference`/`optReference` FK, JSON(B) `extract`, 배열 컬럼, `withDefinition(...).databaseGenerated()`(generated column), 커스텀 `ColumnType`, `insertReturning`, join update, **`mergeFrom`**(MERGE), window function(`denseRank().over()`), CTE
- 안정화 약속: 문서화된 API 계약 준수, minor 릴리스에서 breaking change 없음
- 로드맵: 성능 벤치마크, **DAO 재설계**(캐시 동작 투명화, 1·2차 캐시 사용자 정의), 마이그레이션 플러그인

**학습 포인트**

- 같은 쿼리를 JDBC와 R2DBC 모듈로 작성해 트랜잭션·결과 소비 방식(`Iterable` vs `Flow`) 차이 정리
- DSL과 DAO 중 무엇을 쓸지: 로드맵의 DAO 재설계가 현재 어떻게 진행됐는지 확인 후 결정
- JPA/jOOQ 대비 Exposed의 위치 — window function·MERGE·CTE까지 DSL로 커버되는지 실제 쿼리로 검증

---

## Full-Stack Webapp - Kotlin-only Side Project

> *That's Unpossible - A Full Stack Side Project webapp* — Dan Kim · [영상](https://www.youtube.com/watch?v=FXGT6HbBXNw)
>
> 예제 코드: [lateplate/kc2025-app](https://github.com/lateplate/kc2025-app) · 슬라이드 p.27–141 전반

**요약** — CSS·JS 없이 Kotlin만으로 SPA처럼 동작하는 웹앱 만들기: **Vaadin + Karibu-DSL**(UI) + **Exposed**(DB) + coroutines.

**핵심 내용**

- 동기: 사이드 프로젝트에서 React+백엔드+ORM을 모두 익히다가 프로젝트가 죽는 문제
- 구성: Postgres + Exposed(DSL) + Vaadin/Karibu + coroutines, **Vaadin Boot**로 설정 파일 없이 `main()`에서 내장 서버 실행. 향후 UI 후보로 Compose Wasm 언급
- Vaadin: 서버 측 컴포넌트 프레임워크가 HTML/CSS/JS를 생성. **Karibu-DSL**이 Java 스타일 API를 Kotlin DSL(`verticalLayout { grid { column(...) } }`)로 감쌈. `@Push`로 WebSocket UI 갱신
- Vaadin UI 스레드는 blocking → DB 작업은 coroutine + `newSuspendedTransaction(Dispatchers.IO, db)`, 결과 반영은 `UI.accessSynchronously`
- 의도적으로 테스트·아키텍처 패턴 생략 — "빠르고 단순하게"가 목표. 발표자는 같은 구성을 실제 프로덕션에서도 사용

**학습 포인트**

- 내부 관리 도구처럼 UI 품질보다 개발 속도가 중요한 곳에 이 스택이 맞는지 판단 기준 정리
- 서버 상태 기반 UI(Vaadin)의 동시 접속자 수 대비 메모리 비용 — HTMX·Datastar 방식과 비교

---

## Plugins - Extending Ktor

> *Extending Ktor for Server Side Development* — Ido Flax · [영상](https://www.youtube.com/watch?v=XcxqhmG_bdg)
>
> 예제 코드: 슬라이드 p.7–29 · 발표자 라이브러리 [Flaxoos/extra-ktor-plugins](https://github.com/Flaxoos/extra-ktor-plugins)

**요약** — Ktor 생태계가 Spring보다 얇은 부분(Kafka, 분산 스케줄링, rate limit, circuit breaker)을 직접 플러그인으로 채우는 방법.

**핵심 내용**

- 현실: Kotlin 백엔드의 80% 이상이 Spring Boot, Ktor는 ~9%. IntelliJ 마법사 기준 Spring starter 138개 vs Ktor plugin 67개
- 플러그인 API: `createApplicationPlugin(name, ::Config) { on(MonitoringEvent(ApplicationStarted)) { ... } }` → `install(plugin) { ... }`. 설정 클래스로 파라미터화, `environment.config`와 연결
- 발표자의 [Extra Ktor Plugins](https://github.com/Flaxoos/extra-ktor-plugins): `Kafka`(토픽·producer·consumer·schema registry 설정 DSL), `TaskScheduling`(Redis 락 기반 분산 cron), `RateLimiting`(token bucket), 클라이언트 `CircuitBreaking`

**학습 포인트**

- `Application` 확장 함수로 쓰던 초기화 코드를 플러그인으로 바꿨을 때 얻는 것(설정 분리, 라이프사이클 이벤트, 재사용) 정리
- 분산 환경에서 스케줄 작업이 한 노드에서만 돌도록 하는 방식(Redis 락) — 락 만료 시간 설정의 함정

---

## HTMX - Full-Stack with Ktor

> *Simplifying Full-Stack Kotlin: A Fresh Take with HTMX and Ktor* — Anders Sveen · [영상](https://www.youtube.com/watch?v=AlGWsTXnWsY)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.23, 30–33 (데모는 p.26·35의 QR 코드)

**요약** — SPA 대신 HTML-first + HTMX로 필요한 곳에만 상호작용을 추가하는 풀스택 Kotlin.

**핵심 내용**

- SPA 구조의 비용: 프론트·백 두 빌드, JSON 계약, 별도 서버, 조정 비용과 인지 부하
- HTMX: HTML 요소에 `hx-get`/`hx-target` 같은 속성으로 동작 추가 → 서버가 **HTML 조각**을 반환해 교체. 프론트 빌드 없음, "interactive island"만 점진적으로
- Kotlin 쪽 구성: Ktor + **Kotlin HTML DSL**(타입·리팩터링 지원) 또는 Thymeleaf. 필요하면 Alpine.js 같은 경량 JS와 조합
- 과제: 배터리가 포함되지 않음 — 검증, 내비게이션, 에러 처리, 구조는 직접 설계. "프레임워크는 적게, 도구는 많이"

**학습 포인트**

- 기존 SPA 화면 하나(목록 + 인라인 편집)를 Ktor + HTML DSL + HTMX로 재구현하고 코드량·복잡도 비교
- HTMX가 맞지 않는 화면 유형(오프라인, 복잡한 클라이언트 상태) 정리

---

## http4k - Streaming Protocols

> *Full Stream Ahead: Breaking the protocol barrier with http4k* — Ivan Sanchez · [영상](https://www.youtube.com/watch?v=vewgb-vyJME)
>
> 예제 코드: [http4k/full-stream-ahead](https://github.com/http4k/full-stream-ahead) · 슬라이드 p.12–31

**요약** — 웹 UI 기술의 역사를 짚고, HTTP·WebSocket·SSE를 모두 **함수로 모델링**하는 http4k와 SSE 기반 hypermedia 프레임워크 **Datastar**를 결합.

**핵심 내용**

- 역사: CGI 전체 렌더링 → Ajax/SPA(두 기술 스택, SEO·보안 문제) → SSR 회귀(hydration 복잡도) → **hypermedia 부활**(2020+)
- WebSocket의 단점: HTTP 기능(캐싱·압축) 사용 불가, LB·프록시와 궁합 나쁨, 재연결 없음. **SSE**는 서버→클라이언트만 담당하고 기존 HTTP 위에서 동작
- http4k 철학: `(Request) -> Response` 함수 → 서버 없이 **메모리에서 테스트**. WS·SSE도 같은 방식으로 함수화하고 poly router로 HTTP와 혼합
- Datastar(14KB): 페이지 로드 시 SSE 연결, 서버가 `merge-fragments`(HTML)·`merge-signals`(JSON 상태) 이벤트를 push. 상태는 백엔드에, 부하가 높으면 서버가 반응성을 끌 수도 있음. http4k + Helidon + virtual thread 데모

**학습 포인트**

- [full-stream-ahead](https://github.com/http4k/full-stream-ahead) 예제로 같은 템플릿이 JSON(비반응)·event-stream(반응) 양쪽으로 서빙되는 구조 확인
- SSE 장기 연결이 많을 때 virtual thread가 주는 이점, LB의 idle timeout 설정 영향
- HTMX(요청-응답 교체) vs Datastar(서버 push 병합)의 적합한 화면 유형 구분

---

## Kafka Streams & RocksDB

> *Stream Processing Rocks!* — Hunter Sherman, Geoff Gerrietts · [영상](https://www.youtube.com/watch?v=PY7eOmnOK3s)
>
> 예제 코드: [HunterSherms/stream-processing-kotlin](https://github.com/HunterSherms/stream-processing-kotlin) (슬라이드에는 코드 없음, 라이브 코딩)

**요약** — DB 폴링 아키텍처를 Kafka 스트림 파이프라인으로 바꾸는 이유와, compacted topic + RocksDB로 서비스 내 상태를 다루는 방법.

**핵심 내용**

- 문제 도메인: 높은 처리량, 맥락 의존 변환이 많음, 처리 시간이 응답 SLO보다 김, 버스트 트래픽 → 폴링은 지연·빈 배치·병렬화 곤란
- 이점: 즉시 내구성(여러 노드에 기록 후 ack), 토픽 연결로 파이프라인 구성(cascading·forking·composite), 파티션 수까지 수평 확장
- 예제: 사용자 레코드에서 **PII를 제거**해 별도 토픽으로 forking
- **compacted topic**: 키별 최신 값만 유지 → 복제되는 분산 KV 저장소. 처음부터 읽어 캐시를 warm-up하고 이후 업데이트 소비
- 캐시가 커지면 **RocksDB**(임베디드 KV, 메모리·디스크 자동 균형)로 이전 → RAM은 줄지만 쓰기 가능한 디스크 필요

**학습 포인트**

- [stream-processing-kotlin](https://github.com/HunterSherms/stream-processing-kotlin) 코드로 compacted topic 기반 캐시 재구성 과정 추적
- 파티션 수가 consumer 확장 한계를 정한다는 점(48 파티션 → 약수 개수의 consumer)을 토픽 설계에 반영
- 재시작 시 캐시 warm-up 시간 — RocksDB 영속화로 얼마나 줄어드는지

---

## Flink & Ktor - Real-Time Dashboards

> *Event-Driven Analytics: Building Real-Time Dashboards with Apache Flink and Ktor* — Viktor Gamov · [영상](https://www.youtube.com/watch?v=2Eyq1VhBxUg)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.26–35

**요약** — Apache Flink의 핵심 개념(스트림, 상태, 시간)과 Ktor로 결과를 실시간 대시보드에 띄우는 구성. 슬라이드는 개념 위주, 구현은 데모.

**핵심 내용**

- Flink에서 batch는 bounded stream의 특수한 경우. 용도: ETL 파이프라인, 스트림/배치 분석, 이벤트 기반 앱(사기 탐지)
- API 계층: **SQL** / Table API(선언적, 코드 생성·비용 기반 최적화) ↔ DataStream API / Process Function(저수준, 상태·시간 직접 접근, 실수 여지 큼). 혼용 가능
- 상태: filter는 stateless, count는 stateful. 상태는 로컬·키 단위·단일 스레드로 단순하며 heap 또는 **RocksDB**에 저장, S3/HDFS로 snapshot해 장애 복구
- **event time과 watermark**: 스트림의 시간이 어디까지 진행됐는지 표시 → 늦게 도착한 이벤트 처리

**학습 포인트**

- 같은 집계를 Flink SQL과 DataStream API로 작성해 보고 추상화 수준별 트레이드오프 정리
- Kafka Streams(라이브러리, 서비스에 내장) vs Flink(별도 클러스터) 선택 기준
- 슬라이드·영상: [speaking.gamov.io](https://speaking.gamov.io/)
