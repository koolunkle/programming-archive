/**
 * [타입 별칭, 인터페이스와 특수 타입]
 *
 * 타입 별칭과 인터페이스는 데이터 구조에 이름을 붙인다.
 * `any`, `unknown`, `void`, `never`는 일반 값의 종류보다
 * 타입 검사의 경계와 코드 흐름을 표현한다.
 */

/**
 * [타입 별칭]
 *
 * 타입 별칭은 객체뿐 아니라 원시 타입, 유니온, 튜플 등 모든 타입에 이름을 붙일 수 있다.
 */
type UserId = string;

type User = {
  id: UserId;
  name: string;
};

type Status = "idle" | "loading" | "success" | "error";

/**
 * [인터페이스]
 *
 * 인터페이스는 객체의 구조를 표현하는 데 특화되어 있다.
 * 타입 별칭도 인터섹션으로 확장할 수 있다.
 */
interface Users {
  id: string;
  name: string;
}

interface AdminUser extends Users {
  permissions: string[];
}

type AdminUsers = Users & {
  permissions: string[];
};

/**
 * [무엇을 선택해야 할까?]
 *
 * 팀의 일관성이 가장 중요하다. 일반적으로 다음 기준이 실용적이다.
 *
 * 객체 구조와 확장 중심: `interface`
 * 유니온, 튜플, 타입 조합: `type`
 * 외부에서 선언 병합이 필요함: `interface`
 * 하나의 닫힌 타입 정의를 원함: `type`
 *
 * 인터페이스는 같은 이름으로 다시 선언하면 합쳐진다.
 * 라이브러리 타입 확장에는 유용하지만, 애플리케이션 코드에서는 뜻하지 않은 병합이 될 수 있다.
 */
interface WindowSettings {
  theme: string;
}

interface WindowSettings {
  language: string;
}

const settings: WindowSettings = {
  theme: "dart",
  language: "ko",
};

/**
 * [any]
 *
 * `any`는 검사를 중단시킨다.
 * "어떤 타입이든 가능" 보다 "이 값에 대한 검사를 포기"에 가깝다.
 * 한 번 유입된 `any`는 다른 변수와 함수 반환값으로 퍼진다.
 */
let value: any = { name: "슬기" };

value.notExists.deep.call(); // 타입 오류 없음
const count: number = value; // 타입 오류 없음

/**
 * [unknown]
 *
 * `unknown`은 확인 전까지 사용할 수 없다.
 * `unknown`은 무엇이 들어올지 모르는 외부 데이터에 적합하다.
 * 사용할 때 검사를 강제하므로 불확실성을 코드 밖으로 퍼뜨리지 않는다.
 */
let values: unknown = JSON.parse('{"name":"슬기"}');

// values.name; // 오류

if (
  typeof values == "object" &&
  values !== null &&
  "name" in values &&
  typeof values.name === "string"
) {
  console.log(values.name.toUpperCase());
}

/**
 * [void]
 *
 * `void`는 반환값을 사용하지 않는 함수이다.
 * 콜백 타입에서 `void`는 반환값이 존재하더라도 호출 측이 그 값을 사용하지 않는다는 의미로도 쓰인다.
 */
function logMessage(message: string): void {
  console.log(message);
}

type Callback = () => void;
const fn: Callback = () => 42;

/**
 * [never]
 *
 * `never`는 도달할 수 없는 값이다.
 * 함수가 정상적으로 끝나지 않으면 반환할 값이 존재하지 않는다.
 * 빠짐없는 분기 검사에도 사용한다. 나중에 결제 방식이 추가되면 `default`에서 오류가 발생해 빠진 처리를 알려준다.
 */
type Payment = "card" | "bank";

function pay(method: Payment) {
  switch (method) {
    case "card":
      return "카드 결제";
    case "bank":
      return "계좌 이체";
    default: {
      const impossible: never = method;
      return impossible;
    }
  }
}

/**
 * [핵심 정리]
 *
 * `type`은 모든 타입에 이름을 붙이고, `interface`는 객체 구조와 확장에 강하다.
 * 선택 기준보다 프로젝트 전체의 일관성이 더 중요하다.
 * `any`는 타입 검사를 중단하고 다른 코드로 전파된다.
 * `unknown`은 사용 전에 검사를 강제하므로 외부 데이터 경계에 적합하다.
 * `void`는 반환값을 사용하지 않는 함수, `never`는 도달할 수 없는 코드 흐름을 표현한다.
 */
