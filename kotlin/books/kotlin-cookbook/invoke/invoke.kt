

import java.net.URL/*
    실행 가능한 클래스 만들기

    클래스에서 단일 함수를 간단하게 호출하고 싶은 경우
    함수를 호출할 클래스에서 invoke 연산자 함수를 재정의한다

    연산자를 재정의하려면 오직 해당 연산자와 관련된 함수만 재정의하면 되며
    연산자를 구현하려면 멤버 함수 또는 올바르 이름과 인자를 가진 확장 함수를 제공해야 한다

    연산자를 중복하는 모든 함수는 operator 변경자를 포함해야 한다

    invoke 함수는 클래스의 인스턴스를 함수처럼 호출할 수 있다
*/

// 오픈 노티파이 서비스 JSON 데이터
val json = {
    "people" : [
        {"name": "Oleg Kononenko", "craft": "ISS"},
        {"name": "David Saint-Jacques", "craft": "ISS"},
        {"name": "Anne McClain", "craft": "ISS"}
    ],
    "number": 3,
    "message": "success"
}

// 응답 JSON 데이터를 모델링한 데이터 클래스
data class AstroResult(
    val message : String, 
    val number : Number,
    val people: List<Assignments>
)

data class Assignments(
    val craft : String,
    val name  : String
)

// 오픈 노티파이 서비스에 접근해 결과 파싱하기
// class AstroRequest {
//     companion object {
//         private const val ASTRO_URL = "http://api.open-notify.org/astros.json"
//     }

//     fun execute() : AstroResut { // 포함된 함수를 위한 임의의 이름
//         operator fun invoke() : AstroResult { // invoke 연산자 함수를 통해 실행 가능한 클래스가 됨
//             val response = URL(ASTRO_URL).readText()
//             return Gson().fromJson(response, AstroResut::class.java)
//         }
//     }
// }

fun main() {
    // 오픈 노티파이 서비스에 접근해 결과 파싱하기
    // val request = AstroRequest()
    // val result = request.execute() // 함수명을 invoke로 할 시 request()로 호출 가능
    // println(result.message)

}
