/*
        해당 시나리오에 관한 엔드-투-엔드 테스트를 설정할 때는 다음을 수행할 수 있다

        1. 인덱스 라우트와 /counter 라우트 모두에 대해 Cypress 테스트 파일을 만든다
        2. 인덱스 라우트에 대해 사용자가 링크나 버튼을 클릭했을 때 해당 라우트가 사용자를 /counter로 리다이렉트하는지 확인한다
        3. /counter 라우트에 대해 데이터 가져오기, count 값 증가/감소하기, 변경 사항을 서버에 저장하기, 클라이언트와 업데이트 동기화하기
           같은 사용자 상호작용을 시뮬레이션 하는 테스트를 작성한다
*/
function App() {
    return (
        <Router>
            <Switch>
                <Route
                    exact
                    path="/"
                    component={HomePage} />

                <Route
                    path="/counter"
                    component={CounterList} />
            </Switch>
        </Router>
    )
}

export default App;