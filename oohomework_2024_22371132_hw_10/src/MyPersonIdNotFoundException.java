import com.oocourse.spec2.exceptions.PersonIdNotFoundException;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private final int id;
    private static Counter counter;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    @Override
    public void print() {
        // 输出格式：pinf-x, id-y，x 为此类异常发生的总次数，y 为该 id 触发此类异常的次数。
        System.out.println("pinf-" + counter.classTick() + ", " + id + "-" + counter.idTick(id));
    }
}
