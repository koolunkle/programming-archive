fun main() {
    val p1 = Product("baseball", 10.0)
    val p2 = Product("baseball", 10.0, false)
    val p2Copy = p2.copy(onSale = true)
    println("p1 hashCode: ${p1.hashCode()}\np2 hashCode: ${p2.hashCode()}")
    println("[p2Copy]\n${p2Copy.name}\n${p2Copy.price}\n${p2Copy.onSale}")

    val item1 = OrderItem(p1, 5)
    val item2 = item1.copy()
    println("is equal item1 and item2?: ${item1 === item2}")
    println("is equal item1's product and item2's product?: ${item1.product === item2.product}")

    val (name, price, sale) = p1
    println("name: $name\nprice: $price\nonSale: $sale")
}

// equals, hashCode, toString, copy, component 함수 생성
data class Product(val name: String, var price: Double, var onSale: Boolean = false)

data class OrderItem(val product: Product, val quantity: Int)

/*
    1. 코틀린이 생성한 equals와 hashCdoe 구현 사용하기
        @Test
        fun `check equivalence`() {
            val p1 = Product("baseball", 10.0)
            val p2 = Product("baseball", 10.0, false)

            assertEquals(p1, p2)
            assertEquals(p1.hashCode(), 02.hashCode())
        }

        @Test
        fun `create set to check equals and hashcode`() {
            val p1 = Product("baseball", 10.0)
            val p2 = Product(price = 10.0, onSale = false, name = "baseball")

            val products = setOf(p1, p2) // 중복된 Product는 추가되지 않음
            assertEquals(1, products.size)
        }

    2. copy(얕은 복사) 함수 테스트
        @Test
        fun `change price using copy`() {
            val p1 = Product("baseball", 10.0)
            val p2 = p1.copy(price = 12.0)
            assertAll(
                { assertEquals("baseball", p2.name) },
                { assertThat(p2.price, `is`(closeTo(12.0, 0.01))) },
                { assertFalse(p2.onSale) }
            )
        }

    3. 얕은 복사를 검증하는 테스트
        @Test
        fun `data copy function is shallow`() {
            val item1 = OrderItem(Product("baseball", 10.0), 5)
            val item2 = item1.copy()

            assertAll(
                { assertTrue(item1 == item2) },
                { assertFalse(item1 === item2) },
                { assertTrue(item1.product == item2.product) },
                { assertTrue(item1.product === item2.product) },
            )
        }

    4. Product 인스턴스 구조 분해(component 함수)
        @Test
        fun `destructure using component functions`() {
            val p = Product("baseball", 10.0)

            val (name, price, sale) = p
            assertAll(
                { assertEquals(p.name, name) },
                { assertThat(p.price, `is`(closeTo(price, 0.01))) },
                { assertFalse(sale) },
            )
        }
*/
