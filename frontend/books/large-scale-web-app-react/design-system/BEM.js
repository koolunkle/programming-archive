//  블록은 메인 컴포넌트 혹은 컨테이너, 요소는 블록의 일부, 수식자는 블록 또는 요소의 변형(variation)
function BEM() {
    return (
        <div className="card card--featured">
            <h2 className="card__title">Featured Product</h2>
            <p className="card__description">
                Lorem ipsum dolor sit amet
            </p>
            <a href="#" className="card__link">Learn More</a>
        </div>
    )
}