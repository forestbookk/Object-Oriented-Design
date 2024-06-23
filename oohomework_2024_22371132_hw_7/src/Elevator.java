import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.ResetRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class Elevator implements Runnable {
    private boolean isReset = false; // NORMAL RESET
    private final CarType type;
    private static final int resetTime = 1200;
    private static final int openTime = 200;
    private static final int closeTime = 200;
    private final int downFloorLimit;

    public int getDownFloorLimit() {
        return downFloorLimit;
    }

    public int getUpFloorLimit() {
        return upFloorLimit;
    }

    private final int upFloorLimit;
    private long lastCloseOrArriveOrResetEndStamp;

    public long getLastCloseOrArriveOrResetEndStamp() {
        return lastCloseOrArriveOrResetEndStamp;
    }

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

    private final Strategy strategy;

    public Strategy getStrategy() {
        return strategy;
    }

    private final RequestTable requestTable;
    // <ToFloor, Persons>

    public RequestTable getRequestTable() {
        return requestTable;
    }

    private final HashMap<Integer, HashSet<Person>> passengers = new HashMap<>();

    public HashMap<Integer, HashSet<Person>> getPassengers() {
        return passengers;
    }

    public Elevator(int id, boolean direction, RequestTable requestTable) {
        // only for Normal
        this.id = id;
        this.direction = direction;
        this.requestTable = requestTable;
        this.downFloorLimit = 1;
        this.upFloorLimit = 11;
        this.strategy = new Strategy(0, requestTable, upFloorLimit, downFloorLimit);
        this.lastCloseOrArriveOrResetEndStamp = 0;
        this.type = null;
    }

    public Elevator(int id, boolean direction, RequestTable requestTable,
                    int maxCapacity, int moveTime, int curFloor,
                    int upFloorLimit, int downFloorLimit, TransferPoint transferPoint,
                    CarType type, long lastCloseOrArriveOrResetEndStamp) {
        // only for Double
        this.id = id;
        this.direction = direction;
        this.requestTable = requestTable;
        this.moveTime = moveTime;
        this.maxCapacity = maxCapacity;
        this.curFloor = curFloor;
        this.downFloorLimit = downFloorLimit;
        this.upFloorLimit = upFloorLimit;
        this.type = type;
        this.strategy = new Strategy(transferPoint.getTransferFloor(), requestTable, transferPoint,
                type, upFloorLimit, downFloorLimit);
        this.lastCloseOrArriveOrResetEndStamp = lastCloseOrArriveOrResetEndStamp;
    }

    public void addPassenger(Person pr) {
        int prToFloor = pr.getToFloor();
        synchronized (passengers) {
            if (passengers.containsKey(prToFloor)) {
                passengers.get(prToFloor).add(pr);
            } else {
                HashSet<Person> persons = new HashSet<>();
                persons.add(pr);
                passengers.put(prToFloor, persons);
            }
        }
    }

    public void move() {
        curFloor = (direction) ? (curFloor + 1) : (curFloor - 1);
        if (curFloor == downFloorLimit || curFloor == upFloorLimit) {
            reverse();
        }
        long currentStamp = System.currentTimeMillis();
        if (lastCloseOrArriveOrResetEndStamp != 0 &&
                currentStamp - lastCloseOrArriveOrResetEndStamp < moveTime) {
            try {
                Thread.sleep(moveTime - currentStamp + lastCloseOrArriveOrResetEndStamp);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        lastCloseOrArriveOrResetEndStamp = OutputHandler.getInstance().
                arrive(false, curFloor, id, requestTable.getType());
    }

    public void in(HashSet<Person> newPassengers) {
        for (Person pr : newPassengers) {
            addPassenger(pr);
            OutputHandler.getInstance().inOrOut(false, true,
                    pr.getId(), curFloor, id, requestTable.getType());
            capacity++;
        }
        if (capacity > maxCapacity) {
            throw new RuntimeException("Capacity Bigger Than Max");
        }
    }

    public ArrayList<Person> out(boolean isIncompleteOutAndCollect,
                                 int upOutLimit, int downOutLimit) {
        if (passengers.containsKey(curFloor)) {
            HashSet<Person> outPersons = passengers.get(curFloor);
            for (Person pr : outPersons) {
                OutputHandler.getInstance().inOrOut(false,
                        false, pr.getId(), curFloor, id, requestTable.getType());
                capacity--;
            }
            if (capacity < 0) {
                throw new RuntimeException("Minus Capacity");
            }
            synchronized (passengers) {
                passengers.remove(curFloor);
            }
        }
        if (!isIncompleteOutAndCollect) {
            return null;
        }

        ArrayList<Person> nonCompleteRequests = new ArrayList<>();
        for (int i = downOutLimit; i <= upOutLimit; i++) {
            if (!passengers.containsKey(i)) {
                continue;
            }

            for (Person pr : passengers.get(i)) {
                pr.setFromFloor(curFloor);
                OutputHandler.getInstance().inOrOut(false,
                        false, pr.getId(), curFloor, id, requestTable.getType());
                nonCompleteRequests.add(pr);
            }
            capacity -= passengers.get(i).size();
            synchronized (passengers) {
                passengers.remove(i);
            }
        }

        return nonCompleteRequests;
    }

    public ArrayList<Person> openAndClose(boolean isTransfer, int upOutLimit, int downOutLimit) {

        // open & sleep windowTime
        try {
            OutputHandler.getInstance().openOrClose(false,
                    true, curFloor, id, requestTable.getType());
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
        final ArrayList<Person> ret = out(isTransfer, upOutLimit, downOutLimit);
        // check whether to reverse
        if (strategy.getAdvice((capacity < maxCapacity),
                curFloor, direction, passengers) == Advice.REVERSE) {
            reverse();
            // in
            newPassengers = (strategy.pickPeople(direction, curFloor,
                    maxCapacity - capacity, requestTable));
            in(newPassengers);
        }
        // close
        lastCloseOrArriveOrResetEndStamp = OutputHandler.getInstance().
                openOrClose(false, false, curFloor, id, requestTable.getType());
        return ret;
    }

    public int reset(ResetRequest rr) {
        // only for NormalElevator
        isReset = true;
        final ArrayList<Person> returnPersonRequest = new ArrayList<>();
        if (!passengers.isEmpty()) {
            // open
            //System.out.println("reset open");
            final long lastResetOpenStamp = OutputHandler.getInstance().
                    openOrClose(false, true, curFloor, id, requestTable.getType());
            // 1.当前楼层即乘客目的地——直接放出
            // 2.中途下梯——倒入请求池
            returnPersonRequest.addAll(out(true, upFloorLimit, downFloorLimit));
            if (capacity != 0 || !passengers.isEmpty()) {
                throw new RuntimeException("Reset Clear Passengers Failure");
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
            OutputHandler.getInstance().openOrClose(false,
                    false, curFloor, id, requestTable.getType());
        }

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
        if (rr instanceof NormalResetRequest) {
            NormalResetRequest nr = (NormalResetRequest) rr;
            this.maxCapacity = nr.getCapacity();
            this.moveTime = (int) (nr.getSpeed() * 1000);
        }
        isReset = false;
        // 电梯乘客倒回请求池;侯乘表倒回请求池，必须在RESET-BEGIN后
        returnPersonRequest.addAll(requestTable.getAllPersonRequests());
        //System.out.println("return begin");
        Scheduler.getInstance().returnPool(returnPersonRequest);
        //System.out.println("return end");
        //System.out.println("Elevator "+id+" set requestTable:begin");
        if (rr instanceof DoubleCarResetRequest) {
            Scheduler.getInstance().splitElevator(requestTable, rr,
                    lastCloseOrArriveOrResetEndStamp);
            //System.out.println(String.valueOf(id)+": "+SPLIT-OVER);
            //ret = 1;
            //setRequest在上面的函数做掉！
            return 1;
        }
        Scheduler.getInstance().setRequestTableReset(requestTable, false, null);
        //System.out.println("Elevator "+id+" set requestTable:end");
        /*int ret = 0;
        if (rr instanceof DoubleCarResetRequest) {
            Scheduler.getInstance().splitElevator(requestTable, rr,
                    lastCloseOrArriveOrResetEndStamp);
            //System.out.println(String.valueOf(id)+": "+SPLIT-OVER);
            ret = 1;
        }*/
        return 0;
    }

    public void transfer() {
        throw new RuntimeException("Normal Elevator Shouldn't Transfer");
    }

    @Override
    public void run() {
        //System.out.println(id + "run begin");
        while (true) {
            //System.out.println(id + "loop: ");
            Advice advice = strategy.getAdvice((capacity < maxCapacity),
                    curFloor, direction, passengers);
            //System.out.println(id + ": " + advice);
            if (advice == Advice.OVER) {
                //System.out.println(id + ": " + advice);
                break;
            } else if (advice == Advice.REVERSE) {
                reverse();
            } else if (advice == Advice.MOVE) {
                move();
            } else if (advice == Advice.WAIT) {
                int waitRes = requestTable.waitRequest();
                if (waitRes == 1) {
                    break;
                }
                //requestTable.waitRequest();
            } else if (advice == Advice.OPEN) {
                openAndClose(false, curFloor, curFloor);
            } else if (advice == Advice.RESET) {
                if (reset(requestTable.getResetRequest()) != 0) {
                    break;
                }
            } else if (advice == Advice.TRANSFER) {
                transfer();
            } else {
                throw new RuntimeException("Wrong Advice");
            }
        }
    }
}
