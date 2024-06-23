import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;
import sun.nio.ch.Net;

import java.util.HashMap;

public class MyNetwork implements Network {
    public MyNetwork() {

    }
    private HashMap<Integer, Person> persons = new HashMap<>();

    @Override
    public boolean containsPerson(int id) {
        return false;
    }

    @Override
    public Person getPerson(int id) {
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {

    }

    @Override
    public void addRelation(int id1, int id2, int value) throws PersonIdNotFoundException, EqualRelationException {

    }

    @Override
    public void modifyRelation(int id1, int id2, int value) throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {

    }

    @Override
    public int queryValue(int id1, int id2) throws PersonIdNotFoundException, RelationNotFoundException {
        return 0;
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        return false;
    }

    @Override
    public int queryBlockSum() {
        return 0;
    }

    @Override
    public int queryTripleSum() {
        return 0;
    }
}
