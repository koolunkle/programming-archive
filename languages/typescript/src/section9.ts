/**
 * [제네릭 함수, 타입, 인터페이스와 클래스]
 *
 * 제네릭은 타입을 미리 하나로 고정하지 않고
 * 사용하는 시점의 구체적인 타입 정보를 끝까지 보존하는 문법이다.
 */

/**
 * [any]
 *
 * `any`로 만든 재사용 함수의 문제
 * 재사용은 가능하지만 입력과 출력의 관계가 사라진다.
 */
function first(values: any[]): any {
  return values[0];
}

const value = first([1, 2, 3]);
value.toUpperCase();

/**
 * [제네릭 함수]
 *
 * `T`는 호출 시 결정되는 타입 매개변수이다.
 * 배열 요소 타입이 반환 타입까지 이어진다.
 */
function firsts<T>(values: T[]): T | undefined {
  return values[0];
}

// number | undefined
const numberValue = first([1, 2, 3]);

// string | undefined
const textValue = first(["a", "b", "c"]);

/**
 * [타입 인수와 추론]
 *
 * 대부분은 TypeScript가 타입 인수를 추론한다.
 * 필요하면 직접 지정하며, 추론 가능한 곳에 타입 인수를 반복해서 적을 필요는 없다.
 */

// `T`는 boolean
const result = firsts([true, false]);

// string | undefined
const empty = firsts<string>([]);

/**
 * [여러 타입 매개변수]
 *
 * 타입 매개변수가 많아질수록 관계를 이해하기 어려워진다.
 * 실제로 독립된 타입 정보가 필요할 때만 추가한다.
 */
function pair<K, V>(key: K, value: V): [K, V] {
  return [key, value];
}

// [string, number]
const entry = pair("age", 20);

/**
 * [제네릭 타입 별칭과 인터페이스]
 *
 * 컨테이너 구조는 같고 내부 데이터만 달라질 때 제네릭이 중복을 줄인다.
 */
type ApiResponse<T> = {
  data: T;
  status: number;
};

interface Page<T> {
  items: T[];
  page: number;
  total: number;
}

type User = { id: string; name: string };

const response: ApiResponse<User> = {
  data: { id: "u-1", name: "슬기" },
  status: 200,
};

// 제네릭 클래스
class Stack<T> {
  #items: T[] = [];

  push(item: T) {
    this.#items.push(item);
  }

  pop(): T | undefined {
    return this.#items.pop();
  }

  get size() {
    return this.#items.length;
  }
}

const numberStack = new Stack<number>();
numberStack.push(10);
// numberStack.push("10");

/**
 * [제네릭 기본값]
 *
 * 오류 타입을 생략하면 `Error`, 필요할 때만 다른 타입을 지정한다.
 */
type Result<T, E = Error> = { ok: true; value: T } | { ok: false; error: E };

type ValidationError = { field: string; message: string };

type UserResult = Result<User, ValidationError>;

/**
 * [핵심 정리]
 *
 * 제네릭은 재사용하면서 구체적인 타입 정보를 보존한다.
 * `any`는 관계를 없애지만 제네릭은 입력과 출력의 관계를 표현한다.
 * 대부분의 타입 인수는 호출 인자로부터 추론된다.
 * 컨테이너, 응답, 자료구조처럼 내부 타입만 달라지는 구조에 적합하다.
 * 타입 매개변수는 많을수록 좋은 것이 아니라 의미 있는 관계가 있을 때만 사용한다.
 */
