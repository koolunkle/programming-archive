fun main() {

    Pair("hello", "kotlin")
    Triple("hello", "world", "kotlin")

    val someClass1 = SomeClass("홍길동", 20)
    val someClass2 = SomeClass("홍길동", 20)

    println("isEqual: ${someClass1 == someClass2}")
    println("hashCode1: ${someClass1.hashCode()}")
    println("hashCode2: ${someClass2.hashCode()}")

    val someDataClass1 = SomeDataClass("홍길동", 20)
    val someDataClass2 = SomeDataClass("홍길동", 20)

    println("isEqual: ${someDataClass1 == someDataClass2}")

    println("hashCode1: ${someDataClass1.hashCode()}")

    println("hashCode2: ${someDataClass2.hashCode()}")

    // val someDataClass3 = SomeDataClass("kim", 30)

    val someDataClass3 = SomeDataClass()

    // val (name, age, nickname) = someDataClass3

    val name = someDataClass3.component1()
    val age = someDataClass3.component2()
    val nickname = someDataClass3.component3()
    val nicknameNew = someDataClass3.copy(nickname = "kim").nickname

    println("name: $name")
    println("age: $age")
    println("nickname: $nickname")
    println("nicknameNew: $nicknameNew")
}

class SomeClass(val name: String, val age: Int)

open class SomeOpenClass(open val name: String)

interface SomeInterface {

    var grade: String
}

data class SomeDataClass(override val name: String, val age: Int, val nickname: String = "noname") :
        SomeOpenClass(name = name), SomeInterface {

    override var grade: String = "100"

    constructor() : this(name = "홍길동", age = 20) {
        println("constructor(): called")
    }
}
