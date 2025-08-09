import { createContext, useContext } from 'react';

type Theme = 'light' | 'dark';

const ThemeContext = createContext<Theme>("light");

const ThemedComponent = () => {
    const theme = useContext(ThemeContext);

    const style = {
        background: theme === "light" ? "#fff" : "#333",
    };

    return <div style={style}>...</div>
}

export default ThemedComponent;