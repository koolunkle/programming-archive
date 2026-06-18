import 'dart:async';

void main() async {
  // var controller = StreamController<String>.broadcast();
  var controller = StreamController<String>();

  var subscription = controller.stream.listen(
    (data) {
      print('onData: $data');
    },
    onDone: () => print('onDone'),
    onError: (error) => print('onError: $error'),
    cancelOnError: true,
  );

  //  controller.stream.listen(
  //   (data) {
  //     print('onData: $data');
  //   },
  //   onDone: () => print('onDone'),
  //   onError: (error) => print('onError: $error'),
  //   cancelOnError: true,
  // );

  print('Controller.hasListener: ${controller.hasListener}');

  controller.add('Hello, world!');
  controller.add('kim');
  controller.add('lee');
  controller.add('park');

  // controller.addError('error');

  await Future.delayed(Duration.zero);
  subscription.cancel();

  controller.add('Goodbye, world!');
  // await controller.addStream(someDelayedWords());

  if (!controller.isClosed) {
    print('Controller.close');
    controller.close();
  }
}

Stream<String> someDelayedWords() async* {
  await Future.delayed(const Duration(seconds: 2));
  yield 'Hello,';

  await Future.delayed(const Duration(seconds: 2));
  yield 'world!';
}
