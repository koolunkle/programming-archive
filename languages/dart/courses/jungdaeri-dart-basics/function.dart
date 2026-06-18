void main() {
  saySomething();

  String hello = sayHello('kim');

  print(hello);
  sayMessage('Hello, world!', isDone: false);

  void Function() greeting = () {
    print('Hello');
  };

  greeting();
}

void saySomething() {
  print('Something');
}

// String sayHello(String name) {
//   return 'Hello, $name';
// }

String sayHello(String name) => 'Hello, $name';

void sayMessage(String message, {bool isDone = true}) {
  print('$message / isDone: $isDone');
}
