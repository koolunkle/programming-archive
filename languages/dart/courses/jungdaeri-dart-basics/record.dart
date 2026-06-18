void main() {
  var someRecord = (
    10,
    10,
    false,
    [1, 2, 3],
    {'red', 'green', 'blue'},
    {'name': 'kim', 'age': 30},
    (String name, int age) => print('name: $name, age: $age'),
  );

  final int first = someRecord.$1;
  final bool third = someRecord.$3;
  final List<int> fourth = someRecord.$4;
  final Set<String> fifth = someRecord.$5;
  final Map<String, dynamic> sixth = someRecord.$6;
  final Function(String, int) seventh = someRecord.$7;

  print(first);
  print(third);
  print(fourth);
  print(fifth);
  print(sixth);
  print(seventh('lee', 20));

  // var intRecord = (first: 100, second: 200);
  // (int, {int first, int second}) intRecord = (0, first: 100, second: 200);
  RecordType intRecord = (0, first: 100, second: 200);

  // intRecord.$1 = 300;

  print(intRecord.runtimeType);

  var exampleRecord = (name: 100, age: 200);

  // otherFunction(exampleRecord);
  someFunction(exampleRecord);

  var (names, ages) = otherReturnFunction();

  print(names);
  print(ages);

  var (:String name, :int age) = someReturnFunction();

  print(name);
  print(age);
}

typedef RecordType = (int, {int first, int second});

(String, int) otherReturnFunction() {
  return ('park', 10);
}

({String name, int age}) someReturnFunction() {
  return (name: 'red velvet', age: 50);
}

void otherFunction((int, int) param) {
  print('param name: ${param.$1}');
  print('param age: ${param.$2}');
}

void someFunction(({int name, int age}) param) {
  print('param name: ${param.name}');
  print('param age: ${param.age}');
}
