// Customers 클래스의 equals와 hashCode 구현
class Customers(val name: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherCustomer = (other as? Customers) ?: return false
        return this.name == otherCustomer.name
    }

    override fun hashCode() = name.hashCode()
}

// 인텔리제이 IDEA에서 생성된 equals와 hashCode 함수
class Customerss(val name: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Customerss

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

/*
    equals 재정의를 위해 안전 타입 변환, 레퍼런스 동등, 엘비스 사용하기
    논리적으로 동등한 인스턴스인지를 확인하도록 클래스의 equals 메소드를 잘 구현하기
    레퍼런스 동일 연산자(===), 안전 타입 변환 함수(as?), 엘비스 연산자(?:) 다 같이 사용
    *  두 객체가 할당된 메모리 주소가 같으면 동일하고, 두 객체의 내용이 같으면 동등하다고 함

    가. Any 클래스에 equals, hashCode, toString 선언
        open class Any {
            open operator fun equals(other: Any?): Boolean

            open fun hashCode(): Int

            open fun toString(): String
        }

    나. KotlinVersion 클래스의 equals 함수
        override fun equals(other: Any?): Boolean {
            if(this === other) return true
            val otherVersion = (other as? KotlinVersion) ?: return false
            return this.version == otherVersion.version
        }

*/
