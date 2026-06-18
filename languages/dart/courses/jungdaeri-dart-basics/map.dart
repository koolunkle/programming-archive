void main() {
  List<int> numbers = [1, 2, 2, 2, 3];
  List<String> numberString = ['one', 'two', 'three'];

  Map<int, String> students = {1: 'kim', 2: 'lee'};
  final String? foundStudents = students[1];

  students[3] = 'park';

  print(students);
  print(students.runtimeType);

  print(foundStudents);
  print(foundStudents.runtimeType);

  // final numberMap = Map.fromIterable(numbers);

  // final numberMap = Map<int, int>.fromIterable(
  //   numbers,
  //   key: (num) => num,
  //   value: (num) => num * 10,
  //   // value: (num) => '${num * 10}',
  // );

  final Map<int, int> numberMap = <int, int>{
    for (int num in numbers) num: num * 10,
  };

  final numberStringMap = Map<int, String>.fromIterables(
    numbers.toSet(),
    numberString,
  );

  numberStringMap[4] = 'four';
  // numberStringMap = {4: 'four'};

  print(numberMap);
  print(numberStringMap);

  final unmodifiableMap = Map<int, String>.unmodifiable(numberStringMap);
  final constMap = const {1: 'one'};

  // unmodifiableMap[5] = 'five';
  // constMap[2] = 'two';

  // var emptyMap = {};
  // Map<int, String> emptyMap = Map.new();
  Map<int, String> emptyMap = {};

  print(emptyMap.runtimeType);
}
