import com.oocourse.spec2.exceptions.EqualTagIdException;

public class MyEqualTagIdException extends EqualTagIdException {
    public MyEqualTagIdException(int id) {
        // 输出格式：eti-x, id-y，x 为此类异常发生的总次数，y 为该 id 触发此类异常的次数。
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    private int id;
    private static Counter counter;

    @Override
    public void print() {
        System.out.println("eti-" + counter.classTick() + ", " + id + "-" + counter.idTick(id));
    }
}
