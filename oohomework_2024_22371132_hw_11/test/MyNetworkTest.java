import org.junit.Test;
import com.oocourse.spec3.exceptions.*;
import com.oocourse.spec3.main.*;

import static org.junit.Assert.*;

public class MyNetworkTest {
    private MyNetwork network = new MyNetwork();
    private MyNetwork oldNetwork = new MyNetwork();

    public void addEmojiToOld(int messageId, int emojiNumber, int personId1, int personId2) throws EmojiIdNotFoundException, EqualPersonIdException, EqualMessageIdException, RelationNotFoundException, TagIdNotFoundException, MessageIdNotFoundException {
        MyEmojiMessage mem = new MyEmojiMessage(messageId, emojiNumber, oldNetwork.getPerson(personId1), oldNetwork.getPerson(personId2));
        oldNetwork.addMessage(mem);
        oldNetwork.sendMessage(messageId);
    }

    public void addEmoji(int messageId, int emojiNumber, int personId1, int personId2) throws EmojiIdNotFoundException, EqualPersonIdException, EqualMessageIdException, RelationNotFoundException, TagIdNotFoundException, MessageIdNotFoundException {
        MyEmojiMessage mem = new MyEmojiMessage(messageId, emojiNumber, oldNetwork.getPerson(personId1), oldNetwork.getPerson(personId2));
        network.addMessage(mem);
        network.sendMessage(messageId);
    }

