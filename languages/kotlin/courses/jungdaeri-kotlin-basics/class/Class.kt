fun main() {

    // val toy1: Toy = Toy(nickname = "kim")
    // val toy1: Toy = Toy(hasPrice = false)
    val toy1: Toy = Toy(param = mapOf("nickname" to "kim"))
    val toy2: Toy = Toy()

    toy1.name = "koolunkle"
    toy1.price = 100

    println("toy ${toy1.info()}")
    println("toy ${toy2.info()}")

    val toy3: Toy = Toy.Factory.getInstance()

    println("toy ${toy3.info()}")

    val drink: Drink = Drink("drink")

    println(drink.info())

    val candy: Candy = Candy(name = "candy")

    println(candy)
}

class Toy(var name: String = "Unknown", var price: Int = 0) {

    // var name: String = ""
    // var price: Int = 0
    var nickname: String = ""
    var hasPrice: Boolean = false

    init {
        println("init called")
    }

    constructor(nickname: String) : this(name = "Unknown", price = 0) {
        this.nickname = nickname
        println("constructor(): ${this.name} / ${this.price} / ${this.nickname}")
    }

    constructor(hasPrice: Boolean) : this(name = "Unknown", price = 100) {
        if (!hasPrice) {
            this.name = "No Price"
            this.price = 0
        }
        this.hasPrice = hasPrice
        println("constructor(): ${this.name} / ${this.price} / ${this.hasPrice}")
    }

    constructor(param: Map<String, Any>) : this(hasPrice = true) {
        val nickname = param.get("nickname") ?: "No Nickname"
        this.nickname = nickname.toString()

        println("constructor(): ${this.name} / ${this.price} / ${this.nickname} / ${this.hasPrice}")
    }

    fun info(): String {
        return "[name: $name price: $price]"
    }

    companion object Factory {

        fun getInstance(): Toy {
            return Toy(param = mapOf("nickname" to "test"))
        }
    }
}

class Drink constructor(private val context: Any, val price: Int = 100) {

    init {
        println("init called: $context")
    }

    fun info(): String {
        return "[context: $context / price: $price]"
    }
}

class Candy() {

    constructor(name: String) : this() {
        println("name: $name")
    }
}
