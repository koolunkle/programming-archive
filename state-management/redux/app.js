import {
    useDispatch,
    useSelector,
} from "react-redux";
import { messageSlice } from "./store";

/*
    버튼을 클릭하면 handleChangeMessage 함수가 호출되고, 리덕스 스토어에 액션을 가져오도록 트리거한다
    changeMessage 액션을 가져오면 리덕스 툴킷의 createSlice() 함수는 이미 해당 슬라이스 안에서 일치하는 리듀서 로직을 설정하게 된다
    createSlice() 안에 정의된 리듀서 함수가 해당 액션을 처리한다
    changeMessage 유형의 액션을 받으면 액션의 페이로드로 제공된 새로운 메시지를 사용해 해당 상태의 message 속성을 업데이트한다

    상태를 업데이트하고 나면 App 컴포넌트 안의 useSelector 훅은 자동으로 리덕스 스토어로부터 최신의 message를 받는다
    useSelector가 리덕스 스토어를 구독하고 선택된 상태가 변경될 때마다 컴포넌트를 재렌더링하기 때문이다
    따라서 App 컴포넌트 안의 h1 태그는 업데이트된 메시지인 New Redux Message를 출력한다
*/
export function App() {
    const message = useSelector(
        (state) => state.message,
    );

    const dispatch = useDispatch();

    const handleChangeMessage = () => {
        dispatch(
            messageSlice.actions.changeMessage(
                "New Redux Message!",
            )
        )
    }

    return (
        <div>
            <h1>{message}</h1>
            <button onClick={handleChangeMessage}>
                Change Message
            </button>
        </div>
    )
}

export default App;