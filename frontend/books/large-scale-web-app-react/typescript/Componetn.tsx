import useToggle from "./useToggle";

// 올바르지 않은 useToggle 훅의 사용
// const Component: React.FC = () => {
//     const [isOn, setToggle] = useToggle("wrong type")
//     return (
//         <div></div>
//     )
// }

// 올바른 useToggle 훅의 사용 
const Component: React.FC = () => {
    const [isOn, toggle] = useToggle(false)
    return (
        <div></div>
    )
}

export default Component;