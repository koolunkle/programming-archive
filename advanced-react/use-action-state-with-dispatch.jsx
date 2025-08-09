import { useActionState } from "react";

// dispatch를 <form> 액션 prop에 전달하기

// 템플릿에서 폼 state와 isPending 상태를 표시하기

const submitForm = async () => {
    // 폼을 제출한다
};

const action = async (currentState, formData) => {
    try {
        const result = await submitForm(formData);
        return { message: result };
    } catch {
        return { messag: "Failed to complete action" }
    }
};

export function Component() {
    const [state, dispatch, isPending] = useActionState(action, null);

    return (
        <form action={dispatch}>
            <input
                type="text"
                name="text"
                disabled={isPending}>
                <button type="submit" disabled={isPending}>
                    Add Todo
                </button>
            </input>

            {/* 폼 제출이 성공 혹은 실패했을 때 전달할 폼 상태 메시지를 출력한다 */}
            {state.message && <h4>{state.message}</h4>}
        </form>
    )
}