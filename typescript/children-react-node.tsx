interface Props {
    children: React.ReactNode;
}

const Person = ({ children }: Props) => {
    return <div>{children}</div>
}

export default Person;