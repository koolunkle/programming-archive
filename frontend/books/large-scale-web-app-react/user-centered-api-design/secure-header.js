// Express.js에서 보안 헤더 설정하기
app.use((req, res, next) => {
    res.setHeader(
        "Content-Security-Policy",
        "default-src 'self';",
    );

    res.setHeader(
        "Content-Type",
        "application/json"
    );

    res.setHeader(
        "Strict-Transport-Security",
        "max-age=63072000; includeSubDomains; preload"
    );

    // ...
    // ...
})