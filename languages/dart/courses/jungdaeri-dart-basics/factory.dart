void main() {
  // Pet pet = Pet(null);
  Pet pet = Pet('white');

  print('pet name: ${pet.name}');
  print('pet age: ${pet.age}');

  Pet pett = Pet.aged(7);

  print('pett name: ${pett.name}');
  print('pett age: ${pett.age}');

  print('==========');

  UserInfo firstUserInfo = UserInfo.getInstance();
  UserInfo secondUserInfo = UserInfo.getInstance();

  print('firstUserInfo: $firstUserInfo');
  print('secondUserInfo: $secondUserInfo');
  print(identical(firstUserInfo, secondUserInfo));
}

class UserInfo {
  static final UserInfo _instance = UserInfo._internalInit();

  UserInfo._internalInit() {
    print('UserInfo init called');
  }

  factory UserInfo.getInstance() => _instance;
}

class Pet {
  final String? name;
  final int? age;

  // Pet({this.name = 'white', this.age = 3});
  // Pet.named() : this.name = 'white', age = 3;

  factory Pet(String? name) {
    print('Pet init called / name: $name');

    if (name == null) {
      throw Exception('noname');
    }

    return Pet._internalInit(name: name, age: 3);
  }

  factory Pet.aged(int? age) {
    print('Pet init called / age: $age');

    if (age == null) {
      throw Exception('no age');
    }

    return Pet._internalInit(name: 'brown', age: age);
  }

  Pet._internalInit({this.name, this.age});
}
