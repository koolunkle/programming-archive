// (보통 map의 항목으로써) Pair 클래스의 인스턴스 생성 시 중위 to 함수를 사용할 수 있음
//     fun <K, V> mapOf(vararg pairs: Pair<K, V>): Map<K, V>
    
//     data class Pair<out A, out B> : Serializable
    
//     public infix fun <A, B> A.to(that: B) : Pair<A, B> = Pair(this, that)

//     @Test
//     fun `create map using infix to function`() {
//         val map = mapOf("a" to 1, "b" to 2, "c" to 2)
//         assertAll(
//             { assertThat(map, haskKey("a")) },
//             { assertThat(map, haskKey("b")) },
//             { assertThat(map, haskKey("c")) },
//             { assertThat(map, haskKey(1)) },
//             { assertThat(map, haskKey(2)) },
//         )
//     }

//     @Test
//     fun `create a Pair from constructor vs to function`() {
//         val p1 = Pair("a", 1) // 생성자를 통해 Pair 생성
//         val p2 = "a" to 1 // to를 사용해서 Pair 생성

//         assertAll(
//             { assertThat(p1.first, `is`("a")) },
//             { assertThat(p1.second, `is`(1)) },
//             { assertThat(p2.first, `is`("a")) },
//             { assertThat(p2.second, `is`(1)) },
//             { assertThat(p1, `is`(equalTo(p2))) },
//         )
//     }

//    @Test
//    fun `destructuring a Pair`() {
//         val pair = "a" to 1
//         val (x,y) = pair

//         assertThat(x, `is`("a"))
//         assertThat(y, `is`(1))
//    }
