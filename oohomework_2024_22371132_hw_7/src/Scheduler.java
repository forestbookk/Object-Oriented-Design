import com.oocourse.elevator3.DoubleCarResetRequest;
import com.oocourse.elevator3.NormalResetRequest;
import com.oocourse.elevator3.ResetRequest;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

public class Scheduler implements Runnable {
    private final HashMap<Integer, RequestTable> realReqIndexMap = new HashMap<>(); // for Normal
    private final HashMap<Integer, Elevator> realNormalEleIndexMap = new HashMap<>(); // for Normal
    private final HashMap<RequestTable, ArrayList<Person>> buffer = new HashMap<>();
    private int transferRequestsNum = 0;
    private final int inAndNeedTransfer = 1;
    private final int inAndNoTransfer = 0;
    private final int noIn = -1;

    public synchronized int isRequestNeedTransfer(Person pr, int upFloorLimit,
                                                  int downFloorLimit, CarType type) {
        if (type == null) {
            return inAndNoTransfer;
        }
        // 解决：其他地方也要check
        final int prFromFloor = pr.getFromFloor();
        final int prToFloor = pr.getToFloor();
        if (prFromFloor >= downFloorLimit && prFromFloor <= upFloorLimit) {
            if (prToFloor >= downFloorLimit && prToFloor <= upFloorLimit) {
                return inAndNoTransfer;
            } else {
                return inAndNeedTransfer;
            }
        } else {
            return noIn;
        }
    }

    public synchronized void checkIncrease(Person pr, int upFloorLimit,
                                           int downFloorLimit, CarType type) {
        if (isRequestNeedTransfer(pr, upFloorLimit, downFloorLimit, type) == inAndNeedTransfer) {
            transferRequestsNum++;
            //System.out.println("transferNum: " + transferRequestsNum + " in " + pr.getId());
        }
    }

    public synchronized void decrease(Person pr) {
        transferRequestsNum--;
        //System.out.println("transferNum = " + transferRequestsNum + " off: " + pr.getId());
        if (transferRequestsNum == 0 && isInputEnd()) {
            notifyAll();
        } else if (transferRequestsNum < 0) {
            throw new RuntimeException("TransferRequestsNum < 0");
        }
    }

    private int elevatorNum = 6;
    private static final Scheduler instance = new Scheduler();

    public static synchronized Scheduler getInstance() {
        return instance;
    }

    private Scheduler() {
    }

    public synchronized void splitElevator(RequestTable over, ResetRequest rr,
                                           long lastCloseOrArriveOrResetEndStamp) {
        final DoubleCarResetRequest dr = (DoubleCarResetRequest) rr;
        final int transferFloor = dr.getTransferFloor();
        final int downFloorLimitA = (1);
        final int upFloorLimitB = (11);
        final int capacity = dr.getCapacity();
        final int moveTime = (int) (dr.getSpeed() * 1000);
        final int eleId = dr.getElevatorId();
        RequestTable rtA = new RequestTable(new HashMap<>(), new HashMap<>(), false,
                null, eleId, CarType.A, (transferFloor), downFloorLimitA);
        RequestTable rtB = new RequestTable(new HashMap<>(), new HashMap<>(), false,
                null, eleId, CarType.B, upFloorLimitB, (transferFloor));
        TransferPoint tp = new TransferPoint(transferFloor, rtA, rtB);
        CarElevator carA = new CarElevator(eleId, false, rtA, capacity, moveTime,
                transferFloor, tp, lastCloseOrArriveOrResetEndStamp, CarType.A,
                (transferFloor), downFloorLimitA);
        CarElevator carB = new CarElevator(eleId, false, rtB, capacity, moveTime,
                transferFloor, tp, lastCloseOrArriveOrResetEndStamp, CarType.B,
                upFloorLimitB, (transferFloor));

        synchronized (reqTabArray) {
            over.setReset(false, null);
            reqTabArray.remove(realReqIndexMap.get(eleId));
            elevatorArray.remove(realNormalEleIndexMap.get(eleId));
            reqTabArray.add(rtA);
            elevatorArray.add(carA);
            reqTabArray.add(rtB);
            elevatorArray.add(carB);
            elevatorNum++;
            new Thread(carA, eleId + "-A").start();
            new Thread(carB, eleId + "-B").start();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("**" + eleId);
            synchronized (buffer) {
                if (buffer.containsKey(over) && !buffer.get(over).isEmpty()) {
                    for (Person pr : buffer.get(over)) {
                        //System.out.println("buffer :" + pr.getId());
                        if (isRequestNeedTransfer(pr, transferFloor,
                                downFloorLimitA, CarType.A) == inAndNoTransfer) {
                            receiveAndAdd(rtA, pr, true);
                        } else if (isRequestNeedTransfer(pr, upFloorLimitB,
                                transferFloor, CarType.B) == inAndNoTransfer) {
                            receiveAndAdd(rtB, pr, true);
                        } else if (isRequestNeedTransfer(pr, transferFloor,
                                downFloorLimitA, CarType.A) == inAndNeedTransfer) {
                            receiveAndAdd(rtA, pr, true);
                        } else if (isRequestNeedTransfer(pr, upFloorLimitB,
                                transferFloor, CarType.B) == inAndNeedTransfer) {
                            receiveAndAdd(rtB, pr, true);
                        } else {
                            throw new RuntimeException("Person" + pr.getId() + " Can't In CarEle");
                        }
                    }
                    buffer.get(over).clear();
                }
            }
        }
        notifyAll();
    }

