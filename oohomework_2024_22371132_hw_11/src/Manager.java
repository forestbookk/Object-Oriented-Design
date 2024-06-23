import com.oocourse.spec3.main.Person;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class Manager {
    private final HashMap<Integer, MyPerson> persons;
    private final DisjointSet disjointSet = new DisjointSet();
    private boolean isValidate = true;
    private HashMap<Integer, HashSet<Integer>> removedEdges = null;
    private HashMap<Integer, HashSet<Integer>> addedEdges = null;
    private HashMap<Integer, Person> addedPersons = null;
    private int tripleSum = 0;
    private int coupleSum = 0;
    private boolean isCoupleSumMo = true;
    private HashMap<Integer, Integer> bestIds;

    public Manager(HashMap<Integer, MyPerson> persons) {
        this.persons = persons;
        this.removedEdges = new HashMap<>();
        this.addedEdges = new HashMap<>();
        this.bestIds = new HashMap<>();
        this.addedPersons = new HashMap<>();
    }

    public void manageAddPerson(int prId, Person person) {
        if (isValidate) {
            disjointSet.add(prId);
        } else {
            addedPersons.put(prId, person);
        }
    }

    public void manageModifyRelation(boolean ifKeep, int id1, int id2,
                                     MyPerson pr1, MyPerson pr2, int value) {
        isCoupleSumMo = true;
        manageBestIds(id1, id2);
        if (pr1.getIncludedTagsSize() < pr2.getIncludedTagsSize()) {
            pr1.notifyValueSum(value, id2);
        } else {
            pr2.notifyValueSum(value, id1);
        }
        if (ifKeep) {
            return;
        } else {
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
    }

    public void manageAddRelation(int id1, int id2, MyPerson pr1, MyPerson pr2, int value) {
        isCoupleSumMo = true;
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
    }

    public boolean isCircle(int id1, int id2) {
        if (!isValidate) {
            buildDisjointSet();
            isValidate = true;
        }
        return disjointSet.find(id1) == disjointSet.find(id2);
    }

    public int queryBlockSum() {
        if (!isValidate) {
            buildDisjointSet();
            isValidate = true;
        }
        return disjointSet.getBlockSum();
    }

    public int queryTripleSum() {
        return this.tripleSum;
    }

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
        /*printAcquaintance();if(coupleSum != jmlQueryCoupleSum()) {
            throw new RuntimeException("Error");}*/
        return this.coupleSum;
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
    }

    public int zeroOneBfs(int src, int dest) {
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
                // 如果未访问过且新距离比当前距离小，则更新距离并将节点加入队列
                if (!vis.get(acqId) && dis.get(curId) + w < dis.get(acqId)) {
                    dis.put(acqId, dis.get(curId) + w);
                    // 如果当前边的权值为 0，放在队首，否则放在队尾
                    deque.addLast(new Node(acqId, dis.get(acqId)));
                }
            }
        }
        return -1; // 如果没有找到最短路径，则返回 -1
    }
}
