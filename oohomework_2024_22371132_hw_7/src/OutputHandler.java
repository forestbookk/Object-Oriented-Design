import com.oocourse.elevator3.TimableOutput;

public class OutputHandler {
    private static final OutputHandler instance = new OutputHandler();

    private OutputHandler() {
    }

    public static synchronized OutputHandler getInstance() {
        return instance;
    }

    public synchronized void receive(int eleId, int prId, CarType type) {
        TimableOutput.println("RECEIVE-" + prId + "-" + eleId + getCarType(type));
    }

    public synchronized long reset(boolean isBegin, int eleId) {
        String activity = isBegin ? "RESET_BEGIN-" : "RESET_END-";
        return TimableOutput.println(activity + eleId);
    }

    public synchronized long arrive(boolean isShadow, int curFloor, int eleId, CarType type) {
        return TimableOutput.println((isShadow ? "**" : "") + "ARRIVE-" + curFloor + "-" +
                eleId + getCarType(type));
    }

    public synchronized String getCarType(CarType type) {
        if (type == CarType.A) {
            return "-A";
        } else if (type == CarType.B) {
            return "-B";
        }
        return "";
    }

    public synchronized long inOrOut(boolean isShadow, boolean isIn, int prId,
                                     int curFloor, int eleId, CarType type) {
        String activity = (isIn) ? "IN" : "OUT";
        return TimableOutput.println((isShadow ? "**" : "") + activity + "-" + prId + "-" +
                curFloor + "-" + eleId + getCarType(type));
    }

    public synchronized long openOrClose(boolean isShadow, boolean isOpen,
                                         int curFloor, int eleId, CarType type) {
        String activity = (isOpen) ? "OPEN" : "CLOSE";
        return TimableOutput.println((isShadow ? "**" : "") + activity + "-" +
                curFloor + "-" + eleId + getCarType(type));
    }

    public synchronized long empty(int curFloor) {
        return TimableOutput.println("empty" + "-" + curFloor);
    }

    public synchronized void run() {
        TimableOutput.println("Scheduler run");
    }

    public synchronized void chooseBestElevator2() {
        TimableOutput.println("Scheduler chooseBestElevator2");
    }

    public synchronized void chooseBestElevator4() {
        TimableOutput.println("Scheduler chooseBestElevator4");
    }

    public synchronized void addPersonRequest() {
        TimableOutput.println("Scheduler addPersonRequest");
    }

    public synchronized void setInputEnd() {
        TimableOutput.println("Scheduler setInputEnd");
    }

    public synchronized void isAnyReqReset() {
        TimableOutput.println("Scheduler isAnyReqReset");
    }

    public synchronized void isPersonRequestsEmpty() {
        TimableOutput.println("Scheduler isPersonRequestsEmpty");
    }

    public synchronized void isResetRequestsEmpty() {
        TimableOutput.println("Scheduler isResetRequestsEmpty");
    }

    public synchronized void dealResetRequest() {
        TimableOutput.println("Scheduler dealResetRequest");
    }

    public synchronized void isReqTabsAllReset() {
        TimableOutput.println("Scheduler isReqTabsAllReset");
    }

    public synchronized void popPersonRequest() {
        TimableOutput.println("Scheduler popPersonRequest");
    }

    public synchronized void checkWait() {
        TimableOutput.println("Scheduler checkWait");
    }

    public synchronized void dispatchPerson() {
        TimableOutput.println("Scheduler dispatchPerson");
    }

    public synchronized void prNullDeal() {
        TimableOutput.println("Scheduler prNullDeal");
    }

    public synchronized void setRequestTableReset() {
        TimableOutput.println("Scheduler setRequestTableReset");
    }

    public synchronized void print(String str) {
        TimableOutput.println(str);
    }

}
