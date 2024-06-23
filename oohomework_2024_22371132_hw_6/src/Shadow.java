import com.oocourse.elevator2.ResetRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Shadow {
    private long time = 0;
    private static final int resetTime = 1200;
    private static final int openTime = 200;
    private static final int closeTime = 200;
    private int id;
    private int maxCapacity;
    private int moveTime;
    private int capacity = 0;
    private int curFloor;
    private boolean direction;
    //private boolean skipFirstArrive;
    private final Strategy strategy = new Strategy();
    private final RequestTable requestTable;
    private final HashMap<Integer, HashSet<Person>> passengers; // <ToFloor, Persons>

    public Shadow(Elevator elevator, RequestTable requestTable, Person pr) {
        this.id = elevator.getId();
        this.maxCapacity = elevator.getMaxCapacity();
        this.direction = elevator.getDirection();
        this.moveTime = elevator.getMoveTime();
        this.curFloor = elevator.getCurFloor();
        this.requestTable = requestTable.selfDeepClone();
        if (pr != null) {
            this.requestTable.addRequest(pr);
        }
        this.passengers = new DeepClone().mapDeepClone(elevator.getPassengers());
        for (Map.Entry<Integer, HashSet<Person>> entry : passengers.entrySet()) {
            this.capacity += entry.getValue().size();
        }
        //this.skipFirstArrive = elevator.getLastCloseOrArriveOrResetEndStamp() == 0;
    }

    public void reverse() {
        direction = !direction;
    }

    public void move() {
        curFloor = (direction) ? (curFloor + 1) : (curFloor - 1);
        if (curFloor == 11 || curFloor == 1) {
            reverse();
        }
        time += moveTime;
        /*if(skipFirstArrive) {
            skipFirstArrive = false;
        } else {
            time += moveTime;
        }*/
        //OutputHandler.getInstance().arrive(true, curFloor, id);
    }

    public void openAndClose() {
        //OutputHandler.getInstance().openOrClose(true, true, curFloor, id);
        time += openTime + closeTime;
        // pick people
        HashSet<Person> newPassengers;
        newPassengers = strategy.pickPeople(direction, curFloor,
                maxCapacity - capacity, requestTable);
        // in & out
        in(newPassengers);
        out();
        // check whether to reverse
        if (strategy.getAdvice((capacity < maxCapacity), curFloor, direction,
                requestTable, passengers) == Advice.REVERSE) {
            reverse();
            // in
            newPassengers = (strategy.pickPeople(direction, curFloor,
                    maxCapacity - capacity, requestTable));
            in(newPassengers);
        }
        // close
        //OutputHandler.getInstance().openOrClose(true, false, curFloor, id);
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

    public void in(HashSet<Person> newPassengers) {
        for (Person pr : newPassengers) {
            addPassenger(pr);
            //OutputHandler.getInstance().inOrOut(true, true, pr.getId(), curFloor, id);
            capacity++;
        }
        if (capacity > maxCapacity) {
            throw new RuntimeException("Shadow: Capacity Bigger Than Max");
        }
    }

    public void out() {
        if (!passengers.containsKey(curFloor)) {
            return;
        }
        HashSet<Person> outPersons = passengers.get(curFloor);
        //System.out.println("***Shadow capacity:"+capacity+", passengers Num: "+passengers.size());
        /*for (Person pr : outPersons) {
            OutputHandler.getInstance().inOrOut(true, false, pr.getId(), curFloor, id);
            capacity--;
        }*/
        capacity -= outPersons.size();
        ;
        if (capacity < 0) {
            throw new RuntimeException("Shadow: Minus Capacity");
        }
        passengers.remove(curFloor);
    }

    public void reset(ResetRequest rr) {
        if (!passengers.isEmpty()) {
            // open
            // 1.当前楼层即乘客目的地，直接放出
            // 2.中途下梯——倒入请求池（好像无法倒入）
            capacity = 0;
            passengers.clear();
            time += openTime + closeTime;
            // close
        }
        this.maxCapacity = rr.getCapacity();
        this.moveTime = (int) (rr.getSpeed() * 1000);
        requestTable.shadowClear();
        time += resetTime;
        requestTable.setReset(false, null);
    }

    public long getTime() {
        while (true) {
            Advice advice = strategy.getAdvice(capacity < maxCapacity, curFloor,
                    direction, requestTable, passengers);
            //System.out.println("SHADOW: "+advice);
            if (advice == Advice.OVER) {
                break;
            } else if (advice == Advice.REVERSE) {
                reverse();
            } else if (advice == Advice.MOVE) {
                move();
            } else if (advice == Advice.WAIT) {
                break;
            } else if (advice == Advice.OPEN) {
                openAndClose();
            } else if (advice == Advice.RESET) {
                reset(requestTable.getResetRequest());
            } else {
                throw new RuntimeException("Shadow Wrong Advice");
            }
        }
        return this.time;
    }
}
