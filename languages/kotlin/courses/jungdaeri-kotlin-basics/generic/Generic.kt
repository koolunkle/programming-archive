fun main() {
    
    sayHello("Hello, World!")
    sayHello(123)
    sayHello(Vegetables)

    val result1 = sayHello(1004)

    println((result1 as? Int)?.minus(4))

    val result2: Int = sayGoodMorning(1004)

    println(result2.minus(4))

    val vegetable: Vegetable = Vegetable()
    val ingredient: Ingredient = Ingredient()

    // val restaurant = Restaurant("noodle")
    val restaurant: Restaurant<Vegetable, Ingredient> = Restaurant(vegetable, ingredient)

    println(restaurant.orderFood())
    restaurant.washDish("too hard")

    // var food = orderFood(123)
    // println(++food)

    orderFood(Food())
    orderFood(Meat())
    orderFood(Lamb())

    // orderSpecialFood(Meat())
    orderSpecialFood(Lamb())

    val lambDiningRoom1: DiningRoom<Vegetable, Lamb> = LambDiningRoom()
    val lambDiningRoom2: DiningRoom<Vegetable, out Meat> = LambDiningRoom()
    val lambDiningRoom3: DiningRoom<Vegetable, out Food> = LambDiningRoom()

    val lambDiningRoom4: DiningRoom<in Potato, Lamb> = LambDiningRoom()
    val lambDiningRoom5: DiningRoom<in Potato, out Meat> = LambDiningRoom()

    goToDiningRoom(lambDiningRoom1)
    goToDiningRoom(lambDiningRoom2)

    val potato = Potato()

    lambDiningRoom1.run {
        startCooking(potato)
        doneCooking()
    }

    lambDiningRoom2.run {
        startCooking(potato)
        doneCooking()
    }

    lambDiningRoom3.run {
        startCooking(potato)
        doneCooking()
    }

    val pizza1: Pizza<in Potato> = HawaiianPizza(Vegetable())
    val pizza2: Pizza<out Ingredient> = HawaiianPizza(Vegetable())
}

fun goToDiningRoom(param: DiningRoom<Vegetable, Meat>) {
    val diningRoom1: DiningRoom<in Potato, out Meat> = param
    val result1: Food = diningRoom1.doneCooking()

    diningRoom1.startCooking(Potato())
    println("=====${result1}=====")

    val diningRoom2: DiningRoom<*, *> = param

    if (diningRoom2 is LambDiningRoom) {
        val result2: Food = diningRoom2.doneCooking()

        diningRoom2.startCooking(Potato())
        println("=====${result2}=====")
    }
}

open class Food

open class Meat : Food(), Course

class Lamb : Meat(), Course, SpecialFood

interface SpecialFood

interface Course

fun <F> orderSpecialFood(param: F): F where F : Course, F : SpecialFood {
    println("orderSpecialFood(): $param")

    return param
}

fun <F : Food> orderFood(param: F): F {
    println("orderFood(): $param")

    return param
}

data object Vegetables

data object Noodle

open class Ingredient

open class Vegetable : Ingredient()

class Potato : Vegetable()

class Restaurant<F, I : Ingredient>(val food: F, val ingredient: I) {

    fun orderFood(): F {
        return food
    }

    fun <K> washDish(param: K) {
        println("washDish(): $param")
    }
}

interface DiningRoom<in I, out F> {

    fun startCooking(param: I)
    fun doneCooking(): F
}

class LambDiningRoom : DiningRoom<Vegetable, Lamb> {

    override fun startCooking(param: Vegetable) {
        println("startCokoking() - vegetable: $param")
    }

    override fun doneCooking(): Lamb {
        println("doneCooking()")
        return Lamb()
    }
}

open class Pizza<I : Ingredient>(val param: I)

class HawaiianPizza<P : Vegetable>(param: P) : Pizza<P>(param)

fun sayHello(param: Any): Any {
    println("sayHello(): $param")

    return param
}

fun <T> sayGoodMorning(param: T): T {
    println("sayGoodMorning(): $param")

    return param
}
