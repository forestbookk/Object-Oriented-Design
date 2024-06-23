import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Adventurer implements Commodity {
    private long inanimatePrice = 0;
    private int id;
    private String name;
    private int level = 1;
    private int hitPoint = 500;
    private int bottleCount = 0;
    private int equipmentCount = 0;
    private int foodCount = 0;
    private long money = 0;
    private HashMap<Integer, Bottle> bottles = new HashMap<>();
    private HashMap<Integer, Equipment> equipments = new HashMap<>();
    private HashMap<Integer, Food> foods = new HashMap<>();
    private HashMap<Integer, Adventurer> employees = new HashMap<>();
    private HashMap<String, Integer> equBag = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> botBag = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> fooBag = new HashMap<>();
    private ArrayList<ArrayList<String>> log0 = new ArrayList<>();
    private ArrayList<ArrayList<String>> log1 = new ArrayList<>();
    private boolean isSellAll = false;

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addABottle(int id, Bottle bottle) {
        inanimatePrice += bottle.getPrice();
        bottleCount += 1;
        bottles.put(id, bottle);
    }

    public void sellABottle(int id) {
        inanimatePrice -= bottles.get(id).getPrice();
        bottleCount -= 1;
        if (!isSellAll) {
            System.out.print(bottleCount);
            System.out.print(" ");
            System.out.println(bottles.get(id).getName());
        }
        if (bottles.get(id).isCarried()) {
            bottles.get(id).setCarried(false);
        }
        //bag
        String name = bottles.get(id).getName();
        if (botBag.containsKey(name)) {
            int size = botBag.get(name).size();
            for (int i = 0; i < size; i++) {
                if (botBag.get(name).get(i) == id) {
                    botBag.get(name).remove(i);
                    break;
                }
            }
            if (botBag.get(name).isEmpty()) {
                botBag.remove(name);
            }
        }
        //store
        int capacity = bottles.get(id).getCapacity();
        long price = bottles.get(id).getPrice();
        Store.getInstance().getBottle(capacity, price);
        this.money += price;
        //remove
        bottles.remove(id);
    }

    public void addAnEquipment(int id, Equipment equipment) {
        inanimatePrice += equipment.getPrice();
        equipmentCount += 1;
        equipments.put(id, equipment);
    }

    public void sellAnEquipment(int id) {
        inanimatePrice -= equipments.get(id).getPrice();
        equipmentCount -= 1;
        if (!isSellAll) {
            System.out.print(equipmentCount);
            System.out.print(" ");
            System.out.println(equipments.get(id).getName());
        }
        if (equipments.get(id).isCarried()) {
            equipments.get(id).setCarried(false);
        }
        //bag
        String name = equipments.get(id).getName();
        if (equBag.containsKey(name)) {
            if (equBag.get(name) == id) {
                equBag.remove(name);
            }
        }
        //store
        int star = equipments.get(id).getStar();
        long price = equipments.get(id).getPrice();
        Store.getInstance().getEquipment(star, price);
        this.money += price;
        //remove
        equipments.remove(id);
    }

    public void upgradeAnEquipment(int id) {
        int old = equipments.get(id).getStar();
        equipments.get(id).setStar(old + 1);
        System.out.print(equipments.get(id).getName());
        System.out.print(" ");
        System.out.println(old + 1);
    }

    public void addAFood(int id, Food food) {
        inanimatePrice += food.getPrice();
        foodCount++;
        foods.put(id, food);
    }

    public void removeAFood(int id) {
        inanimatePrice -= foods.get(id).getPrice();
        foodCount -= 1;
        if (!isSellAll) {
            System.out.print(foodCount);
            System.out.print(" ");
            System.out.println(foods.get(id).getName());
        }
        if (foods.get(id).isCarried()) {
            foods.get(id).setCarried(false);
        }
        //bag
        String name = foods.get(id).getName();
        if (fooBag.containsKey(name)) {
            int size = fooBag.get(name).size();
            for (int i = 0; i < size; i++) {
                if (fooBag.get(name).get(i) == id) {
                    fooBag.get(name).remove(i);
                    break;
                }
            }
            if (fooBag.get(name).isEmpty()) {
                fooBag.remove(name);
            }
        }
        //store
        int energy = foods.get(id).getEnergy();
        long price = foods.get(id).getPrice();
        Store.getInstance().getFood(energy, price);
        this.money += price;
        //remove
        foods.remove(id);
    }

    public void carryAnEquipment(int id) {
        if (!equipments.containsKey(id)) {
            return;//item do not belong to adventurer
        }
        if (equipments.get(id).isCarried()) {
            return;//item is already in the bag
        }
        String name = equipments.get(id).getName();
        if (equBag.containsKey(name)) {
            int previousId = equBag.get(name);
            equipments.get(previousId).setCarried(false);
            equipments.get(id).setCarried(true);
            equBag.replace(name, id);//same-name item replace the substitute
        } else {
            equipments.get(id).setCarried(true);
            equBag.put(name, id);
        }
    }

    public void carryABottle(int id) {
        if (!bottles.containsKey(id)) {
            return;//item do not belong to adventurer
        }
        if (bottles.get(id).isCarried()) {
            return;//item is already carried
        }
        String name = bottles.get(id).getName();
        if (!botBag.containsKey(name)) {
            //the name is not in the bag
            bottles.get(id).setCarried(true);
            ArrayList<Integer> nameBottle = new ArrayList<>();
            nameBottle.add(id);
            botBag.put(name, nameBottle);
        } else {
            //the name is already in the bag
            //check maxBottles
            int maxBottles = this.level / 5 + 1;
            int arraySize = botBag.get(name).size();
            if (arraySize >= maxBottles) {
                return;//the amount of same-bottles is too large
            }
            bottles.get(id).setCarried(true);
            botBag.get(name).add(id);
        }
    }

    public void carryAFood(int id) {
        if (!foods.containsKey(id)) {
            return;//item do not belong to adventurer
        }
        if (foods.get(id).isCarried()) {
            return;//item is already in the bag
        }
        String name = foods.get(id).getName();
        foods.get(id).setCarried(true);
        if (!fooBag.containsKey(name)) {
            ArrayList<Integer> nameFood = new ArrayList<>();
            nameFood.add(id);
            fooBag.put(name, nameFood);
        } else {
            fooBag.get(name).add(id);
        }
    }

    public int useAnEquipment(int hitPoint0, Boolean isFight, String name) {
        if (!equBag.containsKey(name)) {
            if (isFight) {
                System.out.println("Fight log error");
            } else {
                System.out.println("fail to use equipment " + name);
            }
            return -1;//the item is not in the bag
        }

        int equId = equBag.get(name);
        String type = equipments.get(equId).getClassName();

        if (type.equals("RegularEquipment")) {
            return this.level * this.equipments.get(equId).getStar();//healthPointDec
        } else if (type.equals("CritEquipment")) {
            int star = this.equipments.get(equId).getStar();
            int critical = this.equipments.get(equId).getCritical();
            return this.level * star + critical;
        } else {
            return (int) (hitPoint0 * this.equipments.get(equId).getRatio());
        }

    }

    public Boolean useABottle(boolean isFight, String name) {
        if (!botBag.containsKey(name)) {
            if (isFight) {
                System.out.println("Fight log error");
            } else {
                System.out.println("fail to use " + name);//fail
            }
            return Boolean.FALSE;//the item is not in the bag
        }

        int size = botBag.get(name).size();
        int min = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < size; i++) {
            int temp = botBag.get(name).get(i);
            if (min > temp) {
                min = temp;
                index = i;
            }
        }
        if (!bottles.get(min).getEmpty()) {
            String type = bottles.get(min).getClassName();
            if (type.equals("RegularBottle")) {
                this.hitPoint += bottles.get(min).getCapacity();
            } else if (type.equals("ReinforcedBottle")) {
                double ratio = bottles.get(min).getRatio();
                int capacity = bottles.get(min).getCapacity();
                this.hitPoint += (int) ((1.0 + ratio) * capacity);
            } else {
                double ratio = bottles.get(min).getRatio();
                double temp = ratio * this.hitPoint;
                this.hitPoint += (int) temp;
            }
        }


        if (bottles.get(min).getEmpty()) {
            bottles.get(min).setCarried(false);
            botBag.get(name).remove(index);
            if (botBag.get(name).isEmpty()) {
                botBag.remove(name);
            }
            inanimatePrice -= bottles.get(min).getPrice();
            bottles.remove(min);
            bottleCount -= 1;
        } else {
            bottles.get(min).setEmpty(Boolean.TRUE);
        }

        System.out.print(min);
        System.out.print(" ");
        System.out.println(this.hitPoint);
        return Boolean.TRUE;
    }

    public void useAFood(String name) {
        if (!fooBag.containsKey(name)) {
            System.out.println("fail to eat " + name);//fail
            return;
        }

        int min = Integer.MAX_VALUE;
        int index = 0;
        int size = fooBag.get(name).size();
        for (int i = 0; i < size; i++) {
            int temp = fooBag.get(name).get(i);
            if (min > temp) {
                min = temp;
                index = i;
            }
        }
        inanimatePrice -= foods.get(min).getPrice();
        this.level += foods.get(min).getEnergy();
        foods.get(min).setCarried(false);
        fooBag.get(name).remove(index);
        foods.remove(foods.get(min).getId());
        if (fooBag.get(name).isEmpty()) {
            fooBag.remove(name);
        }
        foodCount -= 1;
        System.out.println(min + " " + this.level);
    }

    public void storeLog0(ArrayList<String> logAnalysis) {
        log0.add(logAnalysis);
    }

    public void loadLog(Boolean isLog1) {
        if ((isLog1 ? log1 : log0).isEmpty()) {
            System.out.println("No Matched Log");
            return;
        }
        for (ArrayList<String> logAnalysis : isLog1 ? log1 : log0) {
            String year = logAnalysis.get(1);
            String month = logAnalysis.get(2);
            String advName1 = logAnalysis.get(3);
            System.out.print(year + "/" + month + " " + advName1 + " ");
            if (logAnalysis.get(0).equals("ao")) {
                System.out.println("AOE-attacked with " + logAnalysis.get(4));
            } else if (logAnalysis.get(0).equals("so")) {
                String advName2 = logAnalysis.get(4);
                System.out.println("attacked " + advName2 + " with " + logAnalysis.get(5));
            } else {
                System.out.println("Adventurer loadLog0 error");
            }
        }
    }

    public void employAnAdventurer(int id, Adventurer employee) {
        if (employees.containsKey(id)) {
            return;//already
        }
        employees.put(id, employee);
    }

    public void calculateSumCommodity() {
        int employeeCount = employees.size();
        int number = foodCount + equipmentCount + bottleCount + employeeCount;
        long price = this.getPrice() - money;
        System.out.println(number + " " + price);
    }

    public void searchMaxPrice() {
        long maxPrice = 0;
        Iterator<Equipment> equIterator = equipments.values().iterator();
        while (equIterator.hasNext()) {
            Equipment equipment = equIterator.next();
            if (equipment.getPrice() > maxPrice) {
                maxPrice = equipment.getPrice();
            }
        }
        Iterator<Bottle> botIterator = bottles.values().iterator();
        while (botIterator.hasNext()) {
            Bottle bottle = botIterator.next();
            if (bottle.getPrice() > maxPrice) {
                maxPrice = bottle.getPrice();
            }
        }
        Iterator<Food> fooIterator = foods.values().iterator();
        while (fooIterator.hasNext()) {
            Food food = fooIterator.next();
            if (food.getPrice() > maxPrice) {
                maxPrice = food.getPrice();
            }
        }
        Iterator<Adventurer> empIterator = employees.values().iterator();
        while (empIterator.hasNext()) {
            Adventurer employee = empIterator.next();
            if (employee.getPrice() > maxPrice) {
                maxPrice = employee.getPrice();
            }
        }
        System.out.println(maxPrice);
    }

    public void getCommodityName(int id) {
        System.out.print("Commodity whose id is " + id + " belongs to ");
        if (equipments.containsKey(id)) {
            System.out.println(equipments.get(id).getClassName());
        } else if (bottles.containsKey(id)) {
            System.out.println(bottles.get(id).getClassName());
        } else if (foods.containsKey(id)) {
            System.out.println(foods.get(id).getClassName());
        } else if (employees.containsKey(id)) {
            System.out.println(employees.get(id).getClassName());
        } else {
            System.out.println("Adventurer.getCommodityName error");
        }
    }

    public void sellAllItems() {
        this.isSellAll = true;
        final long preMoney = this.money;
        ArrayList<Integer> toSellId = new ArrayList<>();
        toSellId.addAll(equBag.values());
        int equSize = equBag.size();
        equBag.clear();
        Iterator<ArrayList<Integer>> botIterator = botBag.values().iterator();
        int botSize = 0;
        while (botIterator.hasNext()) {
            ArrayList<Integer> botId = botIterator.next();
            toSellId.addAll(botId);
            botSize += botId.size();
        }
        botBag.clear();
        Iterator<ArrayList<Integer>> fooIterator = fooBag.values().iterator();
        int fooSize = 0;
        while (fooIterator.hasNext()) {
            ArrayList<Integer> fooId = fooIterator.next();
            toSellId.addAll(fooId);
            fooSize += fooId.size();
        }
        fooBag.clear();
        for (int i = 0; i < toSellId.size(); i++) {
            if (i < equSize) {
                sellAnEquipment(toSellId.get(i));
            } else if (i < equSize + botSize) {
                sellABottle(toSellId.get(i));
            } else {
                removeAFood(toSellId.get(i));
            }
        }
        long delta = this.money - preMoney;
        System.out.println(this.name + " emptied the backpack " + delta);
        this.isSellAll = false;
    }

    public void buyAnItem(CommandUtil cmd) {
        Store.getInstance().offerAnItem(this, this.money, cmd);
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public int getId() {
        return id;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money += money;
    }

    public void storeLog1(ArrayList<String> logAnalysis) {
        log1.add(logAnalysis);
    }

    public HashMap<Integer, Adventurer> getEmployees() {
        return employees;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getClassName() {
        return "Adventurer";
    }

    @Override
    public long getPrice() {
        long sumPrice = this.inanimatePrice + this.money;
        Iterator<Adventurer> empIterator = employees.values().iterator();
        while (empIterator.hasNext()) {
            Adventurer adventurer = empIterator.next();
            sumPrice += adventurer.getPrice();
        }
        return sumPrice;
    }
}
