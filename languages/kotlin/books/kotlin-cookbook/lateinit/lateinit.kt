fun main() {
    LateInitDemo().initializeName()
}

class LateInitDemo {

    lateinit var name: String

    fun initializeName() {
        println("Before assignment: ${::name.isInitialized}")
        name = "World"
        println("After assignment: ${::name.isInitialized}")
    }
}

/*
    지연 초기화를 위한 lateinit
    생성자에 속성 초기화를 위한 정보가 충분하지 않으면 해당 속성을 널 비허용 속성으로 만듦
    의존성 주입의 경우 유용하나, 일반적으로 가능하다면 지연 평가 같은 대안을 먼저 고려

    가. 스프링 컨트롤러 테스트
        @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
        class OfficerControllerTests {
            @Autowired
            lateint var client: WebTestClient

            @Autowired
            lateinit var repository: OfficerRepository

            @Before
            fun setUp() {
                repository.addTestData()
            }

            @Test
            fun `GET to route returns all officers in db`() {
                client.get().uri("/route")
                // ... 데이터를 얻고 값을 확인 ...
            }

            // ... 그 밖의 테스트
        }

    나. lateint 속성의 동작 방식
        class LateInitDemo {
            lateinit var name: String
        }

        class LateInitDemoTest {
            @Test
            fun `uninitialized lateinit property throws exception`() {
                assertThrows<UninitializedPropertyAccessException> {
                    LateInitDemo().name
                }
            }
        }

        @Test
        fun `set the lateinit property and no exception is thrown`() {
            assertDoesNotThrow { LateInitDemo().apply { name = "Dolly" } }
        }
*/
