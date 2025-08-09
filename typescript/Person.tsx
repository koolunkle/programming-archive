interface Props {
    name: string;
    age: number;
    address?: string;
}

/*
    컴포넌트가 렌더링되면 name, age를 각각 string, number 타입으로 정의할 것을 요구한다
    또한 string 타입의 address 속성을 선택적으로 받을 수 있다
    정확하지 않은 타입을 사용해 컴포넌트를 렌더링하면 타입스크립트는 컴파일타임 에러를 출력한다
*/
const Person = ({ name, age, address }: Props) => {
    return (
        <div>
            <p>Name: {name}</p>
            <p>Age: {age}</p>
            {address && <p>Address: {address}</p>}
        </div>
    )
}

export default Person;