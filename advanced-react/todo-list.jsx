import { useEffect, useState } from "react";

const TodoList = () => {
    const [todos, setTodos] = useState([]);

    useEffect(() => {
        const fetchTodos = async () => {
            try {
                const response = await fetch(
                    "https://dummyjson.com/todos"
                );
                const data = await response.json();
                setTodos(data.todos);
            } catch (error) {
                console.error("Error fetching todos:", error);
            }
        }
        fetchTodos();
    }, [])

    return (
        <div>
            <ul>
                {todos.map((todo) => (
                    <li key={todo.id}>{todo.todo}</li>
                ))}
            </ul>
        </div>
    )
}

export default TodoList;