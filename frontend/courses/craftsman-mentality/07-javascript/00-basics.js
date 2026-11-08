// // // // // // // // // // // // // console.log('Hello, World!')

// // // // // // // // // // // // // let number1 = 5;
// // // // // // // // // // // // // let number2 = 10;

// // // // // // // // // // // // // let sum = number1 + number2;

// // // // // // // // // // // // // console.log("first number: ", number1)
// // // // // // // // // // // // // console.log("second number: ", number2)
// // // // // // // // // // // // // console.log("added number: ", sum)

// // // // // // // // // // // // // let age;

// // // // // // // // // // // // // age = 25;
// // // // // // // // // // // // // console.log('first assignment', age);

// // // // // // // // // // // // // age = 30;
// // // // // // // // // // // // // console.log('re-assignment', age);

// // // // // // // // // // // // // const pi = 3.14;

// // // // // // // // // // // // // console.log('first assignment', pi);

// // // // // // // // // // // // // pi = 3.1415
// // // // // // // // // // // // // console.log('re-assignment', pi);

// // // // // // // // // // // // // number
// // // // // // // // // // // // let age = 25;
// // // // // // // // // // // // console.log(typeof age);

// // // // // // // // // // // // // strings
// // // // // // // // // // // // let user = 'koolunkle';
// // // // // // // // // // // // console.log(typeof user);

// // // // // // // // // // // // // boolean
// // // // // // // // // // // // let isStudent = true;
// // // // // // // // // // // // console.log(typeof isStudent);

// // // // // // // // // // // // // null
// // // // // // // // // // // // let value = null;
// // // // // // // // // // // // console.log(typeof value);

// // // // // // // // // // // // // undefinded
// // // // // // // // // // // // let variable;
// // // // // // // // // // // // console.log(typeof variable);

// // // // // // // // // // // // // bigint
// // // // // // // // // // // // let bigInt = 1000000000000000000n;
// // // // // // // // // // // // console.log(typeof bigInt);

// // // // // // // // // // // // // symbol
// // // // // // // // // // // // let symbol = Symbol('unique');
// // // // // // // // // // // // console.log(typeof symbol);


// // // // // // // // // // // let num1 = 11;
// // // // // // // // // // // let num2 = 5;

// // // // // // // // // // // // addtion
// // // // // // // // // // // let sum = num1 + num2;
// // // // // // // // // // // console.log(sum);

// // // // // // // // // // // // subtraction
// // // // // // // // // // // let difference = num1 - num2;
// // // // // // // // // // // console.log(difference);

// // // // // // // // // // // // multiplication
// // // // // // // // // // // let product = num1 * num2;
// // // // // // // // // // // console.log(product);

// // // // // // // // // // // // division
// // // // // // // // // // // let quotient = num1 / num2;
// // // // // // // // // // // console.log(quotient);

// // // // // // // // // // // // modulo
// // // // // // // // // // // let remainder = num1 % num2;
// // // // // // // // // // // console.log(remainder);

// // // // // // // // // // let a = 5;
// // // // // // // // // // let b = '5';

// // // // // // // // // // // == output: true
// // // // // // // // // // console.log(a == b);

// // // // // // // // // // // === output: false
// // // // // // // // // // console.log(a === b);

// // // // // // // // // // // '!=' output: false
// // // // // // // // // // console.log(a != b);


// // // // // // // // // // // '!==' output: true
// // // // // // // // // // console.log(a !== b);

// // // // // // // // // // let x = true;
// // // // // // // // // // let y = false;

// // // // // // // // // // // && output: false
// // // // // // // // // // console.log(x && y);

// // // // // // // // // // // || output: true
// // // // // // // // // // console.log(x || y);

// // // // // // // // // // // '!x' output: false
// // // // // // // // // // console.log(!x);

// // // // // // // // // let age = 25;
// // // // // // // // // let isStudent = false;

// // // // // // // // // let isYoungStudent1 = age < 30 && isStudent;
// // // // // // // // // let isYoungStudent2 = age < 30 || isStudent;
// // // // // // // // // let isYoungStudent3 = age < 30 || !isStudent;

// // // // // // // // // console.log(isYoungStudent1); // false
// // // // // // // // // console.log(isYoungStudent2); // true
// // // // // // // // // console.log(isYoungStudent3); // true

// // // // // // // // let age = 15;

// // // // // // // // if (age >= 18) {
// // // // // // // //     console.log('성인입니다.');
// // // // // // // // } else {
// // // // // // // //     console.log('미성년자입니다.');
// // // // // // // // }

// // // // // // // // console.log(age >= 18 ? '성인' : '미성년자');

// // // // // // // // let score = 85;

// // // // // // // // if (score >= 90) {
// // // // // // // //     console.log('A');
// // // // // // // // } else if (score >= 80) {
// // // // // // // //     console.log('B');
// // // // // // // // } else if (score >= 70) {
// // // // // // // //     console.log('c');
// // // // // // // // } else {
// // // // // // // //     console.log('d');
// // // // // // // // }

// // // // // // // // let temperature = 25;

