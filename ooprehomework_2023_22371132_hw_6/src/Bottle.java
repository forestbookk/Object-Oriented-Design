public class Bottle implements Commodity {
    private long price;
    private int id;
    private String name;
    private int capacity;
    private boolean isCarried;
    private boolean isEmpty;
    private double ratio;

    public Bottle(int id, String name, int capacity, long price) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.isCarried = false;
        this.isEmpty = false;
        this.price = price;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Boolean getEmpty() {
        return isEmpty;
    }

    public void setEmpty(Boolean empty) {
        isEmpty = empty;
    }

    public String getName() {
        return name;
    }

    public boolean isCarried() {
        return isCarried;
    }

    public void setCarried(boolean carried) {
        isCarried = carried;
    }

    @Override
    public String getClassName() {
        return "RegularBottle";
    }

    @Override
    public long getPrice() {
        return price;
    }
}
