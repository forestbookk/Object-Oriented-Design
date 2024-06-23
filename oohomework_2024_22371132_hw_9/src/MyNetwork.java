import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MyNetwork implements Network {
    public MyNetwork() {

    }

    private final HashMap<Integer, Person> personMap = new HashMap<>();
    private final ArrayList<Integer> ids = new ArrayList<>();
    private final DisjointSet disjointSet = new DisjointSet();
    private boolean isValidate = true;
    private HashMap<Integer, HashSet<Integer>> removedEdges = null;
    private HashMap<Integer, HashSet<Integer>> addedEdges = null;
    private HashMap<Integer, Person> addedPersons = null;
    private int tripleSum = 0;

    public void addEdge(HashMap<Integer, HashSet<Integer>> collection, int id1, int id2) {
        // for edges: id1 is smaller than id2
        // for doubleEdges: 无关大小
        if (collection.isEmpty() || !collection.containsKey(id1)) {
            HashSet<Integer> maxIdSet = new HashSet<>();
            maxIdSet.add(id2);
            collection.put(id1, maxIdSet);
        } else {
            collection.get(id1).add(id2);
        }
    }

    public void removeEdge(HashMap<Integer, HashSet<Integer>> collection, int id1, int id2) {
        if (collection == null) {
            throw new RuntimeException("Null Edges");
        }
        if (collection.containsKey(id1) && collection.get(id1).contains(id2)) {
            collection.get(id1).remove(id2);
            if (collection.get(id1).isEmpty()) {
                collection.remove(id1);
            }
        } else {
            throw new RuntimeException("Remove Edge: Edge doesn't exit");
        }
    }

    public boolean containEdge(HashMap<Integer, HashSet<Integer>> collection, int id1, int id2) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        return collection.containsKey(id1) && collection.get(id1).contains(id2);
    }

    public void manageTripleSum(boolean isIncrease, MyPerson pr1, MyPerson pr2) {
        HashMap<Integer, Person> a1 = pr1.getAcquaintance();
        HashMap<Integer, Person> a2 = pr2.getAcquaintance();
        if (a1.size() <= a2.size()) {
            for (Integer key : a1.keySet()) {
                if (a2.containsKey(key)) {
                    tripleSum += isIncrease ? 1 : -1;
                }
            }
        } else {
            for (Integer key : a2.keySet()) {
                if (a1.containsKey(key)) {
                    tripleSum += isIncrease ? 1 : -1;
                }
            }
        }
    }

    public HashSet<Integer> dfs(int id1, int id2, HashSet<Integer> visited) {
        visited.add(id2);
        HashMap<Integer, Person> acqOf2 = ((MyPerson) personMap.get(id2)).getAcquaintance();
        for (Integer linked : acqOf2.keySet()) {
            if (visited.contains(linked)) {
                continue;
            }
            disjointSet.modify(id1, linked);
            dfs(id1, linked, visited);
        }
        return visited;
    }

    public void buildDisjointSet() {
        final HashSet<Integer> visited = new HashSet<>();
        if (addedPersons != null && !addedPersons.isEmpty()) {
            disjointSet.addAll(addedPersons.keySet());
            addedPersons.clear();
        }

        if (removedEdges != null && !removedEdges.isEmpty()) {
            for (Map.Entry<Integer, HashSet<Integer>> entry : removedEdges.entrySet()) {
                int minId = entry.getKey();
                HashSet<Integer> maxSet = entry.getValue();
                if (!visited.contains(minId)) {
                    disjointSet.add(minId);
                    for (Integer maxId : maxSet) {
                        if (!visited.contains(maxId)) {
                            disjointSet.add(maxId);
                        }
                    }
                    visited.addAll(dfs(minId, minId, new HashSet<>()));
                }
                for (Integer maxId : maxSet) {
                    if (!visited.contains(maxId) && disjointSet.find(maxId) != minId) {
                        disjointSet.add(maxId);
                        visited.addAll(dfs(maxId, maxId, new HashSet<>()));
                    }
                }
            }
            removedEdges.clear();
        }

        if (addedEdges != null && !addedEdges.isEmpty()) {
            for (Map.Entry<Integer, HashSet<Integer>> entry : addedEdges.entrySet()) {
                int minId = entry.getKey();
                if (visited.contains(minId)) {
                    continue;
                }
                for (Integer maxId : entry.getValue()) {
                    disjointSet.merge(minId, maxId);
                }
            }
            addedEdges.clear();
        }
        /*printRoots();
        System.out.println("**********************************");
        printAcquaintance();*/
    }

    @Override
    public boolean containsPerson(int id) {
        return personMap.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (!containsPerson(id)) {
            return null;
        }
        return personMap.get(id);
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        int prId = person.getId();
        if (!containsPerson(prId)) {
            personMap.put(prId, person);
            ids.add(prId);
            if (isValidate) {
                disjointSet.add(prId);
            } else {
                if (addedPersons == null) {
                    addedPersons = new HashMap<>();
                }
                addedPersons.put(prId, person);
            }
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
            manageTripleSum(true, pr1, pr2);
            if (isValidate) {
                disjointSet.merge(id1, id2);
            } else {
                if (addedEdges == null) {
                    addedEdges = new HashMap<>();
                }
                int minId = Math.min(id1, id2);
                int maxId = Math.max(id1, id2);
                if (containEdge(removedEdges, minId, maxId)) {
                    removeEdge(removedEdges, id1, id2); // 抵消
                    removeEdge(removedEdges, id2, id1); // 抵消
                } else {
                    addEdge(addedEdges, minId, maxId);
                }
            }
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
            if (pr1.queryValue(pr2) + value > 0) {
                int newValue = pr1.queryValue(pr2) + value;
                pr1.addAcquaintance(pr2, newValue);
                pr2.addAcquaintance(pr1, newValue);
            } else {
                pr1.removeAcquaintance(pr2);
                pr2.removeAcquaintance(pr1);
                manageTripleSum(false, pr1, pr2);
                isValidate = false;

                if (removedEdges == null) {
                    removedEdges = new HashMap<>();
                }

                int minId = Math.min(id1, id2);
                int maxId = Math.max(id1, id2);
                if (containEdge(addedEdges, minId, maxId)) {
                    removeEdge(addedEdges, minId, maxId);
                } else {
                    addEdge(removedEdges, id1, id2);
                    addEdge(removedEdges, id2, id1);
                }

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
            if (!isValidate) {
                buildDisjointSet();
                isValidate = true;
            }
            return disjointSet.find(id1) == disjointSet.find(id2);
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
        if (!isValidate) {
            buildDisjointSet();
            isValidate = true;
        }
        return disjointSet.getBlockSum();
    }

    @Override
    public int queryTripleSum() {
        return tripleSum;
    }

    public void printAcquaintance() {
        for (Person pr : personMap.values()) {
            System.out.println("**" + pr.getId() + "**");
            for (Integer id : ((MyPerson) pr).getAcquaintance().keySet()) {
                System.out.print(id + " ");
            }
            System.out.println(" ");
            System.out.println("******");
        }
    }

    public void printRoots() {
        for (Integer id : personMap.keySet()) {
            System.out.println(disjointSet.find(id) + " : " + id);
        }
    }

    public int calculateBlockSumJml() {
        int cnt = 0;
        for (int i = 0; i < ids.size(); i++) {
            boolean canReach = false;
            for (int j = 0; j < i; j++) {
                try {
                    if (isCircle(ids.get(i), ids.get(j))) {
                        canReach = true;
                        break;
                    }
                } catch (PersonIdNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!canReach) {
                cnt++;
            }
        }
        return cnt;
    }

    /*public boolean isCircleJml() {

    }*/

    public int calculateTripleSumJml() {
        int cnt = 0;
        for (int i = 0; i < ids.size(); i++) {
            Person pri = personMap.get(ids.get(i));
            for (int j = i + 1; j < ids.size(); j++) {
                Person prj = personMap.get(ids.get(j));
                for (int k = j + 1; k < ids.size(); k++) {
                    Person prk = personMap.get(ids.get(k));
                    if (pri.isLinked(prj) && prj.isLinked(prk) && prk.isLinked(pri)) {
                        cnt++;
                    }
                }
            }
        }
        if (cnt != tripleSum) {
            throw new RuntimeException("queryTripleSum: Error");
        }
        return cnt;
    }

    public Person[] getPersons() {
        return null;
    }
}