    public void prepare() throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException, RelationNotFoundException,
            MessageIdNotFoundException, TagIdNotFoundException, EqualEmojiIdException, PersonIdNotFoundException, EqualRelationException {
        for (int i = 1; i <= 15; i++) {
            oldNetwork.addPerson(new MyPerson(i, String.valueOf(i), i));
            network.addPerson(new MyPerson(i, String.valueOf(i), i));
        }


        oldNetwork.addRelation(2, 12, 8);
        oldNetwork.addRelation(8, 13, 6);
        oldNetwork.addRelation(4, 3, 3);
        oldNetwork.addRelation(3, 5, 4);
        oldNetwork.addRelation(2, 6, 5);

        network.addRelation(2, 12, 8);
        network.addRelation(8, 13, 6);
        network.addRelation(4, 3, 3);
        network.addRelation(3, 5, 4);
        network.addRelation(2, 6, 5);


        for (int i = 1; i <= 10; i++) {
            oldNetwork.storeEmojiId(i * 100);
        }
        addEmojiToOld(1, 100, 2, 12);
        addEmojiToOld(2, 100, 8, 13);
        addEmojiToOld(3, 100, 4, 3);

        addEmojiToOld(4, 200, 3, 5);
        addEmojiToOld(5, 200, 2, 6);
        addEmojiToOld(6, 200, 2, 12);

        addEmojiToOld(7, 300, 2, 6);
        addEmojiToOld(8, 400, 8, 13);
        addEmojiToOld(9, 500, 2, 12);
        addEmojiToOld(10, 500, 2, 6);
        addEmojiToOld(11, 600, 8, 13);
        addEmojiToOld(12, 600, 4, 3);
        addEmojiToOld(13, 600, 8, 13);
        addEmojiToOld(14, 600, 8, 13);
        addEmojiToOld(15, 700, 4, 3);
        addEmojiToOld(16, 700, 3, 5);
        addEmojiToOld(17, 700, 3, 5);

        MyEmojiMessage emoji1 = new MyEmojiMessage(18, 100, oldNetwork.getPerson(3), oldNetwork.getPerson(5));
        oldNetwork.addMessage(emoji1);
        MyEmojiMessage emoji2 = new MyEmojiMessage(19, 200, oldNetwork.getPerson(4), oldNetwork.getPerson(3));
        oldNetwork.addMessage(emoji2);
        MyEmojiMessage emoji3 = new MyEmojiMessage(20, 300, oldNetwork.getPerson(2), oldNetwork.getPerson(12));
        oldNetwork.addMessage(emoji3);

        MyNoticeMessage notice1 = new MyNoticeMessage(21, "1", oldNetwork.getPerson(3), oldNetwork.getPerson(5));
        oldNetwork.addMessage(notice1);
        MyNoticeMessage notice2 = new MyNoticeMessage(22, "22", oldNetwork.getPerson(4), oldNetwork.getPerson(3));
        oldNetwork.addMessage(notice2);
        MyNoticeMessage notice3 = new MyNoticeMessage(23, "333", oldNetwork.getPerson(2), oldNetwork.getPerson(12));
        oldNetwork.addMessage(notice3);
        MyNoticeMessage notice4 = new MyNoticeMessage(24, "4444", oldNetwork.getPerson(3), oldNetwork.getPerson(5));
        oldNetwork.addMessage(notice4);

        MyRedEnvelopeMessage red1 = new MyRedEnvelopeMessage(25, 666, oldNetwork.getPerson(3), oldNetwork.getPerson(5));
        oldNetwork.addMessage(red1);
        MyRedEnvelopeMessage red2 = new MyRedEnvelopeMessage(26, 666, oldNetwork.getPerson(2), oldNetwork.getPerson(12));
        oldNetwork.addMessage(red2);
        MyRedEnvelopeMessage red3 = new MyRedEnvelopeMessage(27, 666, oldNetwork.getPerson(3), oldNetwork.getPerson(4));
        oldNetwork.addMessage(red3);
        MyRedEnvelopeMessage red4 = new MyRedEnvelopeMessage(28, 666, oldNetwork.getPerson(8), oldNetwork.getPerson(13));
        oldNetwork.addMessage(red4);

        for (int i = 1; i <= 10; i++) {
            network.storeEmojiId(i * 100);
        }

        addEmoji(1, 100, 2, 12);
        addEmoji(2, 100, 8, 13);
        addEmoji(3, 100, 4, 3);

        addEmoji(4, 200, 3, 5);
        addEmoji(5, 200, 2, 6);
        addEmoji(6, 200, 2, 12);

        addEmoji(7, 300, 2, 6);
        addEmoji(8, 400, 8, 13);
        addEmoji(9, 500, 2, 12);
        addEmoji(10, 500, 2, 6);
        addEmoji(11, 600, 8, 13);
        addEmoji(12, 600, 4, 3);
        addEmoji(13, 600, 8, 13);
        addEmoji(14, 600, 8, 13);
        addEmoji(15, 700, 4, 3);
        addEmoji(16, 700, 3, 5);
        addEmoji(17, 700, 3, 5);
        MyEmojiMessage nEmoji1 = new MyEmojiMessage(18, 100, network.getPerson(3), network.getPerson(5));
        network.addMessage(nEmoji1);
        MyEmojiMessage nEmoji2 = new MyEmojiMessage(19, 200, network.getPerson(4), network.getPerson(3));
        network.addMessage(nEmoji2);
        MyEmojiMessage nEmoji3 = new MyEmojiMessage(20, 300, network.getPerson(2), network.getPerson(12));
        network.addMessage(nEmoji3);

        MyNoticeMessage nNotice1 = new MyNoticeMessage(21, "1", network.getPerson(3), network.getPerson(5));
        network.addMessage(nNotice1);
        MyNoticeMessage nNotice2 = new MyNoticeMessage(22, "22", network.getPerson(4), network.getPerson(3));
        network.addMessage(nNotice2);
        MyNoticeMessage nNotice3 = new MyNoticeMessage(23, "333", network.getPerson(2), network.getPerson(12));
        network.addMessage(nNotice3);
        MyNoticeMessage nNotice4 = new MyNoticeMessage(24, "4444", network.getPerson(3), network.getPerson(5));
        network.addMessage(nNotice4);

        MyRedEnvelopeMessage nRed1 = new MyRedEnvelopeMessage(25, 666, network.getPerson(3), network.getPerson(5));
        network.addMessage(nRed1);
        MyRedEnvelopeMessage nRed2 = new MyRedEnvelopeMessage(26, 666, network.getPerson(2), network.getPerson(12));
        network.addMessage(nRed2);
        MyRedEnvelopeMessage nRed3 = new MyRedEnvelopeMessage(27, 666, network.getPerson(3), network.getPerson(4));
        network.addMessage(nRed3);
        MyRedEnvelopeMessage nRed4 = new MyRedEnvelopeMessage(28, 666, network.getPerson(8), network.getPerson(13));
        network.addMessage(nRed4);
    }

