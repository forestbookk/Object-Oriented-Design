import com.oocourse.spec1.exceptions.EqualPersonIdException;

public class MyEqualPersonIdException extends EqualPersonIdException {
    private int id;
    private static Counter counter;

    public MyEqualPersonIdException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    @Override
    public void print() {
        // 输出格式：epi-x, id-y，
        //𝑥
        //x 为此类异常发生的总次数，
        //𝑦
        //y 为该 id 触发此类异常的次数
        int classCnt = counter.classTick();
        int idCnt = counter.idTick(id);
        System.out.println("epi-" + classCnt + ", " + id + "-" + idCnt);
    }
}
