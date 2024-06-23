
public class Acquaintance implements Comparable<Acquaintance> {
    public Acquaintance(int id, int value) {
        this.id = id;
        this.value = value;
    }

    private int id;
    private int value;

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Acquaintance o) {
        if (this.value != o.value) {
            return o.value - this.value;
        } else {
            return this.id - o.id;
        }
    }

}
