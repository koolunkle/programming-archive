void main() {
  final bool isDarkMode = false;

  // if (isDarkMode) {
  //   print('dark mode');
  // } else {
  //   print('light mode');
  // }

  final String result = isDarkMode == true ? 'dark mode' : 'light mode';

  print(result);

  // var name = null;
  String? name = null;

  // name = 'kim';

  final res = name ?? 'lee';

  print('name: $res');
  print('name: ${res.runtimeType}');
}
