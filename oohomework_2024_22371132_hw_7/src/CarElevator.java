import com.oocourse.elevator3.ResetRequest;

import java.util.ArrayList;

public class CarElevator extends Elevator implements Runnable {
    private final TransferPoint transferPoint;
    private final CarType type;
    private final int transferFloor;

    public CarElevator(int id, boolean direction, RequestTable requestTable,
                       int maxCapacity, int moveTime, int transferFloor,
                       TransferPoint transferPoint,
                       long lastCloseOrArriveOrResetEndStamp, CarType type,
                       int upFloorLimit, int downFloorLimit) {
        super(id, direction, requestTable, maxCapacity, moveTime,
                transferFloor + (direction ? 1 : -1),
                upFloorLimit, downFloorLimit,
                transferPoint, type,
                lastCloseOrArriveOrResetEndStamp);
        this.transferFloor = transferFloor;
        this.type = type;
        this.transferPoint = transferPoint;
    }

    @Override
    public void move() {
        final int beforeFloor = getCurFloor();
        final int afterFloor = beforeFloor + (getDirection() ? 1 : -1);
        synchronized (transferPoint) {
            if (afterFloor == transferFloor) {
                if (transferPoint.isMark()) {
                    transferPoint.dealMarkAndRing(type);
                } else {
                    transferPoint.setSelfMark(true, type);
                }
            }
        }
        super.move();
        if (beforeFloor == transferFloor) {
            transferPoint.setSelfMark(false, type);
        }
    }

    @Override
    public ArrayList<Person> out(boolean isIncompleteOutAndCollect,
                                 int upOutLimit, int downOutLimit) {
        ArrayList<Person> ret = super.out(isIncompleteOutAndCollect, upOutLimit, downOutLimit);
        if (ret == null) {
            return null;
        }
        for (Person pr : ret) {
            Scheduler.getInstance().decrease(pr);
        }
        return ret;
    }

    @Override
    public int reset(ResetRequest rr) {
        throw new RuntimeException("Car Elevator Reset");
    }

    @Override
    public void transfer() {
        Scheduler.getInstance().setRequestTableTransfer(getRequestTable(), true);
        final int upOutLimit = type == CarType.A ? 11 : (transferFloor - 1);
        final int downOutLimit = type == CarType.A ? (transferFloor + 1) : 1;
        Scheduler.getInstance().returnPool(super.openAndClose(true, upOutLimit, downOutLimit));
        Scheduler.getInstance().setRequestTableTransfer(getRequestTable(), false);
    }
}
