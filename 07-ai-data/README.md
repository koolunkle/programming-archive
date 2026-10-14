# 07. AI & Data — AI 에이전트와 데이터 분석

Kotlin으로 LLM 애플리케이션을 만들고 운영하는 방법, AI를 코드 마이그레이션에 쓰는 방법, Kotlin 데이터 분석 도구를 다룬다. 라이브러리 버전은 2025.05 기준(LangChain4j·Spring AI 1.0 직후, MCP spec 2024-11-05)이라 API 세부는 바뀌었을 수 있다.

## 학습 순서

1. **AI Basics** — RAG, embedding, function calling, MCP의 개념 지도. 입문용이므로 아는 부분은 건너뛸 것
2. **MCP** → **LangChain4j** — 프로토콜과, 이를 선언적으로 감싼 프레임워크
3. **Agents - Koog** — 그래프 기반 에이전트 전략, 히스토리 압축, 스트리밍 tool call
4. **Agents - LLM App Evaluation** — 프로덕션 운영: 멀티 에이전트 구조, 평가, 관측성
5. **AI Bot** — LLM 호출을 줄이는 semantic routing·caching
6. **Migration (Uber)** — AI를 *제품*이 아니라 *개발 도구*로 쓰는 사례
7. **Data Analysis** — 별개 주제. Kotlin DataFrame + Kandy

발표 간 연결:

- 같은 문제, 다른 답 — "어떤 함수를 호출할지" 결정: LLM function calling(AI Basics, LangChain4j) vs 그래프로 고정된 전략(Koog) vs embedding 유사도(AI Bot의 semantic routing)
- 평가와 관측성은 Koog(`Tracing`)와 Agentic Platform(OpenTelemetry + Phoenix)이 공통으로 강조 — 프로토타입과 프로덕션을 가르는 지점
- Uber의 마이그레이션 검증 문제(null 안전성) ↔ [01 JSpecify](<../01-language/Nullness - JSpecify Annotations for Java Interop.pdf>)

---

## AI Basics - Getting Started for Kotlin Devs

