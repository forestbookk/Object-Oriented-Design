import com.oocourse.spec2.exceptions.RelationNotFoundException;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private final int minId;
    private final int maxId;
    private static Counter counter;

    public MyRelationNotFoundException(int id1, int id2) {
        this.minId = Math.min(id1, id2);
        this.maxId = Math.max(id1, id2);
        if (counter == null) {
            counter = new Counter();
        }
    }

    @Override
    public void print() {
        //输出格式：rnf-x, id1-y, id2-z，
        //𝑥
        //x 为此类异常发生的总次数，
        //𝑦
        //y 为 id1 触发此类异常的次数，
        //𝑧
        //z 为 id2 触发此类异常的次数。
        //
        //输出中的 id1，id2 按数值大小排序，由小到大输出。
        System.out.println("rnf-" + counter.classTick() + ", " + minId +
                "-" + counter.idTick(minId) + ", " + maxId + "-" + counter.idTick(maxId));
    }
}
