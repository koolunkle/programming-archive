"use client"
import { useState } from 'react';

// 리액트 클라이언트 컴포넌트
export function Comments({ id, text }) {
    const [likes, setLikes] = useState(0);

    function handleLike() {
        setLikes(likes + 1);
    }

    return (
        <div>
            <p>{text}</p>
            <button onClick={handleLike}>
                Like ({likes})
            </button>
        </div>
    );
}