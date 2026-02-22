// Intl 자바스크립트 객체를 사용해 숫자 형식 지정하기
const number = 1004580.89;

const formattedNumberDE = new Intl.NumberFormat(
    "de-DE"
).format(number);

const formattedNumberUS = new Intl.NumberFormat(
    "en-US"
).format(number);

console.log(formattedNumberDE)

console.log(formattedNumberUS)