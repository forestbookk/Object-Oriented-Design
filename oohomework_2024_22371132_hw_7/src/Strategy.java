import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Strategy {
    private final RequestTable requestTable;
    private final TransferPoint transferPoint;

    public TransferPoint getTransferPoint() {
        return transferPoint;
    }

    private final int transferFloor;

    public int getTransferFloor() {
        return transferFloor;
    }

    private final CarType type;

    public CarType getType() {
        return type;
    }

    private final int upFloorLimit;
    private final int downFloorLimit;

    public Strategy(int transferFloor, RequestTable requestTable,
                    int upFloorLimit, int downFloorLimit) {
        this.requestTable = requestTable;
        this.upFloorLimit = upFloorLimit;
        this.downFloorLimit = downFloorLimit;
        this.transferFloor = transferFloor;
        transferPoint = null;
        type = null;
    }

    public Strategy(int transferFloor, RequestTable requestTable,
                    TransferPoint transferPoint, CarType type,
                    int upFloorLimit, int downFloorLimit) {
        this.requestTable = requestTable;
        this.transferPoint = transferPoint;
        this.transferFloor = transferFloor;
        this.type = type;
        this.upFloorLimit = upFloorLimit;
        this.downFloorLimit = downFloorLimit;
    }

    public boolean needToTransfer(int floor, HashMap<Integer, HashSet<Person>> passengers) {
        if (type == null || transferPoint == null) {
            return false;
        }
        if (floor != transferPoint.getTransferFloor()) {
            return false;
        }
        final int checkDownLimit = type == CarType.A ? (transferFloor + 1) : 1;
        final int checkUpLimit = type == CarType.A ? 11 : (transferFloor - 1);
        for (int i = checkDownLimit; i <= checkUpLimit; i++) {
            if (passengers.containsKey(i) && !passengers.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean needGetWay(int floor) {
        return transferPoint != null && floor == transferFloor;
    }

    public Advice getAdvice(boolean canIn, int floor, boolean direction,
                            HashMap<Integer, HashSet<Person>> passengers) {
        // Priority: RESET
        if (requestTable.isReset()) {
            return Advice.RESET;
        }
        // whether to open the door
        // check whether passengers need to transfer when the elevator is CarElevator
        if (needToTransfer(floor, passengers)) {
            return Advice.TRANSFER;
        }
        // check whether passengers out this floor
        if (passengers.containsKey(floor)) {
            return Advice.OPEN;
        }
        // check whether there are requests this floor and elevator allow new passengers in
        if ((canIn) && requestTable.checkFloorRequest(floor, direction)) {
            return Advice.OPEN;
        }

        // whether there are passengers in the elevator
        // have passengers
        if (!passengers.isEmpty()) {
            //System.out.println("passengers");
            return Advice.MOVE;
        }
        // no passengers, then check requestTable
        // is empty
        if (requestTable.isEmpty()) {
            if (needGetWay(floor)) {
                //System.out.println("transferPoint");
                return Advice.MOVE;
            } else if (requestTable.isSchedulerEnd()) {
                // 是CarElevator且SelfMark-Move
                if (type != null && transferPoint != null && transferPoint.isSelfMark(type)) {
                    return Advice.MOVE;
                }
                return Advice.OVER;
            } else {
                //System.out.println(requestTable.isSchedulerEnd());
                return Advice.WAIT;
            }
        }

        // not empty
        if (requestTable.checkForwardRequest(direction, floor)) {
            //System.out.println("checkForwardRequest");
            return Advice.MOVE;
        } else {
            return Advice.REVERSE;
        }
    }

    public HashSet<Person> pickPeople(boolean direction, int curFloor, int pickNum,
                                      RequestTable requestTable) {
        synchronized (requestTable) {
            HashSet<Person> newPassengers = new HashSet<>();
            HashMap<Integer, HashSet<Person>> dirRequests = requestTable.getDirRequests(direction);
            if (!dirRequests.containsKey(curFloor)) {
                return newPassengers;
            }
            HashSet<Person> floorRequest = dirRequests.get(curFloor);
            Iterator<Person> it = floorRequest.iterator();
            int factNum = 0;
            while (factNum < pickNum && it.hasNext()) {
                Person pr = it.next();
                newPassengers.add(pr);
                it.remove();
                factNum++;
            }
            if (floorRequest.isEmpty()) {
                dirRequests.remove(curFloor);
            }
            /*if(requestTable.isEmpty()){
                OutputHandler.getInstance().empty(curFloor);
            }*/

            return newPassengers;
        }
    }

}
