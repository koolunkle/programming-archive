// 외부 함수 메모이제이션, TodoList에서 가상의 값비싼 함수 사용하기

// 리액트 컴파일러는 함수 자체를 메모화하지는 않는다
const getPriorityTodos = (todos) => {
    // ...
};

const TodoList = () => {
    // ...

    // 리액트 컴파일러는 함수 호출을 메모화한다
    const priorityTodos = getPriorityTodos(items);

    return <div>
        {/* ... */}
    </div>
}

export default TodoList;
