import com.oocourse.elevator2.TimableOutput;

public class OutputHandler {
    private static OutputHandler instance = new OutputHandler();

    private OutputHandler() {
    }

    public static OutputHandler getInstance() {
        return instance;
    }

    public synchronized long receive(int eleId, int prId) {
        return TimableOutput.println("RECEIVE-" + prId + "-" + eleId);
    }

    public synchronized long reset(boolean isBegin, int eleId) {
        String activity = isBegin ? "RESET_BEGIN-" : "RESET_END-";
        return TimableOutput.println(activity + eleId);
    }

    public synchronized long arrive(boolean isShadow, int curFloor, int eleId) {
        return TimableOutput.println((isShadow ? "**" : "") + "ARRIVE-" + curFloor + "-" + eleId);
    }

    public synchronized long inOrOut(boolean isShadow, boolean isIn, int prId,
                                     int curFloor, int eleId) {
        String activity = (isIn) ? "IN" : "OUT";
        return TimableOutput.println((isShadow ? "**" : "") + activity + "-" + prId + "-" +
                curFloor + "-" + eleId);
    }

    public synchronized long openOrClose(boolean isShadow, boolean isOpen,
                                         int curFloor, int eleId) {
        String activity = (isOpen) ? "OPEN" : "CLOSE";
        return TimableOutput.println((isShadow ? "**" : "") + activity + "-" +
                curFloor + "-" + eleId);
    }

    public synchronized long empty(int curFloor) {
        return TimableOutput.println("empty" + "-" + curFloor);
    }

}
