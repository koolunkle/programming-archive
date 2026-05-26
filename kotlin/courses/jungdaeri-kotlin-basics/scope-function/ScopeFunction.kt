fun main() {
    
    var dummy: String? = null

    val result =
            dummy?.let {
                println("length: ${it.length}")
                it.length
            }
                    ?: 0

    println(result)

    val numbers = (1..5).toList()
    val total = numbers.map { "number: ${it}" }.let { "total: ${it.size}" }

    println(total)

    val puppy1 = Puppy()

    println("before puppy: ${puppy1.info()}")

    puppy1.let { it.age = 10 }

    println("after puppy: ${puppy1.info()}")

    val puppy2 =
            puppy1.run {
                this.age = 15
                println(puppy1.info())
                "Hello, ${this.age}"
            }

    println(puppy2)

    val puppy3 =
            with(puppy1) {
                age = 20
                println(puppy1.info())
                "Hello, $age"
            }

    println(puppy3)

    val puppy4 =
            puppy1.also { it.age = 25 }.let {
                println("${it.info()}")
                "Hello, ${it.age}"
            }

    println(puppy4)

    val puppy5 =
            puppy1.apply { age = 30 }.let {
                println("${it.info()}")
                "Hello, ${it.age}"
            }

    println(puppy5)

    // val recyclerView : RecyclerView = findViewById(R.id_recycler_view)
    // recyclerView.layoutManager = LinearLayoutManager(this)
    // recyclerView.adapter = customAdapter

    // val recyclerView: RecyclerView =
    //         findViewById(R.id_recycler_view).apply {
    //             layoutManager = LinearLayoutManager(this@MainActivity)
    //             adapter = customAdapter
    //         }
}

data class Puppy(val name: String = "", var age: Int = 0) {

    fun info(): String {
        return "name: $name, age: $age"
    }
}

fun Puppy.sayAge(): String {
    return this.age.toString()
}
