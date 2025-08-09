import { useOptimistic, useState } from "react";

export function component({
    message,
    updatedMessage
}) {
    const [inputMessage, setInputMessage] = useState("");

    const [
        optimisticMessage,
        setOptimisticMessage
    ] = useOptimistic(message);

    const submitForm = async (event) => {
        event.preventDefault();
        const newMessage = inputMessage;

        // API 변경을 트리거하기 전에, 낙관적으로 값을 설정한다
        setOptimisticMessage(newMessage);

        // API 제출이 해결되면 상태를 업데이트한다
        const updatedMessage = await submitToAPI(newMessage);
        updatedMessage(updatedMessage);
    };

    return (
        <form onSubmit={submitForm}>
            {/* 낙관적 값을 보여준다 */}
            <p>{optimisticMessage}</p>

            <input
                type="text"
                name="text"
                value={inputMessage}
                onChange={(e) => setInputMessage(e.target.value)}
            />
            <button type="submit">Add Message</button>
        </form>
    )
}