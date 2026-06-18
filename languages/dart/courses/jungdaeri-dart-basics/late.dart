void main() {
  User user = User('kim');

  // Future.delayed(Duration(seconds: 3), () {
  //   print('User.pet.name: ${user.pet?.name}');
  // });

  print('User.dog.name: ${user.dog.name}');
}

class User {
  String? name;
  int? age;

  late String? info = 'name: ${this.name}, age: ${this.age}';
  
  late Pet? pet = Pet();
  late Pet dog;

  User(this.name) {
    print('User init name: $name');
  }
}

class Pet {
  String? name;

  Pet({this.name}) {
    print('Pet init name: $name');
  }
}
