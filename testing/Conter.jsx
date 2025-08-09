const { useState } = require("react")

const Counter = () => {
    const [count, setCount] = useState(0)

    // 단위 테스트 용도 

    // const increment = () => {
    //     setCount(count + 1)
    // }

    // const decrease = () => {
    //     setCount(count - 1)
    // }

    // return (
    //     <div>
    //         <button>-</button>
    //         <span>{count}</span>
    //         <button>+</button>
    //     </div>
    // )

    // 엔드-투-엔드 테스트 용도
    const updateCount = (change) => {
        fetch(`./api/items/${item.id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                count: count + change
            })
        })
            .then((response) => response.json())
            .then((updatedItem) => {
                setCount(updatedItem.count)
            })
    }

    return (
        <div>
            <h3>{item.name}</h3>
            <div>
                Count: {" "}
                <span className="count">{count}</span>
            </div>
            <button
                className="increment"
                onClick={() => updateCount(1)}>
                +
            </button>
            <button
                className="decrement"
                onClick={() => updateCount(-1)}>
                -
            </button>
        </div>
    )
}

export default Counter;