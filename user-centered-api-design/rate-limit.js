// Node.js에서의 레이트 제한 예시
const rateLimit = require("express-rate-limit");

const apiLimiter = rateLimit({
    windowMs: 15 * 60 * 1000, // 15분
    max: 100, // 각 IP에 대해 windowMs당 요청을 100개로 제한
    message: "Too many requests from this IP, try again later"
});

// 모든 요청에 대해 레이트 제한을 적용한다
app.use(apiLimiter);