import com.oocourse.spec1.main.Person;

public class MyPerson implements Person {
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private int id;
    private String name;
    private int age;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Person)) {
            return false;
        }
        return (((Person) obj).getId()) == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return false;
    }

    @Override
    public int queryValue(Person person) {
        return 0;
    }
}
