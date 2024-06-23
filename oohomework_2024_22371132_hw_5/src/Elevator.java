import java.util.HashMap;
import java.util.HashSet;

public class Elevator implements Runnable {
    private static final int maxCapacity = 6;
    private static final int moveTime = 400;
    private static final int openTime = 200;
    private static final int closeTime = 200;

    private int id;
    private int capacity = 0;
    private int curFloor = 1;
    private boolean direction;
    private final Strategy strategy = new Strategy();
    private final RequestTable requestTable;
    private HashMap<Integer, HashSet<Person>> passengers = new HashMap<>(); // <ToFloor, Persons>

    public Elevator(int id, boolean direction, RequestTable requestTable) {
        this.id = id;
        this.direction = direction;
        this.requestTable = requestTable;
    }

    public void move() throws InterruptedException {
        curFloor = (direction) ? (curFloor + 1) : (curFloor - 1);
        if (curFloor == 11 || curFloor == 1) {
            reverse();
        }
        Thread.sleep(moveTime);
        OutputHandler.getInstance().arrive(curFloor, id);
    }

    public void open() {
        OutputHandler.getInstance().openOrClose(true, curFloor, id);
        //Thread.sleep(openTime);
    }

    public void close() {
        //Thread.sleep(closeTime);
        OutputHandler.getInstance().openOrClose(false, curFloor, id);
    }

    public void in(HashSet<Person> newPassengers) {
        for (Person pr : newPassengers) {
            addPassenger(pr);
            OutputHandler.getInstance().inOrOut(true, pr.getId(), curFloor, id);
            capacity++;
        }
        if (capacity > maxCapacity) {
            throw new RuntimeException("Capacity Bigger Than Max");
        }
    }

    public void out() {
        if (!passengers.containsKey(curFloor)) {
            return;
        }
        HashSet<Person> outPersons = passengers.get(curFloor);
        for (Person pr : outPersons) {
            OutputHandler.getInstance().inOrOut(false, pr.getId(), curFloor, id);
            capacity--;
        }
        if (capacity < 0) {
            throw new RuntimeException("Minus Capacity");
        }
        passengers.remove(curFloor);
    }

    public void reverse() {
        direction = !direction;
    }

    public void addPassenger(Person pr) {
        int prToFloor = pr.getToFloor();
        if (passengers.containsKey(prToFloor)) {
            passengers.get(prToFloor).add(pr);
        } else {
            HashSet<Person> persons = new HashSet<>();
            persons.add(pr);
            passengers.put(prToFloor, persons);
        }
    }

    @Override
    public void run() {
        while (true) {
            /*requestTable.printPerson();*/
            Advice advice = strategy.getAdvice((capacity < maxCapacity), curFloor,
                    direction, requestTable, passengers);
            /*System.out.println(advice);*/
            if (advice == Advice.MOVE) {
                try {
                    move();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (advice == Advice.OVER) {
                break;
            } else if (advice == Advice.REVERSE) {
                reverse();
            } else if (advice == Advice.OPEN) {
                // open & sleep windowTime
                try {
                    open();
                    Thread.sleep(openTime + closeTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // pick people
                HashSet<Person> newPassengers;
                newPassengers = strategy.pickPeople(direction, curFloor,
                        maxCapacity - capacity, requestTable.getRequests());
                // in & out
                in(newPassengers);
                out();
                // check whether to reverse
                if (strategy.getAdvice((capacity < maxCapacity), curFloor, direction,
                        requestTable, passengers) == Advice.REVERSE) {
                    reverse();
                    // in
                    newPassengers = (strategy.pickPeople(direction, curFloor,
                            maxCapacity - capacity, requestTable.getRequests()));
                    in(newPassengers);
                }
                // close
                close();
            } else if (advice == Advice.WAIT) {
                try {
                    requestTable.waitRequest();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Wrong Advice");
            }
        }
    }
}
