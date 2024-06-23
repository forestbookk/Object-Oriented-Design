import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyPerson implements Person {
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintances = new HashMap<>();
        this.values = new HashMap<>();
        this.tags = new HashMap<>();
    }

    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, MyPerson> acquaintances;
    private final HashMap<Integer, Integer> values;//<PersonId, Value>
    private final HashMap<Integer, Tag> tags;
    private final HashSet<MyTag> includedTags = new HashSet<>();
    private int bestId;
    private int bestValue;
    private boolean isMo = true;

    public int getIncludedTagsSize() {
        return this.includedTags.size();
    }

    public void addIncludedTag(MyTag tag) {
        includedTags.add(tag);
        // TODO
        for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
            int personId = entry.getKey();
            int value = entry.getValue();
            if (tag.containsPerson(personId)) {
                tag.plusValueSum(value);
            }
        }
    }

    public void removeIncludedTag(MyTag tag) {
        includedTags.remove(tag);
        // TODO
        for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
            int personId = entry.getKey();
            int value = entry.getValue();
            if (tag.containsPerson(personId)) {
                tag.plusValueSum(-value);
            }
        }
    }

    public void notifyValueSum(int value, int otherId) {
        for (MyTag myTag : includedTags) {
            if (myTag.containsPerson(otherId)) {
                myTag.plusValueSum(value);
            }
        }
    }

    public void addAcquaintance(Person person, int value) {
        int personId = person.getId();
        if (acquaintances.containsKey(personId)) {
            if (personId == bestId && value >= bestValue) {
                bestValue = value;
            } else if (personId == bestId) {
                isMo = true;
            } else if (value == bestValue && personId < bestId) {
                bestId = personId;
            } else if (value > bestValue) {
                isMo = true;
            }
        } else if ((value > bestValue) || (value == bestValue && personId < bestId)) {
            bestId = personId;
            bestValue = value;
        }
        acquaintances.put(personId, (MyPerson) person);
        values.put(personId, value);
    }

    public void removeAcquaintance(Person person) {
        int personId = person.getId();
        acquaintances.remove(personId);
        values.remove(personId);
        if (personId == bestId) {
            isMo = true;
        }
    }

    public int getBestAcquaintance() {
        if (this.acquaintances.size() == 1) {
            for (Integer bestId : this.acquaintances.keySet()) {
                this.bestId = bestId;
                this.bestValue = values.get(bestId);
                return this.bestId;
            }
        } else if (this.isMo) {
            boolean isFirst = true;
            for (Map.Entry<Integer, Integer> entry : this.values.entrySet()) {
                if (isFirst) {
                    this.bestId = entry.getKey();
                    this.bestValue = entry.getValue();
                    isFirst = false;
                    continue;
                }
                int personId = entry.getKey();
                int value = entry.getValue();
                if (this.bestValue < value) {
                    this.bestId = personId;
                    this.bestValue = value;
                } else if (this.bestValue == value && this.bestId > personId) {
                    this.bestId = personId;
                }
            }
        }
        return this.bestId;
    }

    /*public int getBestAcquaintance() {
        if (acquaintances.size() == 1) {
            for (Integer bestId : acquaintances.keySet()) {
                this.bestId = bestId;
                this.bestValue = values.get(bestId);
                return this.bestId;
            }
        } else if (!isBuilt) {
            isBuilt = true;
            for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
                Acquaintance a = new Acquaintance(entry.getKey(), entry.getValue());
                findAcquaintance.put(entry.getKey(), a);
                bestAcquaintance.add(a);
            }
            Collections.sort(bestAcquaintance);
            bestId = bestAcquaintance.get(0).getId();
            bestValue = bestAcquaintance.get(0).getValue();
        } else if (isMo) {
            Collections.sort(bestAcquaintance);
            bestId = bestAcquaintance.get(0).getId();
            bestValue = bestAcquaintance.get(0).getValue();
        }
        return this.bestId;
    }*/

    public HashMap<Integer, MyPerson> getAcquaintance() {
        return this.acquaintances;
    }

    public void removePersonFromTags(Person person) {
        for (Tag tag : tags.values()) {
            if (tag.hasPerson(person)) {
                tag.delPerson(person);
            }
        }
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
    public boolean containsTag(int id) {
        return tags.containsKey(id);
    }

    @Override
    public Tag getTag(int id) {
        if (!containsTag(id)) {
            return null;
        } else {
            return tags.get(id);
        }
    }

    @Override
    public void addTag(Tag tag) {
        if (tags.containsKey(tag.getId())) {
            throw new RuntimeException("addTag: Contains" + id);
        } else {
            tags.put(tag.getId(), tag);
        }
    }

    @Override
    public void delTag(int id) {
        if (!tags.containsKey(id)) {
            throw new RuntimeException("delTag: !Contains" + id);
        } else {
            tags.remove(id);
        }
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
        return values.containsKey(person.getId()) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        return values.getOrDefault(person.getId(), 0);
    }

    public boolean strictEquals(Person person) {
        return true;
    }

    public void printValues() {
        for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
            System.out.println("    " + entry.getKey() + "(" + entry.getValue() + ")");
        }
    }
}
