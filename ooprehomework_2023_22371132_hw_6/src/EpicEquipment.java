public class EpicEquipment extends Equipment {
    private double ratio;

    public EpicEquipment(int id, String name, int star, long price, double ratio) {
        super(id, name, star, price);
        this.ratio = ratio;
    }

    @Override
    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public double getRatio() {
        return ratio;
    }

    @Override
    public String getClassName() {
        return "EpicEquipment";
    }
}
