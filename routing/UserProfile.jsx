import {
    createBrowserRouter,
    createRoutesFromElements,
    Route,
    useLoaderData,
    useParams
} from "react-router-dom";

// 라우트 매개변수와 loader prop
function UserProfile() {
    // useParams 훅은 URL에서 userId를 추출하고 이를 사용해 라우트에 정의된 로더 함수를 통해 사용자 데이터를 가져온다
    // 이 함수는 userId를 사용해 API 엔드포인트로부터 비동기적으로 사용자 데이터를 가져오고,
    // 가져온 데이터는 useLoaderData 훅을 통해 UserProfile 컴포넌트 안에서 사용된다
    const { userId } = useParams();
    const userData = useLoaderData();

    return (
        <div>
            <h1>User Profile</h1>
            <p>User ID: {userId}</p>
            <p>User Name: {userData.name}</p>
        </div>
    )
}

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route
            path="/users/:userId"
            element={<UserProfile />}
            loader={async ({ params }) => {
                return fetch(
                    `/api/users/${params.userId}`
                )
            }}
        />
    )
)