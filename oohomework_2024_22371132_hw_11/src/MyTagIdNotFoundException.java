import com.oocourse.spec3.exceptions.TagIdNotFoundException;

public class MyTagIdNotFoundException extends TagIdNotFoundException {
    public MyTagIdNotFoundException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    private int id;
    private static Counter counter;

    public void print() {
        System.out.println("tinf-" + counter.classTick() +
                ", " + id + "-" + counter.idTick(id));
    }
}
