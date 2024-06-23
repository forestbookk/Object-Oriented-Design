import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private int id;
    private static Counter counter;

    public MyEmojiIdNotFoundException(int id) {
        this.id = id;
        if (counter == null) {
            counter = new Counter();
        }
    }

    public void print() {
        System.out.println("einf-" + counter.classTick() + ", " + id + "-" + counter.idTick(id));
    }

}