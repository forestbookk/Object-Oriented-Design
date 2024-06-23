import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Person;
import org.junit.Test;

import static org.junit.Assert.*;

public class MyNetworkTest {
    private static int prId = 0;
    private MyNetwork network;
    private MyNetwork shadowNetwork;
    private int coupleSum = 0;

    public int getShadowCoupleSum(Person[] collection) {
        int res = 0;
        assertNotNull(collection);
        for (int i = 0; i < prId; i++) {
            assertNotNull(collection[i]);
            MyPerson mpi = (MyPerson) collection[i];
            boolean hasAcquaintance = false;
            int bestId = 0;
            int bestValue = 0;
            for (int j = 0; j < prId; j++) {
                assertNotNull(collection[j]);
                if (j != i && collection[j].isLinked(mpi)) {
                    if (!hasAcquaintance) {
                        bestId = j;
                        bestValue = mpi.queryValue(collection[j]);
                        hasAcquaintance = true;
                    } else if ((bestValue < mpi.queryValue(collection[j]))
                            || (bestValue == mpi.queryValue(collection[j]) && bestId > j)) {
                        bestId = j;
                        bestValue = mpi.queryValue(collection[j]);
                    }
                }
            }
            if (!hasAcquaintance) {
                continue;
            }

            boolean flag = false;
            for (int j = 0; j <= i; j++) {
                assertNotNull(collection[j]);
                if (collection[j].getId() == bestId) {
                    flag = true;
                }
            }
            for (int j = i + 1; j < prId; j++) {
                assertNotNull(collection[j]);
                MyPerson mpj = (MyPerson) collection[j];
                if (mpj.getId() == bestId) {
                    flag = true;
                } else {
                    continue;
                }

                hasAcquaintance = false;
                int jbestId = 0;
                int jbestValue = 0;
                for (int k = 0; k < prId; k++) {
                    if (k != j && collection[k].isLinked(mpj)) {
                        if (!hasAcquaintance) {
                            jbestId = k;
                            jbestValue = mpj.queryValue(collection[k]);
                            hasAcquaintance = true;
                        } else if (jbestValue < mpj.queryValue(collection[k])
                                || (jbestValue == mpj.queryValue(collection[k]) && jbestId > k)) {
                            jbestId = k;
                            jbestValue = mpj.queryValue(collection[k]);
                        }
                    }
                }
                if (!hasAcquaintance) {
                    continue;
                }
                if (jbestId == mpi.getId()) {
                    res++;
                }
            }
            assertTrue(flag);
        }
        return res;
    }

    public void generatePerson() throws EqualPersonIdException {
        MyPerson pr = new MyPerson(prId, prId + "name", 20);
        network.addPerson(pr);
        MyPerson shadowPr = new MyPerson(prId, prId + "name", 20);
        shadowNetwork.addPerson(shadowPr);
        prId++;
    }

    public void addRelationAndAssert(int id1, int id2, int value) throws PersonIdNotFoundException, EqualRelationException {
        network.addRelation(id1, id2, value);
        shadowNetwork.addRelation(id1, id2, value);

        int shadowCoupleSum = getShadowCoupleSum(shadowNetwork.getPersons());
        coupleSum = getShadowCoupleSum(network.getPersons());

        assertEquals(network.queryCoupleSum(), shadowCoupleSum);
        assertEquals(network.queryCoupleSum(), coupleSum);

        assertNotNull(network.getPersons());
        for (int i = 0; i < prId; i++) {
            assertNotNull(network.getPersons()[i]);
            assertTrue(((MyPerson) shadowNetwork.getPersons()[i]).strictEquals(network.getPersons()[i]));
        }
    }

    public void modRelationAndAssert(int id1, int id2, int value) throws PersonIdNotFoundException, EqualRelationException, RelationNotFoundException, EqualPersonIdException {
        network.modifyRelation(id1, id2, value);
        shadowNetwork.modifyRelation(id1, id2, value);

        int shadowCoupleSum = getShadowCoupleSum(shadowNetwork.getPersons());
        coupleSum = getShadowCoupleSum(network.getPersons());

        assertEquals(network.queryCoupleSum(), shadowCoupleSum);
        assertEquals(network.queryCoupleSum(), coupleSum);

        assertNotNull(network.getPersons());
        for (int i = 0; i < prId; i++) {
            assertNotNull(network.getPersons()[i]);
            assertTrue(((MyPerson) shadowNetwork.getPersons()[i]).strictEquals(network.getPersons()[i]));
        }
    }

    @Test
    public void queryCoupleSum() throws PersonIdNotFoundException, EqualRelationException, EqualPersonIdException, RelationNotFoundException {
        network = new MyNetwork();
        shadowNetwork = new MyNetwork();
        for (int i = 0; i <= 30; i++) {
            generatePerson();
        }

        assertEquals(0, network.queryCoupleSum());
        assertNotNull(network.getPersons());
        for (int i = 0; i < prId; i++) {
            assertNotNull(network.getPersons()[i]);
            assertTrue(((MyPerson) shadowNetwork.getPersons()[i]).strictEquals(network.getPersons()[i]));
        }
        // 加入一组联系 12
        addRelationAndAssert(1, 2, 1);
        modRelationAndAssert(1, 2, 1);
        modRelationAndAssert(1, 2, -2);
        addRelationAndAssert(1, 2, 1);
        // 加入第二组联系 13
        addRelationAndAssert(1, 3, 2);
        // 加入第三组联系 23，形成三角形123
        addRelationAndAssert(2, 4, 1);
        addRelationAndAssert(2, 5, 1);

        // 124：加入24
        addRelationAndAssert(2, 3, 1);

        addRelationAndAssert(5, 7, 3);
        addRelationAndAssert(5, 6, 3);
        addRelationAndAssert(6, 7, 3);
        modRelationAndAssert(6, 7, 3);
        modRelationAndAssert(6, 7, -10);

        addRelationAndAssert(3, 4, 2);
        addRelationAndAssert(4, 10, 3);
        addRelationAndAssert(5, 10, 5);
        addRelationAndAssert(4, 5, 3);

        addRelationAndAssert(6, 10, 2);
        addRelationAndAssert(7, 10, 3);
        addRelationAndAssert(3, 10, 1);
    }
}