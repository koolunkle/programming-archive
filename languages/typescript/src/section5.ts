/**
 * [타입 좁히기와 판별 유니온]
 *
 * 타입 좁히기는 넓은 타입에 런타임 검사를 추가해
 * 현재 분기에서 가능한 타입을 더 구체적으로 만드는 과정이다.
 */

/**
 * [typeof로 원시 타입 좁히기]
 *
 * 검사문 안에서 `value`는 `string`, 그 밖에서는 `number`로 좁혀진다.
 * `typeof null`은 JavaScript 역사상 이유로 `object`이다.
 * 객체 검사에는 null 확인을 함께 사용한다.
 */
function format(value: string | number) {
  if (typeof value === "string") {
    return value.trim();
  }

  return value.toFixed(2);
}

/**
 * [instanceof와 in]
 *
 * `instanceof`는 클래스 인스턴스를 구분한다.
 * `in`은 객체에 속성이 존재하는지 확인한다.
 */
function printDate(value: Date | string) {
  if (value instanceof Date) {
    console.log(value.toISOString());
  } else {
    console.log(value);
  }
}

type Cat = { meow(): void };
type Dog = { bark(): void };

function speak(animal: Cat | Dog) {
  if ("meow" in animal) animal.meow();
  else animal.bark();
}

/**
 * [진실성 검사의 함정]
 *
 * `0`은 유효한 숫자지만 거짓으로 평가된다. (빈 문자열과 `false`도 동일)
 * 값의 존재 여부가 목적이라면 명시적으로 검사한다.
 */
function printCount(count: number | null) {
  if (count) {
    console.log(count);
  }
  //   if (count !== null) {
  //     console.log(count); // 0도 처리
  //   }
}

/**
 * [사용자 정의 타입 가드]
 *
 * 검사 로직을 재사용하려면 반환 타입에 `value is Type`을 작성한다.
 * 타입 가드의 구현이 거짓말을 하면 TypeScript는 알아낼 수 없다.
 * 반환 타입만 맞추고 실제 검사를 빼먹지 않아야 한다.
 */
type User = {
  id: string;
  name: string;
};

function isUser(value: unknown): value is User {
  return (
    typeof value === "object" &&
    value !== null &&
    "id" in value &&
    typeof value.id === "string" &&
    "name" in value &&
    typeof value.name === "string"
  );
}

const data: unknown = JSON.parse('{"id":"u-1", "name": "슬기"}');

if (isUser(data)) {
  console.log(data.name);
}

/**
 * [판별 유니온]
 *
 * 각 객체에 공통 리터럴 속성을 두면 가장 안정적으로 좁힐 수 있다.
 * `kind`를 판별자라고 한다. 상태, 이벤트, API 결과를 모델링할 때 유용하다.
 */
type Shape =
  | { kind: "circle"; radius: number }
  | { kind: "rectangle"; width: number; height: number };

function area(shape: Shape) {
  if (shape.kind === "circle") {
    return Math.PI * shape.radius ** 2;
  }

  return shape.width * shape.height;
}

/**
 * [빠짐없는 분기 검사]
 *
 * 새 타입을 추가하고 분기를 빼먹으면 `never` 대입에서 오류가 발생한다.
 */
type Notification =
  | { type: "email"; address: string }
  | { type: "sms"; phone: string };

function send(notification: Notification) {
  switch (notification.type) {
    case "email":
      return `메일: ${notification.address}`;
    case "sms":
      return `문자: ${notification.phone}`;
    default: {
      const exhaustive: never = notification;
      return exhaustive;
    }
  }
}

/**
 * [오류 수정 실습]
 *
 * `status`와 다른 속성 사이에 관계가 없으므로 `data`는 선택적이다.
 * 객체 유니온으로 관계를 만든다.
 */
type LoadState = {
  status: "loading" | "success" | "error";
  data?: string[];
  message?: string;
};

function render(state: LoadState) {
  if (state.status === "success") {
    return state.data?.join(", ");
  }
}

type LoadStates =
  | { status: "loading" }
  | { status: "success"; data: string[] }
  | { status: "error"; message: string };

/**
 * [핵심 정리]
 *
 * 타입 좁히기는 런타임 검사 결과를 타입 검사에 반영한다.
 * `typeof`, `instanceof`, `in`, 동등성 검사를 상황에 맞게 사용한다.
 * 진실성 검사는 `0`, 빈 문자열, `false`를 놓칠 수 있다.
 * 사용자 정의 타입 가드는 실제 검사 구현이 정확해야 한다.
 * 상태별 데이터가 다르면 판별 유니온과 `never` 검사가 가장 안전하다.
 */

/**
 * [타입 가드 주의사항]
 *
 * `value is User`라는 반환 타입은 구현을 자동 검증하지 않는다.
 * 검사 없이 `return true`를 작성해도 TypeScript는 믿는다.
 * 타입 가드는 외부 데이터와 도메인 타입 사이의 보안 검색대이므로 테스트가 필요하다.
 */
