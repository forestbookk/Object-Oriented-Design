import com.oocourse.elevator1.TimableOutput;

public class Main {

    public static void main(String[] args) throws Exception {
        //final long startTime = System.currentTimeMillis();
        TimableOutput.initStartTimestamp();
        InputHandler inputHandler = new InputHandler();
        inputHandler.start();
        //inputHandler.join();
        //final long endTime = System.currentTimeMillis();
        //long period = endTime - startTime;
        //System.out.println("Total Time: "+period);
    }

}
