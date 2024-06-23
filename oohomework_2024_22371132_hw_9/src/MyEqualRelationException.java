import com.oocourse.spec1.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private final int minId;
    private final int maxId;
    private static Counter counter;

    public MyEqualRelationException(int id1, int id2) {
        this.minId = Math.min(id1, id2);
        this.maxId = Math.max(id1, id2);
        if (counter == null) {
            counter = new Counter();
        }
    }

    @Override
    public void print() {
        //输出格式：er-x, id1-y, id2-z，
        //𝑥
        //x 为此类异常发生的总次数，
        //𝑦
        //y 为id1 触发此类异常的次数，
        //𝑧
        //z 为 id2 触发此类异常的次数。
        //输出中的 id1，id2 按数值大小排序，由小到大输出。
        //id1 与 id2 相等时，视为 id1 触发了一次此类异常，即相等时不重复计算。
        int classCnt = counter.classTick();
        int minIdCnt = counter.idTick(minId);
        int maxIdCnt;
        if (maxId == minId) {
            maxIdCnt = minIdCnt;
        } else {
            maxIdCnt = counter.idTick(maxId);
        }
        System.out.println("er-" + classCnt + ", " + minId +
                "-" + minIdCnt + ", " + maxId + "-" + maxIdCnt);
    }
}
