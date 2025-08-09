function HelloWorld() {
    const [message, setMessage] = React.useState("Hello World!");

    return (
        <div>
            {/* message 값을 표시한다 */}
            <h1>message</h1>

            {/* 버튼을 클릭하면 'message' 상태는 업데이트되고 컴포넌트는 재렌더링해서 새로운 'message' 값을 표시한다 */}
            <button onClick={() => setMessage('Hello React!')}>
                Change Message
            </button>
        </div>
    )
}