/*
    의존성 주입하기

    오토와이어링이 필요한 빈과 필요하지 않는 빈을 선언하고
    의존성 주입을 사용해서 빈을 서로 오토와이어링하고 싶다

    코틀린 스프링은 생성자 주입을 제공하지만 필드 주입에는 lateinit var 구조를 사용해야 한다
    선택적인 빈은 널 허용 타입으로 선언한다

    스프링은 실제보다 더욱 복잡하게 들리는 의존성 주입이라고
    알려진 과정 중에 빈을 서로 연결한다

    한 타입의 레퍼런스를 다른 타입의 클래스에 추가하면
    스프링은 개발자를 대신해서 레퍼런스 타입의 인스턴스를 제공하는 방법을 찾아 준다

    스프링은 가능하다면 의존성을 생성자 주입하는 것을 선호한다
    코틀린에서 @Autowired 어노테이션을 생성자 인자에 직접 사용할 수 있다

    클래스에서 생성자가 하나뿐이라면 스프링이 자동으로 클래스의 유일한 생성자에
    모든 인자를 자동으로 오토와이어링하기 때문에 @Autowired 어노테이션을 사용할 필요가 없다
    (스프링이 관리하는 빈에 생성자가 하나만 있담ㄴ 스프링은 모든 인자를 자동으로 주입한다)
*/

/*
    스프링으로 의존성 오토와이어링하기

    서비스를 주입하는 REST 컨트롤러가 있다고 할 때 아래 예제에서
    보여주는 모든 방법의 오토와이어링은 잘 동작한다

    @RestController
    class GreetingController(val service: GreetingService) {} // 단일 생성자를 갖는 클래스

    @RestController
    class GreetingController(@Autowired val service: GreetingService) {} // 명시적으로 오토와이어링

    @RestController
    class GreetingController @Autowired constructor(val service: GreetingService) {} // 오토와이어링 생성자 호출 (주로 다수의 의존성을 갖는 클래스)

    @RestController
    class GreetingController {
        @Autowired
        lateinit var service: GreetingService
    } // 필드 주입(비추천, 하지만 유용할 수 있음)

    상기 예제 코드는 의존성을 주입하는 모든 방법을 보여준다
        1. 생성자가 하나뿐인 클래스는 의존성을 속성으로 선언하면 해당 속성을 자동으로 오토와이어링한다
        2. 같은 방식으로 동작하는 @Autowired 어노테이션을 생성자 인자에 명시적으로 사용한다
           하지만 @Autowired의 명시적인 선언은 두 번째 생성자를 추가해도 계속해서 잘 동작한다
        3. 대체로 다수의 의존성을 주입할 때는 @Autowired를 생성자 함수 앞에 위치시켜 간소화한다
        4. 마지막으로 반드시 필드 주입을 사용해야 한다면 필드에 lateinit var 변경자를 사용한다

        var 속성은 선언 시에 값이 반드시 있어야 하기 때문에 값을 나중에 제공해 초기화할 수 없다
        (이것이 lateinit 키워드가 var와 함께 사용된 이유)
        var 속성의 단점은 원치 않더라도 정의대로 차후에 언제든지 값이 변경될 수 있다는 것이다
        여러 가지 이유 중에서 값을 언제든지 변경할 수 있다는 점이 생성자 주입을 선호하는 이유다
*/

/*
    선택 가능한 파라미터를 갖는 컨트롤러 함수

    클래스의 속성이 필수가 아니라면 해당 속성을 널 허용 타입으로 선언할 수 있다
    코틀린은 널 허용 타입 속성을 선택 가능한 속성이라는 의미로 받아들인다

    예를 들어 GreetingController의 함수 하나가 선택 가능한
    요청 파라미터를 바탕으로 인삿말을 생성한다고 한다고 가정해보자

    @GetMapping("/hello")
    fun greetUser(@RequestParam name: String?) = Greeting(service.sayHello(name ?: "World"))

    널이 될 수 있는 파라미터를 선언하면 코틀린은
    해당 파라미터가 필수가 아니라는 사실을 알 수 있다

    또한 스프링은 테스트에 JUnit 5 사용을 지지한다
    Junit 4에서는 사용할 수 없던 다음 두 기능이 JUnit 5에서 제공된다
        1. 부 생성자를 JUnit 5 테스트에 사용할 수 있다
        2. 테스트 클래스의 수명주기를 메소드별로 초기화하지 않고
           클래스당 하나의 인스턴스로 설정할 수 있다

    코틀린 스프링 튜토리얼(https://oreil.ly/mO5ci)의 예제는
    Jnit 5 코드로 시작하는 테스트를 보여준다

    JUnit 5에서 생성자 인자를 사용해 의존성 주입
    @DateJpaTest
    class RepositoriesTests @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val articleRepository: ArticleRepository
    ) {
        ... 테스트 시작 ...
    }

    RestTemplate 클래스를 사용하는 테스트
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    class IntegrationTests(@Autowired val restTemplate: TestRestTemplate) {
        ... 테스트 시작 ...
    }

    autowired 생성자를 사용하면 lateint var 접근 방식을 사용할 필요가 없다

    첫 번째 Repositories Tests는 개발자가 생성한 클래스를 오토와이어링한다

    두 번째 IntegrationTests는 임의의 포트 번호로 테스트 서버를 시작하고,
    웹 애플리케이션을 테스트 서버에 배포한 다음 getForObject 또는 getForEntity 함수를
    사용해 REST 요청을 만들기 위해 (이미 적절한 포트 번호로 설정된)
    TestRestTemplate 클래스를 사용한다
*/

fun main() {}
