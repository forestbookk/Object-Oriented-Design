
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;

public class MyTag implements Tag {
    public MyTag(int id) {
        this.id = id;
        this.persons = new HashMap<>();
    }

    private final int id;
    private final HashMap<Integer, MyPerson> persons;
    private int ageSum = 0;
    private int agePowSum = 0;
    private int valueSum = 0;

    public HashMap<Integer, MyPerson> getPersons() {
        return persons;
    }

    public void plusValueSum(int num) {
        this.valueSum += num;
    }

    public boolean containsPerson(int personId) {
        if (persons == null || persons.isEmpty()) {
            return false;
        } else {
            return persons.containsKey(personId);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Tag)) {
            return false;
        } else {
            return (((Tag) obj).getId() == id);
        }
    }

    @Override
    public void addPerson(Person person) {
        if (hasPerson(person)) {
            throw new RuntimeException("addPerson: Person exists");
        } else {
            int personId = person.getId();
            int personAge = person.getAge();
            persons.put(personId, (MyPerson) person);
            ageSum += personAge;
            agePowSum += personAge * personAge;
        }
    }

    @Override
    public boolean hasPerson(Person person) {
        return persons != null && persons.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return 2 * this.valueSum;
    }

    @Override
    public int getAgeMean() {
        if (persons == null || persons.isEmpty()) {
            return 0;
        } else {
            return this.ageSum / persons.size();
        }
    }

    @Override
    public int getAgeVar() {
        if (persons == null || persons.isEmpty()) {
            return 0;
        } else {
            int num = persons.size();
            int ageMean = getAgeMean();
            return (agePowSum + num * ageMean * ageMean - 2 * ageMean * ageSum) / num;
        }
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            int prId = person.getId();
            persons.remove(prId);
            int personAge = person.getAge();
            ageSum -= personAge;
            agePowSum -= personAge * personAge;
        } else {
            throw new RuntimeException("delPerson: Person no exist");
        }
    }

    @Override
    public int getSize() {
        return persons == null ? 0 : persons.size();
    }
}
