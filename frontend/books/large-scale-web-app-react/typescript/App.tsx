// import Person from "./Person";
// import Person from "./children-react-node";
// import Person from "./children-react-element";

import DataTable from "./DataTable";
import DataTableWithGeneric from "./DataTable-With-Generic";

type UserData = {
    id: number;
    name: string;
    age: number;
}

type ProductData = {
    productId: string;
    productName: string;
    price: number;
}

const App = () => {
    const userData = [
        { id: 1, name: "John", age: 30 },
        { id: 2, name: "Jane", age: 25 },
    ]

    const productData = [
        { productId: "p1", productName: "Laptop", price: 1000 },
        { productId: "p2", productName: "Phone", price: 500 },
    ]

    return (
        // 정확하지 않은 타입을 사용해 컴포넌트 렌더링하기
        // <Person name={42} age={"twenty"} />

        // 요소의 배열을 갖는 컴포넌트를 children으로 렌더링하기
        // <Person>
        //     {[
        //         <p key="1">Name: James</p>,
        //         <p key="2">Age: 30</p>
        //     ]}
        // </Person>

        // 단일 요소를 갖는 컴포넌트를 children으로 렌더링하기
        // <Person>
        //     <p>Name: James</p>
        // </Person>

        // 사용자 정보를 사용해 DataTable 렌더링하기
        <div>
            <DataTable
                data={[
                    {
                        id: 1,
                        name: "John Doe",
                        email: "john.doe@example.com",
                    },
                ]}
                columns={["id", "name", "email"]}
            />
            <DataTableWithGeneric<UserData> data={userData} columns={[]} />
            <DataTableWithGeneric<ProductData> data={productData} columns={[]} />
        </div>
    )
}

export default App;