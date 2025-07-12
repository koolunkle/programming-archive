// 자바에서 배열 인스턴스화
// String[] strings = new String[4];
// strings[0] = "an";
// strings[1] = "array";
// strings[2] = "of";
// strings[3] = "strings";

// 또는 아래와 같이
// strings = "an array of strings".split(" ");

// arrayOf 팩토리 메소드 사용하기
val strings = arrayOf("this", "is", "an", "array", "of", "strings")

// null로 채워진 배열 생성하기
val nullStringArray = arrayOfNulls<String>(5)

// 0부터 4까지의 제곱을 담고 있는 문자열 배열
val squares = Array(5) { i -> (i * i).toString() }

/*
    배열의 적법한 인덱스 값 획득하기
    @Test
    fun `valid indices`() {
        val strings = arrayOf("this", "is", "an", "array", "of", "strings")
        val indicies = strings.indices
        assertThat(indicies, contains(0, 1, 2, 3, 4, 5))
    }
*/

/*
    withIndex를 사용해 배열 값에 접근하기

    fun <T> Array<out T>.withIndex() : Iterable<IndexedValue<T>>
    data class IndexedValue<out T>(public val index: Int, public val value: T)

    @Test
    fun `withIndex returns IndexValues`() {
        val strings = arrayOf("this", "is", "an", "array", "of", "strings")
        for((index, value) in strings.withIndex()) {
            println("Index $index maps to $value")
            assertTrue(index in 0..5)
        }
    }
*/

/*
    listOf 함수 구현

    public fun <T> listOf(vararg elements: T): List<T> =
        if(elements.size > 0) elements.asList() else emptyList()  // asList() 읽기 전용 리스트 리턴
*/

// 불변 리스트, 세트, 맵 생성하기 (기본적으로 코틀린 컬렉션은 불변)
var numList = listOf(3, 1, 4, 5, 9)
var numSet = setOf(3, 1, 4, 1, 5, 9)

// set은 중복을 포함하지 않음
// numSet.size == 5

// Pair 인스턴스에서 맵 생성
var map = mapOf(1 to "one", 2 to "two", 3 to "three")

// 가변 리스트, 세트, 맵 생성하기
var numberList = mutableListOf(3, 1, 4, 1, 5, 9)
var numberSet = mutableSetOf(3, 1, 4, 1, 5, 9)
var mapp = mutableMapOf(1 to "one", 2 to "two", 3 to "three")

/*
    mapOf 함수 구현
    public fun <K, V> mapOf(vararg pairs: Pair<K, V>): Map<K, V> =
        if (pairs.size > 0) pairs.toMap(LinkedHashMap(mapCapacity(pairs.size))) else emptyMap()
*/

/*
    LinkedList 인스턴스화
        1. add 메소드는 addLast의 별칭
        2. 배열 타입 접근은 get 또는 set 호출
    @Test
    internal fun `instantiating a linked list`() {
        val list = LinkedList<Int>()
        list.add(3)
        list.add(1)
        list.addLast(999)
        list[2] = 4
        list.addAll(listOf(1, 5, 9, 2, 6, 5))
        assertThat(list, contains(3, 1, 4, 1, 5, 9, 2, 6, 5))
    }
*/

/*
    컬렉션에서 읽기 전용 뷰 생성하기

    변경 가능한 리스트, 세트, 맵이 있을 때 해당 컬렉션의 읽기 전용 버전을 생성하고 싶은 경우
    toList, toSet, toMap 메소드를 사용해 새로운 읽기 전용 컬렉션을 생성하거나
    기존 컬렉션을 바탕으로 읽기 전용 뷰를 만들려면 List, Set 또는 Map 타입 변수에 기존 컬렉션을 할당한다
*/

// 가변 리스트의 경우 리스트를 증가 또는 감소시킬 수 있는 add, remove 등과 같은 메소드가 있음
val mutableNums = mutableListOf(3, 1, 4, 1, 5, 9)

