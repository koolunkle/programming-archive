# 09. Other — 기타

주제 분류에 들어가지 않는 Kotlin 활용 사례: 임베디드 IoT 기기와 레트로 게임기 에뮬레이터.

---

## IoT - Embedded Linux & ESP32

> *IoT development with Kotlin* — Erik Hellman · [영상](https://www.youtube.com/watch?v=bKwdgMKyFOY)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.19–26, 32–34, 44–47 · 라이브러리 [JuulLabs/kable](https://github.com/JuulLabs/kable), [dossiersolutions/amqp-connector](https://github.com/dossiersolutions/amqp-connector)(보관됨)

**요약** — 화면이 거의 없고 항상 연결된 임베디드 Linux 기기를 Kotlin으로 개발할 때의 선택지: 런타임, Bluetooth, 백엔드 프로토콜, 프로비저닝.

**핵심 내용**

- 대상: ESP32 마이크로컨트롤러 / 임베디드 Linux(ARMv8). 선택 1순위 고민은 **Kotlin/Native vs Kotlin/JVM** — 실시간 요구 vs 쓸 수 있는 라이브러리
- Bluetooth: Linux에서는 BlueZ를 **D-Bus**로 제어. K/N은 Unix domain socket 직접 또는 cinterop + D-Bus 라이브러리, JVM은 Java 16+ Unix socket 또는 dbus-java. 크로스플랫폼 BLE는 [Kable](https://github.com/JuulLabs/kable)
- 백엔드 통신 과제(느린 4G, 저전력 대기, 버퍼링) → HTTP/2, CoAP, **LwM2M**(Eclipse Leshan), **MQTT 5**(Eclipse Paho), AMQP. 기기 상태는 digital twin으로 동기화
- 프로비저닝: **Wi-Fi Easy Connect(DPP)** — QR/NFC/BLE로 공개키 전달 → 상호 ECDH → 암호화된 Wi-Fi 자격 증명 전달. Android·iOS·Linux wpa_supplicant 지원
- 그 외: 펌웨어 빌드(Buildroot, Yocto), Matter·Thread, JVM 튜닝과 GraalVM

**학습 포인트**

- 기기 메모리·시작 시간 제약에서 K/N vs JVM(+GraalVM native image) 비교 기준 정리
- MQTT·LwM2M·CoAP 중 연결 품질이 나쁜 환경에 맞는 프로토콜 선택 근거

---

## NES Emulator - Built in Kotlin

> *Build your own NES Emulator... with Kotlin* — Artur Skowroński · [영상](https://www.youtube.com/watch?v=4A6aLK2KznU)
>
> 예제 코드: 별도 저장소 없음 · 슬라이드 p.62–98, 114, 121

**요약** — Java 애플릿 기반 NES 에뮬레이터(vNES)를 Kotlin + Gradle + Compose로 다시 만든 kNES. NES 하드웨어 구조와 에뮬레이션이 어려운 이유.

**핵심 내용**

- NES 구조: Ricoh 2A03(MOS 6502 호환, 8비트 레지스터·16비트 주소 버스 → 64KB), PPU(타일·팔레트 기반 그래픽), APU(Pulse 2·Triangle·Noise·DMC 채널), 그리고 **카트리지 자체가 작은 컴퓨터**(PRG-ROM, CHR-ROM, mapper로 bank switching)
- 어려운 점 1: NES엔 RGB 팔레트가 없음 — 아날로그 신호에서 나온 64색을 에뮬레이터들이 "합의한" 팔레트로 근사
- 어려운 점 2: **mapper**가 약 100종, 각각 CPU·PPU·APU 전체에 결합 → `sealed class` 계층으로 정리. iNES 헤더로 mapper 판별, 구현한 mapper 수만큼 호환성 증가
- 어려운 점 3: **오디오** — 초당 약 1억 샘플 계산, DMC가 DMA로 CPU 사이클을 훔치는 동작까지 사이클 단위로 재현해야 함. 반면 save state는 메모리 배열 직렬화라 쉬움(단, 에뮬레이션을 멈추고 원자적으로)
- UI: Compose Canvas로 매 프레임 이미지를 그리면 느려 Skiko 버전 작성. 컴포넌트 교체 구조 덕에 터미널 렌더링, 휴대폰 컨트롤러(WebSocket), Gemini 플레이 같은 실험 가능
- AI 경험: 코드 마이그레이션은 Junie가 유용, 버그 수정에는 사실상 무용(무차별 동기화·간접 계층 추가), 예상 밖 동작 설명에는 탁월 — 이 문제에선 추론 모델보다 일반 모델이 나았음

**학습 포인트**

- [NESdev wiki](https://www.nesdev.org/wiki/Emulator_tests)의 test ROM으로 CPU 명령어 구현을 검증하는 방식 — 명세가 불완전한 시스템을 테스트하는 일반적 방법
- 하드웨어 전체에 결합된 mapper를 sealed class로 다루는 설계의 장단점
