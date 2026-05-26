fun main() {

    // object singleton
    // companion object (named, unnamed)
    // anonymous object
    // data object

    val user1 = SomeUser(name = "홍길동")
    val user2 = SomeUser(name = "홍길동")

    println("isEqual: ${user1 == user2}")
    println("hashCode1: ${user1.hashCode()}")
    println("hashCode2: ${user2.hashCode()}")

    val userInfo1 = UserInfo
    val userInfo2 = UserInfo

    println("isEqual: ${userInfo1 == userInfo2}")
    println("hashCode1: ${userInfo1.hashCode()}")
    println("hashCode2: ${userInfo2.hashCode()}")

    userInfo1.saySomething()

    println("=====${UserInfo}=====")
    println("=====${UserInfoWithDataObject}=====")

    // Superman.Companion.saySomething()
    Superman.Factory.saySomething()
    Superman.saySomething()

    var someInstance1 =
            object : SomeUser(name = "noname"), Liking {

                override var name: String = "kim"
                var age: Int = 30

                override fun saySomething() {
                    println("someInstance: saySomething()")
                }
            }

    var someInstance2 =
            object {
                var name: String = "kim"
                var age: Int = 30
            }

    println(someInstance1)
    println(someInstance1.name)
    println(someInstance1.age)
    someInstance1.saySomething()
    println(someInstance1.hashCode() == someInstance2.hashCode())

    val testUser = TestUser()
    val someUser = testUser.getSomeUser()
    (someUser as? Liking)?.saySomething()
}

class TestUser {

    private var someInstance1 =
            object : SomeUser(name = "noname"), Liking {

                override var name: String = "kim"
                var age: Int = 30

                override fun saySomething() {
                    println("someInstance: saySomething()")
                }
            }

    fun getSomeUser(): SomeUser {
        return object : SomeUser(name = "noname"), Liking {

            override var name: String = "kim"
            var age: Int = 30

            override fun saySomething() {
                println("TestUser: saySomething()")
            }
        }
    }
}

open class SomeUser(open val name: String)

class Superman {

    companion object Factory {

        const val TAG = "Superman Tag"

        fun saySomething() {
            println("Superman: saySomething()")
        }
    }
}

open class SuperClass(var age: Int)

interface Liking {

    fun saySomething()
}

object UserInfo : SuperClass(age = 10), Liking {

    var name: String = "noname"

    init {
        println("UserInfo init called")
    }

    override fun saySomething() {
        println("UserInfo: saySomething()")
    }
}

data object UserInfoWithDataObject {

    var name: String = "noname"
}
