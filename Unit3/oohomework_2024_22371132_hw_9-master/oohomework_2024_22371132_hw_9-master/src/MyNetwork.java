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
    private HashMap<Integer, HashSet<Integer>> edges = null;
    private HashMap<Integer, HashSet<Integer>> doubleEdges = null;
    private HashMap<Integer, HashSet<Integer>> removedEdges = null;
    private HashMap<Integer, HashSet<Integer>> addedEdges = null;
    private HashMap<Integer, Person> addedPersons = null;
    //private int blockSum = 0;
    private int tripleSum = 0;

    public DisjointSet getDisjointSet() {
        return disjointSet;
    }

    public HashMap<Integer, Person> getPersonMap() {
        return personMap;
    }

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

    public void addSafeGuardCollect(int id1, int id2) {
        if (edges == null) {
            edges = new HashMap<>();
        }
        if (doubleEdges == null) {
            doubleEdges = new HashMap<>();
        }
        addEdge(edges, Math.min(id1, id2), Math.max(id1, id2));
        addEdge(doubleEdges, id1, id2);
        addEdge(doubleEdges, id2, id1);
        checkIncreaseOfTriple(id1, id2);
    }

    public void removeSafeGuardCollect(int id1, int id2) {
        checkDecreaseOfTriple(id1, id2);
        removeEdge(edges, Math.min(id1, id2), Math.max(id1, id2));
        removeEdge(doubleEdges, id1, id2);
        removeEdge(doubleEdges, id2, id1);
    }

    public void checkIncreaseOfTriple(int id1, int id2) {
        // id1 和 id2 有无共同的
        HashSet<Integer> union = new HashSet<>(doubleEdges.get(id1));
        union.addAll(doubleEdges.get(id2));
        int overlap = doubleEdges.get(id1).size() + doubleEdges.get(id2).size() - union.size();
        if (overlap > 0) {
            tripleSum += overlap;
        }
    }

    public void checkDecreaseOfTriple(int id1, int id2) {
        HashSet<Integer> union = new HashSet<>(doubleEdges.get(id1));
        union.addAll(doubleEdges.get(id2));
        int overlap = doubleEdges.get(id1).size() + doubleEdges.get(id2).size() - union.size();
        if (overlap > 0) {
            tripleSum -= overlap;
        }
    }

    public void modifyDisjointSet(int id1, int id2) {
        disjointSet.add(id1); // reset
        disjointSet.add(id2); // reset
        dfs(id1, id1, new ArrayList<>());
        if (disjointSet.find(id2) != id1) {
            //blockSum++;
            //System.out.println("**modifyDisjo" + id1 + " : " + id2 + " bs=" + (blockSum));
            dfs(id2, id2, new ArrayList<>());
        }
    }

    public void dfs(int id1, int id2, ArrayList<Integer> visited) {
        visited.add(id2);
        HashMap<Integer, Person> acqOf2 = ((MyPerson) personMap.get(id2)).getAcquaintance();
        for (Integer linked : acqOf2.keySet()) {
            if (visited.contains(linked)) {
                continue;
            }
            int minId = Math.min(linked, id1);
            int maxId = Math.max(linked, id1);
            if (addedEdges != null && !addedEdges.isEmpty() &&
                    addedEdges.containsKey(minId) && addedEdges.get(minId).contains(maxId)) {
                addedEdges.get(minId).remove(maxId);
                if (addedEdges.get(minId).isEmpty()) {
                    addedEdges.remove(minId);
                }
                /*if (disjointSet.find(minId) != disjointSet.find(maxId)) {
                    //blockSum--;
                    //System.out.println("**dfs:" + minId + ":" + maxId + "bs=" + blockSum);
                }*/
            }
            disjointSet.modify(id1, linked);
            dfs(id1, linked, visited);
        }
    }

    public void buildDisjointSet() {
        // TO/DO
        if (addedPersons != null && !addedPersons.isEmpty()) {
            disjointSet.addAll(addedPersons.keySet());
            //blockSum += addedPersons.size();
            //printRoots();
            //System.out.println("**bd " + "bs=" + blockSum);
            addedPersons.clear();
        }

        if (removedEdges != null && !removedEdges.isEmpty()) {
            for (Map.Entry<Integer, HashSet<Integer>> entry : removedEdges.entrySet()) {
                int minId = entry.getKey();
                HashSet<Integer> maxSet = entry.getValue();
                for (Integer maxId : maxSet) {
                    modifyDisjointSet(minId, maxId);
                }
            }
            removedEdges.clear();
        }

        if (addedEdges != null && !addedEdges.isEmpty()) {
            for (Map.Entry<Integer, HashSet<Integer>> entry : addedEdges.entrySet()) {
                int minId = entry.getKey();
                for (Integer maxId : entry.getValue()) {
                    /*System.out.println("***"+minId+maxId+" "+
                    disjointSet.find(minId)+disjointSet.find(maxId));*/
                    /*if (disjointSet.find(minId) != disjointSet.find(maxId)) {
                        //blockSum--;
                        //System.out.println("**buildDis"+ minId+ ":" + maxId + " bs=" + blockSum);
                    }*/
                    disjointSet.merge(minId, maxId);
                }
            }
            addedEdges.clear();
        }
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
                //blockSum++;
                //System.out.println("**ap" + prId + " bs=" + (blockSum));
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
            addSafeGuardCollect(id1, id2);
            if (isValidate) {
                /*if (disjointSet.find(id1) != disjointSet.find(id2)) {
                    //blockSum--;
                    //System.out.println("**ar" + id1 + ":" + id2 + " bs=" + blockSum);
                }*/
                disjointSet.merge(id1, id2);
                //printRoots();
            } else {
                if (addedEdges == null) {
                    addedEdges = new HashMap<>();
                }
                int minId = Math.min(id1, id2);
                int maxId = Math.max(id1, id2);
                //System.out.print("addEdge: addedEdges" + id1 + " " + id2);
                if (containEdge(removedEdges, minId, maxId)) {
                    removeEdge(removedEdges, minId, maxId); // 抵消
                } else {
                    //System.out.println("added");
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
                removeSafeGuardCollect(id1, id2);
                isValidate = false;

                if (removedEdges == null) {
                    removedEdges = new HashMap<>();
                }

                int minId = Math.min(id1, id2);
                int maxId = Math.max(id1, id2);
                //System.out.print("modifyRelation: minId="+minId+" maxId="+maxId);
                if (containEdge(addedEdges, minId, maxId)) {
                    //System.out.println(" addedEdges contains");
                    removeEdge(addedEdges, minId, maxId);
                } else {
                    addEdge(removedEdges, Math.min(id1, id2), Math.max(id1, id2));
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
                //throw RelationNotFoundException e;
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
                //throw RelationNotFoundException e;
                throw new MyRelationNotFoundException(id1, id2);
            }
        }
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (containsPerson(id1) && containsPerson(id2)) {
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
        // TO/DO: DELETE
        // printRoots();
        // printAcquaintance();
        //return blockSum;
        /*if(calculateBlockSumJml() != disjointSet.getBlockSum()) {
            // 这玩意儿不可信，如果你觉得disjointSet建得就有问题的话
            throw new RuntimeException("Wrong Block Sum");
        }*/
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

    /*public void buildDisjointSet() {
        // OLD VER
        HashSet<Integer> toRebuildPoints = new HashSet<>();
        if (removedEdges != null && !removedEdges.isEmpty()) {
            for (Integer key : removedEdges.keySet()) {
                HashMap<Integer, HashSet<Integer>> blocks = disjointSet.getBlocks();
                int root = disjointSet.find(key);
                if (blocks.containsKey(root)) {
                    toRebuildPoints.addAll(blocks.get(root));
                    blocks.remove(root);
                }
            }
            removedEdges.clear();
        }

        for (Integer id : toRebuildPoints) {
            disjointSet.add(id);
        }
        if (addedPersons != null && !addedPersons.isEmpty()) {
            for (Integer newId : addedPersons.keySet()) {
                disjointSet.add(newId);
            }
            addedPersons.clear();
        }
        if (edges != null && !edges.isEmpty()) {
            for (Map.Entry<Integer, HashSet<Integer>> entry : edges.entrySet()) {
                int minId = entry.getKey();
                for (Integer maxId : entry.getValue()) {
                    if (toRebuildPoints.contains(minId) || toRebuildPoints.contains(maxId)) {
                        disjointSet.merge(minId, maxId);
                    }
                }
            }
        }

        if (addedEdges != null && !addedEdges.isEmpty()) {
            for (Map.Entry<Integer, HashSet<Integer>> entry : addedEdges.entrySet()) {
                int minId = entry.getKey();
                for (Integer maxId : entry.getValue()) {
                    disjointSet.merge(minId, maxId);
                }
            }
            addedEdges.clear();
        }
    }*/
}
