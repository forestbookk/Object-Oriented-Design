public class Equipment implements Commodity {
    private long price;
    private int id;
    private String name;
    private int star;
    private boolean isCarried;
    private int critical;
    private double ratio;

    public Equipment(int id, String name, int star, long price) {
        this.id = id;
        this.name = name;
        this.star = star;
        this.price = price;
        this.isCarried = false;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
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

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    @Override
    public String getClassName() {
        return "RegularEquipment";
    }

    @Override
    public long getPrice() {
        return price;
    }
}
