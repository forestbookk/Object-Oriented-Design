import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;

public class MyAcquaintanceNotFoundException extends AcquaintanceNotFoundException {
    public MyAcquaintanceNotFoundException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    private int id;
    private static Counter counter = new Counter();

    @Override
    public void print() {
        System.out.println("anf-" + counter.classTick() +
                ", " + id + "-" + counter.idTick(id));
    }
}
