import java.io.File

/*
    파일에 기록하기

    File 클래스의 확장 함수에는 일반적인 자바 입출력 메소드뿐만 아니라
    출력 스트림과 라이터(Writer)를 리턴하는 확장 함수가 있다

    forEachLine 함수를 사용해서 파일을 순회할 수 있으며
    파일이 매우 크지 않다면 File의 readLines를 호출해 모든 줄이 담긴 컬렉션을 획득할 수 있다

    useLines 함수를 사용해 파일의 줄마다 호출되는 함수를 제공할 수 있으며
    파일의 크기가 작다면 readText 또는 readBytes를 사용해 전체 내용을
    각각 문자열이나 바이트 배열로 읽어올 수 있다

    또한 File 클래스에는 파일에 데이터를 추가하는 appenText라는 확장 함수가 있다
    writeText와 appendText 함수는 writeBytes와 appendBytes에 기록 작업을 위임한다
    writeBytes와 appendBytes는 기록이 끝나면 use 함수를 사용해 파일을 닫는다

    OutputStreamWriter와 BufferedWriter를 리턴하는 writer(또는 printWriter)와 bufferedWriter 함수를
    사용할 수 도 있으며, use 블록을 사용해 실제로 내용을 기록할 수 있다
*/

fun main() {
    val file = File("file.txt")

    // 파일의 모든 텍스트 치환하기
    file.writeText("Hello, World!")

    // use 함수로 파일 작성하기
    file.printWriter().use { writer -> writer.println("Kotlin Cookbooks") }
}
