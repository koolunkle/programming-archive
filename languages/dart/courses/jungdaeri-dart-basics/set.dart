void main() {
  List<int> numbers = [1, 1, 2, 2, 3];
  Set<int> uniqueNumbers = {1, 1, 2, 2, 3};

  print('numbers: $numbers');
  print('unique numbers: $uniqueNumbers');

  // var nums = Set.from(numbers);
  var nums = Set.of(numbers);

  Set emptySet = {};
  // var emptySet = Set();
  // var emptySet = Set.new();
  // var emptySet = Set.identity();

  print(nums);
  print(emptySet);
  print(emptySet.runtimeType);

  final unmodifiableSet = Set.unmodifiable(numbers);
  // final Set<int> unmodifiableSet = const {};
  // final unmodifiableSet = const {};

  // unmodifiableSet.add(10);

  final someList = uniqueNumbers.toList();
  print(someList);
}
