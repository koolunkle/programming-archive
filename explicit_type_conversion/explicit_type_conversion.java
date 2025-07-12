// 명시적으로 타입 변환하기
public class explicit_type_conversion {
    public static void main(String[] args) {
        // 자바에서 더 짧은 기본 타입을 더 긴 기본 타입으로 승격시키기
        int myInt = 3;
        long myLong = myInt;

        // Integer 타입을 Long 타입으로 변환하기
        Integer myInteger = 3;

        // 컴파일 되지 않음
        // Long myWrappedLong = myInteger;

        // long으로 추출한 다음 래퍼 타입으로 감쌈
        Long myWrappedLong = myInteger.longValue();

        // 래퍼 타입을 벗겨 int 타입을 얻고 long으로 승격시킨 다음 다시 래핑
        myWrappedLong = Long.valueOf(myInteger);
    }
}
