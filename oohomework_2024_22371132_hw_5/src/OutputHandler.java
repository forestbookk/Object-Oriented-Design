import com.oocourse.elevator1.TimableOutput;

public class OutputHandler {
    private static OutputHandler instance = new OutputHandler();

    private OutputHandler() {
    }

    public static OutputHandler getInstance() {
        return instance;
    }

    public synchronized void arrive(int curFloor, int eleId) {
        TimableOutput.println("ARRIVE-" + curFloor + "-" + eleId);
    }

    public synchronized void inOrOut(boolean isIn, int prId, int curFloor, int eleId) {
        String activity = (isIn) ? "IN" : "OUT";
        TimableOutput.println(activity + "-" + prId + "-" + curFloor + "-" + eleId);
    }

    public synchronized void openOrClose(boolean isOpen, int curFloor, int eleId) {
        String activity = (isOpen) ? "OPEN" : "CLOSE";
        TimableOutput.println(activity + "-" + curFloor + "-" + eleId);
    }

    public synchronized void empty(int curFloor) {
        TimableOutput.println("empty" + "-" + curFloor);
    }

}
