void main() {
  Name name = 'kim';

  print(name.runtimeType);

  // Operation operation = add;
  // operation(x: 5, y: 5);

  void Function(int, int) operation = (int x, int y) {
    print('x: $x, y:$y');
  };

  operation(5, 5);
}

typedef Name = String;
typedef Operation = void Function({required int x, required int y});

void add({required x, required y}) {
  print('x+y: ${x + y}');
}
