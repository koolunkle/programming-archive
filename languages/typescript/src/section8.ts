/**
import { describe } from 'node:test';
 * [클래스, 접근 제어자, 추상 클래스와 인터페이스]
 *
 * TypeScript 클래스는 JavaScript 클래스에 필드 타입, 접근 제어, 추상 계약을 추가한다.
 * 하지만 타입 접근 제어와 런타임 은닉은 같은 것이 아니다.
 */

/**
 * [클래스 필드와 생성자]
 *
 * `readonly` 필드는 생성 시 정한 뒤 TypeScript 코드에서 다시 할당할 수 없다.
 * 생성자 매개변수에 접근 제어자를 쓰면 선언과 할당을 줄일 수 있다.
 */
class Product {
  readonly id: string;
  name: string;
  price: number;

  constructor(id: string, name: string, price: number) {
    this.id = id;
    this.name = name;
    this.price = price;
  }

  discount(rate: number) {
    return this.price * (1 - rate);
  }
}

class User {
  constructor(
    public readonly id: string,
    public name: string,
  ) {}
}

/**
 * [접근 제어자]
 *
 * `public`: 어디서나 접근
 * `protected`: 클래스와 하위 클래스에서 접근
 * `private`: 해당 클래스 내부에서만 접근
 */
class BankAccount {
  public readonly owner: string;
  protected transactionCount = 0;
  private balance: number;

  constructor(owner: string, balance: number) {
    this.owner = owner;
    this.balance = balance;
  }

  deposit(amount: number) {
    if (amount <= 0) throw new Error("입금액은 0보다 커야 합니다.");
    this.balance += amount;
    this.transactionCount++;
  }

  getBalace() {
    return this.balance;
  }
}

/**
 * [private와 #private의 차이]
 *
 * TypeScript의 `private`는 타입 검사 단계의 제한이다. JavaScript로 출력되면 일반 속성이 될 수 있다.
 * `#secret`은 JavaScript 런타임 자체가 외부 접근을 막는다.
 * 실제 보안과 은닉이 필요하다면 런타임 동작까지 고려해야 한다.
 */
class Counter {
  private count = 0;
  #secret = "runtime private";
}

/**
 * [상속과 메서드 재정의]
 *
 * 하위 클래스는 부모 계약을 깨뜨리지 않아야 한다.
 * 부모가 받을 수 있는 입력을 하위 클래스가 임의로 더 좁히면 대체 가능성이 깨진다.
 */
class PremiumAccount extends BankAccount {
  deposit(amount: number) {
    super.deposit(amount);
    console.log("프리미엄 포인트 적립");
  }
}

/**
 * [추상 클래스]
 *
 * 추상 클래스는 직접 생성할 수 없고, 공통 구현과 반드시 구현해야 할 메서드를 함께 제공한다.
 */
abstract class Shape {
  abstract area(): number;

  describe() {
    return `넓이: ${this.area()}`;
  }
}

class Circle extends Shape {
  constructor(private radius: number) {
    super();
  }

  area() {
    return Math.PI * this.radius ** 2;
  }
}

/**
 * [인터페이스 구현]
 *
 * 인터페이스는 인스턴스가 외부에 제공할 구조를 검사한다.
 * 구현 코드를 제공하지 않는다.
 */
interface Serializable {
  serialize(): string;
}

class Order implements Serializable {
  constructor(public readonly id: string) {}

  serialize(): string {
    return JSON.stringify({ id: this.id });
  }
}

/**
 * [추상 클래스와 인터페이스 선택]
 *
 * 공통 상태와 동작을 공유하면 추상 클래스,
 * 서로 다른 클래스가 같은 기능 계약을 지켜야 하면 인터페이스가 자연스럽다.
 */

/**
 * [오류 수정 실습]
 *
 * 외부에서 잔액을 직접 수정하면 규칙을 우회한다.
 * 데이터를 감추는 목적은 단순히 못 보게 하는 것이 아니라 상태 변경 규칙을 한곳에 모으는 것이다.
 */

// class Account {
//   constructor(public balance: number) {}
// }

// const account = new Account(10000);
// account.balance = -999999;

class Account {
  #balance: number;

  constructor(initialBalance: number) {
    if (initialBalance < 0) throw new Error("초기 잔액 오류");
    this.#balance = initialBalance;
  }

  withdraw(amount: number) {
    if (amount > this.#balance) throw new Error("잔액 부족");
    this.#balance -= amount;
  }

  get balance() {
    return this.#balance;
  }
}

/**
 * [핵심 정리]
 *
 * 클래스 필드에 타입과 접근 규칙을 선언할 수 있다.
 * TypeScript `private`와 JavaScript `#private`는 런타임 동작이 다르다.
 * 상태를 직접 노출하지 말고 유효성 검사를 통과하는 메서드로 변경한다.
 * 추상 클래스는 공통 구현, 인터페이스는 능력 계약에 적합하다.
 * 상속은 코드 재사용보다 대체 가능한 관계인지 먼저 판단해야 한다.
 */
