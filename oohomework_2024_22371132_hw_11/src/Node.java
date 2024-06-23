public class Node implements Comparable<Node> {
    public Node(int id, int dis) {
        this.id = id;
        this.dis = dis;
    }

    private final int id;
    private final int dis;

    public int getId() {
        return id;
    }

    public int getDist() {
        return dis;
    }

    @Override
    public int compareTo(Node o) {
        return this.dis - o.dis;
    }
}
