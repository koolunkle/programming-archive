void main() {
  Cat cat = Cat(name: 'kitty', age: 3);

  cat.setName = 'meow';
  cat.setAge = 7;

  print('cat name: ${cat.name}');
  print('cat age: ${cat.age}');
  print(cat.info);
  print(cat.isOld);
}

class Cat {
  String? name;
  int? age;

  Cat({this.name, required this.age});

  void set setName(String name) {
    this.name = name;
  }

  void set setAge(int age) => this.age = age;

  bool get isOld => (age ?? 0) > 5;

  String get info => 'cat info / name: $name, age: $age';
}
