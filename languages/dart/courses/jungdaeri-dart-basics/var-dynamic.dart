void main() {
  int age = 10;
  double num = 1.1;
  String greeting = 'Hello';

  var name = 'kim';

  print(name);

  dynamic d = 'd';

  print(d.runtimeType);

  d = 100;
  d = false;

  print(d.runtimeType);
}
