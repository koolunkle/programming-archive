void main() {
  // var user = User('kim');
  // var user = User('kim', 30);
  // var user = User('kim', age: 30);

  // var user = User.withDetails('kim');
  // var user = User.withDetails('kim', 30);

  var user = User.test('kim');

  print('name: ${user.name}, age: ${user.age}');

  positionalParamFunction('lee');
  positionalParamFunction('lee', 20);

  namedParamFunction('park');
  namedParamFunction('park', age: 30);
}

class User {
  String? name;
  int? age;

  // User(this.name, this.age);
  // User(this.name, [this.age = 0]);
  // User(this.name, {this.age = 0});

  // User.withDetails(this.name, [this.age]);
  User.withDetails({required this.name, this.age});

  User.test(String userName)
    : this.withDetails(name: userName, age: userName.length);

  User.anonymous() : this.name = 'Anonymous', age = 0;
}

void namedParamFunction(String name, {int? age = 0}) {
  print('namedParamFunction / name: $name, age: $age');
}

void positionalParamFunction(String name, [int? age = 0]) {
  print('positionalParamFunction / name: $name, age: $age');
}
