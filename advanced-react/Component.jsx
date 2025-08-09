/*
    [비동기 폼 제출 처리를 위한 간단한 컴포넌트]

    폼이 제출되면 컴포넌트 formAction() 메소드가 트리거되고, 여기에서 대기 상태(pending state)를 설정하고,
    폼을 제출하고, 최종적으로 응답(혹은 에러)에 기반해 폼 상태를 업데이트할 수 있다
*/

import { useCallback, useState } from "react";

// 폼이 제출되었을 때 상태 변경 처리하기
const submitForm = async () => {
    // 폼 제출
}

export function Component() {
    // 폼 상태를 생성한다
    const [formState, setFormState] = useState(null);
    const [isPending, setIsPending] = useState(false);

    // 폼 제출을 처리한다
    const formAction = useCallback(async (event) => {
        event.preventDefault();
        setIsPending(true)
        try {
            const result = await submitForm();
            setFormState(result)
        } catch (error) {
            setFormState({
                message: "Failed to complete action"
            })
        }
        setIsPending(false)
    }, [])

    // 폼 템플릿을 표시한다
    return (
        <form onSubmit={formAction}>
            {/* 폼 템플릿 */}
        </form>
    )
}