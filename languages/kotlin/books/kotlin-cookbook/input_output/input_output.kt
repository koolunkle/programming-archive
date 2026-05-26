import java.io.Closeable
import java.io.File
import java.nio.charset.Charset

/*
    use로 리소스 관리하기

    파일 같은 리소스를 처리하고 사용을 끝마쳤을 때 확실하게 리소스를 닫고 싶지만
    코틀린은 자바의 try-with-resources 구문을 지원하지 않는다
    코틀린은 kotlin.io 패키지의 use 또는 java.io.Reader의 useLines 확장 함수를 사용한다

    * try-with-resources 구문의 유일한 요구 사항은 사용할 리소스가 Closeable 인터페이스를 구현한 클래스여야 함
    * File, Stream, Reader 외에도 많은 클래스가 Closeable을 구현한다

    자바 10 이상은 try 괄호 안의 BufferedReader와 Stream 선언을 var 예약어로 대치할 수 있다
    자바에서 이 부분이 로컬 변수 타입 추론의 주요한 유스 케이스이다
    (코틀린의 var 키워드와 혼동하지 말 것)

    자바 9 이상은 괄호 안에 Closeable 변수를 생성할 필요가 없으며 Closeable 변수는 괄호 밖에서 제공된다
    코틀린은 Closeable에는 확장 함수 use, Reader와 File에는 useLine을
    추가하였으며, useLines의 시그니처는 다음과 같다
*/

// File.useLines 확장 함수의 구현
inline fun <T> File.useLines(charset: Charset = Charsets.UTF_8, block: (Sequence<String>) -> T): T =
        bufferedReader(charset).use { block(it.lineSequence()) }

// Closeable에 들어 있는 use 확장 함수의 시그니처
inline fun <T : Closeable?, R> T.use(block: (T) -> R): R {
    try {
        return block(this)
    } catch (e: Throwable) {
        throw e
    } finally {
        // close()
    }
}

// 웹스터 사전에서 가장 긴 단어 10개 찾기
fun get10LongestWordsInDictionary() =
        File("/usr/share/dict/words").useLines { line ->
            line.filter { it.length > 20 }.sortedByDescending(String::length).take(10).toList()
        }

fun main() {}
