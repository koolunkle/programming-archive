import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from "react-router-dom";

// lazy prop을 사용해서 라우트 정의를 지연 실행하기
// 동적 로딩을 위해 `lazy` prop을 사용해서 라우트를 정의한다
// lazy 함수는 각 모듈의 동적 임포트를 반환하고, 지연 로딩된 라우트 모듈(a.js) 안에서 라우트에 정의하고자 하는 속성을 익스포트할 수 있다
let routes = createRoutesFromElements(
    <Route path="/" element={<Layout />}>
        <Route
            path="a"
            lazy={() => import("./a")} />
    </Route>
)

function App() {
    return (
        <RouterProvider router={createBrowserRouter(routes)} />
    )
}

export default App;