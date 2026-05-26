fun main() {
    // val base: Base = Base("base")

    // base.sayName()

    val person: Person = Person("kim", "black")

    person.sayName()

    val pocketMon: PocketMon = PocketMon("pikachu")

    pocketMon.sayName()

    val pocketMonWithContext: PocketMon = PocketMon("pikachu", "pikachu-context")

    pocketMonWithContext.sayName()
    pocketMonWithContext.attack()

    pocketMonWithContext.name = "snorlax"

    println(pocketMonWithContext.name)
}

open class Base(open var name: String, private var context: String = "context") {

    init {
        println("Base init called : ${this.name} / ${this.context}")
    }

    open fun sayName() {
        println("sayName(): ${this.name}")
    }

    open fun attack() {
        println("attack(): ")
    }
}

class Person(name: String, var skin: String) : Base(name) {

    init {
        println("Person init called: $name / ${this.skin}")
    }

    override fun sayName() {
        super.sayName()
    }
}

class PocketMon : Base {

    override var name: String
        get() = super.name
        set(value) {
            super.name = value
        }

    constructor(name: String) : super(name) {
        println("constructor(): $name")
    }

    constructor(name: String, context: String) : super(name, context) {
        println("constructor(): $name / $context")
    }

    override fun sayName() {
        super.sayName()
    }

    override fun attack() {
        super.attack()
        println("one million volts")
    }
}
