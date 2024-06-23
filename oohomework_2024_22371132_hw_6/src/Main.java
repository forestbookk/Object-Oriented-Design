import com.oocourse.elevator2.TimableOutput;

public class Main {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
        new Thread(Scheduler.getInstance(),"Scheduler").start();
    }
}
