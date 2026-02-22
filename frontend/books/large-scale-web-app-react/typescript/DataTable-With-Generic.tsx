/*
    DataTable이 타입 변수를 받게 만들기

    해당 예제에서 제네릭 컴포넌트는 Array<T> 타입의 데이터 prop을 받는다. 여기에서 T는 모든 객체가 될 수 있다

    <T extends object> 구문은 객체를 확장한 제네릭 타입 T를 선언한다. 이는 T가 모든 객체 타입이 될 수 있다는 의미이다
    제네릭 타입 변수 T는 Props 인터페이스로 전달되고, Props 인터페이스 역시 제네릭이며 타입 변수를 받을 수 있다
    
    이후 T를 사용해 data와 columns 배열의 타입을 지정한다
    data는 모든 타입 T의 배열이고, columns느 T의 키의 배열이다
*/

// 타입 변수를 Props 제네릭 인터페이스에 전달하기
interface Props<T extends Record<string, any>> {
    data: Array<T>;
    columns: Array<keyof T>;
}

const DataTableWithGeneric = <T extends Record<string, any>>(
    { data, columns }: Props<T>
) => {
    return (
        <table>
            <thead>
                <tr>
                    {columns.map((col, index) => (
                        <th key={index}>{String(col)}</th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {data.map((item: T, index: number) => (
                    <tr key={index}>
                        {columns.map((col, idx) => (
                            <td key={idx}>
                                {item[col as keyof T]}
                            </td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    )
}

export default DataTableWithGeneric;