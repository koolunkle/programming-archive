// 컴포넌트 렌더링하기
// new Date()의 값에 en-US 로케일의 mm/dd/yyyy 형식으로 지정된 날짜를 출력한다
// 영국 날짜 형식인 dd/mm/yyyy로 나타내고 싶다면 로케일을 en-GB로 변경하면 된다
<FormattedDate
    value={new Date()}
    locale="en-US" // en-GB
/>