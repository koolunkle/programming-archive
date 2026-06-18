void main() {
  int? num = null;
  var number = num ?? 100;

  print(number);

  String? name = null;
  name ??= 'kim';

  print(name);

  print('==========');

  int? first = null;
  int? second = null;
  int? third = 300;

  int result = first ?? second ?? third;

  print(result);

  print('==========');

  String? hobby = null;

  String? secondHobby = null;
  String? thirdHobby = 'coding';

  hobby ??= secondHobby ?? thirdHobby;

  int? hobbyLength = hobby?.length;
  bool? hobbyLengthEven = hobby?.length.isEven;

  print(hobbyLength);
  print(hobbyLengthEven);
}
