/**
 * [함수 타입, 매개변수와 오버로딩]
 *
 * 함수 타입은 매개변수로 무엇을 받고 어떤 값을 반환하는지 정한 계약이다.
 * 호출부와 구현부가 같은 계약을 공유하면 함수 사용법이 코드에 남는다.
 */

/**
 * [함수 선언의 타입]
 *
 * 매개변수는 추론할 값이 없으므로 타입을 작성한다.
 * 반환 타입은 추론할 수 있지만 공개 함수의 계약을 고정하고 싶다면 명시한다.
 */
function calculateTotal(price: number, quantity: number): number {
  return price * quantity;
}

/**
 * [함수 타입 별칭]
 *
 * 콜백을 여러 곳에서 공유할 때 유용하다.
 */
type Calculator = (a: number, b: number) => number;

const add: Calculator = (a, b) => a + b;
const multiply: Calculator = (a, b) => a * b;

type Predicate<T> = (value: T) => boolean;

function filterValues<T>(values: T[], predicate: Predicate<T>) {
  return values.filter(predicate);
}

/**
 * [선택적 매개변수와 기본값]
 *
 * 선택적 매개변수는 필수 매개변수 뒤에 둔다.
 * 함수 내부에서는 `string | undefined`이다. 기본값이 있으면 호출 시 생략할 수 있다.
 */
function greet(name: string, title?: string) {
  return title ? `${title} ${name}님` : `${name}님`;
}

function createPage(page = 1, size = 20) {
  return { page, size };
}

/**
 * [나머지 매개변수]
 *
 * 고정된 앞부분과 가변 인자를 함께 받을 수 있다.
 */
function sum(...values: number[]) {
  return values.reduce((total, value) => total + value, 0);
}

function log(level: "info" | "error", ...messages: string[]) {
  console.log(level, ...messages);
}

/**
 * [콜백의 반환값]
 *
 * `void` 콜백은 호출 측이 반환값을 사용하지 않는다는 뜻이다.
 * 구현 함수가 값을 반환하는 것 자체를 반드시 금지하는 의미는 아니다.
 */
type Listener = (message: string) => void;

const listener: Listener = (message) => {
  console.log(message);
  return message.length;
};

/**
 * [함수 오버로딩]
 *
 * 입력 타입에 따라 반환 타입이 달라질 때 사용한다.
 * 아래는 호출 시 보이는 오버로드 시그니처이고, 마지막은 실제 구현 시그니처이다.
 * 구현 시그니처는 외부 호출에 직접 사용되지 않는다.
 * 유니온 하나로 충분하다면 오버로딩보다 단순한 방법을 선택한다.
 */
function format(value: number): string;
function format(value: Date): string;
function format(value: number | Date): string {
  if (value instanceof Date) return value.toISOString();
  return value.toLocaleString();
}

function print(value: string | number) {
  console.log(value);
}

/**
 * [this 매개변수]
 *
 * `this` 매개변수는 실제 JavaScript 인자가 아니라 타입 검사에만 사용된다.
 * 메서드를 잘못 분리해 호출하는 실수를 찾는 데 도움이 된다.
 */
type User = {
  name: string;
  greet(this: User): string;
};

const user: User = {
  name: "민수",
  greet() {
    return `안녕하세요, ${this.name}님`;
  },
};

/**
 * [오류 수정 실습]
 *
 * 입력과 반환 관계를 오버로드로 표현한다.
 */
function parse(value: string | number): string | number {
  if (typeof value === "string") return Number(value);
  return String(value);
}

// string | number 이므로 숫자 메서드를 바로 사용할 수 없다.
const result = parse("10");

// function parse(value: string): number;
// function parse(value: number): string;
// function parse(value: string | number): string | number {
//   return typeof value === "string" ? Number(value) : String(value);
// }

/**
 * [핵심 정리]
 *
 * 함수 타입은 입력과 출력의 계약이다.
 * 선택적 매개변수는 함수 내부에서 `undefined` 가능성을 가진다.
 * 콜백의 `void`는 반환값을 사용하지 않는다는 뜻이다.
 * 입력에 따라 반환 타입이 달라질 때 오버로딩이 유용하다.
 * 반환 관계가 단순하면 복잡한 오버로드보다 유니온을 우선한다.
 */
