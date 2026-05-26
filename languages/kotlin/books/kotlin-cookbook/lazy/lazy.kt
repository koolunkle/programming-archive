fun main() {
    val customer = Customer("Fred").apply { message }
    println("name: ${customer.name}\nmessage: ${customer.message}")
    print("message size: ${customer.message.size}")
}

class Customer(val name: String) {

    // case 1
    // private var _message: List<String>? = null
    // val message: List<String>
    //     get() {
    //         if (`_message` == null) {
    //             `_message` = loadMessages()
    //         }
    //         return `_message`!!
    //     }

    // case 2
    val message: List<String> by lazy { loadMessages() }

    private fun loadMessages(): MutableList<String> =
            mutableListOf(
                            "Initial contact",
                            "Convinced them to use Kotlin",
                            "Sold training class. Sweet."
                    )
                    .also { println("Loaded messages") }
}
