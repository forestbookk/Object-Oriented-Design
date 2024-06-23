import com.oocourse.elevator2.ResetRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class RequestTable {

    public RequestTable(HashMap<Integer, HashSet<Person>> ur, HashMap<Integer, HashSet<Person>> dr,
                        boolean resetTag, ResetRequest resetRequest) {
        this.upRequests = ur;
        this.downRequests = dr;
        this.resetTag = resetTag;
        this.resetRequest = resetRequest;
    }

    private ResetRequest resetRequest;

    public synchronized ResetRequest getResetRequest() {
        return resetRequest;
    }

    private boolean resetTag;

    public synchronized void setReset(boolean resetTag, ResetRequest resetRequest) {
        // 1. Scheduler线程调用 2.Shadow调用（线程安全）
        //System.out.println("RequestTable set reset: "+resetTag);
        this.resetTag = resetTag;
        this.resetRequest = resetRequest;
        notifyAll();
    }

    public synchronized boolean isReset() {
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

    public synchronized void addRequest(Person pr) {
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
        notifyAll();
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
        return upRequests.isEmpty() && downRequests.isEmpty();
    }

    public synchronized void waitRequest() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        return rets;
    }

    public synchronized void shadowClear() {
        // only for shadow
        upRequests.clear();
        downRequests.clear();
    }

    public synchronized RequestTable selfDeepClone() {
        DeepClone deepClone = new DeepClone();
        return new RequestTable(deepClone.mapDeepClone(upRequests),
                deepClone.mapDeepClone(downRequests), resetTag, resetRequest);

    }
}
