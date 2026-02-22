// 문자열 이늄

// enum Status {
//     IDLE = 'IDLE',
//     LOADING = 'LOADING',
//     SUCCESS = 'SUCCESS',
//     ERROR = 'ERROR'
// }

const Status = {
    IDLE: 'IDLE',
    LOADING: 'LOADING',
    SUCCESS: 'SUCCESS',
    ERROR: 'ERROR'
} as const;

// 사용법
// currentStatus는 값 LOADING을 갖는다
const currentStatus = Status.LOADING;