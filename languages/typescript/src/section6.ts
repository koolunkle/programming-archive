/**
 * [타입 단언, satisfies, 구조적 타이핑과 호환성]
 *
 * 타입 단언은 값을 바꾸거나 검증하지 않는다.
 * 개발자가 컴파일러보다 타입을 더 잘 안다고 주장하는 문법이다.
 * `satisfies`는 값이 타입을 만족하는지 검사하면서 값 자체의 구체적인 추론은 유지한다.
 */

/**
 * [타입 단언의 의미]
 *
 * `input` 코드는 요소가 실제로 있는지 확인하지 않는다.
 * 요소가 없으면 `input`은 런타임에서 `null`이고, 잘못된 요소라면 기대한 속성이 없을 수 있다.
 * 안전한 코드는 `element` 코드와 같이 런타임 검사를 함께 한다.
 */

const input = document.querySelector("#email") as HTMLInputElement;
console.log(input.value);

const element = document.querySelector("#email");
if (element instanceof HTMLInputElement) {
  console.log(element.value);
}

/**
 * [이중 단언]
 *
 * 이중 단언은 거의 모든 검사를 무력화한다.
 * 컴파일은 통과하지만 런타임 오류가 발생한다.
 * 이중 단언은 레거시 코드 연결 등 매우 제한된 상황에서만 임시로 사용하고 제거 계획을 남겨야 한다.
 */
const user = "not user" as unknown as { name: string };
console.log(user.name.toUpperCase());

/**
 * [as const]
 *
 * `as const`는 리터럴 값을 가장 구체적인 타입으로 유지하고 객체와 배열을 읽기 전용으로 만든다.
 * `route.home`은 `string`이 아니라 "/"로 추론된다.
 * `Direction`은 "left" | "right"로 추론된다.
 */
const route = {
  home: "/",
  profile: "/profile",
} as const;

const directions = ["left", "right"] as const;
type Direction = (typeof directions)[number];

/**
 * [satisfies]
 *
 * 타입 주석은 값의 타입을 지정된 타입으로 바꿔 추론 정보를 넓힐 수 있다.
 * `routes.user`는 `string | { path: string }`으로 보일 수 있어 다시 좁혀야 한다.
 * `satisfies`는 형태를 검사하면서 각 속성의 구체적인 타입을 유지한다. (또한 오타도 검사한다.)
 */
type RouteMap = Record<string, string | { path: string }>;

// const routes = {
//   home: "/",
//   user: { path: "/users/:id" },
// };

const routes = {
  home: "/",
  user: { path: "/users/:id" },
} satisfies RouteMap;

console.log(routes.user.path);

const config = {
  apiUrl: "/api",
  timeout: 3000,
} satisfies {
  apiUrl: string;
  timeout: number;
};

/**
 * [구조적 타이핑]
 *
 * TypeScript는 이름보다 구조를 비교한다.
 * 클래스도 private/protected 멤버가 없다면 구조가 중심이다.
 */
type Point = { x: number; y: number };
type Position = { x: number; y: number };

const point: Point = { x: 1, y: 2 };
const position: Position = point; // 구조가 같아 허용

/**
 * [추가 속성과 호환성]
 *
 * 필요한 `name`이 있으므로 호환된다.
 * 하지만 객체 리터럴을 직접 대입하면 오타 방지를 위한 초과 프로퍼티 검사가 동작한다.
 */
type Named = { name: string };

const users = {
  name: "슬기",
  age: 20,
};

// const named: Named = users;
const named: Named = {
  name: "슬기",
  // age: 20, // 직접 대입에서는 오류
};

/**
 * [함수 타입의 호환성]
 *
 * 반환값은 더 구체적인 값을 반환해도 된다.
 * 콜백 매개변수는 호출하는 쪽이 어떤 값을 넘길지 기준으로 판단해야 한다.
 * 더 좁은 타입만 처리하는 함수를 넓은 입력 자리에 넣으면 런타임 문제가 생길 수 있다.
 */
type CreateNamed = () => { name: string };

const createUser = () => ({ name: "슬기", age: 20 });
const createNamed: CreateNamed = createUser;

/**
 * [오류 수정 실습]
 *
 * 단언은 오타를 숨긴다. `satisfies`로 바꾸면 값을 검증할 수 있다.
 */
type AppConfig = {
  mode: "development" | "production";
  port: number;
};

// const configs = {
//   mode: "developmnt",
//   port: 3000,
// } as AppConfig;

const configs = {
  mode: "development",
  port: 3000,
} satisfies AppConfig;

/**
 * [핵심 정리]
 *
 * 타입 단언은 런타임 검증이 아니라 컴파일러 검사 우회이다.
 * 이중 단언은 타입 안전성을 거의 완전히 제거한다.
 * `as const`는 리터럴 보존과 읽기 전용 변환을 함께 수행한다.
 * `satisfies`는 형태를 검사하면서 값의 구체적인 추론을 유지한다.
 * TypeScript는 이름보다 구조를 중심으로 타입 호환성을 판단한다.
 */
