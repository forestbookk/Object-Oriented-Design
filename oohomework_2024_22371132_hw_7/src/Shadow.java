import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.ResetRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Shadow {
    private final boolean ifDoubleCarTooMuch;
    private final Person person;
    private long time = 0;
    private static final int resetTime = 1200;
    private static final int openTime = 200;
    private static final int closeTime = 200;
    private final int id;
    private int maxCapacity;
    private int moveTime;
    private int capacity = 0;
    private int curFloor;
    private boolean direction;
    private final int downFloorLimit;
    private final int upFloorLimit;
    private boolean skipFirstArrive;
    private final Strategy strategy;
    private final RequestTable requestTable;
    private final HashMap<Integer, HashSet<Person>> passengers; // <ToFloor, Persons>

    public Shadow(Elevator elevator, RequestTable requestTable, Person pr,
                  boolean ifDoubleCarTooMuch, ArrayList<Person> buffer) {
        this.id = elevator.getId();
        this.maxCapacity = elevator.getMaxCapacity();
        this.direction = elevator.getDirection();
        this.moveTime = elevator.getMoveTime();
        this.curFloor = elevator.getCurFloor();
        this.requestTable = requestTable.selfDeepClone();
        if (pr != null) {
            this.requestTable.addRequest(pr, false);
        }
        //System.out.println("****Shadow " + id);
        if (buffer != null) {
            for (Person bufferPerson : buffer) {
                //System.out.println("******buffer " + bufferPerson.getId());
                this.requestTable.addRequest(bufferPerson, false);
            }
        }
        this.passengers = new DeepClone().mapDeepClone(elevator.getPassengers());
        for (Map.Entry<Integer, HashSet<Person>> entry : passengers.entrySet()) {
            this.capacity += entry.getValue().size();
        }
        this.skipFirstArrive = elevator.getLastCloseOrArriveOrResetEndStamp() == 0;
        this.downFloorLimit = elevator.getDownFloorLimit();
        this.upFloorLimit = elevator.getUpFloorLimit();
        Strategy s = elevator.getStrategy();
        if (elevator instanceof CarElevator) {
            this.strategy = new Strategy(s.getTransferFloor(),
                    this.requestTable, s.getTransferPoint(),
                    s.getType(), upFloorLimit, downFloorLimit);
        } else {
            this.strategy = new Strategy(s.getTransferFloor(), this.requestTable,
                    upFloorLimit, downFloorLimit);
        }
        this.person = pr;
        this.ifDoubleCarTooMuch = ifDoubleCarTooMuch;
    }

    public void reverse() {
        direction = !direction;
    }

    public void move() {
        curFloor = (direction) ? (curFloor + 1) : (curFloor - 1);
        if (curFloor == upFloorLimit || curFloor == downFloorLimit) {
            reverse();
        }
        time += moveTime;
        if (skipFirstArrive && strategy.getType() == null) {
            skipFirstArrive = false;
        } else {
            time += moveTime;
        }
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
                passengers) == Advice.REVERSE) {
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
        /*if (capacity > maxCapacity) {
            throw new RuntimeException("Shadow: Capacity Bigger Than Max");
        }*/
    }

    public void out() {
        if (!passengers.containsKey(curFloor)) {
            return;
        }
        HashSet<Person> outPersons = passengers.get(curFloor);
        //System.out.println("***Shadow capacity:"+capacity+", passengers Num: "+passengers.size());
        capacity -= outPersons.size();
        /*if (capacity < 0) {
            throw new RuntimeException("Shadow: Minus Capacity");
        }*/
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
        if (rr instanceof NormalResetRequest) {
            NormalResetRequest nr = (NormalResetRequest) rr;
            this.maxCapacity = nr.getCapacity();
            this.moveTime = (int) (nr.getSpeed() * 1000);
        } else if (rr instanceof DoubleCarResetRequest) {
            DoubleCarResetRequest dr = (DoubleCarResetRequest) rr;
            this.maxCapacity = dr.getCapacity();
            this.moveTime = (int) (dr.getSpeed() * 1000);
            time += ifDoubleCarTooMuch ? 0 : 3L * moveTime + 2 * (openTime + closeTime);
        }
        time += resetTime;
        //requestTable.shadowClear();
        requestTable.setReset(false, null);
    }

    public void transfer() {
        //open & close
        time += (openTime + closeTime);
        out();
        CarType type = strategy.getType();
        int transferFloor = strategy.getTransferFloor();
        long plusTime = 0;
        int cnt = 0;
        final int upOutLimit = type == CarType.A ? 11 : (transferFloor - 1);
        final int downOutLimit = type == CarType.A ? (transferFloor + 1) : 1;
        for (int i = downOutLimit; i <= upOutLimit; i++) {
            if (passengers.containsKey(i)) {
                cnt++;
                plusTime = Math.max(plusTime, (long) Math.abs(i - transferFloor) * moveTime);
                capacity -= passengers.get(i).size();
                passengers.remove(i);
            }
        }
        time += ifDoubleCarTooMuch ? 0 : plusTime + (long) cnt * (openTime + closeTime);
        time += ifDoubleCarTooMuch ? 0 : 3L * moveTime + 2 * (openTime + closeTime);
        //System.out.println("transfer: " + time);
        /*if (time > 10000000) {
            throw new RuntimeException("Shadow Transfer 轮询");
        }*/
        /*if (capacity < 0) {
            throw new RuntimeException("Shadow: Minus Capacity");
        }*/
        //System.out.println("need to transfer: " + strategy.needToTransfer(curFloor, passengers));
    }

    public long getTime() {
        while (true) {
            Advice advice = strategy.getAdvice(capacity < maxCapacity, curFloor,
                    direction, passengers);
            //System.out.println("SHADOW: " + id + " " + advice); // 可能轮询
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
            } else if (advice == Advice.TRANSFER) {
                transfer();
            } else {
                throw new RuntimeException("Shadow Wrong Advice");
            }
        }
        return this.time;
    }
}

