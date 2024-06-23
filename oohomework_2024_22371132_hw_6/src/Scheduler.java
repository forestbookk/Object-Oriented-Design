import com.oocourse.elevator2.ResetRequest;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Scheduler implements Runnable {
    private static final Scheduler instance = new Scheduler();

    public static synchronized Scheduler getInstance() {
        return instance;
    }

    private Scheduler() {
    }

    private boolean inputEnd = false;

    public synchronized void setInputEnd(boolean inputEnd) {
        this.inputEnd = inputEnd;
        notifyAll();
    }

    public synchronized boolean isAnyReqReset() {
        synchronized (reqTabArray) {
            for (int i = 1; i <= 6; i++) {
                if (reqTabArray.get(i).isReset()) {
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

    public synchronized void addPersonRequest(Person pr, boolean isHead) {
        //System.out.println("personRequestHaveAdd: "+pr.getId());
        if (isHead) {
            personRequests.addFirst(pr);
        } else {
            personRequests.addLast(pr);
        }
        notifyAll();
    }

    public synchronized void returnPool(ArrayList<Person> rets) {
        for (Person ret : rets) {
            personRequests.addFirst(ret);
        }
    }

    public synchronized void dealResetRequest(ResetRequest rr) {
        resetRequests.add(rr);
        resetRequests.remove(0);
        // TODO: 是否保证数组的原子性 第一个防止输入线程，第二个防止电梯线程

        setRequestTableReset(reqTabArray.get(rr.getElevatorId()), true, rr);

        //notifyAll();
    }

    public synchronized void setRequestTableReset(RequestTable rt,
                                                  boolean resetTag, ResetRequest rr) {
        synchronized (reqTabArray) {
            rt.setReset(resetTag, rr); // 由输入线程/电梯线程调度。
        }
        notifyAll();
    }

    public synchronized boolean isReqTabsAllReset() {
        synchronized (reqTabArray) {
            for (int i = 1; i <= 6; i++) {
                if (!reqTabArray.get(i).isReset()) {
                    return false;
                }
            }
            return true;
        }
    }

    public synchronized Person popPersonRequest() {
        //System.out.println("Popping");
        if (isPersonRequestsEmpty()) {
            return null;
        }
        Person pr = personRequests.get(0);
        personRequests.remove(0);
        notifyAll();
        return pr;
    }

    public int chooseBestElevator1(Person pr) {
        int bestId = 0;
        int leastPassengers = 0;
        long leastTime = 0;
        boolean readFlag = false;
        synchronized (reqTabArray) {
            for (int i = 1; i <= 6; i++) {
                if (reqTabArray.get(i).isReset()) {
                    continue;
                }
                long curTime = new Shadow(elevatorArray.get(i), reqTabArray.get(i), pr).getTime();
                int curPassengers = elevatorArray.get(i).getPassengers().size();
                //System.out.println("NO."+i+" ShadowTime: "+curTime);
                if (!readFlag) {
                    readFlag = true;
                    leastTime = curTime;
                    leastPassengers = elevatorArray.get(i).getPassengers().size();
                    bestId = i;
                } else if (leastTime > curTime) {
                    leastTime = curTime;
                    bestId = i;
                    leastPassengers = elevatorArray.get(i).getPassengers().size();
                } else if (leastTime == curTime && leastPassengers > curPassengers) {
                    bestId = i;
                    leastPassengers = elevatorArray.get(i).getPassengers().size();
                }
            }
            if (bestId > 0) {
                OutputHandler.getInstance().receive(bestId, pr.getId());
                reqTabArray.get(bestId).addRequest(pr);
            }
        }
        return bestId;
    }

    public int chooseBestElevator2(Person pr) {
        int bestId = 0;
        long leastTime = 0;
        boolean readFlag = false;
        ArrayList<Integer> availableElevatorIndex = new ArrayList<>();
        long[] addedShadowTime = new long[7]; // 从1编号，和电梯编号一致
        long[] nonAddedShadowTime = new long[7];
        long[] sumShadowTime;
        synchronized (reqTabArray) {
            for (int i = 1; i <= 6; i++) {
                if (reqTabArray.get(i).isReset()) {
                    continue;
                }
                availableElevatorIndex.add(i);
                addedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), pr).getTime();
                nonAddedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), null).getTime();
            }
            for (int i = 0; i < availableElevatorIndex.size(); i++) {
                sumShadowTime = Arrays.copyOf(nonAddedShadowTime, 7);
                int eleId = availableElevatorIndex.get(i);
                sumShadowTime[eleId] = addedShadowTime[eleId];
                long curStrategyTime = Arrays.stream(sumShadowTime).max().getAsLong();
                //System.out.println("NO." + eleId + " ShadowTime: " + curStrategyTime);
                if (!readFlag) {
                    readFlag = true;
                    leastTime = curStrategyTime;
                    bestId = eleId;
                } else if (leastTime > curStrategyTime) {
                    leastTime = curStrategyTime;
                    bestId = eleId;
                }
            }
            if (bestId > 0) {
                OutputHandler.getInstance().receive(bestId, pr.getId());
                reqTabArray.get(bestId).addRequest(pr);
            }
        }
        return bestId;
    }

    public int chooseBestElevator3(Person pr) {
        int bestId = 0;
        long leastTime = 0;
        boolean readFlag = false;
        boolean isBestEleReset = false;
        ArrayList<Integer> availableElevatorIndex = new ArrayList<>();
        long[] addedShadowTime = new long[7]; // 从1编号，和电梯编号一致
        long[] nonAddedShadowTime = new long[7];
        long[] sumShadowTime;
        synchronized (reqTabArray) {
            for (int i = 1; i <= 6; i++) {
                long plus = reqTabArray.get(i).isReset() ? 500 : 0;
                availableElevatorIndex.add(i);
                addedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), pr).getTime() + plus;
                nonAddedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), null).getTime() + plus;
            }
            for (int i = 0; i < availableElevatorIndex.size(); i++) {
                sumShadowTime = Arrays.copyOf(nonAddedShadowTime, 7);
                int eleId = availableElevatorIndex.get(i);
                sumShadowTime[eleId] = addedShadowTime[eleId];
                long curStrategyTime = Arrays.stream(sumShadowTime).max().getAsLong();
                boolean curEleReset = reqTabArray.get(eleId).isReset();
                if (!readFlag) {
                    readFlag = true;
                    leastTime = curStrategyTime;
                    bestId = eleId;
                    isBestEleReset = reqTabArray.get(eleId).isReset();
                } else if (leastTime > curStrategyTime) {
                    leastTime = curStrategyTime;
                    bestId = eleId;
                    isBestEleReset = reqTabArray.get(eleId).isReset();
                } else if (leastTime == curStrategyTime && isBestEleReset && !curEleReset) {
                    bestId = eleId;
                    isBestEleReset = reqTabArray.get(eleId).isReset();
                }
            }
            if (bestId > 0) {
                if (reqTabArray.get(bestId).isReset()) {
                    return -1;
                }
                OutputHandler.getInstance().receive(bestId, pr.getId());
                reqTabArray.get(bestId).addRequest(pr);
            }
        }
        return bestId;
    }

    public int chooseBestElevator4(Person pr) {
        int bestId = 0;
        long leastTime = 0;
        boolean readFlag = false;
        boolean isBestEleReset = false;
        ArrayList<Integer> availableElevatorIndex = new ArrayList<>();
        long[] addedShadowTime = new long[7]; // 从1编号，和电梯编号一致
        long[] nonAddedShadowTime = new long[7];
        long[] sumShadowTime;
        synchronized (reqTabArray) {
            for (int i = 1; i <= 6; i++) {
                availableElevatorIndex.add(i);
                addedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), pr).getTime();
                nonAddedShadowTime[i] = new Shadow(elevatorArray.get(i),
                        reqTabArray.get(i), null).getTime();
            }
            for (int i = 0; i < availableElevatorIndex.size(); i++) {
                sumShadowTime = Arrays.copyOf(nonAddedShadowTime, 7);
                int eleId = availableElevatorIndex.get(i);
                sumShadowTime[eleId] = addedShadowTime[eleId];
                long curStrategyTime = Arrays.stream(sumShadowTime).max().getAsLong();
                boolean curEleReset = reqTabArray.get(eleId).isReset();
                if (!readFlag) {
                    readFlag = true;
                    leastTime = curEleReset ? curStrategyTime + 800 : curStrategyTime;
                    bestId = eleId;
                    isBestEleReset = curEleReset;
                } else if (leastTime > curStrategyTime && !curEleReset) {
                    leastTime = curStrategyTime;
                    bestId = eleId;
                    isBestEleReset = false;
                } else if ((leastTime > curStrategyTime + 800) && curEleReset) {
                    leastTime = curStrategyTime + 800;
                    bestId = eleId;
                    isBestEleReset = true;
                } else if (leastTime == curStrategyTime && isBestEleReset && !curEleReset) {
                    bestId = eleId;
                    isBestEleReset = false;
                }
            }
            if (bestId > 0) {
                if (reqTabArray.get(bestId).isReset()) {
                    return 0;
                }
                OutputHandler.getInstance().receive(bestId, pr.getId());
                reqTabArray.get(bestId).addRequest(pr);
            }
        }
        return bestId;
    }

    public synchronized void checkWait() {
        while ((!inputEnd && isPersonRequestsEmpty() && isResetRequestsEmpty())
                || (isReqTabsAllReset())) {
            try {
                //System.out.println("Scheduler waiting 1: begin");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("Scheduler waiting 1: end");
        }
    }

    public synchronized void dispatchPerson(Person pr) {
        boolean isChosen = false;
        for (int i = 0; i < 2; i++) {
            if (chooseBestElevator4(pr) > 0) {
                isChosen = true;
                break;
            } else {
                if (i == 0) {
                    try {
                        //System.out.println("Scheduler waiting: 4 begin");
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //System.out.println("Scheduler waiting: 4 end");
                }
            }
        }
        while (!isChosen && chooseBestElevator2(pr) == 0) {
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

    @Override
    public void run() {
        reqTabArray.add(null);
        elevatorArray.add(null);
        for (int i = 1; i <= 6; i++) {
            RequestTable rt = new RequestTable(new HashMap<>(), new HashMap<>(), false, null);
            reqTabArray.add(i, rt);
            Elevator elevator = new Elevator(i, true, rt);
            elevatorArray.add(i, elevator);
            new Thread(elevator, String.valueOf(i)).start();
        }

        while (true) {
            if (inputEnd && isPersonRequestsEmpty() && isResetRequestsEmpty() && !isAnyReqReset()) {
                break;
            }
            //System.out.println("Scheduler loop");
            checkWait();

            Person pr = popPersonRequest();
            if (pr != null) {
                dispatchPerson(pr);
            } else {
                if (inputEnd && isPersonRequestsEmpty() &&
                        isResetRequestsEmpty() && !isAnyReqReset()) {
                    break;
                }
                prNullDeal();
            }
            //System.out.println("personRequestsIsEmpty: "+isPersonRequestsEmpty());
        }
        //System.out.println("personRequestsIsEmpty: "+isPersonRequestsEmpty());
        //System.out.println("Scheduler Over");
        for (int i = 1; i <= 6; i++) {
            reqTabArray.get(i).setSchedulerEnd(true);
        }
    }

}
