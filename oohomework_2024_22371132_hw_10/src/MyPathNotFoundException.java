import com.oocourse.spec2.exceptions.PathNotFoundException;

public class MyPathNotFoundException extends PathNotFoundException {
    private final int minId;
    private final int maxId;
    private static Counter counter;

    public MyPathNotFoundException(int id1, int id2) {
        this.minId = Math.min(id1, id2);
        this.maxId = Math.max(id1, id2);
        if (counter == null) {
            counter = new Counter();
        }
    }

    @Override
    public void print() {
        //pnf-x, id1-y, id2-z，x 为此类异常发生的总次数，
        //𝑦
        //y 为 id1 触发此类异常的次数，
        //𝑧
        //z​ 为 id2 触发此类异常的次数。
        //
        //id1 与 id2 相等时，视为 id1 触发了一次此类异常，即相等时不重复计算。
        System.out.println("pnf-" + counter.classTick() + ", " + minId +
                "-" + counter.idTick(minId) + ", " + maxId + "-" + counter.idTick(maxId));
    }
}
