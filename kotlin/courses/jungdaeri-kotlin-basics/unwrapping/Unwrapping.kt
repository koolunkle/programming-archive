fun main() {

    val name: String? = "Test"

    // val unwrappedName: String =
    //     if (name.isNullOrBlank()) {
    //         "Unknown"
    //     } else {
    //         name
    //     }

    // val unwrappedName: String = name ?: "Unknown"

    val unwrappedName: String? = name?.let { it } ?: "Unknown"

    println(unwrappedName)
}
