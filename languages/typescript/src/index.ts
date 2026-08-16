// /**
//  * @param {Array<{price: number, quantity: number}>} items - 주문 상품 목록
//  * @returns {number} 총 주문 금액
//  */
// function calculateTotal(items) {
//   let total = 0;
//   for (const item of items) {
//     total += item.price * item.quantity;
//   }
//   return total;
// }

// const response = { data: { items: [] } };
// calculateTotal(response.items);
// calculateTotal(undefined);
// calculateTotal("상품 목록");
// calculateTotal([{ cost: 500 }]);

// type OrderItem = {
//   price: number;
//   quantity: number;
// };

// const item: OrderItem = {
//   price: -100,
//   quantity: 0,
// };

// function calculateTotal(items: OrderItem[]): number {
//   let total = 0;
//   for (const item of items) {
//     total += item.price * item.quantity;
//   }
//   return total;
// }

// calculateTotal(undefined);
// calculateTotal("상품 목록");
// calculateTotal([{ cost: 500 }]);

// function createOrderItem(price: number, quantity: number): OrderItem {
//   if (price < 0) throw new Error("가격은 0 이상이어야 합니다.");
//   if (!Number.isInteger(quantity) || quantity <= 0) {
//     throw new Error("수량은 1 이상의 정수여야 합니다.");
//   }
//   return { price, quantity };
// }

function greet(name: string) {
  return `안녕하세요, ${name}님`;
}

console.log(greet("슬기"));

// const total: number = "36000";

// function printLength(value: string) {
//   console.log(value.length);
// }

// printLength(undefined);
