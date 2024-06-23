import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.main.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyTagTest {
    private int prId = 0;

    @Test
    public void getValueSum() {
    }

    @Test
    public void getAgeMean() throws EqualPersonIdException {
        MyTag tag = new MyTag(1);
        for(int i = 0; i < 10; i++) {
            tag.addPerson(generatePerson());
        }
        tag.getAgeMean();
    }

    public Person generatePerson() throws EqualPersonIdException {
        MyPerson pr = new MyPerson(prId, prId + "name", 1);
        prId++;
        return pr;
    }
}