import com.oocourse.spec3.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private int id;
    private static Counter counter;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    public void print() {
        System.out.println("emi-" + counter.classTick() + ", " + id + "-" + counter.idTick(id));
    }
}
