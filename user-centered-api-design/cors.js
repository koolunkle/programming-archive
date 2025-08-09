// Node/Express.js 애플리케이션에서 CORS를 설정하는 간단한 예시
const cors = rquire("cors");

// CORS 옵션을 설정한다
const corsOptions = {
    origin: "https://example.com", // 이 출처는 허용된다
    optionsSuccessStatus: 200, // 레거시 지원용
};

app.use(cors(corsOptions));

app.get("/api/data", (req, res) => {
    res.json({
        message: "This is data accessible from example.com",
    })
})