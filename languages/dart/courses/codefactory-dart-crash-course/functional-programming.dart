void main() {
  List<String> blackPink = ['Rose', 'Jisoo', 'Jennie', 'Lisa'];

  blackPink.add('Lisa');

  print(blackPink);
  print(blackPink.asMap());
  print(blackPink.toSet());

  Map blackPinkMap = blackPink.asMap();

  print(blackPinkMap.keys.toList());
  print(blackPinkMap.values.toList());

  Set blackPinkSet = Set.from(blackPink);

  print(blackPinkSet.toList());

  final newBlackPink = blackPink.map((x) {
    return 'BlackPink $x';
  });

  print(blackPink);
  print(newBlackPink.toList());

  final newBlackPink2 = blackPink.map((x) => 'BlackPink $x');

  print(newBlackPink2.toList());
  print(blackPink == blackPink);
  print(blackPink == newBlackPink);

  // [1.jpg, 3.jpg, 5.jpg, 7.jpg, 9.jpg]
  String number = '13579';
  final parsed = number.split('').map((x) => '$x.jpg');

  print(parsed.toList());

  Map<String, String> harryPotter = {
    'Harry': 'Gryffindor',
    'Ron': 'Gryffindor',
    'Hermione': 'Gryffindor',
    'Draco': 'Slytherin',
  };

  final newHarryPotter = harryPotter.map(
    (key, value) => MapEntry(
      'Harry Potter Character $key',
      'Harry Potter Character $value',
    ),
  );

  // print(harryPotter);
  // print(newHarryPotter);

  final keys = harryPotter.keys.map((x) => 'Harry Potter Character $x');
  final values = harryPotter.values.map((x) => 'Harry Potter Character $x');

  // print(keys);
  // print(values);

  Set harryPotterSet = {'Harry', 'Ron', 'Hermione', 'Draco'};

  final newHarryPotterSet = harryPotterSet
      .map((x) => 'Harry Potter $x')
      .toSet();

  // print(newHarryPotterSet);

  List<Map<String, String>> idol = [
    {'name': 'rei', 'group': 'ive'},
    {'name': 'wonyoung', 'group': 'ive'},
    {'name': 'yujin', 'group': 'ive'},
    {'name': 'seulgi', 'group': 'red velvet'},
    {'name': 'wendy', 'group': 'red velvet'},
    {'name': 'irene', 'group': 'red velvet'},
  ];

  print(idol);

  final newIdol = idol.where((x) => x['group'] == 'ive');

  print(newIdol);

  final parsedIdol = idol
      .map((x) => Idol(name: x['name']!, group: x['group']!))
      .toList();

  print(parsedIdol);

  for (Idol idol in parsedIdol) {
    print(idol.name);
    print(idol.group);
  }

  final ive = parsedIdol.where((x) => x.group == 'ive');

  print(ive.toList());

  List<int> numbers = [1, 3, 5, 7, 9];

  final result = numbers.reduce((acc, cur) {
    print('----------');
    print('acc: $acc');
    print('cur: $cur');
    print('total: ${acc + cur}');

    return acc + cur;
  });

  print(result);

  List<String> words = ['Hello', 'World', '!'];

  final sentence = words.reduce((prev, next) => prev + next);

  print(sentence);

  // words.reduce((prev, next) => prev.length + next.length);
  final count = words.fold<int>(0, (prev, next) => prev + next.length);

  print(count);

  // final sum = numbers.fold<int>(0, (prev, next) => prev + next);
  final sum = numbers.fold<int>(0, (prev, next) {
    print('----------');
    print('prev: $prev');
    print('next: $next');
    print('total: ${prev + next}');

    return prev + next;
  });

  print(sum);

  List<int> even = [2, 4, 6, 8];
  List<int> odd = [1, 3, 5, 7];

  print([even, odd]);
  print([...even, ...odd]);

  print(even);
  print([...even]);
  print(even == [...even]);
}

class Idol {
  final String name;
  final String group;

  Idol({required this.name, required this.group});

  @override
  String toString() {
    return 'Idol(name:${this.name}, group:${this.group})';
  }
}
