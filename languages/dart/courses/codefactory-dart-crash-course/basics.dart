void main() {
  String name = 'World';
  name = 'Red Velvet';

  print('Hello, $name!');
  print(name.runtimeType);

  int num1 = 10;
  int num2 = 20;
  int sum = num1 + num2;

  print(sum);

  double num3 = 2.5;
  double num4 = 0.5;

  print(num3 / num4);

  bool isTrue = true;
  bool isFalse = false;

  print(isTrue);
  print(isTrue && isFalse);

  var variable = 'I am a string';
  // variable = 10;

  print(variable.runtimeType);

  dynamic dynamicVariable = 'I can be anything';
  dynamicVariable = 10;

  print(dynamicVariable.runtimeType);

  String? nullableString;
  nullableString = 'I am not null';

  // print(nullableString!);
  print(nullableString);

  final String finalString = 'I cannot be changed';
  // finalString = 'Trying to change'; // This will cause an error

  const String constString = 'I am a compile-time constant';
  // constString = 'Trying to change'; // This will cause an error

  print(finalString);
  print(constString);

  final DateTime now = DateTime.now();

  print(now);

  // const DateTime now2 = DateTime.now();

  double? nullableDouble;
  
  nullableDouble ??= 3.14;

  print(nullableDouble);
  print(nullableDouble is int);
  print(nullableDouble is! String);

  bool result = 12 > 10 && 1 > 0;
  bool result2 = 12 > 10 || 1 < 0;

  print(result);
  print(result2);

  List<String> fruits = ['Apple', 'Banana', 'Cherry'];

  print(fruits);
  print(fruits[0]);

  fruits.add('Grape');

  print(fruits.last);
  print(fruits.length);

  fruits.remove('Banana');

  print(fruits);
  print(fruits.indexOf('Cherry'));

  Map<String, String> dictionary = {
    'Harry Poter': '해리포터',
    'Ron Weasley': '론 위즐리',
    'Hermione Granger': '헤르미온느 그레인저',
  };

  print(dictionary);

  dictionary['Albus Dumbledore'] = '알버스 덤블도어';
  dictionary.addAll({'Severus Snape': '세베루스 스네이프', 'Draco Malfoy': '드라코 말포이'});

  print(dictionary);
  print(dictionary.keys);
  print(dictionary.values);

  final Set<String> uniqueFruits = {'Apple', 'Banana', 'Cherry'};

  print(uniqueFruits);

  uniqueFruits.add('Apple');

  print(uniqueFruits);

  int count = 0;

  if (count == 0) {
    print('Count is zero');
  } else if (count > 0) {
    print('Count is positive');
  } else {
    print('Count is negative');
  }

  switch (count) {
    case 0:
      print('Count is zero');
      break;
    case 1:
      print('Count is one');
      break;
    default:
      print('Count is something else');
  }

  for (int i = 0; i < 10; i++) {
    print(i);
  }

  int total = 0;

  List<int> numbers = [1, 2, 3, 4, 5];

  for (int i = 0; i < numbers.length; i++) {
    total += numbers[i];
  }

  print(total);

  total = 0;

  for (int number in numbers) {
    total += number;
  }

  print(total);

  total = 0;

  while (total < 15) {
    total += 1;
  }

  print(total);

  total = 0;

  do {
    total += 1;
  } while (total < 15);

  print(total);

  total = 0;

  while (total < 15) {
    total += 1;

    if (total == 5) {
      break;
    }
  }

  print(total);

  for (int i = 0; i < 10; i++) {
    if (i == 5) {
      continue;
    }
    print(i);
  }

  Status status = Status.approved;

  if (status == Status.approved) {
    print('Status is approved');
  } else if (status == Status.pending) {
    print('Status is pending');
  } else if (status == Status.rejected) {
    print('Status is rejected');
  }

  addNumbers(x: 10, y: 20, z: 30);
  addNumbers(x: 10, y: 20);

  int addNum = addNumbers(x: 10);

  print(addNum);

  Operation operation = add;
  int res = operation(10, 20, 30);

  res = subtract(10, 20, 30);

  print(res);

  int calculated = calculate(10, 20, 30, add);
  
  print(calculated);
}

int addNumbers({required int x, int? y, int? z}) {
  int sum = x + (y ?? 0) + (z ?? 0);

  print('x: $x, y: $y, z: $z, sum: $sum');

  if (sum % 2 == 0) {
    print('Sum is even');
  } else {
    print('Sum is odd');
  }

  return sum;
}

// void addNumbers(int x, [int? y, int? z]) {
//   // int x  = 10;
//   // int y  = 20;
//   // int z  = 30;

//   int sum = x + (y ?? 0) + (z ?? 0);

//   print('x: $x, y: $y, z: $z, sum: $sum');

//   if (sum % 2 == 0) {
//     print('Sum is even');
//   } else {
//     print('Sum is odd');
//   }
// }

int add(int x, int y, int z) => x + y + z;

int subtract(int x, int y, int z) => x - y - z;

int calculate(int x, int y, int z, Operation operation) {
  return operation(x, y, z);
}

typedef Operation = int Function(int x, int y, int z);

enum Status { approved, pending, rejected }