> *From 0 to h-AI-ro: high-speed track to AI for Kotlin developers* — Urs Peter · [PDF](<AI Basics - Getting Started for Kotlin Devs.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.9, 16, 19, 27, 32, 37–55, 60–70

**요약** — 이탈리안 레스토랑 챗봇 예제로 RAG → function calling → MCP를 차례로 쌓아 올리는 개념 정리.

**핵심 내용**

- LLM 데이터 확장 방법: fine-tuning(도메인 데이터셋으로 재학습) vs **RAG**(prompt에 관련 데이터를 "stuffing")
- RAG 파이프라인: 문서 분할 → embedding → vector DB 저장(ingestion) / 질의 embedding → 근접 이웃 검색 → prompt에 주입(retrieval). 유사도는 주로 cosine
- 검색 알고리즘: 정확한 KNN은 느리므로 HNSW·IVFFlat 등 인덱스 기반 **ANN**으로 정확도를 일부 희생하고 속도 확보
- prompt 구조: `system` / `user` / `assistant` / `tool` role, chat history 전체를 매번 전송
- MCP: JSON-RPC, 로컬은 stdio·원격은 HTTP(SSE). 서버가 prompt template·resource·tool을 제공하고 클라이언트(Claude, LangChain4j, Spring AI, kotlin-sdk)가 발견·실행
- **LangChain4j vs Spring AI**: 모듈성·고급 document splitter·RAG routing은 LangChain4j, Spring 통합·transcription·멀티모달은 Spring AI

**학습 포인트**

- 고급 독자라면 ANN 인덱스 트레이드오프, RAG routing(LLM이 검색 tool 호출 여부를 결정) 부분만 확인
- 같은 RAG 챗봇을 LangChain4j와 Spring AI로 각각 구성할 때 추상화 차이 비교

---

## MCP - Model Context Protocol SDK

> *Model Context Protocol in Kotlin* — Alexander Sysoev · [PDF](<MCP - Model Context Protocol SDK.pdf>)
>
> 예제 코드: [Mr3zee/kotlinconf-app](https://github.com/Mr3zee/kotlinconf-app/blob/main/MCP.md) (KotlinConf 앱에 MCP 서버를 붙인 예) · 슬라이드 p.6–21, 46–59

**요약** — MCP를 "LLM을 위한 HTTP"로 설명하고 Kotlin SDK로 서버를 만드는 라이브 데모 중심 발표.

**핵심 내용**

- 비유: HTTP 서버(Spring, Ktor) ↔ **MCP 서버**(kotlin-sdk, spring-ai) / curl·Ktor Client ↔ **MCP 클라이언트** / 브라우저 ↔ **MCP 호스트**(Claude, Raycast, AI Assistant)
- OpenAPI의 endpoint 정의가 MCP에서는 tool의 `name` + `description` + JSON Schema `inputSchema`가 됨 — 설명문이 곧 LLM이 읽는 인터페이스
- 흐름: 호스트가 `list_tools`로 기능 탐색 → LLM이 tool 선택 → `execute` → 결과를 LLM이 자연어로 정리
- 기능 4종: **Tools**, **Prompts**, **Resources**, **Sampling**(서버가 클라이언트의 LLM을 역으로 호출)

**학습 포인트**

- 슬라이드는 개요뿐이고 본론은 데모 — [kotlinconf-app MCP.md](https://github.com/Mr3zee/kotlinconf-app/blob/main/MCP.md)의 예제를 따라 직접 서버 구현
- tool `description`을 바꿔가며 LLM의 tool 선택이 어떻게 달라지는지 실험
- Sampling이 필요한 경우(서버 측에서 LLM 추론이 필요한데 API 키를 두고 싶지 않을 때)를 정리

---

## LangChain4j - with Quarkus

> *LangChain4j with Quarkus* — Max Rydahl Andersen, Konstantin Pavlov · [PDF](<LangChain4j - with Quarkus.pdf>)
>
> 예제 코드: 슬라이드 p.9, 13–23 · 확장 [quarkiverse/quarkus-langchain4j](https://github.com/quarkiverse/quarkus-langchain4j), [quarkiverse/quarkus-mcp-server](https://github.com/quarkiverse/quarkus-mcp-server)

**요약** — 인터페이스 선언만으로 AI 서비스를 만드는 quarkus-langchain4j와, LLM 호출을 mock하는 테스트 방법.

**핵심 내용**

- **선언적 AI 서비스**: `@RegisterAiService(tools = [...])` 인터페이스에 `@SystemMessage`/`@UserMessage` 템플릿(`{topic}` 바인딩) → 구현은 프레임워크가 생성. tool은 CDI bean의 `@Tool` 메서드
- LangChain4j는 "AI계의 Hibernate" — 특정 프레임워크에 묶이지 않는 공통 계층. 1.0 출시(2025.05), 다음 과제는 **네이티브 async API**
- Quarkus 계층이 더하는 것: OpenTelemetry·Micrometer 기반 metric/tracing/auditing, fault tolerance, Dev UI, prompt hot reload
- Quarkus MCP Server SDK가 Kotlin 지원: `@Tool` 함수 하나 + jbang으로 실행
- **테스트**: [mokksy](https://mokksy.dev)로 OpenAI API를 mock(`completion { userMessageContains(...) } responds { ... }`) — 빠르고, 무료이고, 오프라인

**학습 포인트**

- 샘플 `samples/chatbot-easy-rag-kotlin`(quarkus-langchain4j 저장소) 실행 후 tool 호출 trace를 LGTM devservice에서 확인
- LLM 의존 코드의 테스트 전략 정리: mock 응답(결정적) vs 실제 모델(평가) 구분
- 발표자가 요청한 Kotlin 기여 지점(extension 함수, coroutine 친화 async API)이 지금 어떻게 해결됐는지 확인

---

## Agents - Koog Framework

> *Building AI Agents in Kotlin* — Vadim Briliantov · [PDF](<Agents - Koog Framework.pdf>)
>
> 예제 코드: [JetBrains/koog](https://github.com/JetBrains/koog) (발표 대상 프레임워크) · 슬라이드 p.76–85, 124–128, 135–148

**요약** — JetBrains의 오픈소스 에이전트 프레임워크 Koog. 에이전트 = **Strategy + Tools + Model(s)**.

**핵심 내용**

- AI workflow(사람이 정한 고정 단계) vs AI agent(LLM이 "두뇌"가 되어 tool 호출 순서를 결정)의 구분
- 설계 요구사항: 예측 가능·빠름·확장 가능·재사용·조합 가능·추적 가능·멀티플랫폼
- tool 정의: 함수에 `@Tool` + `@LLMDescription` → `ToolRegistry { tool(::sendMoney) }`로 등록. 단순 사용은 `simpleSingleRunAgent` 하나로 끝
- **그래프 전략 DSL**: `nodeLLMRequest`, `nodeExecuteTool`, `nodeLLMSendToolResult` 를 `edge(a forwardTo b onToolCall { ... })` 로 연결. `subgraphWithTask`로 단계별 **다른 모델·다른 tool 집합** 지정(예: 데이터 수집 GPT-4o → 보고서 Sonnet → 검증 o3, 검증 실패 시 되돌아가는 edge)
- LLM의 비신뢰성에 대한 실전 해법
  - 긴 히스토리 → `nodeLLMCompressHistory` (chunk 단위 요약, 키워드 기반 **fact 추출**)
  - 순차 tool 호출이 느림 → Markdown 구조 정의 + **Kotlin Flow 스트리밍**으로 응답이 오는 중에 tool 호출, 병렬 실행도 가능
  - `install(AgentMemory)` 로 사실(fact)을 scope별(사용자/제품)로 저장·재사용, `install(Tracing)` 으로 평가 기반 마련
- OpenAI·Anthropic·Google·Ollama·OpenRouter 지원, MCP 통합, embeddings

**학습 포인트**

- "LLM 요청 → tool 실행 → 결과 전송" 기본 루프를 그래프 DSL로 직접 구성해 보고, 같은 동작을 LangChain4j의 선언적 서비스와 비교
- 히스토리 압축 전략별로 토큰 비용과 응답 품질 변화 측정
- 단계마다 모델을 다르게 두는 설계가 비용·지연·정확도에 미치는 영향 정리

---

## Agents - LLM App Evaluation Platform

> *Building an Agentic Platform with Kotlin* — Patrick Whelan · [PDF](<Agents - LLM App Evaluation Platform.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.9–10 · 프레임워크 [Eclipse LMOS Arc](https://eclipse.dev/lmos/arc2/), [a2a-4k/a2a-4k](https://github.com/a2a-4k/a2a-4k)

**요약** — 월 50만 대화를 처리하는 멀티 테넌트 고객 지원 봇의 구조와, "가장 빠르게 피드백 루프를 돌리는 시스템이 최고"라는 운영 원칙.

**핵심 내용**

- 규모: 월 ~50만 대화, LLM 호출 200~300만 회, 20개 이상 에이전트, 허용 가능 답변률 87~93%
- 구조: Supervisor가 intent로 에이전트 라우팅(Billing, FAQ, Contract…) → 답 못 하면 `NO_ANSWER`/`HANDOVER`. guardrail, 익명화 서비스, Redis 메모리, Qdrant RAG
- 에이전트 유형: **RAG 기반**(유동적 흐름, FAQ) vs **tool 기반**(정해진 흐름, 단일 비즈니스 도메인)
- **ARC**: Eclipse LMOS의 Kotlin DSL 에이전트 프레임워크. 목표는 "프로토타입과 프로덕션에 같은 기술" — Kotlin Script로 빠르게 작성
- 평가 3단계: 코드(빠르고 싸지만 제한적) / **LLM-as-a-Judge**(자연어 이해, 덜 신뢰) / 사람(최고 품질, 비쌈)
- 관측성: OpenTelemetry + OpenInference 규약 → [Phoenix](https://phoenix.arize.com/)로 trace·데이터셋·비용 추정·대화 감사
- 피드백 루프: Prototype → Test → Deploy → Monitor → Annotate → Evaluate → Improve

**학습 포인트**

- 자신의 LLM 기능에 대해 평가 데이터셋을 만들고, 코드 검사와 LLM-as-a-Judge를 각각 어디에 쓸지 나눠보기
- Supervisor 라우팅 vs Koog의 그래프 전략 — 에이전트 수가 늘어날 때 각각의 확장성
- [ARC](https://eclipse.dev/lmos/arc2/), [a2a-4k](https://github.com/a2a-4k/a2a-4k)(Agent-to-Agent 프로토콜 Kotlin 구현)

---

## AI Bot - Bluesky Stream Analysis

> *From Data to Insights: Building a Bluesky Bot powered by AI* — Raphael De Lio · [PDF](<AI Bot - Bluesky Stream Analysis.pdf>)
>
> 예제 코드: 없음 (슬라이드에는 아키텍처만, 코드는 라이브 데모)

**요약** — 분당 ~2,200건의 Bluesky 게시물을 실시간 분류·집계하는 봇. 핵심은 **LLM 호출을 embedding 유사도로 대체**하는 기법들.

**핵심 내용**

- 수집: Jetstream → **Redis Streams**(consumer group으로 여러 서비스가 분산 소비)
- **Semantic classification**: "AI 관련 글인가?"를 LLM에 묻는 대신, 예시 500개를 embedding해 두고 유사도로 판별
- 주제 추출은 LLM으로 하고, 빈도는 확률적 자료구조 **Top-K**로 집계
- **Semantic routing**: 사용자 질문을 어떤 함수로 보낼지 LLM 대신 route별 예시 embedding과 비교, 유사도가 임계값 이상일 때만 호출
- **Semantic caching**: 표현만 다른 같은 질문은 캐시된 답 재사용
- 중복 제거: at-least-once 전달이므로 SET 또는 **Bloom filter**로 처리 여부 확인

**학습 포인트**

- LLM 호출 하나를 semantic classification으로 바꿨을 때 지연·비용·정확도 비교
- semantic routing의 임계값 설정 기준과, 임계값 미달 시 LLM fallback 설계
- Top-K, Bloom filter의 오차 특성이 이 용도에서 왜 허용 가능한지 설명해 보기

---

## Migration - Java to Kotlin with AI at Uber

> *Large Scale Changes with AI: Migrating millions of lines of Java to Kotlin at Uber* — Ty Smith · [PDF](<Migration - Java to Kotlin with AI at Uber.pdf>)
>
> 예제 코드: 슬라이드 p.31–41 · 도구 [fbsamples/kotlin_ast_tools](https://github.com/fbsamples/kotlin_ast_tools)

**요약** — 수백만 줄 Java→Kotlin 전환을 분산 수작업 → 중앙 자동화 → LLM+AST → 에이전트로 발전시킨 과정. 예상 완료 시점이 2033 → 2029 → 2028 → 2027로 당겨짐.

**핵심 내용**

- Kotlin 전환 효과(Uber 측정): PR +3건/월, LOC −17%, 복잡도 −30%, null 관련 이슈 −20%
- **ReKtify**: J2K 전후에 preprocessor(nullable 어노테이션 보강)·postprocessor(Mockito/Guava/AutoDispose 등 관용구 변환 규칙)를 붙인 파이프라인. git 히스토리 보존
- **AutoMigrate**(LSC 플랫폼) 4단계: 문제 식별(파일/모듈 단위 sharding) → 코드 변환(headless IntelliJ + J2K) → 안전성 검증(CI) → **Shepherd**(PR 생성·리뷰어 배정·자동 rebase 캠페인 관리)
- **AST + LLM**: LLM이 코드를 직접 고치는 대신 **결정적인 ReKtify 규칙을 생성** → 빌드·테스트·사람 리뷰. 환각 위험을 규칙 검증 단계로 흡수 (Amazon Q가 OpenRewrite를 쓰는 것과 같은 발상)
- 런타임 위험: Java의 `@NonNull` 위반이 Kotlin에서는 crash가 됨 → **NullPatrol**(javac 플러그인으로 null 검사 대신 보고 코드 삽입)로 사전 탐지
- 다음 단계: J2K·CI·코드 리뷰를 MCP 서버로 노출하고 에이전트가 사용. 병목은 리뷰어·merge queue 과부하

**학습 포인트**

- "LLM이 코드를 고친다" vs "LLM이 변환 규칙을 만든다" — 대규모 변경에서 후자가 유리한 이유 정리
- 자기 코드베이스에서 LSC가 필요한 작업(라이브러리 교체, deprecated API 제거)을 하나 골라 4단계에 맞춰 설계
- [kotlin_ast_tools](https://github.com/fbsamples/kotlin_ast_tools), [NullAway](https://github.com/uber/NullAway)

---

## Data Analysis - Finance with DataFrame & Kandy

> *Data Analysis for Finance in Kotlin* — Enrique López-Mañas · [PDF](<Data Analysis - Finance with DataFrame & Kandy.pdf>)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.36–91 · 라이브러리 [Kotlin/dataframe](https://github.com/Kotlin/dataframe), [Kotlin/kandy](https://github.com/Kotlin/kandy)

**요약** — R·Python이 장악한 재무 데이터 분석을 Kotlin Notebook + DataFrame + Kandy로 해보고 생태계를 비교.

**핵심 내용**

- **Kotlin DataFrame**: schema 추론으로 타입이 붙은 열, 열 접근이 컴파일 타임에 검사되어 키 오타가 런타임 오류가 되지 않음. CSV·JSON·SQL·Kotlin 객체에서 읽기
- 예제: 독일 대기업 12곳의 재무 데이터를 `convert`/`add`/`groupBy { }.aggregate { }`로 정제·집계(mean, median, std)
- **Kandy**: Grammar of Graphics 기반 차트 DSL(`plot { x(...); y(...); line { color(sector) } }`), IntelliJ·Datalore·Jupyter에서 동작
- 비교: 타입 안전성·성능·백엔드 통합은 Kotlin 우위, 커뮤니티·시계열·금융 전용 라이브러리는 R/Python이 성숙(Kotlin은 수동 구현이나 Java interop)

**학습 포인트**

- 익숙한 pandas 분석 하나를 Kotlin Notebook으로 옮겨 보고, 타입 안전한 열 접근이 실제로 어떤 실수를 막는지 확인
- 시계열·금융 지표처럼 Kotlin 생태계가 비어 있는 영역을 Java 라이브러리로 메우는 방법
- [Kotlin DataFrame](https://github.com/Kotlin/dataframe), [Kandy](https://github.com/Kotlin/kandy), [Kotlin for data analysis](https://kotlinlang.org/docs/data-analysis-overview.html)
