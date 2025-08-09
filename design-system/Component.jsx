import styled from 'styled-component';
import './Component.css';
import { tokens } from './designTokens.js';

function Button() {
    return (
        <button className="button">Click Me</button>
    )
}

/* JS에서의 디자인 토큰 값 임포트하기 */
const Button = styled.button`
    background-color: ${tokens.colorPrimary};
    padding: ${tokens.spacingLarge};
    font-family: ${tokens.fontFamilyDefault};
`;

function App() {
    return <Button>Click Me</Button>;
}