/*
    테스트 클래스 수명주기 설정하기

    Junit 5의 테스트 수명주기를 기본값인 테스트 함수당 한 번 대신
    클래스 인스턴스당 한 번씩 인스턴스화하고 싶은 경우
    @TestInstance 애노테이션을 사용하거나 junit-platform.properties 파일의
    lifecycle.default 속성을 설정한다

    기본적으로 JUnit 4는 각 테스트 메소드마다 테스트 클래스의 새 인스턴스를 생성
    이러한 방식은 테스트 클래스 속성이 테스트마다 다시 초기화되므로 테스트 자체로 독립적이다
    (단점으로는 초기화 코드가 각 테스트마다 반복해서 실행된다)

    자바에서는 이 문제를 해결하기 위해 클래스의 모든 속성을 static으로 표시해 모든 초기화 코드를
    딱 한 번만 실행되는 @BeforeClass 애노테이션이 달린 static 메소드 안에 배치
*/

/*
 리스트에 대한 JUnit 4 테스트

 public class JUnit4ListTests {
    // 전체 클래스에서 한 번만 실행
    private static List<String> strings = Arrays.asList("this", "is", "a", "list", "of", "strings");

    // 테스트 메소드당 한 번씩 실행
    private List<Integer> modifiable = new ArrayList<>();

    // 전체 클래스에서 한 번만 실행
    @BeforeClass
    public static void runBefore() {
        System.out.println("BeforeClass: " + strings);
    }

    // 테스트 메소드당 한 번씩 실행
    @Before
    public void initialize() {
        System.out.println("Before: " + modifiable);
        modifiable.add(3);
        modifiable.add(1);
        modifiable.add(4);
        modifiable.add(1);
        modifiable.add(5);
    }

    @Test
    public void test1() {

    }

    @Test
    public void test2() {

    }

    @Test
    public void test3() {

    }

    @After
    public void finish() {
        System.out.println("After: " + modifiable);
    }

    @AfterClass
    public static void runAfter() {
        System.out.println("AfterClass: " + strings);
    }
}
*/

/*
    코틀린에서 자바와 동일한 동작을 구현하려고 할 때, 코틀린에는 static 키워드가 없음
    동일한 동작을 하도록 코틀린으로 간단히 포팅하려면 동반 객체(Companion Object) 사용

    코틀린 JUnit 4 리스트 테스트(더 나은 방법을 위해 JUnit 5 살펴보기)

    class JUnit4ListTests {
    companion object { // 클래스당 한 번씩 실행하도록 동반 객체 사용
        @JvmStatic // static 변경자를 사용해 자바 바이트코드 생성
        private val strings = listOf("this", "is", "a", "list", "of", "strings")

        @BeforeClass // 클래스당 한 번씩 실행하도록 동반 객체 사용
        @JvmStatic // static 변경자를 사용해 자바 바이트코드 생성
        fun runBefore() {
            println("BeforeClass: $strings")
        }

        @AfterClass // 클래스당 한 번씩 실행하도록 동반 객체 사용
        @JvmStatic // static 변경자를 사용해 자바 바이트코드 생성
        fun runAfter() {
            print("AfterClass: $strings")
        }
    }

    private val modifiable = ArrayList<Int>()

    @Before
    fun initialize() {
        println("Before: $modifiable")
        modifiable.add(3)
        modifiable.add(1)
        modifiable.add(4)
        modifiable.add(1)
        modifiable.add(5)
    }

    @Test
    public void test1() {

    }

    @Test
    public void test2() {

    }

    @Test
    public void test3() {

    }

    @After
    fun finish() {
        println("After: $modifiable")
    }
}
*/

/*
    strings 리스트를 initialize 메소드 안에서 인스턴스화하려면
    strings 속성을 val 타입이 아닌 lateinit과 var로 선언

    class JUnit4listTests {
        companion object {
            @JvmStatic
            private lateinit var strings

            @BeforeClass
            @JvmStatic
            fun runBefore() {
                strings = listOf("this", "is", "a", "list", "of", "strings")
            }
        }
    }
*/

/*
    @Before를 꼭 설정해야 하는 경우(리스트보다 더 복잡하고, 설정도 추가해야 하는 객체를 테스트하는 등)라도
    var 사용은 코틀린 다운 구현에서 멀어지게 만듦
    JUnit 5는 테스트 클래스에서 @TestInstance 어노테이션을 사용해 수명주기를 명시할 수 있음

    코틀린 JUnit 5 리스트 테스트(선호하는 방법)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS) // 모든 테스트의 테스트 클래스 인스턴스가 오직 하나만 생성됨
    class JUnit5ListTests {
        private val strings = listOf("this", "is", "a", "list", "of", "strings") // 인스턴스화와 원소 채움이 오직 한 번

        private lateinit var modifiable : MutableList<Int> // 테스트할 때마다 실행 전에 다시 초기화

        @BeforeEach
        fun setUp() {
            modifiable = mutableListOf(3, 1, 4, 1, 5) // 테스트할 때마다 실행 전에 다시 초기화
            println("Before: $modifiable")
        }

        @AfterEach
        fun finish() {
            println("After: $modifiable")
        }

        @Test
        fun test1() {}

        @Test
        fun test2() {}

        @Test
        fun test3() {}
    }
*/

