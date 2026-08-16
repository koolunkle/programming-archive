/**
 * [제네릭 제약 조건과 재사용 가능한 타입 설계]
 *
 * 제네릭 제약 조건은 아무 타입이나 받는 재사용 코드에 필요한 최소 능력을 요구한다.
 * 핵심은 타입을 좁히는 것이 아니라 타입 사이의 관계를 표현하는 것이다.
 */

/**
 * [길이가 있는 값만 받기]
 *
 * 문자열, 배열 등 `length`가 있는 값은 받을 수 있고 숫자는 거부된다.
 */

// function printLength<T>(value: T) {
//   // value.length; // T에 length가 있다는 보장이 없음
// }

type HasLength = {
  length: number;
};

function printLength<T extends HasLength>(value: T) {
  console.log(value.length);
  return value;
}

/**
 * [keyof로 키 관계 표현하기]
 *
 * `K`는 단순 문자열이 아니라 `T`에 실제로 존재하는 키이다.
 * 반환 타입 `T[K]`는 선택한 키에 맞춰 달라진다.
 */
function getProperty<T, K extends keyof T>(object: T, key: K): T[K] {
  return object[key];
}

const user = {
  id: "u-1",
  name: "슬기",
  age: 20,
};

// string
const name = getProperty(user, "name");

// number
const age = getProperty(user, "age");

// getProperty(user, "email");

/**
 * [객체 병합의 관계]
 *
 * 숫자나 문자열을 객체처럼 병합하는 실수를 제약 조건으로 막는다.
 */
function merge<T extends object, U extends object>(left: T, right: U): T & U {
  return { ...left, ...right };
}

const result = merge({ name: "슬기" }, { age: 20 });

/**
 * [식별자를 가진 엔티티]
 *
 * 함수는 구체적인 엔티티의 추가 속성을 잃지 않으면서 `id`가 있다는 사실만 요구한다.
 */
type Entity = {
  id: string;
};

function findById<T extends Entity>(
  items: readonly T[],
  id: T["id"],
): T | undefined {
  return items.find((item) => item.id === id);
}

/**
 * [기본 타입 매개변수]
 *
 * 자주 사용하는 타입은 기본값으로 두고 특별한 경우에만 바꾼다.
 */
type Paginated<T, Meta = { page: number; total: number }> = {
  items: T[];
  meta: Meta;
};

/**
 * [제네릭이 필요 없는 경우]
 *
 * `T`가 다른 위치와 관계를 만들지 않는다.
 * 입력 타입을 그대로 반환하거나 다른 매개변수와 연결하는 등
 * 타입 관계가 없다면 제네릭의 이점이 적다.
 */

// function log<T>(value: T): void {
//   console.log(value);
// }

function log(value: unknown): void {
  console.log(value);
}

/**
 * [유니온과 오버로딩과의 선택]
 *
 * 입력 종류만 제한하고 반환이 같음: 유니온
 * 입력에 따라 반환이 달라짐: 오버로딩 고려
 * 호출 시 구체 타입을 보존해야 함: 제네릭
 *
 * `string | number`를 사용하면 반환 타입도 항상 유니온이지만
 * 제네릭은 호출별 구체 타입을 유지한다.
 */
function identity<T>(value: T): T {
  return value;
}

/**
 * [오류 수정 실습]
 *
 * `object`에는 임의 문자열 인덱스가 없고 키가 실제로 존재하는지도 모른다.
 * 아래 함수는 키의 존재와 반환 타입이 함께 보장된다.
 */
// function pick(object: object, key: string) {
//   return object[key];
// }

function pick<T, K extends keyof T>(object: T, key: K): T[K] {
  return object[key];
}

/**
 * [핵심 정리]
 *
 * `extends`는 제네릭 값이 가져야 할 최소 구조를 제한한다.
 * `K extends keyof T`는 객체와 키 사이의 관계를 표현한다.
 * 제약 조건을 사용해도 호출자의 구체 타입 정보는 유지된다.
 * 제네릭은 타입 관계가 있을 때 가치가 있다.
 * 단순히 모든 값을 받기 위한 제네릭은 `unknown`보다 복잡할 수 있다.
 */
