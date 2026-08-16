/**
 * [유틸리티 타입과 실전 타입 변환]
 *
 * 유틸리티 타입은 기존 타입을 다시 작성하지 않고 목적에 맞게 변환하는 TypeScript 표준 도구이다.
 * 실무에서는 복잡한 커스텀 타입보다 `Partial`, `Pick`, `Omit`, `ReturnType` 같은 내장 타입을 정확히 사용하는 일이 더 중요하다.
 */

/**
 * [유틸리티 타입이 필요한 이유]
 *
 * 원본 타입과 비슷한 타입을 별도로 작성하면 속성이 추가될 때 함께 수정해야 한다.
 * 유틸리티 타입을 사용하면 두 타입의 관계를 코드에 남길 수 있다.
 * 유틸리티 타입은 함수나 메서드가 아니다.
 * 타입 검사에만 사용되며 출력되는 JavaScript에는 남지 않는다.
 */
type User = {
  readonly id: string;
  name: string;
  age?: number;
  password: string;
};

// type UpdateUser = {
//   name?: string;
//   age?: number;
//   password?: string;
// };

type UpdateUser = Partial<Omit<User, "id">>;

/**
 * [객체 유틸리티 타입]
 *
 * Partial<T>: 모든 속성을 선택적으로 만들기
 * 일부 값만 받는 수정 입력에 자주 사용한다.
 * `Partial`은 빈 객체도 허용한다. 최소 한 개의 수정 값이 필요하다면
 * 별도의 타입이나 런타임 검사가 필요하다.
 */
type UserDraft = Partial<User>;

// 다음과 비슷한 결과이다.
type UserDraftResult = {
  readonly id?: string;
  name?: string;
  age?: number;
  password?: string;
};

function updateUser(id: string, patch: Partial<User>) {
  // 수정 로직
}

updateUser("u-1", { name: "새 이름" });

/**
 * [객체 유틸리티 타입]
 *
 * Required<T>: 모든 속성을 필수로 만들기
 *
 * 원본에서 선택적이었던 `age?`도 필수가 된다.
 * 설정을 모두 채운 최종 상태처럼 모든 값이 준비되어야 하는 시점에 사용할 수 있다.
 */
type CompleteUser = Required<User>;

const user: CompleteUser = {
  id: "u-1",
  name: "슬기",
  age: 20,
  password: "secret",
};

/**
 * [객체 유틸리티 타입]
 *
 * Readonly<T>: 모든 속성을 읽기 전용으로 만들기
 *
 * `Readonly`는 최상위 속성의 재할당을 막는다.
 * 중첩 객체 내부까지 자동으로 읽기 전용으로 만들지는 않는다.
 */
type ReadonlyUser = Readonly<User>;

type Profile = {
  settings: { theme: string };
};

const profile: Readonly<Profile> = {
  settings: { theme: "dark" },
};

// 중첩 객체 내부는 변경 가능
profile.settings.theme = "light";

/**
 * [객체 유틸리티 타입]
 *
 * Pick<T, K>: 필요한 속성만 선택하기
 *
 * 첫 번째 타입 인수 `T`는 원본 타입이고 두 번째 타입 인수 `K`는 선택할 키이다.
 * 존재하지 않는 키를 선택하면 오류가 발생한다.
 * 선택한 속성의 선택 여부와 `readonly` 같은 특성도 유지된다.
 */
type PublicUser = Pick<User, "id" | "name">;

// 결과
// {readonly id: string, name: string}

// 오류
// type Wrong = Pick<User, "email">;

/**
 * [객체 유틸리티 타입]
 *
 * Omit<T, K>: 지정한 속성을 제외하기
 *
 * `Pick`은 가져올 속성이 적을 때, `Omit`은 제외할 속성이 적을 때 읽기 쉽다.
 * `Omit`은 타입엣 속성을 제외할 뿐, 실제 객체의 `password` 값을 삭제하지 않는다.
 * 외부에 반환할 객체는 런타임에서도 별도로 구성해야 한다.
 */
type UserInput = Omit<User, "id">;

type UserSummary = Omit<User, "password" | "age">;

// 다음 두 결과는 같지만 의도는 다르다.
// 공개할 필드를 명시하려면 `Pick`, 제외할 필드를 명시하려면 `Omit`이 자연스럽다.
type PublicUsers = Pick<User, "id" | "name">;
type UsersSummary = Omit<User, "password" | "age">;

/**
 * [객체 유틸리티 타입]
 *
 * Record<K, V>: 키와 값 타입으로 객체 만들기
 *
 * `Record<K, V>는 `K`의 각 키가 `V` 타입 값을 가지는 객체 타입을 만든다.
 * 키가 리터럴 유니온이면 모든 키가 필요하다.
 */
type Role = "admin" | "member";
type UsersByRole = Record<Role, User[]>;

const usersByRole: UsersByRole = {
  admin: [],
  member: [],
};

// @ts-expect-error member 키가 필요합니다.
const incomplete: UsersByRole = {
  admin: [],
};

/**
 * [유틸리티 타입 조합하기]
 *
 * 안쪽부터 읽는다.
 * `Omit`이 변경하면 안 되는 `id`, `password`를 제외한다.
 * `Partial`이 남은 속성을 선택적으로 만든다.
 * 타입을 여러 겹 쌓아 읽기 어려워지면 중간 타입에 이름을 붙인다.
 * 짧은 타입보다 의도가 분명한 타입이 유지보수하기 쉽다.
 */
// type UpdateUserInput = Partial<Omit<User, "id" | "password">>;

type EditableUser = Omit<User, "id" | "password">;
type UpdateUserInput = Partial<EditableUser>;