    public void assertConstant(Message m1, Message m2) {
        assertEquals(m1.getType(), m2.getType());
        assertEquals(m1.getSocialValue(), m2.getSocialValue());
        assertTrue(m1.getPerson1().equals(m2.getPerson1()));
        assertTrue(m1.getPerson2().equals(m2.getPerson2()));
    }

    @Test
    public void deleteColdEmoji() throws EqualPersonIdException,
            PersonIdNotFoundException, EqualRelationException, EqualMessageIdException,
            EmojiIdNotFoundException, RelationNotFoundException,
            MessageIdNotFoundException, TagIdNotFoundException, EqualEmojiIdException {
        prepare();

        int limit = 3;
        int res = network.deleteColdEmoji(limit);

        Message[] oldMessages = oldNetwork.getMessages();
        int[] oldEmojiIdList = oldNetwork.getEmojiIdList();
        int[] oldEmojiHeatList = oldNetwork.getEmojiHeatList();
        Message[] messages = network.getMessages();
        int[] emojiIdList = network.getEmojiIdList();
        int[] emojiHeatList = network.getEmojiHeatList();

        assertEquals(res, emojiIdList.length);
        assertEquals(res, emojiHeatList.length);

        int len = 0;
        for (int i = 0, j = 0; i < oldEmojiIdList.length; i++) {
            if (oldEmojiHeatList[i] >= limit) {
                len++;
                assertEquals(emojiIdList[j], oldEmojiIdList[i]);
                assertEquals(emojiHeatList[j], oldEmojiHeatList[i]);
                j++;
            }
        }
        assertEquals(len, emojiIdList.length);
        assertEquals(len, emojiHeatList.length);

        for (int i = 0; i < oldMessages.length; i++) {
            if (oldMessages[i] instanceof EmojiMessage) {
                if (network.containsEmojiId(((EmojiMessage) oldMessages[i]).getEmojiId())) {
                    boolean flag = false;
                    for (int j = 0; j < messages.length; j++) {
                        if (oldMessages[i].equals(messages[j])) {
                            flag = true;
                            assertEquals(((EmojiMessage) oldMessages[i]).getEmojiId(), ((EmojiMessage) messages[j]).getEmojiId());
                            assertConstant(oldMessages[i], messages[j]);
                            break;
                        }
                    }
                    assertTrue(flag);
                } else {
                    boolean flag = true;
                    for (int j = 0; j < messages.length; j++) {
                        if (oldMessages[i].equals(messages[j])) {
                            flag = false;
                            break;
                        }
                    }
                    assertTrue(flag);
                }
            } else if (oldMessages[i] instanceof NoticeMessage) {
                boolean flag = false;
                for (int j = 0; j < messages.length; j++) {
                    if (oldMessages[i].equals(messages[j])) {
                        flag = true;
                        assertEquals(((NoticeMessage) oldMessages[i]).getString(), ((NoticeMessage) messages[j]).getString());
                        assertConstant(oldMessages[i], messages[j]);
                        break;
                    }
                }
                assertTrue(flag);
            } else {
                boolean flag = false;
                for (int j = 0; j < messages.length; j++) {
                    if (oldMessages[i].equals(messages[j])) {
                        flag = true;
                        assertEquals(((RedEnvelopeMessage) oldMessages[i]).getMoney(), ((RedEnvelopeMessage) messages[j]).getMoney());
                        assertConstant(oldMessages[i], messages[j]);
                        break;
                    }
                }
                assertTrue(flag);
            }
        }

        len = 0;
        for (int i = 0; i < oldMessages.length; i++) {
            if (oldMessages[i] instanceof EmojiMessage && !network.containsEmojiId(((EmojiMessage) oldMessages[i]).getEmojiId())) {
                continue;
            }
            len++;
        }
        assertEquals(len, messages.length);
    }
}