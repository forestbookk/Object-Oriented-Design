import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

public class Adventurer {
    private final int id;
    private final String name;
    private int level = 1;
    private int hitPoint = 500;
    private int bottleCount = 0;
    private int equipmentCount = 0;
    private int foodCount = 0;
    private HashMap<Integer, Bottle> bottles = new HashMap<>();
    private HashMap<Integer, Equipment> equipments = new HashMap<>();
    private HashMap<Integer, Food> foods = new HashMap<>();
    private HashMap<String, Integer> equBag = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> botBag = new HashMap<>();
    private HashMap<String, ArrayList<Integer>> fooBag = new HashMap<>();
    private HashSet<Integer> itemsInBag = new HashSet<>();
    private ArrayList<ArrayList<String>> log0 = new ArrayList<>();
    private ArrayList<ArrayList<String>> log1 = new ArrayList<>();

    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addABottle(String idStr, String name, String capacityStr) {
        bottleCount += 1;
        Bottle bottle = new Bottle();
        int id = Integer.parseInt(idStr);
        bottle.setId(id);
        bottle.setName(name);
        int capacity = Integer.parseInt(capacityStr);
        bottle.setCapacity(capacity);
        bottles.put(id, bottle);
    }

    public void removeABottle(String idStr) {
        bottleCount -= 1;
        System.out.print(bottleCount);
        System.out.print(" ");
        int id = Integer.parseInt(idStr);
        System.out.println(bottles.get(id).getName());
        if (itemsInBag.contains(id)) {
            itemsInBag.remove(id);
        }
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
        bottles.remove(id);
    }

    public void addAnEquipment(String idStr, String name, String starStr) {
        equipmentCount += 1;
        Equipment equipment = new Equipment();
        int id = Integer.parseInt(idStr);
        equipment.setId(id);
        equipment.setName(name);
        int star = Integer.parseInt(starStr);
        equipment.setStar(star);
        equipments.put(id, equipment);
    }

    public void removeAnEquipment(String idStr) {
        equipmentCount -= 1;
        System.out.print(equipmentCount);
        System.out.print(" ");
        int id = Integer.parseInt(idStr);
        System.out.println(equipments.get(id).getName());
        if (itemsInBag.contains(id)) {
            itemsInBag.remove(id);
        }
        String name = equipments.get(id).getName();
        if (equBag.containsKey(name)) {
            if (equBag.get(name) == id) {
                equBag.remove(name);
            }
        }
        equipments.remove(id);
    }

    public void upgradeAnEquipment(String idStr) {
        int id = Integer.parseInt(idStr);
        int old = equipments.get(id).getStar();
        equipments.get(id).setStar(old + 1);
        System.out.print(equipments.get(id).getName());
        System.out.print(" ");
        System.out.println(old + 1);
    }

    public void addAFood(String idStr, String name, String energyStr) {
        foodCount += 1;
        Food food = new Food();
        int id = Integer.parseInt(idStr);
        food.setId(id);
        food.setName(name);
        int energy = Integer.parseInt(energyStr);
        food.setEnergy(energy);
        foods.put(id, food);
    }

    public void removeAFood(String idStr) {
        foodCount -= 1;
        System.out.print(foodCount);
        System.out.print(" ");
        int id = Integer.parseInt(idStr);
        System.out.println(foods.get(id).getName());
        if (itemsInBag.contains(id)) {
            itemsInBag.remove(id);
        }
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
        foods.remove(id);
    }

    public void carryAnEquipment(String idStr) {
        int id = Integer.parseInt(idStr);
        if (!equipments.containsKey(id)) {
            return;//item do not belong to adventurer
        }
        if (itemsInBag.contains(id)) {
            return;//item is already in the bag
        }
        String name = equipments.get(id).getName();
        if (equBag.containsKey(name)) {
            itemsInBag.remove(equBag.get(name));
            itemsInBag.add(id);
            equBag.replace(name, id);//same-name item replace the substitute
        } else {
            itemsInBag.add(id);
            equBag.put(name, id);
        }
    }

    public void carryABottle(String idStr) {
        int id = Integer.parseInt(idStr);
        if (!bottles.containsKey(id)) {
            return;//item do not belong to adventurer
        }
        if (itemsInBag.contains(id)) {
            return;//item is already in the bag
        }
        String name = bottles.get(id).getName();
        if (!botBag.containsKey(name)) {
            //the name is not in the bag
            itemsInBag.add(id);
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
            itemsInBag.add(id);
            botBag.get(name).add(id);
        }
    }

    public void carryAFood(String idStr) {
        int id = Integer.parseInt(idStr);
        if (!foods.containsKey(id)) {
            return;//item do not belong to adventurer
        }
        if (itemsInBag.contains(id)) {
            return;//item is already in the bag
        }
        String name = foods.get(id).getName();
        itemsInBag.add(id);
        if (!fooBag.containsKey(name)) {
            ArrayList<Integer> nameFood = new ArrayList<>();
            nameFood.add(id);
            fooBag.put(name, nameFood);
        } else {
            fooBag.get(name).add(id);
        }
    }

    public int useAnEquipment(Boolean isFight, String name) {
        if (!equBag.containsKey(name)) {
            if (isFight) {
                System.out.println("Fight log error");
            } else {
                System.out.println("fail to use equipment " + name);
            }
            return -1;//the item is not in the bag
        }
        int equId = equBag.get(name);
        return this.level * this.equipments.get(equId).getStar();//healthPointDec
    }

    public Boolean useABottle(Boolean isFight, String name) {
        if (!botBag.containsKey(name)) {
            if (isFight) {
                System.out.println("Fight log error");
            } else {
                System.out.println("fail to use " + name);//fail
            }
            return Boolean.FALSE;//the item is not in the bag
        }
        /*
        if (botBag.get(name).isEmpty()) {
            botBag.remove(name);
            if (isFight) {
                System.out.println("Fight log error");
            } else {
                System.out.println("fail to use " + name);//fail
            }
            return Boolean.FALSE;//no item exists
        }
        */
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

        this.hitPoint += bottles.get(min).getCapacity();

        if (bottles.get(min).getEmpty()) {
            itemsInBag.remove(min);
            botBag.get(name).remove(index);
            bottles.remove(min);
            if (botBag.get(name).isEmpty()) {
                botBag.remove(name);
            }
            bottleCount -= 1;
        } else {
            bottles.get(min).setCapacity(0);
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
        /*
        if (fooBag.get(name).isEmpty()) {
            fooBag.remove(name);
            System.out.println("fail to eat " + name);//fail
            return;//no item exists
        }
        */

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

        this.level += foods.get(min).getEnergy();
        itemsInBag.remove(min);
        fooBag.get(name).remove(index);
        foods.remove(foods.get(min).getId());
        if (fooBag.get(name).isEmpty()) {
            fooBag.remove(name);
        }
        foodCount -= 1;
        System.out.print(min);
        System.out.print(" ");
        System.out.println(this.level);
    }

    /*     ------------------------------------      */
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

    public void storeLog1(ArrayList<String> logAnalysis) {
        log1.add(logAnalysis);
    }

    public int getEquipmentCount() {
        return equipmentCount;
    }

    public int getBottleCount() {
        return bottleCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public HashSet<Integer> getItemsInBag() {
        return itemsInBag;
    }

    public int getHitPoint() {
        return hitPoint;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer, Equipment> getEquipments() {
        return equipments;
    }

    public void setHitPoint(int hitPoint) {
        this.hitPoint = hitPoint;
    }

    public int getId() {
        return id;
    }
}
