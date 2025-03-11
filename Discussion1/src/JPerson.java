public class JPerson {
    private final String name;
    private int age;

    public JPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String info() {
        return String.format("Name: %s, Age: %d", name, age);
    }

    static {
        JPerson person = new JPerson("Alice", 30);
        System.out.println(person.info());
    }
}
