fun main() {

    val jolteon: Jolteon = Jolteon(name = "Jolteon", age = 10, power = 100, healthStat = 100)

    jolteon.run {
        sayInfo()
        attack()
    }

    val pikachu: Pikachu = Pikachu()

    gatherFighting(pikachu)
}

interface Fighting {

    val healthStat: Int

    fun attack() {
        println("attack")
    }

    fun move()
}

open class Eevee(var name: String, var age: Int) {

    open fun sayInfo() {
        println("sayInfo() - name: ${this.name} / age: ${this.age}")
    }
}

class Jolteon(name: String, age: Int, val power: Int, override val healthStat: Int) :
        Eevee(name, age), Fighting {

    // override val healthStat: Int = TODO("SET VALUE")

    override fun sayInfo() {
        super.sayInfo()
        println("sayInfo() - power: ${this.power}")
    }

    override fun attack() {
        super<Fighting>.attack()

        val power = (1..100).random()
        println("one million volts! power: $power / healthStat: $healthStat")
    }

    override fun move() {
        println("move")
    }
}

class Pikachu : Fighting {

    override val healthStat: Int = 50

    override fun attack() {
        super.attack()
        println("one million volts! / healthStat: $healthStat")
    }

    override fun move() {
        println("move")
    }
}

fun gatherFighting(fighting: Fighting) {
    fighting.attack()
    fighting.move()
}
