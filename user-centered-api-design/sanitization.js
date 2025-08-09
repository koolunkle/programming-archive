// Node.js에서의 입력 검증 예시
app.post("/register", (req, res) => {
    const { username, email, password } = req.body;

    // 입력 검증
    if (
        !isValidUsername(username) ||
        !isValidEmail(email) ||
        !isStrongPassword(password)
    ) {
        return res
            .status(400)
            .json({ error: "Invalid input" });
    }

    // 입력 안정성 검사
    const sanitizedUsername = sanitizedString(username);
    const sanitizedEmail = sanitizedString(email);

    // 사용자 등록 진행
    // ...
})

function isValidUsername(usernaem) {
    return /^[a-zA-Z0-9_]{3,20}$/.test(usernaem);
}

function isValidEmail(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isStrongPassword(password) {
    return (
        password.length >= 8 &&
        /[A-Z]/.test(password) &&
        /[a-z]/.test(password) &&
        /[0-9]/.test(password)
    )
}