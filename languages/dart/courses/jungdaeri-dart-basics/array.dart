void main() {
  List<int> numbers = [1, 2, 3, 4, 5];

  print('numbers type: ${numbers.runtimeType}');
  print('numbers length: ${numbers.length}');

  numbers.add(1);

  print('numbers length: ${numbers.length}');
  print(numbers[0]);
  print(numbers.indexOf(1));

  final List<String> names = ['kim', 'lee'];
  names.add('park');

  // List<String> names = const ['kim', 'lee'];
  // names.add('park');

  var some = [];
  print(some.runtimeType);
}
