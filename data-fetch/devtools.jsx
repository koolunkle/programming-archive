import { ReactQueryDevtools } from '@tanstack/react-query';

root.render(
    <QueryClientProvider client={queryClient}>
        <App />
        {/* Devtools를 root에 위치시킨다 */}
        <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
)