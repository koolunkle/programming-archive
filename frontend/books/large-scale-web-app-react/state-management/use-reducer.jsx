import { useReducer } from "react";

const initialState = {
    message: "Hello World!",
};

function reducer(state, action) {
    switch (action.type) {
        case "CHANGE_MESSAGE":
            return {
                ...state,
                message: action.payload,
            };
        default:
            throw new Error();
    }
}

/*
    버튼을 클릭하면 다음과 같이 작동한다
        : dispatch 함수가 호출, CHANGE_MESSAGE 유형의 액션과 New Message! 라는 새 메시지를 포함한 페이로드를 사용한다
        : 해당 액션은 reducer 함수로 전달되고 reducer 함수 안에서 액션 유형을 확인하고 케이스가 일치하면, message 상태 속성을 업데이트한다
        : UI에 업데이트된 상태 속성이 표시된다
*/
function App() {
    const [state, dispatch] = useReducer(
        reducer,
        initialState,
    );

    return (
        <div>
            <h1>{state.message}</h1>
            <button
                onClick={() => dispatch({
                    type: "CHANGE_MESSAGE",
                    payload: "New Message!",
                })}
            >
                Change Message
            </button>
        </div>
    )
}