// 기능 플래그 활성화 시 녹색 버튼 표시하기

function ButtonComponent() {
    // 기능 플래그를 평가한다
    const { value: isFeatureEnabled } = useGate("new_button_color")

    // 사용할 버튼 색상을 결정한다
    const buttonColor = isFeatureEnabled ? "green" : "blue"

    return (
        <button style={{ backgroundColor: buttonColor }}>
            Click Me
        </button>
    )
}

export default ButtonComponent;