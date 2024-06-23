import java.util.HashMap;
import java.util.HashSet;

public class Controller {
    private static Controller instance = new Controller();

    private Controller(){}

    private boolean isEnd = false;
    private final HashMap<Integer, HashSet<Person>> requests = new HashMap<>();

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

    /*public synchronized Person popRequest() throws InterruptedException {
        while(requests.isEmpty() && !isEnd) {
            wait();
        }
        if(requests.isEmpty()) {
            return null; // have end TODO: request null 特判
        }
        // TBC
    }*/

    public synchronized boolean isEmpty() {
        return requests.isEmpty();
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

}
