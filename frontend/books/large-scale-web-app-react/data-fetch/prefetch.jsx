// 트리거 했을 때 쿼리를 미리 가져오는 함수 작성하기

import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { queryClient } from ".";

const fetchTodoList = async () => {
    const response = await axios.get(
        "https://jsonplaceholder.typicode.com/todos",
    );
    return response.data;
};

/*
    해당 함수는 이벤트 핸들러에 의해 트리거될 수 있다
    컴포넌트 라이프사이클 이벤트 혹은 심지어 사용자가 특정한 섹션 혹은 페이지로 이동하기 전에 트리거될 수도 있다
*/
const prefetchTodos = async (queryClient) => {
    // 해당 쿼리의 결과는 일반적인 쿼리와 동일하게 캐시된다
    await queryClient.prefetchQuery({
        queryKey: ["todos"],
        queryFn: fetchTodoList,
    });
};