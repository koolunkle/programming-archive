void main() {
  final result = nameAndAge({'name': 'seulgi', 'age': 20});

  print(result);
  print(result.$1);
  print(result.$2);

  print('==========');

  final result2 = getRedVelvetWithType();

  for (final item in result2) {
    print(item.$1);
    print(item.$2);
  }

  print('==========');

  final result3 = getRedVelvetWithType2();

  for (final item in result3) {
    print(item.$1);
    print(item.$2);
  }

  print('==========');

  final result4 = getRedVelvetWithType3();

  for (final item in result4) {
    print(item.name);
    print(item.age);
  }

  final seulgi = getSeulgi();

  print(seulgi);

  final (name, age) = ('seulgi', 20);

  print(name);
  print(age);

  final redVelvet = ['seulgi', 'irene', 'wendy'];
  final [String s, String i, String w] = redVelvet;

  print(s);
  print(i);
  print(w);

  final numbers = [1, 2, 3, 4, 5, 6, 7, 8];
  // final [x, y, ..., z] = numbers;
  final [x, y, ...rest, z] = numbers;

  print(x);
  print(y);
  print(z);
  print(rest);

  print('==========');

  final [xxx, _, yyy, ...rest2, zzz, _] = numbers;

  print(xxx);
  print(yyy);
  print(rest2);
  print(zzz);

  print('==========');

  final seulgiMap = {'name': 'seulgi', 'age': 20};
  final {'name': name2, 'age': age2} = seulgiMap;

  print(name2);
  print(age2);

  print('==========');

  final seulgiIdol = Idol(name: 'seulgi', age: 20);
  final Idol(name: name3, age: age3) = seulgiIdol;

  print(name3);
  print(age3);

  // Validation
  final irene = ('irene', 30);
  final (name4 as String, age4 as int) = irene;

  print(name4);
  print(age4);

  print('==========');

  // switcher('aaa');
  // switcher('bbb');
  // switcher(['1', '2']);
  // switcher([1, 2]);
  // switcher([1, 2, 3]);
  // switcher([4, 5, 6]);
  // switcher([4, 5, 6, 7]);
  // switcher([5, 10]);
  // switcher([5, '10']);
  switcher(7);
  switcher(77);

  print(switcher2(5, true));
  print(switcher2('5', true));
  print(switcher2(7, true));
  print(switcher2(7, false));

  forLooper();
  ifMatcher();
}

void ifMatcher() {
  final seulgi = {'name': 'seulgi', 'age': 20};

  print('==========');

  if (seulgi case {'name': String name, 'age': String age}) {
    print(name);
    print(age);
  }
}

void forLooper() {
  final List<Map<String, dynamic>> members = [
    {'name': 'seulgi', 'age': 20},
    {'name': 'irene', 'age': 30},
  ];

  for (final member in members) {
    print(member['name']);
    print(member['age']);
  }

  print('==========');

  for (var {'name': name, 'age': age} in members) {
    print(name);
    print(age);
  }
}

String switcher2(dynamic value, bool condition) => switch (value) {
  5 => 'match: 5',
  7 when condition => 'match 7 and true',
  _ => 'no match',
};

void switcher(dynamic anything) {
  switch (anything) {
    case 'aaa':
      print('match: aaa');
    case ['1', '2']:
      print('match: [1, 2]');
    case [_, _, _]:
      print('match: [_, _, _]');
    case [int a, int b]:
      print('match: [int $a, int $b]');
    case < 10 && > 5:
      print('match: < 10 && > 5');
    default:
      print('no match');
  }
}

/**
 * mixin => extends and with (X)
 * mixin class => extends and with (X)
 * 
 * class => on (X)
 * mixin class => on (X)
 */
mixin class AnimalMixin {
  String bark() {
    return 'bow-wow';
  }
}

class Dog with AnimalMixin {}

/**
 * final => extends, implements and mixin (X)
 * base => extends (O), implements (X)
 * interface => extends (X), implements (O)
 * sealed => abstract && final and Pattern Matching
 * 
 * extends => final, base, sealed
 */
String whoIsHe(Person person) => switch (person) {
  Idol i => 'idol',
  Engineer e => 'engineer',
  // _ => 'etc'
  Chef c => 'chef',
};

sealed class Person {}

class Engineer extends Person {}

class Chef extends Person {}

class Idol extends Person {
  final String name;
  final int age;

  Idol({required this.name, required this.age});
}

// Record
(String, int) nameAndAge(Map<String, dynamic> json) {
  // final result = [json['name'], json['age']];
  final result = (json['name'] as String, json['age'] as int);

  return result;
}

List<Map<String, dynamic>> getRedVelvet() {
  return [
    {'name': 'wendy', 'age': 10},
    {'name': 'seulgi', 'age': 20},
    {'name': 'irene', 'age': 30},
  ];
}

List<(String, int)> getRedVelvetWithType() {
  return [('wendy', 10), ('seulgi', 20), ('irene', 30)];
}

List<(String name, int age)> getRedVelvetWithType2() {
  return [('wendy', 10), ('seulgi', 20), ('irene', 30)];
}

List<({String name, int age})> getRedVelvetWithType3() {
  return [
    (name: 'wendy', age: 10),
    (name: 'seulgi', age: 20),
    (name: 'irene', age: 30),
  ];
}

(String name, String group, int age) getSeulgi() {
  return ('seulgi', 'red velvet', 10);
}
