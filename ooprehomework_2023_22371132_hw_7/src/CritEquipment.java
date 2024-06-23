public class CritEquipment extends Equipment {
    private int critical;

    public CritEquipment(int id, String name, int star, long price, int critical) {
        super(id, name, star, price);
        this.critical = critical;
    }

    @Override
    public int getCritical() {
        return critical;
    }

    @Override
    public void setCritical(int critical) {
        this.critical = critical;
    }

    @Override
    public double getRatio() {
        return super.getRatio();
    }

    @Override
    public String getClassName() {
        return "CritEquipment";
    }
}
