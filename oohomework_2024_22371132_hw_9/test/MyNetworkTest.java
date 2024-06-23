import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.*;

public class MyNetworkTest {
    private static int prId = 0;
    private MyNetwork network;
    private MyNetwork shadowNetwork;
    private int tripleSum = 0;

    @Test
    public void buildDisjointSet() {
        int sum = 0;
        for (int i = 1; i < 151; i++) {
            for (int j = 1; j < 151; j++) {
                sum++;
            }
        }
        System.out.println(sum);
    }

    @Test
    public void queryBlockSum() throws EqualPersonIdException, PersonIdNotFoundException, EqualRelationException, RelationNotFoundException {
        network = new MyNetwork();
        int testNum = 151;
        for (int i = 1; i < testNum; i++) {
            MyPerson pr = new MyPerson(i, String.valueOf(i), 1);
            network.addPerson(pr);
        }
        for (int i = 1; i < testNum; i++) {
            for (int j = i + 1; j < testNum; j++) {
                network.addRelation(i, j, 1);
            }
        }
        //checkBlockSum();

        for (int i = 1; i < testNum; i++) {
            for (int j = i + 1; j < testNum; j++) {
                network.modifyRelation(i, j, -1);
                network.queryBlockSum();
            }
        }
    }


    @Test
    public void queryTripleSum() throws PersonIdNotFoundException, EqualRelationException, EqualPersonIdException {
        network = new MyNetwork();
        shadowNetwork = new MyNetwork();
        for (int i = 0; i <= 30; i++) {
            generatePerson();
        }

        assertEquals(0, network.queryTripleSum());
        assertNotNull(network.getPersons());
        for (int i = 0; i < prId; i++) {
            assertNotNull(network.getPersons()[i]);
            assertTrue(((MyPerson) shadowNetwork.getPersons()[i]).strictEquals(network.getPersons()[i]));
        }
        // 加入一组联系 12
        addRelationAndAssert(1, 2);
        // 加入第二组联系 13
        addRelationAndAssert(1, 3);
        // 加入第三组联系 23，形成三角形123
        addRelationAndAssert(2, 3);

        // 124：加入24
        addRelationAndAssert(2, 4);
        // 124: 加入14
        addRelationAndAssert(1, 4);

        addRelationAndAssert(5, 6);
        addRelationAndAssert(6, 7);
        addRelationAndAssert(5, 7);

        addRelationAndAssert(3, 4);
        addRelationAndAssert(4, 10);
        addRelationAndAssert(5, 10);
        addRelationAndAssert(4, 5);
        addRelationAndAssert(2, 5);
        addRelationAndAssert(6, 10);
        addRelationAndAssert(7, 10);
        addRelationAndAssert(3, 10);
    }

    public int getShadowTripleSum(Person[] collection) {
        int cnt = 0;
        assertNotNull(collection);
        for (int i = 0; i < prId; i++) {
            assertNotNull(collection[i]);
            Person pri = collection[i];
            for (int j = i + 1; j < prId; j++) {
                assertNotNull(collection[j]);
                Person prj = collection[j];
                for (int k = j + 1; k < prId; k++) {
                    assertNotNull(collection[k]);
                    Person prk = collection[k];
                    if (pri.isLinked(prj) && prj.isLinked(prk) && prk.isLinked(pri)) {
                        cnt++;
                    }
                }
            }
        }
        return cnt;
    }

    public void generatePerson() throws EqualPersonIdException {
        MyPerson pr = new MyPerson(prId, prId + "name", 20);
        network.addPerson(pr);
        MyPerson shadowPr = new MyPerson(prId, prId + "name", 20);
        shadowNetwork.addPerson(shadowPr);
        prId++;
    }

    public void addRelationAndAssert(int id1, int id2) throws PersonIdNotFoundException, EqualRelationException {
        network.addRelation(id1, id2, 1);
        shadowNetwork.addRelation(id1, id2, 1);

        int shadowTripleSum = getShadowTripleSum(shadowNetwork.getPersons());
        tripleSum = getShadowTripleSum(network.getPersons());

        assertEquals(network.queryTripleSum(), shadowTripleSum);
        assertEquals(network.queryTripleSum(), tripleSum);

        assertNotNull(network.getPersons());
        for (int i = 0; i < prId; i++) {
            assertNotNull(network.getPersons()[i]);
            assertTrue(((MyPerson) shadowNetwork.getPersons()[i]).strictEquals(network.getPersons()[i]));
        }
    }
}