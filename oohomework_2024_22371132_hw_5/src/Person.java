public class Person {
    private int id;

    public int getId() {
        return id;
    }

    private int fromFloor;

    public int getFromFloor() {
        return fromFloor;
    }

    private int toFloor;

    public int getToFloor() {
        return toFloor;
    }

    private boolean direction;

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