/*
    복잡한 객체의 경우 객체를 인스턴스화한 다음 apply 메소드를 사용해 설정할 수 있으며
    JUnit 5에서는 @TestInstance 어노테이션을 각 테스트 클래스에 반복하는 대신
    모든 테스트의 수명주기를 properties 파일에 설정할 수 있다

    클래스 패스(보통 src/test/resource 폴더)에 junit-platform.properties 파일을 생성하면
    아래 한 줄을 추가하고 테스트 수명주기를 설정할 수 있다

    junit.jupiter.testinstance.lifecycle.default = per_class
*/

/*
    여러 데이터에 JUnit 5 테스트 반복하기

    데이터 값 세트를 제공해서 JUnit 5 테스트를 실행하고 싶은 경우
    JUnit 5의 파라미터화된 테스트와 동적 테스트 사용

    여러 가지 데이터 세트를 사용해 함수를 테스트하고 싶다면, JUnit 5에는
    쉼표로 구분된 값(CSV)과 팩토리 메소드가 포함된 옵션과 함께 데이터 소스를
    명시할 수 있는 파라미터화된 테스트가 있다

    JUnit은 비록 자바 라이브러리이지만 코틀린으로 작성할 수 있고
    코틀린 테스트 코드도 테스트할 수 있다

    피보나치 수열을 계산하는 꼬리 재귀 함수
    @JvmOverloads
    tailrec fun fibonacci(n: Int, a: Int = 0, b: Int = 1): Int =
        when (n) {
            0 -> a
            1 -> b
            else -> fibonacci(n-1, b, a + b)
        }

    fibonacci 함수의 명시적인 호출
    @Test
    fun `Fibonacci numbers (explicit)`() {
        assertAll(
            { assertThat(fibonacci(4), `is`(3)) },
            { assertThat(fibonacci(9), `is`(34)) },
            { assertThat(fibonacci(2000), `is`(1392522469)) }
        )
    }

    CSV 데이터를 사용해 파라미터화된 테스트 수행
    @ParameterizedTest
    @CsvSource("1, 1", "2, 1", "3, 2", "4, 3", "5, 5", "6, 8", "7, 13", "8, 21", "9, 34", "10, 55")
    fun `first 10 Fibonacci numbers (csv)`(n: Int, fib: Int) =
        assertThat(fibonacci(n), `is`(fib))
*/

/*
    JUnit 5에서는 팩토리 메소드를 사용해 테스트 데이터를 생성할 수 있다
    자바에서 테스트에 @TestInstance(Lifecycle.PER_CLASS) 어노테이션이 명시되지 않았다면
    테스트 클래스의 팩토리 메소드는 반드시 static으로 선언해야 하고, 외부 클래스에
    팩토리 메소드가 들어 있다면 반드시 static으로 선언해야 한다
    또한 팩토리 메소드는 어떤 인자도 받을 수 없다

    리턴 타입은 반드시 JUnit 5 라이브러리가 순회하는 방법을 알고 있는 스트림,
    컬렉션, 이터레이블, 이터레이터, 배열 같은 타입이어야 한다

    테스트 수명주기가 Lifecycle.PER_CLASS를 사용했다면, 테스트 데이터를 생성하는
    함수를 추가하고 @MethodSource로 해당 함수를 간단하게 참조할 수 있다

    파라미터 소스로써 인스턴스 함수에 접근하기
    private fun fibnumbers() =
        listOf(
                Arguments.of(1, 1), // 두 입력 인자를 연결시킬 수 있는 of 라는 팩토리 메소드를 가진 클래스(Arguments)
                Arguments.of(2, 1), // of의 리턴 타입은 테스트 메소드에 사용할 수 있는 각각의 원소가 두 개의 입력 인자를 가지고 있는 List<Arguments>
                Arguments.of(3, 2),
                Arguments.of(4, 3),
                Arguments.of(5, 5),
                Arguments.of(6, 8),
                Arguments.of(7, 13),
                Arguments.of(8, 21),
                Arguments.of(9, 34),
                Arguments.of(10, 55),
        )

    @ParameterizedTest(name = "fibonacci({0} == {1})")
    @MethodSource("fibnumbers")
    fun `first 10 Fibonacci numbers (instance method)`(n: Int, fib: Int) =
        assertThat(fibonacci(n) `is`(fib))

    테스트 수명주기가 기본 옵션인 Lifecycle.PER_METHOD라면
    테스트 데이터 소스 함수를 동반 객체 안에 위치시켜야 한다

    파라미터 소스 함수를 담기 위해 동반 객체 사용
    companion object {
        * Lifecycle.PER_METHOD 수명주기로 파라미터화된 테스트를 하려면 필요
        @JvmStatic // JUnit 자바 라이브러리는 메소드 소스를 static 메소드로 간주하므로 어노테이션 추가
        private fun fibnumbers() =
            listOf(
                    Arguments.of(1, 1),
                    Arguments.of(2, 1),
                    Arguments.of(3, 2),
                    Arguments.of(4, 3),
                    Arguments.of(5, 5),
                    Arguments.of(6, 8),
                    Arguments.of(7, 13),
                    Arguments.of(8, 21),
                    Arguments.of(9, 34),
                    Arguments.of(10, 55),
            )
    }

    @ParameterizedTest(name = "fibonacci({0} == {1})")
    @MethodSource("fibs")
    fun `first 10 Fibonacci numbers (companion method)`(n: Int, fib: Int) =
        assertThat(fibonacci(n), `is`(fib))
*/
