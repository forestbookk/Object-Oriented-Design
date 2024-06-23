import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.Tag;

public class MyMessage implements Message {
    private final int type;
    private final int id;
    private final int socialValue;
    private final MyPerson person1;
    private final MyPerson person2;
    private final MyTag tag;

    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.type = 0;
        this.tag = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = (MyPerson) messagePerson1;
        this.person2 = (MyPerson) messagePerson2;
    }

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1, Tag messageTag) {
        this.type = 1;
        this.person2 = null;
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.person1 = (MyPerson) messagePerson1;
        this.tag = (MyTag) messageTag;
    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public Person getPerson1() {
        return person1;
    }

    @Override
    public Person getPerson2() {
        if (person2 == null) {
            throw new RuntimeException("Person2 is Null");
        }
        return person2;
    }

    @Override
    public Tag getTag() {
        if (tag == null) {
            throw new RuntimeException("Tag is Null");
        }
        return tag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Message) {
            return (((Message) obj).getId() == id);
        } else {
            return false;
        }
    }
}
