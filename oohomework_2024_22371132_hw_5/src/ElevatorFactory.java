public class ElevatorFactory {
    public Elevator create(int id, boolean direction, RequestTable requestTable) {
        return new Elevator(id, direction, requestTable);
    }
}
