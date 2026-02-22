// 사용자 컨텍스트와 공급자 설정하기

// 메인 애플리케이션 컴포넌트 안에서 해당 사용자 정보에 접근해야 하는 컴포넌트를 UserProvider 컴포넌트로 감쌀 수 있다

// UserPreferences 컨텍스트를 만든다
const UserContext = React.createContext({});

export const UserProvider = ({ children }) => {
    // 외부 서비스 또는 API로부터 사용자 선호도 정보를 가져온다
    const userPreferences = getUserPreferences();

    return (
        <UserContext.Provider
            value={{ userPreferences }}
        >
            {children}
        </UserContext.Provider>
    )
}