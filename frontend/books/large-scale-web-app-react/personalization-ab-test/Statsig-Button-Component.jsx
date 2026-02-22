// 실험 구성 상세 정보 얻기(1)
// 실험 그룹에 기반해 다른 버튼 색상 렌더링하기(2)
// 버튼 클릭 기록하기(3)

function ButtonComponent() {
    // 실험 구성 정보에 접근한다(1)
    const { config } = useExperiment("button_color")

    // 실험 매개변수의 값에 접근한다(1)
    const showGreenButton = config.get("enable_feature")

    // 실험 그룹에 따라 버튼 색상을 결정한다(2)
    const buttonColor = showGreenButton ? "green" : "blue"

    const onButtonClick = () => {
        // 버튼 클릭을 기록한다(3)
        Statsig.logEvent(
            "button_click",
            buttonColor
        )
    }

    return (
        <button
            style={{ backgroundColor: buttonColor }}
            onClick={onButtonClick}
        >
            Click Me
        </button>
    )
}

export default ButtonComponent