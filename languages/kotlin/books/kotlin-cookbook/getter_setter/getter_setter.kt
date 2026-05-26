fun main() {
    var myTask = Tasks().apply { priority = 4 }
    println(
            "name: ${myTask.name}\npriority: ${myTask.priority}\nisLowPriority: ${myTask.isLowPriority}"
    )
}

class Tasks(val name: String = "") {
    /*
        속성 정의 문법
        var <propertyName>[: <PropertyType>] [= <property_initializer>]
            [<getter>]
            [<setter>]
    */
    var priority = 3
        set(value) {
            field = value.coerceIn(1..5)
        }

    val isLowPriority
        get() = priority < 3
}
