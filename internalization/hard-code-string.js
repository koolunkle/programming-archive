const en = require('./en.json');

// 텍스트 문자열 하드 코딩
const greetingVariable = 'Hello, Wrold!';

// 키를 사용해 en.json 파일의 텍스트에 접근하기
const greetingVar = en.greeting; // {{ greeting }};

console.log(greetingVar);

/*
    [예시]
      inerpolate는 가상의 함수
      지역화를 위해 구현된 서드파티 라이브러리가 제공하는 유틸리티를 사용해 {{name}}과 같은 플레이스홀더를 실젯값으로 변경할 수 있다
*/
const userName = "Addy";
const greetingMessage = interpolate(
    "{{ greeting }}",
    { name: userName }
);

console.log(greetingMessage);
