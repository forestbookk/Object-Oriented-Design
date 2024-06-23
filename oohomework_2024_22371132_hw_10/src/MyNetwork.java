import com.oocourse.spec2.exceptions.PathNotFoundException;
import com.oocourse.spec2.exceptions.EqualTagIdException;
import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.TagIdNotFoundException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Deque;
import java.util.LinkedList;

public class MyNetwork implements Network {
    public MyNetwork() {
    }

    private final HashMap<Integer, MyPerson> persons = new HashMap<>();
    private final DisjointSet disjointSet = new DisjointSet();
    private boolean isValidate = true;
    private final HashMap<Integer, HashSet<Integer>> removedEdges = new HashMap<>();
    private final HashMap<Integer, HashSet<Integer>> addedEdges = new HashMap<>();
    private final HashMap<Integer, Person> addedPersons = new HashMap<>();
    private int tripleSum = 0;
    private int coupleSum = 0;
    private boolean isCoupleSumMo = true;
    private final HashMap<Integer, Integer> bestIds = new HashMap<>();

    public Person[] getPersons() {
        return null;
    }

    public void manageBestIds(int id1, int id2) {
        bestIds.remove(id1);
        bestIds.remove(id2);
        if (!persons.get(id1).getAcquaintance().isEmpty()) {
            int bestId1 = persons.get(id1).getBestAcquaintance();
            bestIds.put(id1, bestId1);
        }
        if (!persons.get(id2).getAcquaintance().isEmpty()) {
            int bestId2 = persons.get(id2).getBestAcquaintance();
            bestIds.put(id2, bestId2);
        }
    }

    public void addEdge(HashMap<Integer, HashSet<Integer>> collection, int id1, int id2) {
        if (collection.isEmpty() || !collection.containsKey(id1)) {
            HashSet<Integer> maxIdSet = new HashSet<>();
            maxIdSet.add(id2);
            collection.put(id1, maxIdSet);
        } else {
            collection.get(id1).add(id2);
        }
    }

    public void removeEdge(HashMap<Integer, HashSet<Integer>> collection, int id1, int id2) {
        if (collection.containsKey(id1) && collection.get(id1).contains(id2)) {
            collection.get(id1).remove(id2);
            if (collection.get(id1).isEmpty()) {
                collection.remove(id1);
            }
        }
    }

    public boolean containEdge(HashMap<Integer, HashSet<Integer>> collection, int id1, int id2) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }
        return collection.containsKey(id1) && collection.get(id1).contains(id2);
    }

    public void manageTripleSum(boolean isIncrease, MyPerson pr1, MyPerson pr2) {
        HashMap<Integer, MyPerson> a1 = pr1.getAcquaintance();
        HashMap<Integer, MyPerson> a2 = pr2.getAcquaintance();
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
        HashMap<Integer, MyPerson> acqOf2 = persons.get(id2).getAcquaintance();
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
        if (!addedPersons.isEmpty()) {
            disjointSet.addAll(addedPersons.keySet());
            addedPersons.clear();
        }

        if (!removedEdges.isEmpty()) {
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
            if (isValidate) {
                disjointSet.add(prId);
            } else {
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
            isCoupleSumMo = true;
            pr1.addAcquaintance(pr2, value);
            pr2.addAcquaintance(pr1, value);
            manageTripleSum(true, pr1, pr2);
            manageBestIds(id1, id2);
            if (pr1.getIncludedTagsSize() < pr2.getIncludedTagsSize()) {
                pr1.notifyValueSum(value, id2);
            } else {
                pr2.notifyValueSum(value, id1);
            }
            if (isValidate) {
                disjointSet.merge(id1, id2);
            } else {
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
            isCoupleSumMo = true;
            if (pr1.queryValue(pr2) + value > 0) {
                int newValue = pr1.queryValue(pr2) + value;
                pr1.addAcquaintance(pr2, newValue);
                pr2.addAcquaintance(pr1, newValue);
                manageBestIds(id1, id2);
                if (pr1.getIncludedTagsSize() < pr2.getIncludedTagsSize()) {
                    pr1.notifyValueSum(newValue, id2);
                } else {
                    pr2.notifyValueSum(newValue, id1);
                }
            } else {
                int oldValue = pr1.queryValue(pr2); // 注意时序
                if (pr1.getIncludedTagsSize() < pr2.getIncludedTagsSize()) {
                    pr1.notifyValueSum(-oldValue, id2);
                } else {
                    pr2.notifyValueSum(-oldValue, id1);
                }
                pr1.removeAcquaintance(pr2);
                pr2.removeAcquaintance(pr1);
                pr1.removePersonFromTags(pr2);
                pr2.removePersonFromTags(pr1);
                manageTripleSum(false, pr1, pr2);
                manageBestIds(id1, id2);
                isValidate = false;
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
        if (isCoupleSumMo) {
            int res = 0;
            for (Map.Entry<Integer, Integer> entry : bestIds.entrySet()) {
                if (entry.getKey() >= entry.getValue()) {
                    continue;
                }
                if (bestIds.containsKey(entry.getValue()) &&
                        Objects.equals(bestIds.get(entry.getValue()), entry.getKey())) {
                    res++; // System.out.println("couple: "+entry.getKey()+" "+entry.getValue());
                }
            }
            isCoupleSumMo = false;
            coupleSum = res;
        }
        return this.coupleSum;
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
            return zeroOneBfs(id1, id2) - 1;
        }
    }

    private int zeroOneBfs(int src, int dest) {
        HashMap<Integer, Integer> dis = new HashMap<>();
        HashMap<Integer, Boolean> vis = new HashMap<>();
        Deque<Node> deque = new LinkedList<>();
        for (Integer i : persons.keySet()) {
            dis.put(i, Integer.MAX_VALUE);
            vis.put(i, false);
        }
        dis.put(src, 0);
        deque.add(new Node(src, 0));
        while (!deque.isEmpty()) {
            Node cur = deque.poll();
            int curId = cur.getId();
            if (curId == dest) {
                return cur.getDist(); // 找到目标节点，直接返回最短距离
            }
            if (vis.get(curId)) {
                continue;
            }
            vis.put(curId, true);
            MyPerson person = persons.get(curId);
            for (Map.Entry<Integer, MyPerson> entry : person.getAcquaintance().entrySet()) {
                int acqId = entry.getKey();
                int w = 1; // 因为是 01 BFS，边的权值都是 1
                if (!vis.get(acqId) && dis.get(curId) + w < dis.get(acqId)) {
                    dis.put(acqId, dis.get(curId) + w);
                    deque.addLast(new Node(acqId, dis.get(acqId)));
                }
            }
        }
        return -1; // 如果没有找到最短路径，则返回 -1
    }
}