import { UserProvider } from "./User-Context-Provider";

// UserProvider를 사용해서 자식 컴포넌트 감싸기
function App() {
    return (
        <UserProvider>
            {/* 다른 컴포넌트들 */}
        </UserProvider>
    )
}

export default App;