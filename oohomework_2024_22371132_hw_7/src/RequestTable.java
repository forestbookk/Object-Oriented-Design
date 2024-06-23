import com.oocourse.elevator3.ResetRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

public class RequestTable {

    public RequestTable(HashMap<Integer, HashSet<Person>> ur, HashMap<Integer, HashSet<Person>> dr,
                        boolean resetTag, ResetRequest resetRequest, int id, CarType type,
                        int upFloorLimit, int downFloorLimit) {
        this.upRequests = ur;
        this.downRequests = dr;
        this.resetTag = resetTag;
        this.resetRequest = resetRequest;
        this.type = type;
        this.id = id;
        this.upFloorLimit = upFloorLimit;
        this.downFloorLimit = downFloorLimit;
    }

    private final int downFloorLimit;
    private final int upFloorLimit;

    public int getDownFloorLimit() {
        return downFloorLimit;
    }

    public int getUpFloorLimit() {
        return upFloorLimit;
    }

    private boolean isTransfer = false;

    public synchronized boolean isTransfer() {
        return isTransfer;
    }

    public synchronized void setTransfer(boolean isTransfer) {
        this.isTransfer = isTransfer;
        notifyAll();
    }

    private final CarType type;

    public CarType getType() {
        return type;
    }

    private final int id;

    public int getId() {
        return id;
    }

    private ResetRequest resetRequest;

    public synchronized ResetRequest getResetRequest() {
        return resetRequest;
    }

    private boolean resetTag;

    public synchronized void setReset(boolean resetTag, ResetRequest resetRequest) {
        // 1. Scheduler线程调用 2.Shadow调用（线程安全）
        //System.out.println("RequestTable set reset: "+resetTag);
        if (type != null) {
            throw new RuntimeException("Car Elevator Reset");
        }
        this.resetTag = resetTag;
        this.resetRequest = resetRequest;
        notifyAll();
    }

    public boolean isReset() {
        return resetTag;
    }

    private boolean schedulerEnd = false;

    public synchronized void setSchedulerEnd(boolean endTag) {
        this.schedulerEnd = endTag;
        notifyAll();
    }

    public synchronized boolean isSchedulerEnd() {
        return schedulerEnd;
    }

    private final HashMap<Integer, HashSet<Person>> upRequests;
    private final HashMap<Integer, HashSet<Person>> downRequests;

    public synchronized HashMap<Integer, HashSet<Person>> getDirRequests(boolean direction) {
        return direction ? upRequests : downRequests;
    }

    public synchronized void addRequest(Person pr, boolean isNotify) {
        //System.out.println("RequestTable addRequest begin: " + pr.getId());
        if (pr == null) {
            throw new RuntimeException("Person is Null Pointer");
        }
        int prFromFloor = pr.getFromFloor();
        if (pr.getDirection()) {
            if (upRequests.containsKey(prFromFloor)) {
                upRequests.get(prFromFloor).add(pr);
            } else {
                HashSet<Person> persons = new HashSet<>();
                persons.add(pr);
                upRequests.put(prFromFloor, persons);
            }
        } else {
            if (downRequests.containsKey(prFromFloor)) {
                downRequests.get(prFromFloor).add(pr);
            } else {
                HashSet<Person> persons = new HashSet<>();
                persons.add(pr);
                downRequests.put(prFromFloor, persons);
            }
        }

        if (isNotify) {
            //System.out.println("RequestTable addRequest notify");
            notifyAll();
        }
        //System.out.println("RequestTable addRequest end: " + pr.getId());
    }

    public synchronized int size() {
        int size = 0;
        for (Map.Entry<Integer, HashSet<Person>> entry : upRequests.entrySet()) {
            size += entry.getValue().size();
        }
        for (Map.Entry<Integer, HashSet<Person>> entry : downRequests.entrySet()) {
            size += entry.getValue().size();
        }
        return size;
    }

    public synchronized boolean checkFloorRequest(int floor, boolean direction) {
        if (direction && upRequests.containsKey(floor)) {
            return true;
        }
        return (!direction) && downRequests.containsKey(floor);
    }

    public synchronized boolean checkForwardRequest(boolean eleDirection, int eleFloor) {
        boolean isForward = false;
        for (Integer fromFloor : upRequests.keySet()) {
            isForward = eleDirection ? fromFloor > eleFloor : fromFloor < eleFloor;
            if (isForward) {
                break;
            }
        }
        for (Integer fromFloor : downRequests.keySet()) {
            isForward = eleDirection ? fromFloor > eleFloor : fromFloor < eleFloor;
            if (isForward) {
                break;
            }
        }
        return isForward;
    }

    public synchronized boolean isEmpty() {
        return resetRequest == null && upRequests.isEmpty() && downRequests.isEmpty();
    }

    public synchronized int waitRequest() {
        if (schedulerEnd) {
            return 1;
        } else if (!isEmpty()) {
            return 2;
        }
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public synchronized ArrayList<Person> getAllPersonRequests() {
        ArrayList<Person> rets = new ArrayList<>();
        rets.addAll(getSingleMapRequests(upRequests));
        rets.addAll(getSingleMapRequests(downRequests));
        return rets;
    }

    public synchronized ArrayList<Person> getSingleMapRequests(
            HashMap<Integer, HashSet<Person>> requests) {
        ArrayList<Person> rets = new ArrayList<>();
        synchronized (requests) {
            Iterator<Map.Entry<Integer, HashSet<Person>>> itMap = requests.entrySet().iterator();
            while (itMap.hasNext()) {
                Iterator<Person> itSet = itMap.next().getValue().iterator();
                while (itSet.hasNext()) {
                    Person pr = itSet.next();
                    //System.out.println("req return :"+pr.getId());
                    rets.add(pr);
                    itSet.remove();
                }
                itMap.remove();
            }
        }
        return rets;
    }

    public synchronized RequestTable selfDeepClone() {
        DeepClone deepClone = new DeepClone();
        return new RequestTable(deepClone.mapDeepClone(upRequests),
                deepClone.mapDeepClone(downRequests), resetTag,
                resetRequest, id, type, upFloorLimit, downFloorLimit);

    }

    public synchronized void transferNotify() {
        notifyAll();
    }
}
