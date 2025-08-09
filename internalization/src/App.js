import { FormattedMessage, IntlProvider } from 'react-intl';
import messages_en from './locales/en.json';
import messages_fr from './locales/fr.json';

const messages = {
    en: messages_en,
    fr: messages_fr
};


// IntlProvider로 애플리케이션 감싸기
export default function App() {
    // 기본 로케일
    // const locale = "en";

    // 컴포넌트 상태에 로케일 저장하기
    const [locale, setLocale] = useState('en');

    return (
        <IntlProvider
            locale={locale}
            messages={messages[locale]}
        >
            {/* 애플리케이션의 나머지 부분 */}

            {/* 텍스트와 콘텐츠 렌더링하기 */}
            <FormattedMessage id='greeting' />

            {/*
             버튼을 클릭하면 선택한 로케일로 애플리케이션의 로케일 상태가 업데이트되고
             이는 IntlProvider 컴포넌트가 새로운 로케일값과 데이터를 사용해 다시 렌더링 되도록 트리거한다
             결과적으로 react-intl 라이브러리에 의해 관리되는 애플리케이션의 텍스트가 업데이트된다 
             */}
            <div>
                <button onClick={() => setLocale('en')}>
                    English
                </button>
                <button onClick={() => setLocale('fr')}>
                    Francais
                </button>
            </div>
        </IntlProvider>
    )
}