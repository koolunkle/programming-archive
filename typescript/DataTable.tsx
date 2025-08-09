type UserData = {
    id: number;
    name: string;
    email: string;
}

type ProductData = {
    productId: number;
    productName: string;
    price: number;
}

// (*)
type CommonData = UserData | ProductData;

/*
    유니언 타입을 사용해 다른 형태의 데이터를 받을 수 있도록 DataTable 조정하기(*)
    
    한편 any 타입을 사용하면 다음과 같이 할 수 있다
        - data: any;
        - columns: string[];
*/
interface Props {
    // data: Array<UserData> | Array<ProductData>;
    data: CommonData[] // (*)
    columns:
    Array<keyof UserData> |
    Array<keyof ProductData>;
}

// 타입 가드 함수: 데이터가 UserData[] 타입인지 확인(*)
function isUserDataArray(data: CommonData[]): data is UserData[] {
    // 배열의 첫 번째 항목을 확인하여 타입을 추론 (더 정확하게는 모든 항목을 확인하는 로직이 필요할 수 있음)
    if (data.length === 0) return true;
    return 'id' in data[0];
}

// 타입 가드 함수: 데이터가 ProductData[] 타입인지 확인(*)
function isProductDataArray(data: CommonData[]): data is ProductData[] {
    if (data.length === 0) return true;
    return 'productId' in data[0];
}

/*
    DataTable 컴포넌트는 Array<UserData> 타입의 data라는 이름의 prop을 받는다
    UserData는 사용자 정보를 포함한 객체를 나타낸다. DataTable 컴포넌트는 columns라는 prop을 위한 값도 받아야 한다
    이 값은 우리가 표시하고자 하는 UserData 객체들을 가리키는 리스트의 배열이다
    keyof UserData를 사용함으로써 UserData의 키만 사용할 수 있음을 보장한다

    * 다른 형태의 데이터를 위해 동일한 DataTable 컴포넌트를 재사용하고 싶다면 어떻게 할까?(*)
*/
const DataTable = ({ data, columns }: Props) => {
    return (
        <table>
            <thead>
                <tr>
                    {columns.map((col, index) => (
                        <th key={index}>{col}</th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {/* {data.map((item: UserData, index: number) => (
                    <tr key={index}>
                        {columns.map((col, idx) => (
                            <td key={idx}>{item[col]}</td>
                        ))}
                    </tr>
                ))} */}

                {data.map((item, index) => {
                    // 타입 가드를 사용하여 어떤 타입의 데이터를 다루는지 명시(*)
                    if (isUserDataArray(data)) {
                        const userItem = item as UserData; // 타입 단언(*)
                        return (
                            <tr key={index}>
                                {columns.map((col, idx) => (
                                    <td key={idx}>{userItem[col as keyof UserData]}</td>
                                ))}
                            </tr>
                        );
                    }
                    if (isProductDataArray(data)) {
                        const productItem = item as ProductData; // 타입 단언(*)
                        return (
                            <tr key={index}>
                                {columns.map((col, idx) => (
                                    <td key={idx}>{productItem[col as keyof ProductData]}</td>
                                ))}
                            </tr>
                        );
                    }
                    return null;
                })}
            </tbody>
        </table>
    )
}

export default DataTable;