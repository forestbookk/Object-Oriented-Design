import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyPerson implements Person {
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.money = 0;
        this.acquaintances = new HashMap<>();
        this.values = new HashMap<>();
        this.tags = new HashMap<>();
    }

    private final int id;
    private final String name;
    private final int age;
    private int socialValue;
    private int money; // 后续可为负数
    private final HashMap<Integer, MyPerson> acquaintances;
    private final HashMap<Integer, Integer> values;//<PersonId, Value>
    private final HashMap<Integer, Tag> tags;
    private final HashSet<MyTag> includedTags = new HashSet<>();
    private final LinkedList<Message> messages = new LinkedList<>();
    private int bestId;
    private int bestValue;
    private boolean isMo = true;

    public boolean strictEquals(Person person) {
        return true;
    }

    public void insertMessageAtHead(Message e) {
        this.messages.addFirst(e);
    }

    public int getIncludedTagsSize() {
        return this.includedTags.size();
    }

    public void addIncludedTag(MyTag tag) {
        includedTags.add(tag);
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

    public HashMap<Integer, MyPerson> getAcquaintance() {
        return this.acquaintances;
    }

    public void removePersonFromTags(Person person) {
        for (Tag tag : tags.values()) {
            if (tag.hasPerson(person)) {
                ((MyPerson) person).removeIncludedTag((MyTag) tag);
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

    @Override
    public void addSocialValue(int num) {
        this.socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        int length = this.messages.size();
        return this.messages.subList(0, Math.min(length, 5));
    }

    @Override
    public void addMoney(int num) {
        this.money += num;
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    public void printValues() {
        for (Map.Entry<Integer, Integer> entry : values.entrySet()) {
            System.out.println("    " + entry.getKey() + "(" + entry.getValue() + ")");
        }
    }
}
