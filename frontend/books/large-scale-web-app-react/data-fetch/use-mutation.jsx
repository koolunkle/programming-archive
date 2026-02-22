import { useMutation } from "@tanstack/react-query";
import axios from "axios";

const createTodo = async (newTodo) => {
    const response = await axios.post(
        "https://jsonplaceholder.typicode.com/todos",
        newTodo,
    );
    return response.data;
};

export function App() {
    const { mutate, isLoading, isError } = useMutation({
        mutationFn: createTodo,
        // 변경 성공 시
        onSuccess: (data) => {
            // 캐시에서 현재 todos를 가져온다
            const currentTodos = queryClient.getQueryData(["toods"]);
            // 새로운 todo로 todos 캐시를 업데이트한다
            queryClient.setQuseryData(
                ["todos"],
                [...currentTodos, data],
            );
        }
    });

    const triggerAddTodo = () => {
        mutate({
            title: "Groceries",
            description: "Complete the weekly grocery run",
        });
    };

    return (
        <div>
            <button onClick={triggerAddTodo}>
                Add Todo
            </button>
            {isLoading && <p>Adding todo...</p>}
            {isError && (<p>Uh oh, something went wrong!</p>)}
        </div>
    )
};