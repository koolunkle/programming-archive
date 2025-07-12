import java.io.IOException;

public class exception {
    public static void main(String[] args) {
        doNothing();
        useThrowsClause();
        useThrowsClause();
    }
}

/*
 * IOException으로 충돌, try-catch 블록으로 감싸거나
 * 호출하는 자바 쪽에 throws 절을 추가하여도 문제가 발생한다
 */

public static void doNothing() {
    // IOException 충돌
    houstonWeHaveAProblem();
}

public static void useTryCatchBlock() {
    try {
        // 컴파일 실패, 자바는 예외를 던진다고 생각하지 않음
        houstonWeHaveAProblem();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static void useThrowsClause() throws IOException {
    // 컴파일은 되지만 컴파일러가 불필요한 throws 절이라고 경고
    houstonWeHaveAProblem();
}