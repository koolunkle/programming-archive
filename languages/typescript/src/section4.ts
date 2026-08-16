/**
 * [유니온과 인터섹션]
 *
 * 유니온은 여러 타입 중 하나를, 인터섹션은 여러 타입의 조건을 동시에 만족하는 값을 표현한다.
 * 둘을 정확히 구분하면 잘못된 상태를 타입 단계에서 제거할 수 있다.
 */

/**
 * [유니온 타입]
 *
 * `Id`는 문자열 또는 숫자이다. 두 타입에 공통으로 존재하는 기능만 바로 사용할 수 있다.
 * 유니온은 "둘 다"가 아니라 "둘 중 하나"이다.
 */
type Id = string | number;

function printId(id: Id) {
  console.log(id);
}

function normalizeId(id: Id) {
  // return id.toUpperCase(); // number에는 없음
  return String(id).toUpperCase();
}

/**
 * [리터럴 유니온으로 상태 제한]
 *
 * 문자열을 그대로 받는 것보다 자동 완성과 오타 검사가 가능해진다.
 */
type OrderStatus = "pending" | "paid" | "shipped" | "cancelled";

function updateStatus(status: OrderStatus) {}

updateStatus("paid");
// updateStatus("done")

/**
 * [인터섹션 타입]
 *
 * `StoredProduct`는 두 타입의 모든 속성을 가져야 한다.
 */
type Timestamped = {
  createdAt: Date;
  updatedAt: Date;
};

type Product = {
  id: string;
  name: string;
};

type StoredProduct = Product & Timestamped;

const product: StoredProduct = {
  id: "p-1",
  name: "키보드",
  createdAt: new Date(),
  updatedAt: new Date(),
};

/**
 * [원시 타입의 불가능한 인터섹션]
 *
 * 하나의 값이 동시에 문자열이면서 숫자일 수 없으므로 가능한 값이 없다.
 * 객체 인터섹션에서도 같은 속성의 타입이 충돌하면 문제가 생긴다.
 */
type Impossible = string & number; // never

type A = { id: string };
type B = { id: number };
type Broken = A & B; // id는 string & number, 즉 never

/**
 * [잘못된 상태를 허용하는 객체]
 *
 * `RequestState`은 `state`와 같은 모순을 허용한다.
 * 로딩 중이면서 성공 데이터와 오류를 모두 갖는 상태는 의미가 없다.
 * 상태별 객체를 유니온으로 분리한다. 이를 통해 각 상태에서 필요한 데이터만 허용된다.
 */
type RequestState = {
  loading: boolean;
  data?: string[];
  error?: string;
};

const state: RequestState = {
  loading: true,
  data: ["완료된 데이터"],
  error: "실패",
};

type RequestStates =
  | { status: "loading" }
  | { status: "success"; data: string[] }
  | { status: "error"; error: string };

/**
 * [유니온과 인터섹션을 함께 사용하기]
 *
 * 모든 이벤트가 공유하는 속성은 인터섹션으로,
 * 이벤트마다 달라지는 부분은 유니온으로 표현할 수 있다.
 */
type BaseEvent = {
  id: string;
  createdAt: Date;
};

type UserEvent = BaseEvent &
  (
    | { type: "login"; userId: string }
    | { type: "logout"; userId: string }
    | { type: "purchase"; userId: string; amount: number }
  );

/**
 * [오류 수정 실습]
 *
 * 성공했는데 영수증이 없거나, 실패했는데 오류 메시지가 없는 상태를 허용한다.
 * 타입 설계의 핵심은 가능한 값만 표현하는 것이 아니라 "불가능한 상태를 표현할 수 없게 만드는 것"이다.
 */
type PaymentResult = {
  success: boolean;
  receiptId?: string;
  errorMessage?: string;
};

type PaymentResults =
  | { success: true; receiptId: string }
  | { success: false; errorMessage: string };

/**
 * [핵심 정리]
 *
 * 유니온은 여러 가능성 중 하나를 표현한다.
 * 인터섹션은 여러 요구 조건을 동시에 결합한다.
 * 객체의 상태를 선택적 속성으로 뭉개면 모순된 조합이 허용될 수 있다.
 * 상태마다 필요한 데이터가 다르면 객체 유니온으로 분리한다.
 * 좋은 타입은 올바른 값만 허용하고 잘못된 상태를 만들기 어렵게 한다.
 */
