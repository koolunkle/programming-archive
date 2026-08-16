/**
 * [배열, 튜플, 객체 타입]
 *
 * 배열은 같은 종류의 값을 모으고, 튜플은 위치마다 의미가 정해진 값을 묶는다.
 * 객체 타입은 이름이 있는 속성으로 데이터의 구조를 설명한다.
 */
const scores: number[] = [90, 85, 100];
const names: Array<string> = ["민수", "지수"];
const values: (string | number)[] = [1, "two", 3];

/**
 * [읽기 전용 배열]
 *
 * 읽기 전용 배열은 변경 가능한 배열을 받을 수 있지만, 함수 내부에서는 수정할 수 없다.
 * 입력 데이터를 함부로 바꾸지 않는 함수에 적합하다.
 */
function sum(values: readonly number[]) {
  // values.push(10);
  return values.reduce((acc, value) => acc + value, 0);
}

/**
 * [튜플]
 *
 * 라벨은 설명을 돕지만 런타임 속성 이름이 되지는 않는다.
 * 반환값을 짧게 묶거나 고정된 형식의 데이터를 표현할 때 유용하다.
 *
 * 라벨은 설명을 돕지만 런타임 속성 이름이 되지는 않는다. 여전히 `userEntry[0]`처럼 접근한다.
 * 항목이 많아지거나 위치의 의미가 자주 헷갈리면 객체가 더 낫다.
 */
const point: [number, number] = [127.0, 37.5];
const userEntry: [id: number, name: string] = [1, "koolunkle"];

console.log(userEntry[0]);

function parseName(fullName: string): [first: string, last: string] {
  const [first = "", last = ""] = fullName.split(" ");
  return [first, last];
}

/**
 * [객체 타입]
 *
 * 객체 타입은 속성 이름, 값의 타입, 필수 여부를 계약으로 만든다.
 */
type Product = {
  id: number;
  name: string;
  price: number;
};

const keyboard: Product = {
  id: 1,
  name: "기계식 키보드",
  price: 89000,
};

/**
 * [선택적 속성과 undefined]
 *
 * `nickname?`은 속성이 없을 수 있다는 뜻이다. 따라서 읽을 때 `string | undefined`로 다뤄야 한다.
 * `nickname?: string`과 `nickname: string | undefined`는 비슷해 보이지만 다르다.
 * 전자는 속성 자체를 생략할 수 있고, 후자는 속성을 반드시 적되 값으로 `undefined`를 줄 수 있다.
 */
type User = {
  id: number;
  name: string;
  nickname?: string;
  // nickname: string | undefined;
};

function printNickname(user: User) {
  console.log(user.nickname?.toUpperCase() ?? "닉네임 없음");
}

/**
 * [readonly]
 *
 * `readonly`는 TypeScript 검사 규칙이며 런타임에서 객체를 얼리지 않는다.
 * 실제 변경까지 막으려면 `Object.freeze` 같은 런타임 방법이 필요하다.
 */
type Order = {
  readonly id: string;
  status: "pending" | "paid";
};

const order: Order = {
  id: "order-1",
  status: "pending",
};

// order.id = "order-2";
order.status = "paid";

/**
 * [인덱스 시그니처]
 *
 * 키의 이름을 미리 알 수 없지만 값 타입은 일정할 때 사용한다.
 * 지나치게 넓은 인덱스 시그니처는 오타도 정상 키로 받아들인다.
 * 키 목록이 정해져 있다면 리터럴 유니온과 `Record`를 사용하는 편이 안전하다.
 */
type Scores = {
  [studentName: string]: number;
};

const scoress: Scores = {
  민수: 90,
  지수: 95,
};

type Config = {
  [key: string]: string;
};

const config: Config = {
  apiUrl: "/api",
  timeout: "3000",
};

/**
 * [초과 프로퍼티 검사]
 *
 * 객체 리터럴을 직접 대입할 때 TypeScript는 오타 가능성을 확인한다.
 * 변수에 먼저 담으면 구조적 호환성 규칙이 적용되어 추가 속성이 허용될 수 있다.
 * 이는 추가 속성이 객체에서 사라진다는 뜻이 아니다.
 * 타입을 통해 접근할 수 없을 뿐 런타임 객체에는 남아 있다.
 */
type Profile = {
  name: string;
  age: number;
};

// const profile: Profile = {
//   name: "슬기",
//   age: 20,
//   // email: "a@example.com" // 알려지지 않은 속성
// };

const data = { name: "슬기", age: 20, email: "a@example.com" };
const profile: Profile = data; // 필요한 속성이 있으므로 허용

/**
 * [핵심 정리]
 *
 * 배열은 같은 종류의 목록, 튜플은 위치마다 의미가 있는 고정 구조에 사용한다.
 * 입력 배열을 읽기만 한다면 `readonly`로 변경을 막는다.
 * 선택적 속성은 읽을 때 `undefined` 가능성을 처리해야 한다.
 * `readonly`는 컴파일 단게 규칙이지 런타임 불변성을 보장하지 않는다.
 * 키가 정해져 있다면 넓은 인덱스 시그니처보다 구체적인 키 유니온이 안전하다.
 */
