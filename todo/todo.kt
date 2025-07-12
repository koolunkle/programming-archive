fun myCleverFUnction() {
    // TODO: 멋진 구현을 찾는 중
}

// TODO 함수의 구현
// public inline fun TODO(reason: String) : Nothing
public fun TODO(reason: String): Nothing =
        throw NotImplementedError("An operation is not implemented: $reason")

fun completeThis() {
    TODO()
}

// 테스트에서 TODO 함수 사용하기
// fun `todo test`() {
//     val exception = assertThrows<NotImplementedException> { TODO("seriously, finish this") }
//     assertEquals("An operation is not implemented: seriously, finish this", exception.message)
// }

fun main() {
    // 보통의 코드에서 TODO 함수 사용하기
    TODO(reason = "none, really")
}
