import { useQuery } from "@tanstack/react-query";
import axios from "axios";

const fetchTodoList = async () => {
    const response = await axios.get(
        "https://jsonplaceholder.typicode.com/todos",
    );
    return response.data;
};

function App() {
    const {
        data = [],
        isLoading,
        isError
    } = useQuery({
        queryKey: ["todos"],
        queryFn: fetchTodoList,
        placeholderData: [
            {
                id: "placeholder1",
                title: "Fetching...",
                completed: false,
            },
            {
                id: "placeholder2",
                title: "Fetching...",
                completed: false,
            }
        ],
    });
};