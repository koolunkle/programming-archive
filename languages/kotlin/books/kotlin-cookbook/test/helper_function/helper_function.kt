/*
    기본 인자와 함께 도움 함수 사용하기

    테스트 객체를 빠르게 생성하고 싶은 경우 copy 함수를 사용하거나 필요하지도 않은
    생성자 기본 인자를 명시하지 말고 기본 인자를 가진 도움 함수 제공

    Book 클래스 팩토리 함수
    fun createBook(
        isbn: String = "149197317X",
        title: String = "Modern Java Recipes",
        author: String = "Ken Kousen",
        published: LocalDate = LocalDate.parse("2017-08-26")
    ) = Book(isbn, title, author, published)

    MultiAuthorBook 클래스와 사용법
    data class MultiAuthorBook(
        val isbn: String,
        val title: String,
        val authors: List<String>,
        val published: LocalDate
    )

    fun createMultiAuthorBook(
        isbn: String = "9781617293290",
        title: String = "Kotlin in Action",
        authors: List<String> = listOf("Dimitry Jeremov", "Svetlana Isakova"),
        published: LocalDate = LocalDate.parse("2017-08-26")
    ) = MultiAuthorBook(isbn, title, authors, published)

    val kotlin_in_action = createMultiAuthorBook()
*/

fun main() {}
