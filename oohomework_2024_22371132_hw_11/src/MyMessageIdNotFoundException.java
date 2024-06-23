
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private int id;
    private static Counter counter;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    public void print() {
        System.out.println("minf-" + counter.classTick() + ", " + id + "-" + counter.idTick(id));
    }
}