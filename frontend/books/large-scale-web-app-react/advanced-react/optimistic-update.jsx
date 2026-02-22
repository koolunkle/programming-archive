import { useState } from "react";

// 폼 제출 이후 message prop을 업데이트하는 컴포넌트
export function Component({
    message,
    updateMessage
}) {
    const [inputMessage, setInputMessage] = useState("");

    const submitForm = async (event) => {
        event.preventDefault();
        const newMessage = inputMessage;

        // API 제출이 해결되면 상태를 업데이트한다
        const updatedMessage = await submitToAPI(newMessage);

        updatedMessage(updateMessage);
    }

    return (
        <form onSubmit={submitForm}>
            {/* 현재 메시지를 보여준다 */}
            <p>{message}</p>

            <input
                type="text"
                name="text"
                value={inputMessage}
                onChange={(e) => {
                    setInputMessage(e.target.value)
                }} />
            <button type="submit">Add Message</button>
        </form>
    )
}