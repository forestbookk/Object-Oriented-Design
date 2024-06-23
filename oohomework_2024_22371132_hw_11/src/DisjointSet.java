import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DisjointSet {
    public DisjointSet() {
        this.pre = new HashMap<>(2048);
        this.rank = new HashMap<>(2048);
        this.blocks = new HashMap<>(2048);
    }

    private final HashMap<Integer, Integer> pre;
    private final HashMap<Integer, Integer> rank;
    private final HashMap<Integer, HashSet<Integer>> blocks;

    public int getBlockSum() {
        return blocks.size();
    }

    public void add(int id) {
        // set ROOT
        pre.put(id, id);
        rank.put(id, 0);
        HashSet<Integer> block = new HashSet<>();
        block.add(id);
        blocks.put(id, block);
    }

    public int find(int id) {
        int rep = id;
        while (rep != pre.get(rep)) {
            rep = pre.get(rep);
        }

        int now = id;
        while (now != rep) {
            int fa = pre.get(now);
            pre.put(now, rep);
            now = fa;
        }
        return rep;
    }

    public int merge(int id1, int id2) {
        int fa1 = find(id1);
        int fa2 = find(id2);
        if (fa1 == fa2) {
            return -1;
        }
        int rank1 = rank.get(fa1);
        int rank2 = rank.get(fa2);
        if (rank1 < rank2) {
            pre.put(fa1, fa2);
            blocks.get(fa2).addAll(blocks.get(fa1));
            blocks.remove(fa1);
        } else {
            if (rank1 == rank2) {
                rank.put(fa1, rank1 + 1);
            }
            pre.put(fa2, fa1);
            blocks.get(fa1).addAll(blocks.get(fa2));
            blocks.remove(fa2);
        }
        return 0;
    }

    public void modify(int parent, int son) {
        pre.put(son, parent);
        rank.put(parent, 1);
        blocks.get(parent).add(son);
        blocks.remove(son);
    }

    public void addAll(Set<Integer> ids) {
        for (Integer id : ids) {
            pre.put(id, id);
            rank.put(id, 0);
            HashSet<Integer> block = new HashSet<>();
            block.add(id);
            blocks.put(id, block);
        }
    }
}