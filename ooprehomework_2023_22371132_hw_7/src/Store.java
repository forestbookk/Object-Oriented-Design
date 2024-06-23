public class Store {
    private static Store store = new Store();
    private long sumCapacity = 0;
    private long sumStar = 0;
    private long sumEnergy = 0;
    private long sumBotPrice = 0;
    private long sumEquPrice = 0;
    private long sumFooPrice = 0;
    private int bottleCount = 0;
    private int equipmentCount = 0;
    private int foodCount = 0;

    public void getBottle(int capacity, long price) {
        bottleCount++;
        sumCapacity += capacity;
        sumBotPrice += price;
    }

    public void getEquipment(int star, long price) {
        equipmentCount++;
        sumStar += star;
        sumEquPrice += price;
    }

    public void getFood(int energy, long price) {
        foodCount++;
        sumEnergy += energy;
        sumFooPrice += price;
    }

    public int getAveEnergy() {
        return (int) (sumEnergy / foodCount);
    }

    public int getAveCapacity() {
        return (int) (sumCapacity / bottleCount);
    }

    public int getAveStar() {
        return (int) (sumStar / equipmentCount);
    }

    public long getSAveBotPrice() {
        return sumBotPrice / bottleCount;
    }

    public long getAveEquPrice() {
        return sumEquPrice / equipmentCount;
    }

    public long getSAveFooPrice() {
        return sumFooPrice / foodCount;
    }

    public void offerAFood(Adventurer adv, long money, CommandUtil cmd) {
        int flag = 0;
        int itemId = cmd.getItemId();
        String name = cmd.getName();
        long itemPrice = this.getSAveFooPrice();
        if (money >= itemPrice) {
            flag = 1;
            adv.setMoney(-itemPrice);
            int energy = this.getAveEnergy();
            Food food = new Food(itemId, name, energy, itemPrice);
            adv.addAFood(itemId, food);
        }
        if (flag == 1) {
            System.out.println("successfully buy " + name + " for " + itemPrice);
        } else {
            System.out.println("failed to buy " + name + " for " + itemPrice);
        }
    }

    public void offerABottle(Adventurer adv, long money, CommandUtil cmd) {
        int flag = 0;
        String type = cmd.getType();
        int itemId = cmd.getItemId();
        String name = cmd.getName();
        long itemPrice = this.getSAveBotPrice();
        int capacity = this.getAveCapacity();
        if (type.equals("RegularBottle")) {
            if (money >= itemPrice) {
                flag = 1;
                adv.setMoney(-itemPrice);
                Bottle bottle = new Bottle(itemId, name, capacity, itemPrice);
                adv.addABottle(itemId, bottle);
            }
        } else if (type.equals("ReinforcedBottle")) {
            if (money >= itemPrice) {
                flag = 1;
                adv.setMoney(-itemPrice);
                double ratio = cmd.getRatio();
                Bottle bottle = new ReinforcedBottle(itemId, name, capacity, itemPrice, ratio);
                adv.addABottle(itemId, bottle);
            }
        } else {
            if (money >= itemPrice) {
                flag = 1;
                adv.setMoney(-itemPrice);
                double ratio = cmd.getRatio();
                Bottle bottle = new RecoverBottle(itemId, name, capacity, itemPrice, ratio);
                adv.addABottle(itemId, bottle);
            }
        }
        if (flag == 1) {
            System.out.println("successfully buy " + name + " for " + itemPrice);
        } else {
            System.out.println("failed to buy " + name + " for " + itemPrice);
        }
    }

    public void offerAnEquipment(Adventurer adv, long money, CommandUtil cmd) {
        int flag = 0;
        String type = cmd.getType();
        int itemId = cmd.getItemId();
        String name = cmd.getName();
        long itemPrice = 0;
        itemPrice = this.getAveEquPrice();
        int star = this.getAveStar();
        if (type.equals("RegularEquipment")) {
            if (money >= itemPrice) {
                flag = 1;
                adv.setMoney(-itemPrice);
                Equipment equipment = new Equipment(itemId, name, star, itemPrice);
                adv.addAnEquipment(itemId, equipment);
            }
        } else if (type.equals("EpicEquipment")) {
            if (money >= itemPrice) {
                flag = 1;
                adv.setMoney(-itemPrice);
                double ratio = cmd.getRatio();
                Equipment equipment = new EpicEquipment(itemId, name, star, itemPrice, ratio);
                adv.addAnEquipment(itemId, equipment);
            }
        } else {
            if (money >= itemPrice) {
                flag = 1;
                adv.setMoney(-itemPrice);
                int critical = cmd.getCritical();
                Equipment equipment = new CritEquipment(itemId, name, star, itemPrice, critical);
                adv.addAnEquipment(itemId, equipment);
            }
        }
        if (flag == 1) {
            System.out.println("successfully buy " + name + " for " + itemPrice);
        } else {
            System.out.println("failed to buy " + name + " for " + itemPrice);
        }
    }

    public void offerAnItem(Adventurer adv, long money, CommandUtil cmd) {
        String type = cmd.getType();
        if (type.equals("Food")) {
            Store.getInstance().offerAFood(adv, money, cmd);
        } else if (type.contains("Bottle")) {
            Store.getInstance().offerABottle(adv, money, cmd);
        } else if (type.contains("Equipment")) {
            Store.getInstance().offerAnEquipment(adv, money, cmd);
        }
    }

    public static Store getInstance() {
        if (store == null) {
            store = new Store();
        }
        return store;
    }
}
