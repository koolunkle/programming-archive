import { useFormStatus } from 'react';

export function NestedComponent() {
    // 마지막 폼 제출 정보에 접근한다
    const { pending, data, method, action } = useFormStatus();

    return (
        <div>
            {/* ... */}
        </div>
    )
}