public class RecoverBottle extends Bottle {
    private double ratio;

    public RecoverBottle(int id, String name, int capacity, long price, double ratio) {
        super(id, name, capacity, price);
        this.ratio = ratio;
    }

    @Override
    public double getRatio() {
        return ratio;
    }

    @Override
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public String getClassName() {
        return "RecoverBottle";
    }
}