/*
    가변 리스트의 읽기 전용 뷰 생성하기
    @Test
    fun `toList on mutablList makes a readOnly new list`() {
        val readOnlyNumList: List<Int> = mutableNums.toList()
        assertEquals(mutableNums, readOnlyNumList)
        assertNotSame(mutableNums, readOnlyNumList)
    }

    @Test
    internal fun `modify mutable list does not change read-only list`() {
        val readOnly: List<Int> = mutableNums.toList()
        assertEquals(mutableNums, readOnly)

        mutableNums.add(2)
        assertThat(readOnly, not(contains(2)))
    }

    @Test
    internal fun `read-only view of a mutable list`() {
        val readOnlySameList: List<Int> = mutableNums
        assertEquals(mutableNums, readOnlySameList)
        assertSame(mutableNums, readOnlySameList)

        mutableNums.add(2)
        assertEquals(mutableNums, readOnlySameList)
        assertSame(mutableNums, readOnlySameList)
    }
*/

// 값을 생성하기 위해 associate 함수 사용하기
val keys = 'a'..'f'
val maps = keys.associate { it to it.toString().repeat(5).replaceFirstChar { it.uppercase() } }

// associateWith로 값 생성하기
val keyss = 'a'..'f'
val mapss = keys.associateWith { it.toString().repeat(5).replaceFirstChar { it.uppercase() } }

// 컬렉션이 빈 경우 기본값 리턴하기
data class Products(val name: String, val price: Double, val onSale: Boolean = false)

fun namesOfProductsOnSale(products: List<Products>) =
        products.filter { it.onSale }.map { it.name }.joinToString(", ")

// 컬렉션과 문자열에서 ifEmpty 사용하기
fun onSaleProducts_ifEmptyCollections(products: List<Products>) =
        products
                .filter { it.onSale }
                .map { it.name }
                .ifEmpty { listOf("none") }
                .joinToString(", ") // 빈 컬렉션에 기본 리스트 제공

fun onSaleProducts_ifEmptyString(products: List<Products>) =
        products.filter { it.onSale }.map { it.name }.joinToString(", ").ifEmpty {
            "none"
        } // 빈 문자열에 기본 문자열 제공

// (코틀린 표준 라이브러리) List의 component 확장 함수
// @kotlin.internal.InlineOnly
// public inline operator fun <T> List<T>.component1(): T {
//     return get(0)
// }

// 다수의 속성으로 정렬하기
data class Golfer(val score: Int, val first: String, val last: String)

val golfers =
        listOf(
                Golfer(70, "Jack", "Nicklaus"),
                Golfer(68, "Tom", "Watson"),
                Golfer(68, "Bubba", "Watson"),
                Golfer(70, "Tiger", "Woods"),
                Golfer(68, "Ty", "Webb")
        )

// 표준 라이브러리의 sortedWith, compareBy 함수의 시그니처
// fun <T> Iterable<T>.sortedWith(comparator: Comparator<in T>): List<T>
// fun <T> compareBy(vararg selectors: (T) -> Comparable<*>?) : Comparator<T>

fun main() {
    squares.forEach { println(it) }
    println(squares.indices)
    squares.withIndex().forEach { (index, value) -> println("index: $index value: $value") }

    val readOnlySameList: List<Int> = mutableNums.toList()
    mutableNums.add(2)
    readOnlySameList.forEach { println(it) }
    println(mapss)

    val products =
            listOf(
                    Products("1", 1.0, false),
                    Products("2", 2.0, true),
                    Products("3", 3.0, false),
                    Products("4", 4.0, true),
                    Products("5", 5.0, true),
            )
    println(namesOfProductsOnSale(products))
    println(onSaleProducts_ifEmptyCollections(products))
    println(onSaleProducts_ifEmptyString(products))

    // 리스트의 원소를 구조 분해하기
    val list = listOf("a", "b", "c", "d", "e")
    val (a, b, c, d, e) = list
    println(a)

    // 다수의 속성으로 정렬하기 (sortedWith, compareBy)
    val sorted = golfers.sortedWith(compareBy({ it.score }, { it.last }, { it.first }))
    sorted.forEach { println(it) }

    println("**********")

    // comparator 연쇄(chaning)
    // thenBy: 이전 비교 후에 새로운 비교를 적용
    // val comparator = compareBy<Golfer>(Golfer::score).thenBy { it.last }.thenBy { it.first }
    val comparator = compareBy<Golfer>(Golfer::score).thenBy(Golfer::last).thenBy(Golfer::first)
    golfers.sortedWith(comparator).forEach(::println)
}
