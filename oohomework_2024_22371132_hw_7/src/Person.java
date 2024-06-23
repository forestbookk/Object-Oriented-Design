public class Person {
    private final int id;

    public int getId() {
        return id;
    }

    private int fromFloor;

    public int getFromFloor() {
        return fromFloor;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    private int toFloor;

    public int getToFloor() {
        return toFloor;
    }

    private final boolean direction;

    public boolean getDirection() {
        return direction;
    }

    public Person(int id, int fromFloor, int toFloor) {
        this.id = id;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.direction = toFloor > fromFloor;
    }

}
