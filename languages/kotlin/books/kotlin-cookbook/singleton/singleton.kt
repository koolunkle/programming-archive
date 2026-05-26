fun main() {
    val processors = Runtime.getRuntime().availableProcessors()
    println(processors)

    println(MySingleton.myFunction())
    println(MySingleton.myProperty)

    // 자바에서 싱글톤 멤버에 접근하기
    // MySingleton.INSTANCE.myFunction();
    // MySingleton.INSTANCE.myProperty();
}

// 코틀린에서의 싱글톤 선언 (코틀린 object는 생성자를 가질 수 없음)
object MySingleton {
    val myProperty = 3

    fun myFunction() = "Hello"
}

/*
    싱글톤 패턴을 사용하는 Runtime의 구현
    public class RunTime() {

        private static final RunTime currentRuntime = new RunTime();

        private RunTime() {};

        public static RunTime getRuntime() {
            return currentRuntime;
        };
    }
*/
