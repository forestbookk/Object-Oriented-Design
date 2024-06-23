import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Strategy {

    public Advice getAdvice(boolean canIn, int floor, boolean direction, RequestTable requestTable,
                            HashMap<Integer, HashSet<Person>> passengers) {
        // Priority: RESET
        if (requestTable.isReset()) {
            return Advice.RESET;
        }
        // whether to open the door
        // check whether passengers out this floor
        if (passengers.containsKey(floor)) {
            return Advice.OPEN;
        }
        // check whether there are requests this floor and elevator allow new passengers in
        if (requestTable.checkFloorRequest(floor, direction) && (canIn)) {
            return Advice.OPEN;
        }

        // whether there are passengers in the elevator
        // no passengers
        if (!passengers.isEmpty()) {
            return Advice.MOVE;
        }
        // check requestTable
        // is empty
        if (requestTable.isEmpty()) {
            if (requestTable.isSchedulerEnd()) {
                return Advice.OVER;
            } else {
                return Advice.WAIT;
            }
        }

        // not empty
        if (requestTable.checkForwardRequest(direction, floor)) {
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
