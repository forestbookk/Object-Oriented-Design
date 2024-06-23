import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Person> acquaintances = new HashMap<>();
    private final HashMap<Person, Integer> values = new HashMap<>();//<Person, Value>

    public void addAcquaintance(Person person, int value) {
        acquaintances.put(person.getId(), person);
        values.put(person, value);
    }

    public void removeAcquaintance(Person person) {
        acquaintances.remove(person.getId());
        values.remove(person);
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return this.acquaintances;
    }

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
        if (!(obj instanceof Person)) {
            return false;
        }
        return (((Person) obj).getId()) == id;
    }

    @Override
    public boolean isLinked(Person person) {
        return values.containsKey(person) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        return values.getOrDefault(person, 0);
    }

    public boolean strictEquals(Person person) {
        return true;
    }
}