/**
 * [유니온 유틸리티 타입]
 *
 * `Exclude<T, U>`: `T`에서 `U`에 해당하는 구성원을 제거
 * `Extract<T, U>`: `T`와 `U`에 공통으로 해당하는 구성원만 선택
 * `NonNullable<T>`: `null`과 `undefined` 제거
 *
 * 해당 타입들은 실제 값을 검사하거나 제거하지 않는다.
 * 기존 유니온에서 새로운 타입을 계산할 뿐이다.
 */
type Status = "idle" | "loading" | "success" | "error";

// "success" | "error"
type Finished = Exclude<Status, "idle" | "loading">;

// "loading"
type LoadingOnly = Extract<Status, "loading" | "pending">;

// string
type Present = NonNullable<string | null | undefined>;

/**
 * [함수 유틸리티 타입]
 *
 * `ReturnType<T>`: 함수의 반환 타입 추출
 * `Parameters<T>`: 함수의 매개변수 타입을 튜플로 추출
 *
 * 함수 시그니처를 별도로 다시 작성하지 않으므로
 * 원본 함수가 바뀌면 파생 타입도 함께 바뀐다.
 */
function createProduct(name: string, price: number) {
  return {
    id: crypto.randomUUID(),
    name,
    price,
  };
}

type Product = ReturnType<typeof createProduct>;
type CreateProductArgs = Parameters<typeof createProduct>;

/**
 * [유틸리티 타입 이해하기]
 *
 * 유틸리티 타입을 사용하기 위해 복잡한 맵드 타입을 직접 작성할 필요는 없다.
 * 다만 오류 메시지를 읽기 위해 기본 형태는 알아두는 것이 좋다.
 */

/**
 * [객체 키를 순회하는 형태]
 *
 * `keyof T`의 각 키를 순회하면서 원래 값 타입 `T[K]`를 유지하고
 * 선택적 속성 `?`를 추가한다. 이것이 `Partial<T>`의 핵심 원리이다.
 */
type MyPartial<T> = {
  [K in keyof T]?: T[K];
};

/**
 * [조건에 따라 타입을 선택하는 형태]
 *
 * 조건부 타입은 값의 `if`문이 아니라 타입의 할당 가능성을 기준으로 분기한다.
 * 실무에서는 직접 만들기보다 `Exclude`, `Extract`, `NonNullable` 같은 내장 타입을 먼저 검토한다.
 */
type RemoveNull<T> = T extends null | undefined ? never : T;

/**
 * [내부 타입을 꺼내는 infer]
 *
 * `infer U`는 조건이 성립할 때 타입 내부의 일부를 `U`라는 이름으로 추론한다.
 * `ReturnType`, `Parameters` 같은 내장 유틸리티를 이해하는 데 필요한 수준으로만 익히면 충분하다.
 *
 * 키 재매핑, 분배 조건부 타입 제어처럼 복잡한 기법은 라이브러리 타입을 직접 설계할 때 필요하다.
 * 일반 애플리케이션에서는 내장 유틸리티 타입과 명시적인 타입 별칭을 우선한다.
 */
type ElementOf<T> = T extends readonly (infer U)[] ? U : never;

// string
type Item = ElementOf<readonly string[]>;

/**
 * [템플릿 리터럴 타입]
 *
 * `Endpoint`는 정해진 문자열 조합만 허용한다.
 * 이벤트 이름이나 경로처럼 문자열 형식 자체가 중요한 경우에 유용하다.
 * 가능한 문자열이 너무 많아지면 오류 메시지와 자동 완성이 복잡해질 수 있으므로 적용 범위를 제한한다.
 */
type HttpMethod = "GET" | "POST";
type Resource = "users" | "orders";

type Endpoint = `${HttpMethod} /${Resource}`;

const endpoint: Endpoint = "GET /users";

// 오류
// const wrong: Endpoint = "FETCH /users";

/**
 * [재귀 타입]
 *
 * 트리, 댓글 답글, 폴더 구조처럼 같은 구조가 중첩될 때 사용한다.
 * 재귀 변환 타입까지 일반화하기보다 실제 데이터 구조를 명확하게 표현하는 데 집중한다.
 */
type Menu = {
  label: string;
  children?: Menu[];
};

const menu: Menu = {
  label: "설정",
  children: [{ label: "프로필" }, { label: "보안" }],
};

/**
 * [오류 수정 실습]
 *
 * 다음 타입은 원본 `User`와 중복된다.
 * 변경 가능한 필드를 원본에서 파생한다.
 * 비밀번호도 수정할 수 있어야 한다면 `Pick`의 키에 명시적으로 추가한다.
 * 어떤 유틸리티를 쓰느냐보다 어떤 속성을 허용할지 도메인 규칙을 먼저 결정해야 한다.
 */

// type UpdateUsers = {
//   name?: string;
//   age?: number;
// };

type EditableUsers = Pick<User, "name" | "age">;
type UpdateUsers = Partial<EditableUser>;

/**
 * [핵심 정리]
 *
 * 유틸리티 타입은 기존 타입과 새로운 타입의 관계를 표현한다.
 * `Pick`은 선택, `Omit`은 제외, `Partial`은 선택적 속성 변환에 사용한다.
 * `Record`는 정해진 키와 값 타입으로 객체 구조를 만든다.
 * `Exclude`, `Extract`, `NonNullable`은 유니온을 변환한다.
 * `ReturnType`, `Parameters`는 함수 타입에서 정보를 추출한다.
 * 맵드 타입, 조건부 타입, `infer`는 내장 유틸리티를 이해할 정도로 익히고 복잡한 직접 구현은 필요할 때 학습한다.
 * 타입 변환은 런타임 데이터의 검증이나 삭제를 대신하지 않는다.
 */
