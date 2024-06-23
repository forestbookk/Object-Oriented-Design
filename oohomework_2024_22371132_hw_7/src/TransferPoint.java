public class TransferPoint {
    private final int transferFloor;

    public TransferPoint(int transferFloor, RequestTable rtA, RequestTable rtB) {
        this.transferFloor = transferFloor;
        this.rtA = rtA;
        this.rtB = rtB;
    }

    public int getTransferFloor() {
        return transferFloor;
    }

    private boolean isAMark = false;
    private boolean isBMark = false;

    private boolean isARing = false;
    private boolean isBRing = false;
    private final RequestTable rtA;
    private final RequestTable rtB;

    public synchronized boolean isMark() {
        return isAMark || isBMark;
    }

    public synchronized boolean isSelfMark(CarType type) {
        return type == CarType.A ? isAMark : isBMark;
    }

    public synchronized void setSelfMark(boolean isMark, CarType type) {
        if (type == CarType.A) {
            this.isAMark = isMark;
        } else {
            this.isBMark = isMark;
        }
        if (!isMark) {
            notifyAll();
        }
    }

    public synchronized boolean isRing(CarType type) {
        // 查询姊妹电梯是否响铃
        return type == CarType.A ? isBRing : isARing;
    }

    public synchronized void setRing(boolean isRing, CarType type) {
        // 设置响铃提醒姊妹电梯
        if (type == CarType.A) {
            isARing = isRing;
        } else {
            isBRing = isRing;
        }
    }

    public synchronized void dealMarkAndRing(CarType type) {
        while (isMark()) {
            setRing(true, type);
            try {
                //System.out.println("TransferPoint wait begin");
                if (type == CarType.A) {
                    rtB.transferNotify();
                } else {
                    rtA.transferNotify();
                }
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        setRing(false, type);
        setSelfMark(true, type);
        //System.out.println("TransferPoint wait end");
    }
}
