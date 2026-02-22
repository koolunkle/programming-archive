// useActionState 훅 시그니처
export function Component() {
    const [state, dispatch, isPending] = useActionState(
        action,
        initialState,
        permalink
    )
    // ...
}