    public synchronized void receiveAndAdd(RequestTable bestReqTab, Person pr, boolean isNotify) {
        OutputHandler.getInstance().receive(bestReqTab.getId(),
                pr.getId(), bestReqTab.getType());
        checkIncrease(pr, bestReqTab.getUpFloorLimit(),
                bestReqTab.getDownFloorLimit(), bestReqTab.getType());
        bestReqTab.addRequest(pr, isNotify);
    }

    private boolean inputEnd = false;

    public synchronized void setInputEnd(boolean inputEnd) {
        this.inputEnd = inputEnd;
        notifyAll();
    }

    public synchronized boolean isInputEnd() {
        return inputEnd;
    }

    public synchronized boolean isAnyReqReset() {
        synchronized (reqTabArray) {
            for (int i = 1; i <= elevatorNum; i++) {
                if (reqTabArray.get(i).isReset()) {
                    return true;
                }
            }
            return false;
        }
    }

    public synchronized boolean isAnyReqTransfer() {
        synchronized (reqTabArray) {
            for (int i = 1; i <= elevatorNum; i++) {
                if (reqTabArray.get(i).isTransfer()) {
                    return true;
                }
            }
            return false;
        }
    }

    private final ArrayList<RequestTable> reqTabArray = new ArrayList<>();
    private final ArrayList<Elevator> elevatorArray = new ArrayList<>();
    private final LinkedList<Person> personRequests = new LinkedList<>();

    public synchronized boolean isPersonRequestsEmpty() {
        return personRequests.isEmpty();
    }

    private final ArrayList<ResetRequest> resetRequests = new ArrayList<>();

    public synchronized boolean isResetRequestsEmpty() {
        return resetRequests.isEmpty();
    }

    public synchronized void addPersonRequest(Person pr) {
        if (pr == null) {
            throw new RuntimeException("Person is Null Pointer");
        }
        personRequests.addLast(pr);
        notifyAll();
    }

    public synchronized void returnPool(ArrayList<Person> rets) {
        for (Person ret : rets) {
            if (ret == null) {
                throw new RuntimeException("Person is Null Pointer");
            }
            personRequests.addFirst(ret);
        }
    }

    public synchronized void dealResetRequest(ResetRequest rr) {
        resetRequests.add(rr);
        resetRequests.remove(0);
        int eleId = 0;
        if (rr instanceof NormalResetRequest) {
            NormalResetRequest nr = (NormalResetRequest) rr;
            eleId = nr.getElevatorId();
        } else if (rr instanceof DoubleCarResetRequest) {
            DoubleCarResetRequest dr = (DoubleCarResetRequest) rr;
            eleId = dr.getElevatorId();
        }
        setRequestTableReset(realReqIndexMap.get(eleId), true, rr);
    }

    public synchronized void setRequestTableReset(RequestTable rt,
                                                  boolean resetTag, ResetRequest rr) {
        synchronized (reqTabArray) {
            final ResetRequest dealt = rt.getResetRequest();
            rt.setReset(resetTag, rr); // 由输入线程/电梯线程调度。
            if (!resetTag && buffer.containsKey(rt) && !buffer.get(rt).isEmpty() &&
                    dealt instanceof NormalResetRequest) {
                int fakeId = 0;
                for (int i = 1; i <= elevatorNum; i++) {
                    if (reqTabArray.get(i) == rt) {
                        fakeId = i;
                        break;
                    }
                }
                if (fakeId == 0) {
                    throw new RuntimeException("Fake Id = 0");
                }
                synchronized (buffer) {
                    ArrayList<Person> bufferPersons = buffer.get(rt);
                    for (int i = 0; i < bufferPersons.size(); i++) {
                        if (i == bufferPersons.size() - 1) {
                            receiveAndAdd(rt, bufferPersons.get(i), true);
                        } else {
                            receiveAndAdd(rt, bufferPersons.get(i), false);
                        }
                    }
                    buffer.get(rt).clear();
                }
            }
        }
        notifyAll();
    }

