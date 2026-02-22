// 클라이언트 컴포넌트

"use client"

import { useState } from "react"

export default function Counter() {
    const [count, setCount] = useState(0)

    return (
        <div>
            <p>Count: {count}</p>
            <button onClick={() => setCount(count + 1)}>
                Increment
            </button>
        </div>
    )
}
