import { createContext, use, useState } from "react";

// 리액트에서 컨텍스트를 사용해 데이터 전달하기(부모 컴포넌트)

// 컨텍스트를 생성한다
const MessageContext = createContext();

function App() {
    const [message, setMessage] = useState("Hello, World!");

    return (
        // 중첩된 컴포넌트들에게 상태를 제공한다
        <MessageContext value={{ message, setMessage }}>
            <Child1>
                <Child2>
                    <Child3>
                        <DeeplyNestedChild />
                    </Child3>
                </Child2>
            </Child1>
        </MessageContext>
    )
}

// use를 사용해 컨텍스트 읽기(자식 컴포넌트)
function DeeplyNestedChild() {
    // 새로운 use() API를 사용해 데이터에 접근한다
    const { message, setMessage } = use(MessageContext);

    return (
        <div>
            <h1>{message}</h1>
            <button onClick={() => setMessage(
                "Hello from nested component!"
            )}>
                Change Message
            </button>
        </div>
    )
}