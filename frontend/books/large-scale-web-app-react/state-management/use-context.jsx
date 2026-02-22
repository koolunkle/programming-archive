import { useState } from "react";

// 리액트에서 컨텍스트를 사용해 데이터 전달하기 (부모 컴포넌트)

// 컨텍스트 생성
const MessageContext = createContext();

function App() {
    const [message, setMessage] = useState(
        'Hello World',
    );

    return (
        //  중첩된 컴포넌트들에게 상태를 제공한다
        <MessageContext.Provider
            value={{ message, setMessage }}
        >
            <Child1>
                <Child2>
                    <Child3>
                        <DeeplyNestedChild />
                    </Child3>
                </Child2>
            </Child1>
        </MessageContext.Provider>
    )
}

// 리액트에서 컨텍스트를 사용해 데이터 전달하기(깊이 중첩된 자식 컴포넌트)
function DeeplyNestedChild() {
    // 데이터를 prop으로 받지 않고 직접 사용한다
    const { message, setMessage } = useContext(
        MessageContext,
    );

    return (
        <div>
            <h1>{message}</h1>
            <button
                onClick={() => setMessage('Hello from nested component!',)}>
                Change Message
            </button>
        </div>
    )
}