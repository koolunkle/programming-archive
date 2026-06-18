void main() {
  List<int> numbers = [1, 2, 3];
  List<int> counts = [5, 6, 7];
  List<int>? optionalCounts = null;

  // var [one, two, three] = numbers;
  // var [one, _, three] = numbers;
  var [one, ...rest] = numbers;

  int num = 4;
  int? numOptional = null;

  var newNumbers = [...numbers, num, ...counts];
  var newNumbersOptional = [...numbers, ?numOptional, ...?optionalCounts];

  print(numbers);

  print(one);
  // print(two);
  // print(three);
  print(rest);

  print(newNumbers);
  print(newNumbersOptional);

  var userInfo = {'age': 10, 'name': 'kim', 'hobby': 'coding'};

  var {'age': age, 'name': name, 'hobby': hobby} = userInfo;

  print(age);
  print(name);
  print(hobby);

  var info = ('red', 100);
  var (String color, int score) = info;

  var catInfo = (colors: 'brown', ages: 2);
  // var (:String colors, :int ages) = catInfo;
  var (colors: String catColor, ages: int catAge) = catInfo;

  print('color: $color, score: $score');
  print('colors: $catColor, ages: $catAge');

  final (:userName, :userAge) = getUserInfo();

  print('userName: $userName, userAge: $userAge');

  final [first, ..., last] = getNumbers();

  print(first);
  print(last);
}

List<int> getNumbers() => [1, 2, 2, 2, 3];

({String userName, int userAge}) getUserInfo() {
  return (userName: 'seulgi', userAge: 20);
}
