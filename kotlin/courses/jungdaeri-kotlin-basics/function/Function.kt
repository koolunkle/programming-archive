fun main() {

    fun sayHello() {
        println("Hello")
    }

    fun sayHelloWithName(name: String) {
        println("Hello, $name")
    }

    fun sayHelloWithDefaultName(name: String = "Kim") {
        println("Hello, $name")
    }

    fun sayHelloWithReturns(): String {
        return "Hello"
    }

    sayHello()
    sayHelloWithName("Test")
    sayHelloWithDefaultName()
    println(sayHelloWithReturns())
}
