// Intl 자바스크립트 객체를 사용해 날짜 형식 지정하기
const date = new Date();

const options = {
    year: "numeric",
    month: "long",
    day: "numeric"
}

const formattedDate = new Intl.DateTimeFormat(
    "en-US",
    options,
).format(date)

console.log(formattedDate)