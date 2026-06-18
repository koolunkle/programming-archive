void main() async {
  // someDelayedFunction(
  //   'red velvet',
  //   callback: (label) => print('label: $label'),
  // );

  // someDelayedFunction(
  //   '1',
  //   callback: (label) {
  //     print('label$label called');
  //     someDelayedFunction(
  //       '2',
  //       callback: (label) {
  //         print('label$label called');
  //         someDelayedFunction(
  //           '3',
  //           callback: (label) {
  //             print('label$label called');
  //           },
  //         );
  //       },
  //     );
  //   },
  // );

  // var result1 = await someDelayedAsyncFunction('1', seconds: 3);
  // print(result1);

  // var result2 = await someDelayedAsyncFunction('2', seconds: 2);
  // print(result2);

  // var result3 = await someDelayedAsyncFunction('3', seconds: 1);
  // print(result3);

  var result = await Future.wait([
    someDelayedAsyncFunction('1', seconds: 3),
    someDelayedAsyncFunction('2', seconds: 2),
    someDelayedAsyncFunction('3', seconds: 1),
  ]);

  print(result);
}

Future<String> someDelayedAsyncFunction(String label, {int seconds = 1}) async {
  print('someDelayedFunction start');

  var result = await Future.delayed(
    Duration(seconds: seconds),
    () => '[$label] $seconds later',
  );

  print('someDelayedFunction end');

  return result;
}

void someDelayedFunction(
  String label, {
  int seconds = 1,
  required void Function(String label) callback,
}) {
  print('someDelayedFunction start');

  // Future.delayed(Duration(seconds: 3), () {
  //   print('label: $label');
  // });

  Future.delayed(Duration(seconds: seconds), () => callback(label));

  print('someDelayedFunction end');
}
