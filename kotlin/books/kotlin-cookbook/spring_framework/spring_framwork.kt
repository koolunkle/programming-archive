/*
    스프링 프레임워크

    스프링 프레임워크는 자바 진영에서 가장 인기 있는 오픈소스 프레임워크 중 하나이다

    모두 프로젝트의 인프라에 대한 것으로써, 개발자는 목표 달성에 필요한
    비즈니스 로직을 담는 빈 작성에 집중하고, 스프링은 개발자가 작성한 메타데이터를 바탕으로
    보안, 트랜잭션, 리소스 풀링 등을 제공한다

    스프링은 항상 JVM의 다른 대안 언어에 친화적이었는데, 버전 2.5부터 그루비를 지원하였으며
    스프링 개발자들은 몇 개의 최신 이전 버전에서 코틀린 지원을 위한 고유한 기능도 추가했다
*/

/*
    확장을 위해 스프링 관리 빈 클래스 오픈하기

    스프링은 개발자가 작성한 클래스를 확장하는 프록시를 생성해야 한다
    하지만 코틀린 클래스는 기본으로 닫혀 있다 (자바의 final)

    확장을 위해 자동으로 필요한 스프링 관리 클래스를 열어주는(open)
    코틀린 스프링 플러그인을 빌드 파일에 추가할 수 있다
*/

/*
    스프링은 시스템에 서비스를 제공해 동작하며, 서비스 제공에는
    프록시 디자인 패턴을 사용한다 (UML 클래스 다이어그램, 시퀀스 다이어그램)

    프록시와 실체(real subject)는 둘 다 같은 인터페이스를 구현하거나 같은 클래스를 확장한다
    들어오는 요청을 프록시가 가로채고, 이 프록시는 서비스가 요구하는
    모든 것을 적용한 후 실체로 요청을 전달한다

    프록시는 필요하다면 응답 또한 가로채서 더 많은 일을 수행한다
    예를 들어 스프링 트랜잭션 프록시는 어떤 메소드 호출을 가로챈 다음 트랜잭션을 시작하고
    해당 메소드를 호출하고, 실체 메소드 안에서 일어난 상황에 맞춰 트랜잭션을 커밋하거나 롤백한다

    스프링은 시동 과정에서 프록시를 생성하며, 실체가 클래스라면 해당 클래스를 확장해야 하는데
    해당 부분이 코틀린에서 문제가 된다 (코틀린은 기본적으로 정적으로 결합하기 때문)
    
    즉, 클래스가 open 키워드를 사용해 확장을 위한 열림으로 표시되지 않으면 메소드 재정의
    또는 클래스 확장이 불가능한데, 코틀린은 이러한 문제를 'all-open'이라는 플러그인으로 해결

    해당 플러그인은 클래스와 클래스에 포함된 함수에 명시적으로 open 키워드를 추가하지 않고
    명시적인 open 어노테이션으로 클래스를 설정한다

    하지만 all-open 플러그인도 유용하지만 좀 더 뛰어난 코틀린 언어 개발자가
    스프링에 꼭 맞는 'kotlin-spring' 플러그인을 개발했는데, 해당 플러그인을 사용하기 위해
    gradle이나 maven 빌드 파일에 플러그인을 추가해야 한다
*/

/*
    kotlin-spring 플러그인이 포함된 gradle 빌드 파일(코틀린 DSL, build.gradle.kts)

    plugins {
        id("org.springframework.boot") version "2.1.8.RELEASE"
        id("io.spring.dependency-management") version "1.0.8.RELEASE"
        kotlin("jvm") version "1.2.71" // 코틀린 JVM 플러그인을 프로젝트에 추가
        kotlin("plugin.spring") version "1.2.71" // 코틀린 스프링 플러그인을 요구
    }

    group = "com.mycompany"
    version = "1.0"

    java.sourceCompatibility = JavaVersion.VERSION_11

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter")
        implementation("org.jetbrains.kotlin:kotlin-reflect") // 소스 코드가 코틀린으로 작성된 경우 필요
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // 소스 코드가 코틀린으로 작성된 경우 필요
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict") // JSR-305와 연관된 널 허용성 어노테이션 지원
            jvmTarget = "1.8"
        }
    }
*/

/*
    all-open 플러그인은 코틀린 클래스를 여는 어노테이션을 명시할 수 있다
    kotlin-spring 플러그인은 다음의 스프링 어노테이션으로 클래스를 열도록 이미 설정되어 있다
        @Component
        @Async
        @Transactional
        @Cacheable
        @SpringBootTest

    @Component 어노테이션은 @Configuration, @Controller 및 @RestController, @Service,
    @Repository 등 여러 가지 스프림 구성 어노테이션에 사용된다
    
    이러한 어노테이션으로 표기되어 관리되는 모든 스프링 빈은 확장을 위해
    자동으로 열린 상태가 된다 (일반적으로는 이것이 스프링 개발에 필요한 전부)
    kotlin-spring이 제공하는 것보다 더 많이 필요하면 all-open 플러그인도
    추가할 수 있으나 거의 필요 없다 
*/

fun main() {}
