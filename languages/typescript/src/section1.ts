/**
 * [타입 추론, 타입 주석, 원시 타입과 리터럴 타입]
 *
 * TypeScript는 모든 타입을 직접 적게 하는 언어가 아니다.
 * 값과 코드의 흐름을 보고 타입을 추론한다.
 * 개발자는 추론만으로 의도가 충분히 드러나지 않는 경계에 타입을 작성한다.
 */

/**
 * [타입 추론]
 *
 * 편집기는 가까운 타입 정보를 추론한다.
 * 하지만 변수의 변경 가능성에 따라 결과가 달라진다.
 * `role`은 다른 문자열로 바뀔 수 있으므로 string으로 넓어진다.
 * `mode`는 다시 할당할 수 없으므로 보다 구체적인 리터럴 타입으로 유지된다.
 */
let age = 20;
const name = "민수";
const active = true;

let role = "admin";
const mode = "dark";

/**
 * [타입 추론은 함수에서도 유지]
 *
 * 공개 API의 계약을 분명히 하거나 구현 실수로
 * 반환 타입이 변하는 것을 막고 싶다면 반환 타입을 적는다.
 */
function add(a: number, b: number) {
  return a + b;
}

function calculateDiscount(price: number): number {
  if (price >= 100000) return price * 0.1;
  return 0;
}

/**
 * [타입 주석]
 *
 * 분명한 타입은 반복해서 적을 필요가 없다.
 * 타입 주석은 다음 경계에서 유용하다.
 * 1. 함수의 매개변수
 * 2. 외부에서 들어오는 데이터
 * 3. 빈 배열처럼 추론 정보가 부족한 값
 * 4. 변수에 허용할 범위를 의도적으로 넓히거나 좁힐 때
 */
const count: number = 0;
const title: string = "TypeScript";

type Status = "idle" | "loading" | "success" | "error";

let status: Status = "idle";
status = "loading";

/**
 * [원시 타입]
 *
 * String, Number, Boolean 처럼 대문자로 시작하는 래퍼 객체 타입보다 소문자 원시 타입을 사용한다.
 * `strictNullChecks`가 켜져 있으면 `null`과 `undefined`는 다른 값에 자동으로 들어가지 않는다.
 */
const productName: string = "키보드";
const price: number = 89000;
const soldOut: boolean = false;
const bigId: bigint = 9007199254740993n;
const uniqueKey: symbol = Symbol("key");
const empty: null = null;
const missing: undefined = undefined;

const text: string = "hello";
// const text: String = "hello";

let selectedUser: string | null = null;
selectedUser = "김개발";
// selectedUser = undefined;

/**
 * [리터럴 타입]
 *
 * 리터럴 타입은 허용 가능한 값을 고정한다.
 */
type Direction = "left" | "right" | "up" | "down";

type HttpStatus = 200 | 201 | 400 | 404 | 500;

function move(direction: Direction) {
  console.log(direction);
}

move("left");

/**
 * [객체 속성은 왜 넓어질까?]
 *
 * `config` 변수는 재할당되지 않지만 `config.mode`는 변경할 수 있다.
 * 따라서 `mode`는 `dark`가 아니라 `string`으로 추론된다.
 * 이를 위해 `객체 타입을 명시`하거나 `값을 완전히 읽기 전용 리터럴`로 유지한다.
 */

// const config = {
//   mode: "dark",
// };

type Mode = "light" | "dark";

function applyMode(mode: Mode) {}

// applyMode(config.mode);

// 객체 타입 명시
// const config: { mode: Mode } = {
//   mode: "dark",
// };

/**
 * [읽기 전용 리터럴 유지]
 *
 * `as const`는 속성을 읽기 전용으로 만드는 동시에 값을 리터럴 타입으로 좁힌다.
 * 모든 속성이 `readonly`가 되므로, 나중에 수정해야 할 객체까지 단순히 오류를 피하려고 남용하면 안 된다.
 */
const config = {
  mode: "dark",
} as const;

/**
 * [핵심 정리]
 *
 * TypeScript는 값에서 타입을 추론하므로 모든 변수에 타입을 적을 필요가 없다.
 * 타입 주석은 함수 입력, 외부 데이터, 빈 컬렉션 같은 경게에서 중요하다.
 * `let`과 변경 가능한 객체 속성은 타입이 넓어질 수 있다.
 * 리터럴 타입은 허용 가능한 값의 목록을 타입으로 고정한다.
 * `null`과 `undefined`를 일반 값과 구분해야 실제 비어 있는 상황을 안전하게 처리할 수 있다.
 */
