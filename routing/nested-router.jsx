import { createBrowserRouter, createRoutesFromElements, Outlet, Route } from "react-router-dom";

/*
    Outlet 컴포넌트를 사용한 중첩된 라우트

    Dashboard는 부모 라우트 컴포넌트 역할을 하며, 그 안에 여러 자식 라우트가 정의되어 있다
    Outlet 컴포넌트는 현재 URL 경로에 기반해 적절한 자식 컴포넌트를 렌더링한다
    해당 설정은 애플리케이션 안에 명확한 계층 구조를 제공하며, 이는 URL 조각을 레이아웃과 연결하는 리액트 라우터의 철학과도 일치한다
 */
const router = createBrowserRouter(
    createRoutesFromElements(
        <Route
            path="/dashboard"
            element={<DashBoard />}
        >
            <Route index element={<Overview />} />
            <Route
                path="profile"
                element={<Profile />} />
            <Route
                path="settings"
                element={<Settings />} />
        </Route>
    )
)

function DashBoard() {
    return (
        <div>
            <h1>Dashboard</h1>
            <nav>
                <Link to="/dashboard">Overview</Link>
                <Link to="/dashboard/profile">Profile</Link>
                <Link to="/dashboard/settings">Settings</Link>
            </nav>
            {/* 이 컴포넌트는 활성화된 자식 라우트 컴포넌트를 렌더링한다 */}
            <Outlet />
        </div>
    )
}