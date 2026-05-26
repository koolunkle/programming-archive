import java.time.LocalDate

/*
    타입으로 컬렉션을 필터링하기

    여러 타입이 섞여 있는 컬렉션에서 특정 타입의 원소로만 구성된 새 컬렉션을 생성하고 싶은 경우
    filterIsInstance 또는 filterIsInstanceTo 확장 함수 사용
*/

public inline fun <reified R> Iterable<*>.filterIsInstance(): List<R> {
    return filterIsInstanceTo(ArrayList<R>())
}

fun main() {
    // 타입으로 컬렉션을 필터링하기, 타입 이레이저 사용
    val list = listOf("a", LocalDate.now(), 3, 1, 4, "b")
    val strings = list.filter { it is String }
    for (s in strings) {
        // s.length // 컴파일 되지 않음, 타입이 삭제됨(Any)
    }

    // 구체적인 타입 사용하기
    val all = list.filterIsInstance<Any>()
    val strs = list.filterIsInstance<String>()
    val ints = list.filterIsInstance<Int>()
    val dates = list.filterIsInstance<LocalDate>()

    // assertThat(all, `is`(list))
    // assertThat(strs, containsInAnyOrder("a", "b"))
    // assertThat(ints, containsInAnyOrder(1, 3, 4))
    // assertThat(dates, contains(LocalDate.now()))

    println(all)
    println(strs)
    println(ints)
    println(dates)

    val alls = list.filterIsInstanceTo(mutableListOf<Any>())
    val strss = list.filterIsInstanceTo(mutableListOf<String>())
    val intss = list.filterIsInstanceTo(mutableListOf<Int>())
    val datess = list.filterIsInstanceTo(mutableListOf<LocalDate>())

    println("****************************************")

    println(alls)
    println(strss)
    println(intss)
    println(datess)
}
