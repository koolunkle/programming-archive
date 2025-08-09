function Button(props) {
    const { children, primary, ...rest } = props;
    return (
        <button
            className={`button ${primary ? "button--primary" : ""}`}
            {...rest}
        >
            {children}
        </button>
    )
}
export default Button;