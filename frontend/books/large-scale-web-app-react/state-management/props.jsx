import { useState } from "react";

// 리액트에서 props를 사용해 콜백 함수를 아래로 전달하기(자식 컴포넌트)
function HelloWorld(props) {
    return (
        <div>
            <h1>{props.message}</h1>
            {/* onChangeMessage() 함수는 부모에게서 왔다 */}
            <button onClick={props.onChangeMessage}>
                Change Message
            </button>
        </div>
    )
}

// 리액트에서 props를 사용해 콜백 함수를 아래로 전달하기(부모 컴포넌트)
function App() {
    const [message, setMessage] = useState(
        'Hello World!',
    );

    const changeMessage = () => {
        setMessage("Hello React!");
    };

    // onChangeMessage()는 props로써 아래로 전달된다
    return (
        <HelloWorld
            message={message}
            onChangeMessage={changeMessage}
        />
    )
}