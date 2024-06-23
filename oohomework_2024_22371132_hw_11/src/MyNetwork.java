import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.EqualTagIdException;
import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.TagIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import com.oocourse.spec3.main.Tag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {

    private final HashMap<Integer, MyPerson> persons;
    private final HashMap<Integer, Message> messages;
    private final HashMap<Integer, Integer> emojis;
    private final Manager manager;

    public MyNetwork() {
        this.persons = new HashMap<>();
        this.manager = new Manager(this.persons);
        this.messages = new HashMap<>();
        this.emojis = new HashMap<>();
    }

    public void printMessage() {
        for (Integer integer : this.messages.keySet()) {
            System.out.print(integer + " ");
        }
        System.out.println("*******");
    }

    public Person[] getPersons() {
        return null;
    }

    public Message[] getMessages() {
        return null;
    }

    public int[] getEmojiIdList() {
        return null;
    }

    public int[] getEmojiHeatList() {
        return null;
    }

    @Override
    public boolean containsPerson(int id) {
        return persons.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (!containsPerson(id)) {
            return null;
        }
        return persons.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int prId = person.getId();
        if (!containsPerson(prId)) {
            persons.put(prId, (MyPerson) person);
            manager.manageAddPerson(prId, person);
        } else {
            throw new MyEqualPersonIdException(prId);
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson pr1 = (MyPerson) getPerson(id1);
        MyPerson pr2 = (MyPerson) getPerson(id2);
        if (containsPerson(id1) && containsPerson(id2) && !pr1.isLinked(pr2)) {
            pr1.addAcquaintance(pr2, value);
            pr2.addAcquaintance(pr1, value);
            manager.manageAddRelation(id1, id2, pr1, pr2, value);
        } else if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (containsPerson(id1) && containsPerson(id2) && pr1.isLinked(pr2)) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    @Override
    public void modifyRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        MyPerson pr1 = (MyPerson) getPerson(id1);
        MyPerson pr2 = (MyPerson) getPerson(id2);
        if (containsPerson(id1) && containsPerson(id2) &&
                id1 != id2 && pr1.isLinked(pr2)) {
            int oldValue = pr1.queryValue(pr2);
            int newValue = oldValue + value;
            if (newValue > 0) {
                pr1.addAcquaintance(pr2, newValue);
                pr2.addAcquaintance(pr1, newValue);
                manager.manageModifyRelation(true, id1, id2, pr1, pr2, newValue);
            } else {
                pr1.removeAcquaintance(pr2);
                pr2.removeAcquaintance(pr1);
                pr1.removePersonFromTags(pr2);
                pr2.removePersonFromTags(pr1);
                manager.manageModifyRelation(false, id1, id2, pr1, pr2, -oldValue);
            }
        } else {
            if (!containsPerson(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!containsPerson(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else if (containsPerson(id1) && containsPerson(id2) && id1 == id2) {
                throw new MyEqualPersonIdException(id1);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }

    }

    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        MyPerson pr1 = (MyPerson) getPerson(id1);
        MyPerson pr2 = (MyPerson) getPerson(id2);
        if (containsPerson(id1) && containsPerson(id2) &&
                pr1.isLinked(pr2)) {
            return pr1.queryValue(pr2);
        } else {
            if (!containsPerson(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else if (!containsPerson(id2)) {
                throw new MyPersonIdNotFoundException(id2);
            } else {
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)) {
            if (getPerson(id1).isLinked(getPerson(id2))) {
                return true;
            }
            return manager.isCircle(id1, id2);
        } else {
            if (!containsPerson(id1)) {
                throw new MyPersonIdNotFoundException(id1);
            } else {
                throw new MyPersonIdNotFoundException(id2);
            }
        }
    }

    @Override
    public int queryBlockSum() {
        return manager.queryBlockSum();
    }

    @Override
    public int queryTripleSum() {
        return manager.queryTripleSum();
    }

    @Override
    public void addTag(int personId, Tag tag) throws
            PersonIdNotFoundException, EqualTagIdException {
        if (containsPerson(personId) && !getPerson(personId).containsTag(tag.getId())) {
            persons.get(personId).addTag(tag);
        } else {
            if (!containsPerson(personId)) {
                throw new MyPersonIdNotFoundException(personId);
            } else {
                throw new MyEqualTagIdException(tag.getId());
            }
        }
    }

    @Override
    public void addPersonToTag(int personId1, int personId2, int tagId)
            throws PersonIdNotFoundException, RelationNotFoundException,
            TagIdNotFoundException, EqualPersonIdException {
        if (!containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        } else if (!containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        } else if (personId1 == personId2) {
            throw new MyEqualPersonIdException(personId1);
        } else if (!getPerson(personId2).isLinked(getPerson(personId1))) {
            throw new MyRelationNotFoundException(personId1, personId2);
        } else if (!getPerson(personId2).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else if (getPerson(personId2).getTag(tagId).hasPerson(getPerson(personId1))) {
            throw new MyEqualPersonIdException(personId1);
        } else {
            MyTag myTag = (MyTag) getPerson(personId2).getTag(tagId);
            if (myTag.getPersons().size() <= 1111) {
                MyPerson pr1 = persons.get(personId1);
                myTag.addPerson(pr1);
                pr1.addIncludedTag(myTag);
            }
        }
    }

    @Override
    public int queryTagValueSum(int personId, int tagId) throws
            PersonIdNotFoundException, TagIdNotFoundException {
        if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else {
            return getPerson(personId).getTag(tagId).getValueSum();
        }
    }

    @Override
    public int queryTagAgeVar(int personId, int tagId) throws
            PersonIdNotFoundException, TagIdNotFoundException {
        if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else {
            return persons.get(personId).getTag(tagId).getAgeVar();
        }
    }

    @Override
    public void delPersonFromTag(int personId1, int personId2, int tagId)
            throws PersonIdNotFoundException, TagIdNotFoundException {
        if (!containsPerson(personId1)) {
            throw new MyPersonIdNotFoundException(personId1);
        } else if (!containsPerson(personId2)) {
            throw new MyPersonIdNotFoundException(personId2);
        } else if (!getPerson(personId2).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else if (!getPerson(personId2).getTag(tagId).hasPerson(getPerson(personId1))) {
            throw new MyPersonIdNotFoundException(personId1);
        } else {
            MyPerson pr1 = (MyPerson) getPerson(personId1);
            Tag tag = persons.get(personId2).getTag(tagId);
            tag.delPerson(pr1);
            pr1.removeIncludedTag((MyTag) tag);
        }
    }

    @Override
    public void delTag(int personId, int tagId) throws
            PersonIdNotFoundException, TagIdNotFoundException {
        if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else if (!getPerson(personId).containsTag(tagId)) {
            throw new MyTagIdNotFoundException(tagId);
        } else {
            getPerson(personId).delTag(tagId);
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return this.messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if ((message instanceof EmojiMessage) &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        } else if (message.getType() == 0 &&
                message.getPerson1().equals(message.getPerson2())) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        } else {
            this.messages.put(message.getId(), message);
        }
        /*System.out.println("**am**begin**for"+message.getId()+"**");
        printMessage();*/
    }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return this.messages.get(id);
        }
        return null;
    }

    public void maintainEmojiMessages(MyEmojiMessage myEmojiMessage) {
        int emojiId = myEmojiMessage.getEmojiId();
        if (!this.emojis.containsKey(emojiId)) {
            this.emojis.put(emojiId, 1);
        } else {
            int newHeat = 1 + this.emojis.get(emojiId);
            this.emojis.put(emojiId, newHeat);
        }
    }

    @Override
    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, TagIdNotFoundException {
        /*System.out.println("**sm**begin**for"+id+"**");
        printMessage();*/
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(
                    getMessage(id).getPerson1().getId(), getMessage(id).getPerson2().getId());
        } else if (getMessage(id).getType() == 1 &&
                !getMessage(id).getPerson1().containsTag(getMessage(id).getTag().getId())) {
            throw new MyTagIdNotFoundException(getMessage(id).getTag().getId());
        } else {
            MyMessage myMessage = (MyMessage) this.messages.get(id);
            this.messages.remove(id);
            MyPerson myPerson1 = (MyPerson) myMessage.getPerson1();
            int num = myMessage.getSocialValue();
            myPerson1.addSocialValue(num);
            if (myMessage.getType() == 0) {
                MyPerson myPerson2 = (MyPerson) myMessage.getPerson2();
                myPerson2.addSocialValue(num);
                myPerson2.insertMessageAtHead(myMessage);
                if (myMessage instanceof RedEnvelopeMessage) {
                    num = ((MyRedEnvelopeMessage) myMessage).getMoney();
                    myPerson1.addMoney(-num);
                    myPerson2.addMoney(+num);
                } else if (myMessage instanceof EmojiMessage) {
                    maintainEmojiMessages((MyEmojiMessage) myMessage);
                }
            } else {
                for (MyPerson myPerson : ((MyTag) myMessage.getTag()).getPersons().values()) {
                    myPerson.addSocialValue(num);
                }
                if (myMessage instanceof RedEnvelopeMessage &&
                        !((MyTag) myMessage.getTag()).getPersons().isEmpty()) {
                    num = ((MyRedEnvelopeMessage) myMessage).getMoney() /
                            myMessage.getTag().getSize();
                    myPerson1.addMoney(-myMessage.getTag().getSize() * num);
                    for (MyPerson myPerson : ((MyTag) myMessage.getTag()).getPersons().values()) {
                        myPerson.addMoney(num);
                    }
                } else if (myMessage instanceof EmojiMessage) {
                    maintainEmojiMessages((MyEmojiMessage) myMessage);
                }
            }
        }
        /*System.out.println("**sm**end**for"+id+"**");
        printMessage();*/
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (containsPerson(id)) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (containsPerson(id)) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public boolean containsEmojiId(int id) {
        return !emojis.isEmpty() && emojis.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        } else {
            emojis.put(id, 0);
        }
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (containsPerson(id)) {
            return getPerson(id).getMoney();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (containsEmojiId(id)) {
            return emojis.get(id);
        } else {
            throw new MyEmojiIdNotFoundException(id);
        }
    }

    @Override
    public int deleteColdEmoji(int limit) {
        Iterator<Map.Entry<Integer, Integer>> it = emojis.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            if (entry.getValue() < limit) {
                it.remove();
                Iterator<Map.Entry<Integer, Message>> itM = messages.entrySet().iterator();
                while (itM.hasNext()) {
                    Map.Entry<Integer, Message> entryM = itM.next();
                    if (entryM.getValue() instanceof EmojiMessage) {
                        if (((MyEmojiMessage) entryM.getValue()).getEmojiId() == entry.getKey()) {
                            itM.remove();
                        }
                    }
                }
            }
        }
        return emojis.size();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!containsPerson(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        } else {
            getPerson(personId).getMessages().removeIf(message -> message instanceof NoticeMessage);
        }
    }

    @Override
    public int queryBestAcquaintance(int id) throws
            PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!containsPerson(id)) {
            throw new MyPersonIdNotFoundException(id);
        } else if (((MyPerson) getPerson(id)).getAcquaintance().isEmpty()) {
            throw new MyAcquaintanceNotFoundException(id);
        } else {
            return ((MyPerson) getPerson(id)).getBestAcquaintance();
        }
    }

    @Override
    public int queryCoupleSum() {
        return manager.queryCoupleSum();
    }

    @Override
    public int queryShortestPath(int id1, int id2) throws
            PersonIdNotFoundException, PathNotFoundException {
        if (!containsPerson(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!containsPerson(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!isCircle(id1, id2)) {
            throw new MyPathNotFoundException(id1, id2);
        } else {
            if (id1 == id2) {
                return 0;
            }
            return manager.zeroOneBfs(id1, id2) - 1;
        }
    }

}