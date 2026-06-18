void main() {
  positional('kim', 30, true);
  
  named(name: 'lee', age: 20, isDone: true);
  
  combine('park', 10);
  combine('park', 10, isDone: true);
}

void positional(String name, int age, bool isDone) {
  print('name: $name, age: $age, isDone: $isDone');
}

void named({String? name, int? age, bool isDone = false}) {
  print('name: $name, age: $age, isDone: $isDone');
}

void combine(String name, int age, {bool? isDone, String? job}) {
  print('name: $name, age: $age isDone: $isDone, job: $job');
}
