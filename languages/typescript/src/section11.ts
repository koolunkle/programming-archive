/**
 * [keyof, typeof, 인덱스드 액세스 타입]
 *
 * `keyof`, 타입 위치의 `typeof`, 인덱스드 액세스 타입은 이미 존재하는 값과 타입에서 필요한 정보를 다시 추출한다.
 * 같은 구조를 두 번 작성하지 않게 해주는 도구이다.
 */

/**
 * [keyof]
 *
 * 객체 타입의 키를 문자열 리터럴 유니온으로 만든다.
 * 다만 반환 타입은 `string | number`이다.
 * 선택한 키와 반환 타입의 관계를 유지하려면 제네릭을 사용한다.
 */
type User = {
  id: string;
  name: string;
  age: number;
};

type UserKey = keyof User; // "id" | "name" | "age"

function getUserValue(user: User, key: keyof User) {
  return user[key];
}

function getValue<T, K extends keyof T>(object: T, key: K): T[K] {
  return object[key];
}

/**
 * [타입 위치의 typeof]
 *
 * JavaScript의 `typeof`는 런타임 문자열을 반환하지만
 * 타입 위치에서는 값의 정적 타입을 가져온다.
 *
 * 값과 타입을 따로 작성해 서로 어긋나는 문제를 줄인다.
 * `typeof`는 타입 위치에서 식 전체를 자유롭게 실행하지 않는다.
 * 주로 변수나 속성처럼 이름이 있는 값에 사용한다.
 */
const defaultConfig = {
  theme: "dark",
  pageSize: 20,
  showSidebar: true,
};

type Config = typeof defaultConfig;

/**
 * [인덱스드 액세스 타입]
 *
 * `Pick`은 메서드나 함수가 아니라 타입을 만드는 도구이다.
 * 첫 번째 인수 `T`: 기준이 되는 객체 타입
 * 두 번째 인수 `K`: 선택할 키의 유니온
 * 결과: 선택한 키와 해당 값 타입으로 구성된 새 객체 타입
 *
 * `Pick`을 포함한 `Omit`, `Partial`, `Required`, `Record` 등의 표준 유틸리티 타입이 있다.
 */
type UserName = User["name"]; // string
type UserValue = User[keyof User]; // string | number

// 여러 키를 한 번에 조회할 수 있다.
type PublicUserValue = User["id" | "name"]; // string

// 위 결과는 객체 타입이 아니라 선택한 속성 "값 타입의 유니온"이다.
// 일부 속성을 가진 객체 타입이 필요하면 내장 유틸리티 타입 `Pick<T, K>`를 사용한다.
type PublicUser = Pick<User, "id" | "name">;

// 다음 타입과 같은 의미이다.
type PublicUserResult = {
  id: string;
  name: string;
};

const user: PublicUser = {
  id: "u-1",
  name: "슬기",
};

/**
 * [배열 요소 타입 추출]
 *
 * 배열 타입에 `[number]`로 접근하면 요소 타입을 얻는다.
 */
const products = [
  { id: "p-1", name: "키보드", price: 89000 },
  { id: "p-2", name: "마우스", price: 49000 },
];

type Product = (typeof products)[number];

/**
 * [상수 배열에서 유니온 만들기]
 *
 * 실행에 사용하는 목록과 타입의 허용 값이 같은 원천을 공유한다.
 */
const roles = ["user", "admin", "manager"] as const;
type Role = (typeof roles)[number];

function isRole(value: string): value is Role {
  return (roles as readonly string[]).includes(value);
}

/**
 * [중첩 타입 조회]
 *
 * 긴 타입을 다시 복사하지 않고 필요한 부분만 정확히 꺼낸다.
 */
type ApiResponse = {
  data: {
    users: Array<{
      id: string;
      profile: {
        nickname: string;
      };
    }>;
  };
};

type ApiUser = ApiResponse["data"]["users"][number];
type Nickname = ApiUser["profile"]["nickname"];

/**
 * [오류 수정 실습]
 *
 * 값 목록과 타입을 따로 관리할 때 오타가 생길 수 있다.
 * 한 원천에서 값과 타입을 함께 파생하면 변경 누락이 줄어든다.
 */
const statuses = ["idle", "loading", "success"] as const;

// type Status = "idle" | "loading" | "sucess";
type Status = (typeof statuses)[number];

/**
 * [핵심 정리]
 *
 * `keyof T`는 객체 타입의 키 유니온을 만든다.
 * 타입 위치의 `typeof`는 값의 정적 타입을 가져온다.
 * `T[K]`는 특정 속성의 값 타입을 조회한다.
 * 배열 타입의 `[number]`는 요소 타입을 추출한다.
 * 값과 타입을 같은 원천에서 파생하면 중복과 변경 누락을 줄일 수 있다.
 */
