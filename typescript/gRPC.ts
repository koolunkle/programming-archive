// gRPC 서비스 정의로부터 자동 생성된 타입스크립트 타입

export interface User {
    id: string;
    name: string;
}

export interface GetUserRequest {
    id: string;
}

export interface CreateUserRequest {
    name: string;
}

// GetUserRequest와 CreateUserRequest에 대한 유사한 암호화(encode)/복호화(decode) 함수들...