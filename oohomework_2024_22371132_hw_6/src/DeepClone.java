import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class DeepClone {

    public HashMap<Integer, HashSet<Person>> mapDeepClone(HashMap<Integer,
            HashSet<Person>> elePas) {
        HashMap<Integer, HashSet<Person>> ret = new HashMap<>();
        for (Map.Entry<Integer, HashSet<Person>> entry : elePas.entrySet()) {
            ret.put(entry.getKey(), setDeepClone(entry.getValue()));
        }
        return ret;
    }

    public HashSet<Person> setDeepClone(HashSet<Person> elePrs) {
        HashSet<Person> ret = new HashSet<>();
        for (Person elePr : elePrs) {
            ret.add(personDeepClone(elePr));
        }
        return ret;
    }

    public Person personDeepClone(Person elePr) {
        return new Person(elePr.getId(), elePr.getFromFloor(), elePr.getToFloor());
    }
}
