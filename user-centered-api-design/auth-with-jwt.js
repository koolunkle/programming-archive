/*
    [Node.js에서 JWT를 사용한 간단한 인증 엔드포인트]
     - 요청이 인증된 뒤에는 허가를 구현해 사용자 혹은 서비스가 액션을 수행하거나
     - 데이터에 접근할 수 있는 올바른 권한을 가지고 있음을 보장할 수 있다
*/
// 인증 엔드포인트
app.post("/login", (req, res) => {
    const { username, password } = req.body;

    // 사용자가 존재하고, 비밀번호가 올바른지 확인한다
    const user = findUserByUsername(username);
    if (
        user &&
        verifyPassword(
            password,
            user.hashedPassword,
        )
    ) {
        // JWT 토큰을 생성하고 전송한다
        const token = generateJWT(user);
        res.json({ token });
    } else {
        res
            .status(401)
            .json({ erorr: "Invalid credentials" })
    }
})

/*
    [Node.js에서 허가를 사용해 보호된 라우트]

    - 아래 코드 예시는 인증과 허가를 모두 요구하는 보호된 라우트를 보여준다
    - authenticationToken() 미들웨어는 가장 먼저 요청 헤더에서 JWT 토큰을 검증한다
    - 해당 토큰이 유효하면 사용자 정보를 요청 객체에 추가한다
    - 다음으로 라우트 핸들러는 사용자의 역할(users.role)을 확인하고 보호된 리소스에 접근하는 데 필요한 권한을 갖고 있는지 결정한다
    - 사용자가 관리자(admin)이면 접근 권한을 받는다
    - 그렇지 않으면 충분한 권한을 갖고 있지 않다는 에러 메시를 받는다
*/

// 허가를 필요로 하는 보호된 라우트
app.get(
    "/api/protected-resource",
    authenticateToken,
    (req, res) => {
        // 사용자가 필요한 권한을 갖고 있는지 확인
        if (req.user.role === "admin") {
            // 보호된 리소스에 대한 접근을 제공
            res.json({
                data: "This is sensitive information"
            });
        } else {
            res
                .status(403)
                .json({
                    error: "Insufficient permissions"
                })
        }
    }
)

// JWT 토큰을 인증하기 위한 미들웨어
function authenticateToken(req, res, next) {
    const token = req.headers["authorization"];

    if (!token)
        return res
            .status(401)
            .json({ error: "No token provided" })

    verifyJWT(token, (err, user) => {
        if (err)
            return res
                .status(403)
                .json({ error: "Invalid token" })
        req.user = user;
        next();
    })
}