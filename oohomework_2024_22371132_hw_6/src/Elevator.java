import com.oocourse.elevator2.ResetRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class Elevator implements Runnable {
    private boolean isReset = false;

    public boolean isReset() {
        return isReset;
    }

    private static final int resetTime = 1200;
    private static final int openTime = 200;
    private static final int closeTime = 200;
    private long lastCloseOrArriveOrResetEndStamp = 0;
    private final int id;

    public int getId() {
        return id;
    }

    private int maxCapacity = 6;

    public int getMaxCapacity() {
        return maxCapacity;
    }

    private int moveTime = 400;

    public int getMoveTime() {
        return moveTime;
    }

    private int capacity = 0;

    public int getCapacity() {
        return capacity;
    }

    private int curFloor = 1;

    public int getCurFloor() {
        return curFloor;
    }

    private boolean direction;

    public boolean getDirection() {
        return direction;
    }

    public void reverse() {
        direction = !direction;
    }

    private final Strategy strategy = new Strategy();
    private final RequestTable requestTable;
    // <ToFloor, Persons>
    private final HashMap<Integer, HashSet<Person>> passengers = new HashMap<>();

    public HashMap<Integer, HashSet<Person>> getPassengers() {
        return passengers;
    }

    public Elevator(int id, boolean direction, RequestTable requestTable) {
        this.id = id;
        this.direction = direction;
        this.requestTable = requestTable;
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

    public void move() {
        curFloor = (direction) ? (curFloor + 1) : (curFloor - 1);
        if (curFloor == 11 || curFloor == 1) {
            reverse();
        }
        // TODO: 量子电梯
        long currentStamp = System.currentTimeMillis();
        if (lastCloseOrArriveOrResetEndStamp != 0 &&
                currentStamp - lastCloseOrArriveOrResetEndStamp < moveTime) {
            try {
                Thread.sleep(moveTime - currentStamp + lastCloseOrArriveOrResetEndStamp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        lastCloseOrArriveOrResetEndStamp = OutputHandler.getInstance().arrive(false, curFloor, id);
    }

    public void in(HashSet<Person> newPassengers) {
        for (Person pr : newPassengers) {
            addPassenger(pr);
            OutputHandler.getInstance().inOrOut(false, true, pr.getId(), curFloor, id);
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
            OutputHandler.getInstance().inOrOut(false, false, pr.getId(), curFloor, id);
            capacity--;
        }
        if (capacity < 0) {
            throw new RuntimeException("Minus Capacity");
        }
        passengers.remove(curFloor);
    }

    public void openAndClose() {
        //long lastOpenTimeStamp;
        // open & sleep windowTime
        try {
            //lastOpenTimeStamp = TimableOutput.println("OPEN" + "-" + curFloor + "-" + id);
            OutputHandler.getInstance().openOrClose(false, true, curFloor, id);
            Thread.sleep(openTime + closeTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        lastCloseOrArriveOrResetEndStamp = OutputHandler.getInstance().
                openOrClose(false, false, curFloor, id);
    }

    public void reset(ResetRequest rr) {
        isReset = true;
        final ArrayList<Person> returnPersonRequest = new ArrayList<>();
        if (!passengers.isEmpty()) {
            // open
            final long lastResetOpenStamp = OutputHandler.getInstance().
                    openOrClose(false, true, curFloor, id);
            // 1.当前楼层即乘客目的地——直接放出
            out();
            // 2.中途下梯——倒入请求池
            Iterator<Map.Entry<Integer, HashSet<Person>>> itMap = passengers.entrySet().iterator();
            while (itMap.hasNext()) {
                Iterator<Person> itSet = itMap.next().getValue().iterator();
                while (itSet.hasNext()) {
                    Person pr = itSet.next();
                    pr.setFromFloor(curFloor);
                    OutputHandler.getInstance().inOrOut(false, false, pr.getId(), curFloor, id);
                    returnPersonRequest.add(pr);
                    itSet.remove();
                }
                itMap.remove();
            }
            capacity = 0;
            if (!passengers.isEmpty()) {
                throw new RuntimeException("People in Elevator when Reset");
            }

            long currentStamp = System.currentTimeMillis();
            if (currentStamp - lastResetOpenStamp < openTime + closeTime) {
                try {
                    Thread.sleep(openTime + closeTime - currentStamp + lastResetOpenStamp);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // close
            OutputHandler.getInstance().openOrClose(false, false, curFloor, id);
        }

        this.maxCapacity = rr.getCapacity();
        this.moveTime = (int) (rr.getSpeed() * 1000);
        long lastResetBeginStamp = OutputHandler.getInstance().reset(true, id);
        long currentStamp = System.currentTimeMillis();
        if (currentStamp - lastResetBeginStamp < resetTime) {
            try {
                Thread.sleep(resetTime - currentStamp + lastResetBeginStamp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        lastCloseOrArriveOrResetEndStamp = OutputHandler.getInstance().reset(false, id);
        isReset = false;
        // 电梯乘客倒回请求池;侯乘表倒回请求池，必须在RESET-BEGIN后
        returnPersonRequest.addAll(requestTable.getAllPersonRequests());
        //System.out.println("return begin");
        Scheduler.getInstance().returnPool(returnPersonRequest);
        //System.out.println("return end");
        //requestTable.setReset(false, null); 应由调度器还原requestTable的reset属性。顺便notifyAll
        Scheduler.getInstance().setRequestTableReset(requestTable, false, null);
    }

    @Override
    public void run() {
        while (true) {
            Advice advice = strategy.getAdvice((capacity < maxCapacity), curFloor,
                    direction, requestTable, passengers);
            //System.out.println(String.valueOf(id)+": "+advice);
            if (advice == Advice.OVER) {
                //System.out.println(String.valueOf(id)+": "+advice);
                break;
            } else if (advice == Advice.REVERSE) {
                reverse();
            } else if (advice == Advice.MOVE) {
                move();
            } else if (advice == Advice.WAIT) {
                requestTable.waitRequest();
            } else if (advice == Advice.OPEN) {
                openAndClose();
            } else if (advice == Advice.RESET) {
                reset(requestTable.getResetRequest());
            } else {
                throw new RuntimeException("Wrong Advice");
            }
        }
    }
}
