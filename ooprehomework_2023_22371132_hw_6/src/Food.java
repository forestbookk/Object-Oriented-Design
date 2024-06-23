public class Food implements Commodity {
    private long price;
    private int id;
    private String name;
    private int energy = 0;
    private boolean isCarried;

    public Food(int id, String name, int energy, long price) {
        this.id = id;
        this.name = name;
        this.energy = energy;
        this.price = price;
        this.isCarried = false;
    }

    public int getId() {
        return id;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isCarried() {
        return isCarried;
    }

    public void setCarried(boolean carried) {
        isCarried = carried;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getClassName() {
        return "Food";
    }

    @Override
    public long getPrice() {
        return price;
    }

}
