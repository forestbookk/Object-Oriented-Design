import com.oocourse.elevator3.TimableOutput;

public class Main {

    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        InputHandler inputHandler = new InputHandler();
        new Thread(Scheduler.getInstance(),"Scheduler").start();
        inputHandler.start();
    }
}
