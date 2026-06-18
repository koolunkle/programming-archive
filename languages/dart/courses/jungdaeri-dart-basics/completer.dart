import 'dart:async';

void main() async {
  var completer = Completer();
  int seconds = 3;

  // Future.delayed(Duration(seconds: seconds), () {
  //   print('$seconds seconds later');
  //   // completer.completeError("error");
  //   completer.complete('complete');
  // });

  // var result = await completer.future;
  // print(result);

  // completer.future.then(
  //   (value) {
  //     print("value: $value");
  //   },
  //   onError: (error) {
  //     print("onError: $error");
  //   },
  // );

  // var result = await someDelayAsyncFunction();
  // print(result);

  multipleCallbackFunction((List<String> result) {
    print(result);
  });
}

void multipleCallbackFunction(void Function(List<String>) callback) {
  var firstCompleter = Completer<String>();
  var secondCompleter = Completer<String>();
  var thirdCompleter = Completer<String>();

  someDelayCallbackFunction(delay: 3, (String first) {
    firstCompleter.complete(first);
  });
  someDelayCallbackFunction(delay: 2, (String second) {
    secondCompleter.complete(second);
  });
  someDelayCallbackFunction(delay: 1, (String third) {
    thirdCompleter.complete(third);
  });

  Future.wait<String>([
    firstCompleter.future,
    secondCompleter.future,
    thirdCompleter.future,
  ]).then((result) => callback(result));
}

Future<String> someDelayAsyncFunction([int delay = 2]) async {
  var completer = Completer();

  Future.delayed(Duration(seconds: delay), () {
    print('$delay seconds later');
    completer.complete('$delay seconds later');
  });

  return await completer.future;
}

void someDelayCallbackFunction(
  void Function(String) callback, {
  int delay = 2,
}) {
  Future.delayed(Duration(seconds: delay), () {
    print('$delay seconds later');
    callback('$delay seconds later');
  });
}
