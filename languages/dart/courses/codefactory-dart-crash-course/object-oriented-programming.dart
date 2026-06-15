void main() {
  _Idol blackPink = _Idol('Black Pink', ['Jisoo', 'Jennie', 'Rosé', 'Lisa']);
  _Idol blackPink2 = _Idol('Black Pink', ['Jisoo', 'Jennie', 'Rosé', 'Lisa']);

  print(blackPink.name);
  print(blackPink.members);
  print(blackPink == blackPink2);
  print(identical(blackPink, blackPink2));

  blackPink.sayHello();
  blackPink.introduce();
  // blackPink.firstMember = 'Jisoo Kim';
  print(blackPink.firstMember);

  _Idol redVelvet = _Idol.fromList([
    'Red Velvet',
    ['Irene', 'Seulgi', 'Wendy', 'Joy', 'Yeri'],
  ]);

  // redVelvet.name = 'BTS';

  print(redVelvet.name);
  print(redVelvet.members);

  print('---------- Idol ----------');

  Idol apink = Idol(name: 'Apink', memberCount: 5);

  apink.sayName();
  apink.sayMemberCount();
  // apink.sayMale();

  print('---------- Boy Group ----------');

  BoyGroup bts = BoyGroup(name: 'BTS', memberCount: 7);

  bts.sayName();
  bts.sayMemberCount();
  bts.sayMale();

  print('---------- Girl Group ----------');

  GirlGroup aespa = GirlGroup(name: 'aespa', memberCount: 4);

  aespa.sayName();
  aespa.sayMemberCount();
  aespa.sayFemale();

  print('---------- Type Comparison ----------');

  print(apink is Idol);
  print(apink is BoyGroup);
  print(apink is GirlGroup);

  print('---------- Type Comparison 2 ----------');

  print(bts is Idol);
  print(bts is BoyGroup);
  print(bts is GirlGroup);

  print('---------- Type Comparison 3 ----------');

  print(aespa is Idol);
  print(aespa is BoyGroup);
  print(aespa is GirlGroup);

  print('--------------------');

  TimesTwo timesTwo = TimesTwo(5);
  TimesFour timesFour = TimesFour(5);

  print(timesTwo.calculate());
  print(timesFour.calculate());

  print('--------------------');

  Employee seulgi = Employee('Seulgi');
  Employee rei = Employee('Rei');

  seulgi.name = 'Irene';
  seulgi.printNameAndBuilding();
  rei.printNameAndBuilding();

  Employee.building = 'SM Entertainment';

  seulgi.printNameAndBuilding();
  rei.printNameAndBuilding();
  Employee.printBuilding();

  print('--------------------');

  BoyIdol bigbang = BoyIdol('BIGBANG');
  GirlIdol ive = GirlIdol('IVE');

  // IdolInterface kara = IdolInterface('KARA');
  bigbang.sayName();
  ive.sayName();

  print(bigbang is IdolInterface);
  print(bigbang is BoyIdol);
  print(bigbang is GirlIdol);

  print(ive is IdolInterface);
  print(ive is BoyIdol);
  print(ive is GirlIdol);

  print('--------------------');

  Lecture<String, String> lecture1 = Lecture('1', 'Dart');
  Lecture<int, String> lecture2 = Lecture(2, 'Flutter');

  lecture1.printIdType();
  lecture2.printIdType();
}

class Lecture<T, X> {
  final T id;
  final X name;

  Lecture(this.id, this.name);

  void printIdType() {
    print(id.runtimeType);
  }
}

class GirlIdol implements IdolInterface {
  String name;

  GirlIdol(this.name);

  void sayName() {
    print('Hello, we are $name!');
  }
}

class BoyIdol implements IdolInterface {
  String name;

  BoyIdol(this.name);

  void sayName() {
    print('Hello, we are $name!');
  }
}

abstract class IdolInterface {
  String name;

  IdolInterface(this.name);

  void sayName() {}
}

class Employee {
  static String? building;
  String name;

  Employee(this.name);

  void printNameAndBuilding() {
    print('My name is $name and I work in $building.');
  }

  static void printBuilding() {
    print('The building is $building.');
  }
}

class TimesFour extends TimesTwo {
  TimesFour(int number) : super(number);

  @override
  int calculate() {
    // return super.number * 4;
    return super.calculate() * 2;
  }
}

class TimesTwo {
  final int number;

  TimesTwo(this.number);

  int calculate() {
    return this.number * 2;
  }
}

class GirlGroup extends Idol {
  GirlGroup({required String name, required int memberCount})
    : super(name: name, memberCount: memberCount);

  void sayFemale() {
    print('We are a girl group!');
  }
}

class BoyGroup extends Idol {
  BoyGroup({required String name, required int memberCount})
    : super(name: name, memberCount: memberCount);

  void sayMale() {
    print('We are a boy group!');
  }
}

class Idol {
  String name;

  int memberCount;

  Idol({required this.name, required this.memberCount});

  void sayName() {
    print('Hello, we are $name!');
  }

  void sayMemberCount() {
    print('We have $memberCount members.');
  }
}

class _Idol {
  // String name = 'Black Pink';
  // List<String> members = ['Jisoo', 'Jennie', 'Rosé', 'Lisa'];

  final String name;
  final List<String> members;

  // Idol(String name, List<String> members) {
  //   this.name = name;
  //   this.members = members;
  // }

  const _Idol(this.name, this.members);

  _Idol.fromList(List values) : this.name = values[0], this.members = values[1];

  void sayHello() {
    print('Hello, we are $name!');
  }

  void introduce() {
    print('Our members are: ${members.join(', ')}.');
  }

  String get firstMember {
    return this.members[0];
  }

  // void set firstMember(String name) {
  //   this.members[0] = name;
  // }
}