// // // // // // // // if(temperature > 30) {
// // // // // // // //     console.log('Hot');
// // // // // // // // } else if(temperature >= 20 && temperature <= 30) {
// // // // // // // //     console.log('Good');
// // // // // // // // } else {
// // // // // // // //     console.log('Cold');
// // // // // // // // }

// // // // // // // // for 
// // // // // // // for (let i = 0; i < 5; i++) {
// // // // // // //     console.log('value fo i: ', i);
// // // // // // // }
// // // // // // // console.log('end of for loop');

// // // // // // // // while
// // // // // // // let count = 0;
// // // // // // // while (count < 3) {
// // // // // // //     console.log('value of count: ', count);
// // // // // // //     count++;
// // // // // // // }
// // // // // // // console.log('end of while loop');

// // // // // // // // do while
// // // // // // // let num = 5;

// // // // // // // do {
// // // // // // //     console.log('num: ', num);
// // // // // // //     num--;
// // // // // // // } while (num > 0)
// // // // // // // console.log('end of do-while loop');

// // // // // // // array
// // // // // // const colors = ["Red", "Blue", "Yellow"];

// // // // // // for (let i = 0; i < colors.length; i++) {
// // // // // //     console.log("color: ", colors[i]);
// // // // // // }

// // // // // // let sum_for = 0;

// // // // // // for (let i = 0; i <= 10; i++) {
// // // // // //     sum_for += i;
// // // // // // }

// // // // // // console.log('sum_for: ', sum_for);

// // // // // // let sum_while = 0;
// // // // // // let i = 0;

// // // // // // while (i <= 10) {
// // // // // //     sum_while += i
// // // // // //     i++
// // // // // // }
// // // // // // console.log('sum_while', sum_while);

// // // // // function greet1(name) {
// // // // //     console.log('Hello, ' + name);
// // // // // }

// // // // // let greet2 = (name) => {
// // // // //     console.log('Hello, ' + name)
// // // // // }

// // // // // greet1('koolunkle');

// // // // // greet2('cool-kim')

// // // // // function add(x, y) {
// // // // //     return x + y;
// // // // // }

// // // // // let result = add(3, 5);
// // // // // console.log('result: ' + result);

// // // // function calculateArea(radius) {
// // // //     const pi = 3.14;
// // // //     return pi * radius * radius;
// // // // }

// // // // let result1 = calculateArea(5);
// // // // let result2 = calculateArea(10);
// // // // let result3 = calculateArea(15);

// // // // console.log('result1: ', result1);
// // // // console.log('result2: ', result2);
// // // // console.log('result3: ', result3);

// // // // let filterShortWords = (wordArray) => {
// // // //     let result = [];
// // // //     for (let i = 0; i < wordArray.length; i++) {
// // // //         if (wordArray[i].length <= 5) {
// // // //             result.push(wordArray[i])
// // // //         }
// // // //     }
// // // //     return result;
// // // // }

// // // // const fruits = ['apple', 'banana', 'kiwi', 'orange', 'grapes', 'pear']
// // // // const shortFruits = filterShortWords(fruits);
// // // // console.log('short fruits: ', shortFruits);

// // // const fruits = ['apple', 'banana', 'orange', 'kiwi'];

// // // fruits.push('grapes');
// // // const lastElement = fruits.pop();

// // // console.log(fruits);
// // // console.log(lastElement); // grapes

// // // const fruits = ['apple', 'banana', 'orange', 'kiwi', 'grapes'];

// // // for (let i = 0; i < fruits.length; i++) {
// // //     console.log('fruit: ', fruits[i]);
// // // }

// // const numbers = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

// // const evenNumbers1 = numbers.filter(number => number % 2 == 0);
// // console.log('even numbers1: ', evenNumbers1);

// // const filtering = (arr) => {
// //     let filterArray = [];
// //     for (let i = 0; i < arr.length; i++) {
// //         if (arr[i] % 2 == 0) {
// //             filterArray.push(arr[i]);
// //         }
// //     }
// //     return filterArray;
// // }
// // const evenNumbers2 = filtering(numbers);
// // console.log('even numbers2: ', evenNumbers2);

// const fruits = ['apple', 'banana', 'orange', 'kiwi', 'grapes', 'pear'];

// const shortFruits = fruits.filter(fruit => fruit.length <= 5);
// console.log('short fruits: ', shortFruits);

// const person = {
//     'name': 'koolunkle',
//     'age': 30,
//     'job': 'developer'
// }
// console.log('name: ', person.name);
// console.log('age: ', person.age);
// console.log('job: ', person.job);

const student = {
    'name': 'koolunkle',
    'age': 29,
    'major': 'economic',
    'grades': [85, 90, 78, 95],
    'calculateAverage': function () {
        const sum = this.grades.reduce((total, grade) => total + grade, 0);
        const result = sum / this.grades.length;
        return result;
    },
}
console.log('name: ', student.name);
console.log('age: ', student.age);
console.log('major: ', student.major);
console.log('grades: ', student.grades);
console.log('calculateAverage: ', student.calculateAverage());