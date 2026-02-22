// memo()가 필요하지 않다

// const TodoItem = memo(({ todo }) => {
//     return <li>{todo}</li>;
// })

const TodoItem = ({ todo }) => {
    return <li>{todo}</li>
};

const TodoList = () => {
    // ...
    // ...

    return (
        <div>
            <ul>
                {todos.map((todo) => {
                    <TodoItem key={todo.id} todo={todo.todo} />
                })}
            </ul>
        </div>
    )
}

export default TodoList;