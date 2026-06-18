import 'dart:async';
import 'dart:math';

void main() async {
  // var firstUesr = await getUserName();
  // var secondUser = await getUserName();
  // var thirdUser = await getUserName();

  // print(firstUesr);
  // print(secondUser);
  // print(thirdUser);

  // await for (var user in getUserNameStream()) {
  //   print('user: $user');
  // }

  var userNameStream = getUserNameStream()
      .asyncMap((user) => 'Hello, $user!')
      .distinct()
      .asBroadcastStream();

  StreamSubscription? subscription;

  subscription = userNameStream.listen(
    (user) {
      print('user: $user');

      if (user == 'kim') {
        print('subscription cancel');
        subscription?.cancel();
      }
    },
    onDone: () => print('onDone'),
    onError: (error) => print('onError: $error'),
  );

  //  subscription = userNameStream.listen(
  //   (user) {
  //     print('user: $user');

  //     if (user == 'kim') {
  //       print('subscription cancel');
  //       subscription?.cancel();
  //     }
  //   },
  //   onDone: () => print('onDone'),
  //   onError: (error) => print('onError: $error'),
  // );
}

Stream<String> getUserNameStream() async* {
  List<String> names = ['kim', 'lee', 'park'];
  // List<String> names = ['kim', 'kim', 'kim'];

  await Future.delayed(const Duration(seconds: 1));
  yield names[Random().nextInt(names.length)];

  await Future.delayed(const Duration(seconds: 2));
  yield names[Random().nextInt(names.length)];

  await Future.delayed(const Duration(seconds: 3));
  yield names[Random().nextInt(names.length)];
}

Future<String> getUserName() async {
  List<String> names = ['kim', 'lee', 'park'];

  await Future.delayed(const Duration(seconds: 1));

  return names[Random().nextInt(names.length)];
}
