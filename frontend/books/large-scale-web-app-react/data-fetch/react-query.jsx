// 리액트 쿼리 초기화하기
import {
    QueryClient,
    QueryClientProvider
} from "@tanstack/react-query";
import { createRoot } from "react-dom/client";
import { App } from './App.js';

const rootElement = document.getElementById("root");
const root = createRoot(rootElement);

// 클라이언트 만들기
const queryClient = new QueryClient();

root.render(
    // 클라이언트를 애플리케이션에 제공
    <QueryClientProvider client={queryClient}>
        <App />
    </QueryClientProvider>
);

// useQuery 훅을 사용해 데이터 가져오기
const fetchTodoList = async () => {
    const response = await axios.get(
        "https://jsonplaceholder.typicode.com/todos"
    );
    return response.data;
};

/*
    useQuery 훅 선언 안에 쿼리를 위한 고유 키를 제공하고, 'todos' 및 지정한 엔드포인트에
    GET 요청을 트리거하는 비동기 함수를 제공한 점에 주목, 비동기 함수에는 axios 라이브러리를 활용

    useQuery 훅은 하나의 result 객체를 반환한다. 해당 객체는 쿼리의 상태와 결과를 반영하는 다른 속성들을 포함한다.
    이 객체는 성공한 쿼리를 통해 얻은 데이터를 갖는 동시에 isLoading, isError 같은 지시자를 갖는다.
    
    isLoading은 요청이 진행 중임을 나타내고 isError 시그널은 데이터를 가져오는 동안 무언가 이슈가 발생했음을 나타낸다
    이 쿼리 속성들을 사용해 컴포넌트가 API 요청 상태에 따라 서로 다른 UI를 렌더링하게 할 수 있다
*/
export function App() {
    const { data, isLoading, isError } = useQuery({
        queryKey: ["todos"],
        queryFn: fetchTodoList,
        staleTime: 1000 * 60, // 1분
    });

    // ... 데이터를 표시/조작하기 위한 추가 코드 (반환 값에 따라 UI 렌더링 하기)
    if (isLoading) {
        return <p>Requset is loading!</p>;
    }

    if (isError) {
        return <p>Requset has failed :(!</p>;
    }

    return (
        <div>
            <h1>Todos List</h1>
            <ul>
                {data.map(todo => (
                    <li key={todo.id}>{todo.title}</li>
                ))}
            </ul>
        </div>
    )
}