import { useReducer } from 'react';

type State = { count: number };

type Action = {
    type: 'increment' | 'decrement'
};

const initialState: State = { count: 0 };

function reducer(
    state: State,
    action: Action
): State {
    switch (action.type) {
        case 'increment':
            return { count: state.count + 1 };
        case 'decrement':
            return { count: state.count - 1 };
        default:
            throw new Error();
    }
}

const Counter = () => {
    const [state, dispatch] = useReducer(
        reducer,
        initialState
    );

    const wrongUpdate = () => {
        dispatch({ type: 'multiply' });
    }
}