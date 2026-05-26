data class Student(val name: String, val age: Int)

fun main() {

    val students = mutableListOf<Student>()

    students.add(Student("학생_1", 10))
    students.add(Student("학생_1", 13))
    students.add(Student("학생_1", 16))
    students.add(Student("학생_2", 20))
    students.add(Student("학생_2", 24))
    students.add(Student("학생_2", 100))
    students.add(Student("학생_7", 101))
    students.add(Student("학생_8", 102))
    students.add(Student("학생_9", 103))
    students.add(Student("학생_10", 104))

    val result1: Map<String, List<Student>> = students.groupBy { it.name }

    println(result1)

    val result2: Map<String, List<Int>> =
            students.groupBy(keySelector = { it.name }, valueTransform = { it.age })

    println(result2)

    val grouping: Grouping<Student, String> = students.groupingBy { it.name }
    val result3 = grouping.eachCount()
    val result4 = grouping.fold(0) { acc, element -> acc + element.age }
    val result5 =
            grouping.aggregate { _, acc: Int?, element, first ->
                if (first) {
                    element.age
                } else {
                    acc?.plus(element.age)
                }
            }

    println(result3)
    println(result4)
    println(result5)
}
