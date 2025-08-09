import { createRoot } from 'react-dom/client';
import { Provider } from 'react-redux';

import { App } from './app';
import { store } from './store';

// 리덕스 스토어를 모든 컴포넌트들이 사용할 수 있게 만들기
const rooElement = document.getElementById('root');
const root = createRoot(rooElement);

root.render(
    <Provider store={store}>
        <App />
    </Provider>
)