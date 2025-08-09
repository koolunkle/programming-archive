import { FormattedDate } from "react-intl";

// 긴 날짜 형식으로 렌더링하기
<FormattedDate
    value={new Date()}
    locale="en-US"
    weekday="long"
    year="numeric"
    month="long"
    day="numeric"
/>