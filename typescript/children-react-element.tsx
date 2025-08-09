interface Props {
    children: React.ReactElement;
}

const Person = ({ children }: Props) => {
    return <div>{children}</div>
}

export default Person;