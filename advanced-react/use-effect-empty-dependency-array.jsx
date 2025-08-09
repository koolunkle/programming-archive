// useEffect의 빈 의존성 배열

import { useEffect } from "react";

useEffect(() => {
    const fetchTodos = async () => {
        // ...
    };
    fetchTodos();
    // 빈 의존성 배열은 이 효과가 컴포넌트가 마운트된 뒤, 단 한 번만 실행되는 것을 보장한다
}, []);