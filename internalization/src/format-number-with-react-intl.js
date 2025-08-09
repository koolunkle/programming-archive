import { FormattedNumber } from "react-intl";


function LocaleComponent() {
    return (
        <div>
            {/* 독일 로케일로 컴포넌트 렌더링하기 */}
            <FormattedNumber
                value={1004580.89}
                style="currency"
                currency="EUR"
                locale="de-DE"
            />

            {/* 미국 로케일로 컴포넌트 렌더링하기 */}
            <FormattedNumber
                value={1004580.89}
                style="currency"
                currency="USD"
                locale="en-U"
            />
        </div>
    )
}
