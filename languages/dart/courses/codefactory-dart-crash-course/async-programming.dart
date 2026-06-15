import 'dart:async';

void main() async {
  // Future<String> name = Future.value('name');
  // Future<int> number = Future.value(1);
  // Future<bool> isTrue = Future.value(true);

  // final result1 = await addNumbers(1, 1);
  // final result2 = await addNumbers(2, 2);

  // print('result1: $result1');
  // print('result2: $result2');
  // print('result1 + result2: ${result1 + result2}');

  final controller = StreamController();
  // final stream = controller.stream;
  final stream = controller.stream.asBroadcastStream();

  // final streamListener1 = stream.where((value) => value % 2 == 0).listen((
  //   value,
  // ) {
  //   print('Listener1: $value');
  // });

  // final streamListener2 = stream.where((value) => value % 2 == 1).listen((
  //   value,
  // ) {
  //   print('Listener2: $value');
  // });

  controller.sink.add(1);
  controller.sink.add(2);
  controller.sink.add(3);
  controller.sink.add(4);
  controller.sink.add(5);

  // calculate(1).listen((value) {
  //   print('calculate(1): ${value}');
  // });

  // calculate(3).listen((value) {
  //   print('calculate(3): ${value}');
  // });

  playAllStream().listen((value) {
    print(value);
  });
}

Stream<int> playAllStream() async* {
  yield* calculate(1);
  yield* calculate(1000);
}

Stream<int> calculate(int number) async* {
  for (int i = 0; i < 5; i++) {
    yield i * number;

    await Future.delayed(Duration(seconds: 1));
  }
}

Future<int> addNumbers(int number1, int number2) async {
  print('calculate: $number1 + $number2');

  await Future.delayed(Duration(seconds: 2), () {
    print('complete: $number1 + $number2 =  ${number1 + number2}');
  });

  print('done: $number1 + $number2');

  return number1 + number2;
}
