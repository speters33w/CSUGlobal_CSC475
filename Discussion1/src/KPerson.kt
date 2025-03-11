data class KPerson(val name: String, var age: Int) {
    val info: String
        get() = "Name: %s, Age: %d".format(name, age)

    fun setAge(newAge: Int) {
        age = newAge
    }
}

fun main() {
    val person = KPerson("Alice", 30)
    println(person.info)
}