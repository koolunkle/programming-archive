fun main() {

    // val emptyNames = emptyMap<String, Int>()
    // val names = mapOf<String, Int>("철수" to 10, "영희" to 13, "영희" to 13)
    // val names = mapOf<String, Int>(Pair("철수", 10), Pair("영희", 13))

    val names = mutableMapOf<String, Int>(Pair("철수", 10), Pair("영희", 13))

    names.put("존시나", 16)
    names["존시나"] = 19

    for ((key, name) in names) {
        println("key : $key, name : $name")
    }

    val keys = names.keys
    val values = names.values

    println(keys)
    println(values)
}
