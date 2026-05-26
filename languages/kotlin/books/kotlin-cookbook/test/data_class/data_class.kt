import java.time.LocalDate
import java.util.stream.Stream

/*
    테스트에 데이터 클래스 사용하기

    코드를 부풀리지 않고 객체의 여러 속성을 체크하고 싶은 경우
    원하는 모든 속성을 캡슐화하는 데이터 클래스를 생성한다

    book의 모든 속성을 수동으로 테스트
    @Test
    internal fun `test book the bard way`() {
        val book = service.findBookById("1935182943")
        assertThat(book.isbn, `is`("1935182943"))
        assertThat(book.title, `is`("Making Java Groovy"))
        assertThat(book.author, `is`("Ken Kousen"))
        assertThat(book.published, `is`(LocalDate.of(2013, Month.SEPTEMBER, 30)))
    }

    Junit 5에서는 Executable 인스턴스의 가변 리스트를 받는 assertAll 메소드를 추가
    Executable은 자바에 정의된 받는 인자가 없고 아무것도 리턴하지 않는 함수형 인터페이스
    assertAll 함수는 Executable 인스턴스의 단언이 1개 이상 실패하더라도 Executable 인스턴스를 모두 실행

    Junit 5의 assertAll을 사용해 모든 속성을 테스트하기
    @Test
    fun `use JUnit 5 assertAll`() {
        val book = service.findBookById("1935182943")
        assertAll(
            "check all properties of a book",
            { assertThat(book.isbn, `is`("1935182943")) }
            { assertThat(book.title, `is`("Making Java Groovy")) }
            { assertThat(book.author, `is`("Ken Kousen")) }
            { assertThat(book.published, `is`(LocalDate.of(2013, Month.SEPTEMBER, 30))) }
        )
    }

    테스트에 Book 데이터 클래스 사용하기
    @Test
    internal fun `use data class`() {
        val book = service.findBookById("1935182943")
        val expected = Boox(
            isbn = "1935182943",
            title = "Making Java Groovy",
            author = "Ken Kousen",
            published = LocalDate.of(2013, Month.SEPTEMBER, 30))
        )
        assertThat(book, `is`(expected))
    }

    리턴된 모든 책을 테스트
    @Test
    internal fun `check all elements in list`() {
        val found = service.findAllBooksById(
            "1935182943", "141947020", "149197317X"
        )
        val expected = arrayOf(
            Book(
            isbn = "1935182943",
            title = "Making Java Groovy",
            author = "Ken Kousen",
            published = LocalDate.of(2013, Month.SEPTEMBER, 30))
        ),
          Book(
            isbn = "141947020",
            title = "Gradle Recipes",
            author = "Ken Kousen",
            published = LocalDate.of(2016, Month.JUNE, 17))
        ),
          Book(
            isbn = "149197317X",
            title = "Modern Java Recipes",
            author = "Ken Kousen",
            published = LocalDate.of(2017, Month.AUGUST, 26))
        ),
        assertThat(found, arrayContainingInAnyOrder(*expected)) // 배열을 개별 항목으로 확장하기 위해 배열 펼침 연산자(*) 사용
    }
*/

/*
    파라미터화된 테스트에 데이터 클래스 사용하기

    파라미터화된 테스트를 좀 더 쉽게 읽는 테스트 결과를 생성하고 싶은 경우
    입력 값과 예상 값을 감싸는 데이터 클래스를 만들고, 만든 데이터 클래스 기반의
    테스트 데이터를 생성하는 함수를 테스트 메소드 소스로써 사용한다

    데이터 클래스를 사용하는 파라미터화된 테스트
    @ParameterizedTest
    @MethodSource("fibonacciTestData")
    fun `check fibonacci using data class`(data: FibonacciTestData) {
        assertThat(fibonacci(data.number), `is`(data.expected))
    }
*/

fun main() {}

data class Book(val isbn: String, val title: String, val author: String, val published: LocalDate)

// 입력과 예상 결과를 담는 데이터 클래스
data class Fibonacci(val number: Int, val expected: Int)

@JvmOverloads
tailrec fun fibonacci(n: Int, a: Int = 0, b: Int = 1): Int =
        when (n) {
            0 -> a
            1 -> b
            else -> fibonacci(n - 1, b, a + b)
        }

private fun fibonacciTestData() =
        Stream.of(
                Fibonacci(number = 1, expected = 1),
                Fibonacci(number = 2, expected = 1),
                Fibonacci(number = 3, expected = 2),
                Fibonacci(number = 4, expected = 3),
                Fibonacci(number = 5, expected = 5),
                Fibonacci(number = 6, expected = 8),
                Fibonacci(number = 7, expected = 13),
        )
