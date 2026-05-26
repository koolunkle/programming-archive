import kotlin.reflect.KClass

fun main() {
    
    someFunction { println("action(): called") }

    println("=========================")

    println("~Hello, world!~")

    someInlineFunction(
            action1 = {
                println("action1(): called")
                // return
                return@someInlineFunction
            },
            action2 = {
                println("action2(): called")
                return@someInlineFunction // not allowed 'return'
            },
            action3 = {
                println("action3(): called")
                return@someInlineFunction // not allowed 'return'
            }
    )

    println("-Hello, world!-")

    println("=========================")

    // println("someInlineFunction(): called")
    // println("action(): called")
    // println("someInlineFunction(): end")

    List(100000, init = { it }).nonInlineTestForeach { it * 2 }
    List(100000, init = { it }).inlineTestForeach { it * 2 }

    val result1 = isTypeMatched<Cat>(param = Dog())
    println(result1)

    val result2 = isTypeMatched<Cat>(param = Dog(), classType = Cat::class)
    println(result2)

    val result3: Dog = someTestFunction<Dog>(Cat())
    result3.bark()
}

fun List<Int>.nonInlineTestForeach(action: (Int) -> Unit) {
    val startTime = System.nanoTime()

    for (i in 0 ..< this.size) {
        action(this[i])
    }

    val endTime = System.nanoTime()

    println("total(non-inline): ${endTime.minus(startTime).div(1_000_000.0)} ms")
}

inline fun List<Int>.inlineTestForeach(action: (Int) -> Unit) {
    val startTime = System.nanoTime()

    for (i in 0 ..< this.size) {
        action(this[i])
    }

    val endTime = System.nanoTime()

    println("total(inline): ${endTime.minus(startTime).div(1_000_000.0)} ms")
}

fun someFunction(action: () -> Unit) {
    println("someFunction(): called")
    action()
    println("someFunction(): end")
}

inline fun someInlineFunction(
        action1: () -> Unit,
        noinline action2: () -> Unit,
        crossinline action3: () -> Unit
) {
    println("someInlineFunction(): called")
    println("=== action 1 ===")
    action1()
    println("=== action 2 ===")
    action2()
    println("=== action 3 ===")
    object : SomeListener {
                override fun onReceive() {
                    println("onReceive(): called")
                    action3()
                }
            }
            .onReceive()
    println("someInlineFunction(): end")
}

interface SomeListener {

    fun onReceive()
}

inline fun <reified T> isTypeMatched(param: Any): Boolean {
    return param is T
}

fun <T : Any> isTypeMatched(param: Any, classType: KClass<T>): Boolean {
    val paramClassName1 = param.javaClass.simpleName
    val paramClassName2 = param::class.simpleName
    val classTypeName = classType.simpleName

    return (paramClassName1 == classTypeName) || (paramClassName2 == classTypeName)
}

inline fun <reified T> someTestFunction(param: Any): T {
    return param as T
}

class Cat {

    fun bark() {
        print("Meow")
    }
}

class Dog {

    fun bark() {
        print(" Bow-wow")
    }
}