    public synchronized boolean isReqTabsAllReset() {
        synchronized (reqTabArray) {
            for (int i = 1; i <= elevatorNum; i++) {
                if (!reqTabArray.get(i).isReset()) {
                    return false;
                }
            }
            return true;
        }
    }

    public synchronized boolean isReqTabsAllTransfer() {
        synchronized (reqTabArray) {
            for (int i = 1; i <= elevatorNum; i++) {
                if (!reqTabArray.get(i).isTransfer()) {
                    return false;
                }
            }
            return true;
        }
    }

    public synchronized void setRequestTableTransfer(RequestTable rt, boolean isTransfer) {
        synchronized (reqTabArray) {
            rt.setTransfer(isTransfer);
        }
        notifyAll();
    }

    public synchronized Person popPersonRequest() {
        if (isPersonRequestsEmpty()) {
            return null;
        }
        Person pr = personRequests.get(0);
        personRequests.remove(0);
        notifyAll();
        return pr;
    }

    public synchronized boolean checkCanDispatch(Person pr, int i) {
        CarType type = elevatorArray.get(i).getStrategy().getType();
        int transferFloor = elevatorArray.get(i).getStrategy().getTransferFloor();
        if (type == null) {
            return true;
        } else if (type == CarType.A) {
            if (pr.getFromFloor() > transferFloor) {
                return false;
            } else if (pr.getFromFloor() == transferFloor) {
                return !pr.getDirection();
            } else {
                return true;
            }
        } else {
            if (pr.getFromFloor() < transferFloor) {
                return false;
            } else if (pr.getFromFloor() == transferFloor) {
                return pr.getDirection();
            } else {
                return true;
            }
        }
    }

    public synchronized int getBestId(ArrayList<Integer> availableElevatorIndex,
                                      long[] addedShadowTime, long[] nonAddedShadowTime) {
        int bestId = 0;
        long[] sumShadowTime;
        long leastTime = 0;
        boolean readFlag = false;
        ArrayList<Integer> bestIds = new ArrayList<>();
        HashMap<Integer, Long> bestIdTimeMap = new HashMap<>();
        for (Integer elevatorIndex : availableElevatorIndex) {
            sumShadowTime = Arrays.copyOf(nonAddedShadowTime, 20);
            int fakeId = elevatorIndex;
            sumShadowTime[fakeId] = addedShadowTime[fakeId];
            long curStrategyTime = Arrays.stream(sumShadowTime).max().getAsLong();
            //System.out.println("**" + fakeId + " Shadow time: " + curStrategyTime);
            if (!readFlag) {
                readFlag = true;
                leastTime = curStrategyTime;
                bestId = fakeId;
            } else if (leastTime > curStrategyTime) {
                leastTime = curStrategyTime;
                bestId = fakeId;
            } else if (leastTime == curStrategyTime) {
                if (bestIds.isEmpty()) {
                    bestIds.add(bestId);
                    bestIdTimeMap.put(bestId, leastTime);
                }
                bestIds.add(fakeId);
                bestIdTimeMap.put(fakeId, curStrategyTime);
            }
        }
        //System.out.println("leastTime: " + leastTime);
        if (!bestIds.isEmpty()) {
            Iterator<Integer> it = bestIds.iterator();
            while (it.hasNext()) {
                int id = it.next();
                if (leastTime != bestIdTimeMap.get(id)) {
                    it.remove();
                }
            }
            if (!bestIds.isEmpty()) {
                int leastPersonRequests = reqTabArray.get(bestIds.get(0)).size();
                if (buffer.containsKey(reqTabArray.get(bestIds.get(0)))) {
                    leastPersonRequests += buffer.get(reqTabArray.get(bestIds.get(0))).size();
                }
                bestId = bestIds.get(0);
                //System.out.println("**" + bestId + " requestsNum: " + leastPersonRequests);
                for (int i = 1; i < bestIds.size(); i++) {
                    int curBestId = bestIds.get(i);
                    int curPersonRequests = reqTabArray.get(curBestId).size();
                    if (buffer.containsKey(reqTabArray.get(curBestId))) {
                        curPersonRequests += buffer.get(reqTabArray.get(curBestId)).size();
                    }
                    //System.out.println("**" + curBestId + " requestsNum: "
                    //        + curPersonRequests);
                    if (leastPersonRequests > curPersonRequests) {
                        leastPersonRequests = curPersonRequests;
                        bestId = curBestId;
                    }
                }
            }
        }
        return bestId;
    }

