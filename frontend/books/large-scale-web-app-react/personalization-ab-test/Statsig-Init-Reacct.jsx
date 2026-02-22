// 리액트 애플리케이션에서 Statsig 초기화하기
function App() {
    return (
        <StatsigProvider
            sdkKey="<STATSIG_CLIENT_SDK_KEY>"
            waitForInitialization={true}
        >
            <div className="App">
                {/* Rest of App ... */}
            </div>
        </StatsigProvider>
    )
}

export default App;