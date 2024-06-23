import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RequestTable {
    public synchronized void printPerson() {
        if (requests.isEmpty()) {
            System.out.println("***Empty");
        }
        for (Map.Entry<Integer, HashSet<Person>> entry : requests.entrySet()) {
            System.out.println("***In the floor:" + entry.getKey());
            for (Person pr : entry.getValue()) {
                System.out.println("*****people: " + pr.getId());
            }
        }
    }

    private boolean endTag = false;

    public synchronized void setEnd(boolean endTag) {
        notifyAll();
        this.endTag = endTag;
    }

    public synchronized boolean isEnd() {
        return endTag;
    }

    private final HashMap<Integer, HashSet<Person>> requests = new HashMap<>();

    public HashMap<Integer, HashSet<Person>> getRequests() {
        return requests;
    }

    public synchronized void addRequest(Person pr) {
        int prFromFloor = pr.getFromFloor();
        if (requests.containsKey(prFromFloor)) {
            requests.get(prFromFloor).add(pr);
        } else {
            HashSet<Person> persons = new HashSet<>();
            persons.add(pr);
            requests.put(prFromFloor, persons);
        }
        notifyAll();
    }

    public synchronized boolean checkFloorRequest(int floor, boolean direction) {
        if (!requests.containsKey(floor)) {
            return false;
        }
        HashSet<Person> floorRequests = requests.get(floor);
        // check direction
        for (Person pr : floorRequests) {
            if (pr.getDirection() == direction) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean checkForwardRequest(boolean eleDirection, int eleFloor) {
        boolean isForward = false;
        for (Integer fromFloor : requests.keySet()) {
            isForward = eleDirection ? fromFloor > eleFloor : fromFloor < eleFloor;
            if (isForward) {
                break;
            }
        }
        return isForward;
    }

    public synchronized boolean isEmpty() {
        // notifyAll();
        return requests.isEmpty();
    }

    public synchronized void waitRequest() throws InterruptedException {
        wait();
    }
}
