# KotlinConf 2025

KotlinConf 2025(2025.05, Copenhagen) 발표를 주제별로 정리한 학습 노트다. 각 폴더의 `README.md`에 발표별 요약과 학습 포인트가 있다.

## 구성

| 폴더 | 다루는 내용 | 발표 |
| --- | --- | --- |
| [01-language](<01-language/README.md>) | 언어 설계 — Rich Errors, 불변성, 스마트 캐스트, 타입 시스템, 컴파일러 플러그인 | 8 |
| [02-coroutines](<02-coroutines/README.md>) | 코루틴 디버깅·성능, Flow 수집, structured concurrency, Swift concurrency 비교 | 5 |
| [03-multiplatform](<03-multiplatform/README.md>) | KMP 도입 사례, iOS/Swift interop, Kotlin/Native, Wasm | 16 |
| [04-compose-android](<04-compose-android/README.md>) | Compose 애니메이션·커스텀 UI·Hot Reload·접근성, Android 아키텍처 | 8 |
| [05-server](<05-server/README.md>) | Ktor, Spring Boot 4, Exposed, 스트림 처리 | 10 |
| [06-build-tooling](<06-build-tooling/README.md>) | Gradle, Amper, Buck2, 정적 분석, 바이너리 호환성 | 7 |
| [07-ai-data](<07-ai-data/README.md>) | AI 에이전트(Koog), MCP, LangChain4j, AI 기반 마이그레이션, 데이터 분석 | 8 |
| [08-design-practices](<08-design-practices/README.md>) | API 설계, 데이터 중심 설계, 시스템 사고, 안티패턴 | 4 |
| [09-other](<09-other/README.md>) | IoT, NES 에뮬레이터 | 2 |

## 관심사별 학습 경로

- **언어의 다음 단계** — 01 전체 → 08 `Data-Oriented Design` → 06 `Compatibility`
- **동시성** — 01 `Smart Casts` → 02 전체 → 05 `streaming/`
- **멀티플랫폼 도입** — 03 `case-studies/` → 03 `ios/`, `native/` → 06 `Gradle`
- **서버 개발** — 05 `ktor/` 또는 `spring/` → 02 `Structured Concurrency` → 05 `streaming/`
- **AI 통합** — 07 `AI Basics` → `MCP` → `Agents`

## 규칙

- 발표마다 폴더 README에 `주제 - 핵심 내용` 형식의 소제목을 두고, 원래 발표 제목, 발표자, 발표 영상 링크를 함께 적는다.
- 발표 슬라이드는 저작권이 발표자에게 있어 저장소에 포함하지 않는다. 슬라이드와 영상은 [KotlinConf 2025 공식 페이지](https://2025.kotlinconf.com/talks/)에서 볼 수 있다.
- 발표가 많은 장(03, 05)은 README 안에서 소주제(`case-studies/`, `ios/` 등)로 나눈다.
- 대부분 발표 시점에 실험·설계 단계였던 기능이므로, 학습 전에 README에 링크된 KEEP·이슈에서 현재 상태를 확인한다.
