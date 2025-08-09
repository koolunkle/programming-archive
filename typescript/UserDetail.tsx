import React, {
    useEffect,
    useState
} from 'react';

import { User } from './types/User';

const UserDetail: React.FC<{ userId: number }> = ({ userId }) => {
    // 사용자 타입이 "User"가 되도록 설정
    const [user, setUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await fetch(
                    `https://example.com/api/users/${userId}`
                );
                if (!response.ok) {
                    throw new Error("User not found")
                }
                const userData: User = await response.json();
                // API로부터 얻은 userData를 "User" 타입으로 형변환한다
                setUser(userData as User);
            } catch (error) {
                console.error("Failed to fetch user:", error)
                setUser(null);
            }
        };
        fetchUser()
    }, [userId]);

    // 사용자 세부 정보 렌더링
    if (!user) {
        return <div>사용자 정보를 불러오는 중입니다...</div>
    }
    // 사용자 정보가 성공적으로 로드되면 화면에 렌더링
    return (
        <div>
            <h2>사용자 정보</h2>
            <p>
                <strong>ID:</strong> {user.id}
            </p>
            <p>
                <strong>이름:</strong> {user.name}
            </p>
        </div>
    )
}

export default UserDetail;