import { useContext } from "react";

function ChildComponent() {
    const { userPreferences } = useContext(UserContext);

    // 사용자 데이터로부터 색상 선호도를 추출한다
    const { backgroundColorPreference } = userPreferences;

    return (
        <div
            style={{
                backgroundColor: backgroundColorPreference
            }}>
            Personalized content based on user
            preference goes here.
        </div>
    )
}