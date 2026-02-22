import { useState } from "react";

const TodoList = () => {
    const [todos, setTodos] = useState([]);

    // ...
    // ...

    useEffect(() => {
        // ...
    }, []);

    // useMemo()가 필요하지 않다

    // const completedCount = useMemo(() => {
    //     return todos.filter((todo) => todo.completed).length;
    // }, [todos]);

    const completedCount = todos.filter((todo) => todo.completed).length;

    return (
        <div>
            {/*  */}
            <div>
                Completed Todos: {completedCount}
            </div>
        </div>
    )
}

export default TodoList;