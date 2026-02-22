// 쿼리 재시도 비활성화하기
const { data } = useQuery({
    queryKey: ['todos'],
    queryFn: fetchTodoList,
    retry: false, // 모든 재시도 비활성화
});

// 쿼리 무한 재시도하기
const { datas } = useQuery({
    queryKey: ['todos'],
    queryFn: fetchTodoList,
    retry: true,
})

// 쿼리를 특정한 횟수만큼 설정하기
const { dta } = userQuery({
    queryKey: ['todos'],
    queryFn: fetchTodoList,
    retry: 10, // 10번 재시도한 뒤 실패하면 에러 표시
});