    public synchronized int chooseBestElevator1(Person pr) {
        ArrayList<Integer> availableElevatorIndex = new ArrayList<>();
        long[] addedShadowTime = new long[20]; // 从1编号，和电梯编号不一致
        long[] nonAddedShadowTime = new long[20];
        int bestId;
        synchronized (reqTabArray) {
            for (int i = 1; i <= elevatorNum; i++) {
                if (!checkCanDispatch(pr, i)) {
                    continue;
                }
                availableElevatorIndex.add(i);
                //System.out.println("********Shadow added No."+i+" *******");
                RequestTable curReq = reqTabArray.get(i);
                addedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        curReq, pr, elevatorNum >= 9,
                        buffer.getOrDefault(curReq, null)).getTime();
                //System.out.println("********Shadow nonAdded No."+i+" *******");
                nonAddedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), null, elevatorNum >= 9,
                        buffer.getOrDefault(curReq, null)).getTime();
            }
            bestId = getBestId(availableElevatorIndex, addedShadowTime, nonAddedShadowTime);
            if (bestId > 0) {
                RequestTable bestReqTab = reqTabArray.get(bestId);
                if (bestReqTab.isReset()) {
                    synchronized (buffer) {
                        if (buffer.containsKey(bestReqTab)) {
                            buffer.get(bestReqTab).add(pr);
                        } else {
                            ArrayList<Person> prs = new ArrayList<>();
                            prs.add(pr);
                            buffer.put(bestReqTab, prs);
                        }
                    }
                    return -1;
                }
                receiveAndAdd(bestReqTab, pr, true);
            }
        }
        return bestId;
    }

    public synchronized void checkWait() {
        while ((!isInputEnd() && isPersonRequestsEmpty() && isResetRequestsEmpty())
                || (isReqTabsAllReset()) || (isReqTabsAllTransfer())) {
            try {
                //System.out.println("Scheduler waiting 1: begin");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //System.out.println("Scheduler waiting 1: end");
    }

    public void dispatchPerson(Person pr) {
        while (chooseBestElevator1(pr) == 0) {
            try {
                //System.out.println("Scheduler waiting 2: begin");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //System.out.println("Scheduler waiting 2: end");
    }

    public synchronized void prNullDeal() {
        try {
            //System.out.println("Scheduler waiting 3: begin");
            wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("Scheduler waiting 3: end");
    }

    public synchronized boolean isSchedulerEnd() {
        return isInputEnd() && isPersonRequestsEmpty() && isResetRequestsEmpty()
                && !isAnyReqReset() && !isAnyReqTransfer() && (transferRequestsNum == 0);
    }

    @Override
    public void run() {
        reqTabArray.add(null);
        elevatorArray.add(null);
        for (int i = 1; i <= 6; i++) {
            RequestTable rt = new RequestTable(new HashMap<>(), new HashMap<>(),
                    false, null, i, null, 11, 1);
            reqTabArray.add(i, rt);
            Elevator elevator = new Elevator(i, true, rt);
            elevatorArray.add(i, elevator);
            new Thread(elevator, String.valueOf(i)).start();
            realReqIndexMap.put(i, rt);
            realNormalEleIndexMap.put(i, elevator);
        }
        while (true) {
            checkWait();
            Person pr = popPersonRequest();
            if (pr != null) {
                dispatchPerson(pr);
            } else {
                synchronized (this) {
                    if (!isPersonRequestsEmpty() || !isResetRequestsEmpty()) {
                        continue;
                    }
                    if (isSchedulerEnd()) {
                        break;
                    }
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (isSchedulerEnd()) {
                break;
            }
        }
        //System.out.println("Scheduler Over with " + elevatorNum + " elevators");
        for (int i = 1; i <= elevatorNum; i++) {
            //System.out.println("Elevator id: " + reqTabArray.get(i).getId());
            reqTabArray.get(i).setSchedulerEnd(true);
        }
    }
}
