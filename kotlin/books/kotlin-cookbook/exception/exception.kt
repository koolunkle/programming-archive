import java.io.IOException

/*
    자바에게 예외 알리기

    코틀린 함수가 자바에서 체크 예외(checked exception)차로 여겨지는 예외를 던지는 경우
    자바에게 해당 예외가 체크 예외임을 알려주고 싶은 때 함수 시그니처에
    @Throws 어노테이션을 추가한다

    코틀린의 모든 예외는 언체크 예외로써 컴파일러는 개발자에게
    해당 예외를 처리할 것을 요구하지 않는다

    예외를 붙잡기 위해 코틀린 함수에 try/catch/finally 블록을
    추가하는 방법이 있지만 강제사항은 아니다
    * 코틀린에는 자바에서 메소드가 예외를 발생시킬지도 모른다고 선언할 때 사용하는 throws 키워드가 없다
*/

/*
    IOException을 던지는 코틀린 함수

    자바에서 코틀린 함수를 호출하기 전까지는 아무런 문제가 없다
    호출한 코틀린 함수가 잠재적으로 자바에서 체크 예외로 여겨지는 예외를 던지고
    자바에서 해당 예외를 잡고 싶다면 자바에게 해당 사실을 알려줘야 한다

    코틀린에서는 해당 함수에 try/catch 블록 또는 throws 절이 없어도 컴파일이 가능하다
    해당 함수는 자바에서 호출할 수 있고 IOException으로 충돌이 발생한다
*/

// before
// fun houstonWeHaveAProblem() {
//     throw IOException("File or resource not found")
// }

// after
@Throws(IOException::class) // 자바에게 이 함수가 IOException을 던진다고 알려준다
fun houstonWeHaveAProblem() {
    throw IOException("File or resource not found")
}

fun main() {}
