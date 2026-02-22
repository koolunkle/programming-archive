import { useState, useTransition } from "react";

// useTransition 훅을 사용해 비동기 업데이트 처리하기
const submitForm = async () => {
    // 폼을 제출한다
}

export function Component() {
    const [formState, setFormState] = useState(null);
    const [isPending, startTransition] = useTransition();

    const formAction = (event) => {
        event.preventDefault();

        startTransition(async () => {
            try {
                const result = await submitForm();
                setFormState(result);
            } catch (error) {
                setFormState({
                    message: "Failed to complete action"
                })
            }
        })
    }

    return (
        <form onSubmit={formAction}>
            {/* 폼 입력 */}

            {/* ... */}

            {isPending ? <h4>Pending...</h4> : null}
            {formState?.message && (<h4>{formState.message}</h4>)}
        </form>
    )
}