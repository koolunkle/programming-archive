const initialState = {
    message: 'Hello  World!',
};

export const messageSlice = createSlice({
    name: "message",
    initialState,
    reducers: {
        changeMessage: (state, action) => {
            state.message = action.payload;
        },
    },
});

export const store = configureStore({
    reducer: messageSlice.reducer,
});