// 폼 제출을 처리하는 액션 함수 만들기

import { useActionState } from "react";

const submitForm = async () => {
    // 폼을 제출한다
}

const action = async (currentState, formData) => {
    try {
        const result = await submitForm(formData);
        return { message: result };
    } catch {
        return { message: "Failed to complete action" }
    }
}

export function Component() {
    const [state, dispatch, isPending] = useActionState(action, null);
    // ...
}

