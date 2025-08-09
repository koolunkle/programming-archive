import { useState } from 'react';

const Counter: React.FC = () => {
    // 명시적으로 count의 타입을 number로 정의
    const [count, setCount] = useState<number>(0);

    const updateCount = () => {
        setCount("5");
    };

    return (
        <div>
            <h2>Counter: {count}</h2>
            <button onClick={updateCount}>
                Increment
            </button>
        </div>
    )
}

export default Counter;