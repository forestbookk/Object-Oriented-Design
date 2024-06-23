import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private int id;
    private static Counter counter;

    public MyEqualEmojiIdException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    public void print() {
        System.out.println("eei-" + counter.classTick() + ", " + id + "-" + counter.idTick(id));
    }

}