import { useEffect, useState } from "react"

const CounterList = () => {
    const [items, setItems] = useState([])

    useEffect(() => {
        fetch("/api/items")
            .then((response) => response.json())
            .then((data) => setItems(data))
    }, [])

    return (
        <div>
            <h1>Counter List</h1>
            {items.map((item) => {
                <Counter key={item.id} item={item} />
            })}
        </div>
    )
}

export default CounterList