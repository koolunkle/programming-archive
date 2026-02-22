/*
    커스텀 선언 파일 예시

    아래 예시는 가상의 2개의 함수, 즉 add와 subtract 함수를 사용해 가상 라이브러리 모듈을 선언했다
    각 함수는 2개의 숫자를 인수로 받고, 1개의 숫자를 반환한다

    커스텀 선언 파일을 생성했다면, 다음으로 타입스크립트 구성 파일을 조정해 타입스크립트가 해당 커스텀 선언 파일을 컴파일에 포함하게 해야 한다
    선언 파일의 경로를 추가하거나 선언 파일이 저장된 폴더를 tsconfig.json 파일의 include 옵션에 포함시키면 된다
*/
declare module 'hypothetical-library' {
    export function add(
        a: number,
        b: number
    ): number;

    export function subtract(
        a: number,
        b: number
    ): number;
}