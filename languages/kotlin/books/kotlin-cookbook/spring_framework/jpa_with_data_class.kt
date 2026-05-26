import java.time.LocalDate

/*
    코틀린 데이터 클래스로 퍼시스턴스 구현하기

    코틀린 데이터 클래스로 자바 퍼시스턴스 API(이하 JPA)를 사용하고 싶은 경우
    kotlin-jpa 플러그인을 빌드 파일에 추가한다
*/

/*
    데이터 클래스와 주 생성자

    JPA 관점에서 데이터 클래스는 두 가지 문제가 있다

    첫 번째, JPA는 모든 속성에 기본값을 제공하지 않는 이상 기본 생성자가 필수지만
    데이터 클래스에는 기본 생성자가 없다

    두 번째, val 속성과 함께 데이터 클래스를 생성하면 불변 객체가 생성되는데
    JPA는 불변 객체와 더불어 잘 동작하도록 설계되지 않았다

    기본 생성자 문제 관련하여 코틀린은 2가지 플러그인을 제공한다
    'no-arg' 플러그인은 인자가 없는 생성자를 추가할 클래스를 선택할 수 있고
    기본 생성자 추가를 호출하는 어노테이션을 정의할 수 도 있다

    no-arg 플러그인은 코틀린 엔티티(@Entity 어노테이션이 붙은 클래스)에
    기본 생성자를 자동으로 구성한다

    no-arg 플러그인을 사용하려면 다음 내용을 추가할 수 있다
*/

/*
    JPA 엔티티에 추가된 부가 의존성

    plugin {
        kotlin("plugin.jpa") version "1.2.71"
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        /// 해당 의존성은 엔티티 설정에 꼭 필요하지 않지만 코틀린 클래스를 JSON 형태로 직렬화와 역직렬화를 도와줌
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    }
*/

/*
    no-arg 컴파일러 플러그인은 합성한 기본 연산자를 코틀린 클래스에 추가한다
    자바나 코틀린에서는 합성 기본 연산자를 호출할 수 없지만 스프링에서는
    리플렉션을 사용해 합성 기본 연산자를 호출할 수 있다

    원한다면 no-arg 플러그인을 사용할 수 있지만, 인자가 없는 생성자가
    필요한 클래스를 표시하는 데 사용할 어노테이션을 정의해야 한다

    kotlin-jpa 플러그인이 no-arg 플러그인보다 사용이 더 쉬우며
    kotlin-jpa 플러그인은 no-arg 플러그인을 기반으로 만들어 졌다
    kotlin-jpa 플러그인은 다음 어노테이션으로 자동 표시된 클래스에 기본 생성자를 추가한다
        1. @Entity
        2. @Embeddable
        3. @MappedSuperclass

*/

/*
    또 다른 문제로 JPA는 엔티티에 불변 클래스를 사용하고 싶어하지 않는다
    따라서 스프링 개발팀은 엔티티로 사용하고 싶은 코틀린 클래스에 (데이터 클래스 대신)
    필드 값을 변경할 수 있게 속성에 var 타입을 사용하는 단순 클래스의 사용을 추천한다

    데이터베이스 테이블로 매핑되는 코틀린 클래스

    @Entity
    class Article(
        var title: String,
        var headline: String,
        var content: String,
        @ManyToOne var author: User,
        var slug: String = title.toSlug(),
        var addedAt: LocalDateTime = LocalDateTime.now(),
        @Id @GeneratedValue var id: Long? = null
    )

    @Entity
    class User(
        var login: String,
        var firstname: String,
        var lastname: String,
        var description: String? = null,
        @Id @GeneratedValue var id: Long? = null
    )

    Article과 User 클래스는 속성에 var를 사용하고
    심지어 생성된 기본 키 필드는 널을 허용한다

    하이버네이트 문법 (가장 흔한 JPA 프로바이더는 여전히 하이버네이트)에서는
    (@Id 어노테이션으로 표시된) 널 기본 키는 임시 상태에 있는 인스턴스임을 나타낸다
    즉, 연결된 데이터베이스 테이블에 해당 인스턴스와 연관된 행이 없다는 의미이다

    기본 키가 없는 상황은 클래스를 처음 인스턴스화하고 아직 데이터베이스에 저장하지 않았을 때,
    또는 데이터베이스에서 행을 삭제했지만 해당 인스턴스가 아직 메모리에 있을 때만 발생한다
    @GeneratedValue 어노테이션은 데이터베이스가 기본 키에 해당하는 값을 제공한다는 것을 의미한다

    광범위한 var 사용과 자동으로 생성되는 toString, equals, hashCode 함수의 부재는
    코틀린 개발자로서 불편함을 느끼게 하지만 이 방식이 JPA가 요구하는 방식에 더 적합한 접근법이다

    스프링 데이터 몽고DB 또는 스프링 데이터 JDBC 같은 스프링 데이터를 기반으로 하는
    API를 사용한다면 일반 클래스 대신 데이터 클래스를 마음껏 사용할 수 있다
*/

data class Person(val name: String, val dob: LocalDate)

fun main() {}
