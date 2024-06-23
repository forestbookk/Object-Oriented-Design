import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;

public class MyTag implements Tag {
    public MyTag(int id) {
        this.id = id;
        this.persons = new HashMap<>();
        this.ages = new HashMap<>();
    }

    private int id;
    private HashMap<Integer, MyPerson> persons;
    private HashMap<Integer, Integer> ages;
    private int ageSum = 0;
    private int ageVar = 0;
    private boolean isAgesMo = true;
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
            //manageValueSum(true, (MyPerson) person);
            ages.put(personId, personAge);
            ageSum += personAge;
            isAgesMo = true;
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
            return this.ageSum / ages.size();
        }
    }

    @Override
    public int getAgeVar() {
        if (persons == null || persons.isEmpty()) {
            return 0;
        } else if (isAgesMo) {
            int res = 0;
            int getAgeMean = getAgeMean();
            for (Integer age : ages.values()) {
                res += (age - getAgeMean) * (age - getAgeMean);
            }
            res = res / persons.size();
            this.ageVar = res;
            this.isAgesMo = false;
            return res;
        } else {
            return this.ageVar;
        }
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            int prId = person.getId();
            persons.remove(prId);
            ages.remove(prId);
            isAgesMo = true;
            ageSum -= person.getAge();
            //manageValueSum(false, (MyPerson) person);
        } else {
            throw new RuntimeException("delPerson: Person no exist");
        }
    }

    @Override
    public int getSize() {
        return persons == null ? 0 : persons.size();
    }
}
