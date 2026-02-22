import { useEffect, useState } from "react";
import { IntlProvider } from "react-intl";

export function App() {
    // 초기 상태의 기본 로케일 설정
    const [locale, setLocale] = useState('en');
    const [messages, setMessages] = useState({});

    /*
        useEffect 훅을 사용해 locale의 상태 변화를 감시한다.
        locale이 변경되면 useEffect 훅은 import() 함수를 사용해 동적으로 필요한 번역 파일을 임포트하도록 돕는다.
        import() 함수는 프로미스이므로 해당 번역 파일이 성공적으로 임포트되면 then()을 사용해 messages 상태를 업데이트할 수 있다
    */
    useEffect(() => {
        // 로케일에 기반해 필요한 번역 파일을 동적으로 임포트한다
        const loadMessages = async () => {
            switch (locale) {
                case "en":
                    await import("./locales/en.json").then((m) => setMessages(m.default))
                    break;
                case "fr":
                    await import("./locales/fr.json").then((m) => setMessages(m.default))
                    break;
                    // 기타 로케일
            }
        }
        loadMessages()
    }, [locale]);

    return (
        <IntlProvider locale={locale} messages={messages}>
            {/* ... */}
        </IntlProvider >
    )
}