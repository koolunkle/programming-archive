void main() {
  User firstUser = const User();
  const User secondUser = User();

  User thirdUser = User();

  User fourthUser = const User.init();
  User fifthUser = const User.init();

  print('firstUser: ${firstUser.hashCode}');
  print('secondUser: ${secondUser.hashCode}');

  print('thirdUser: ${thirdUser.hashCode}');

  print('fourthUser: ${fourthUser.hashCode}');
  print('fifthUser: ${fifthUser.hashCode}');

  bool areTheSame1 = identical(firstUser, secondUser);
  bool areTheSame2 = identical(fourthUser, fifthUser);

  print('areTheSame1: $areTheSame1');
  print('areTheSame2: $areTheSame2');
}

class User {
  final String? name;
  final int? age;

  const User({this.name = 'kim', this.age = 30});
  const User.init() : this();
}
