import java.util.HashMap;

public class Adventurer {
    private int id;
    private String name;
    private int bottleCount = 0;
    private int equipmentCount = 0;
    private HashMap<Integer, Bottle> bottles = new HashMap<>();
    private HashMap<Integer, Equipment> equipments = new HashMap<>();

    public HashMap<Integer, Bottle> getBottles() {
        return bottles;
    }

    public void setBottles(HashMap<Integer, Bottle> bottles) {
        this.bottles = bottles;
    }

    public HashMap<Integer, Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(HashMap<Integer, Equipment> equipments) {
        this.equipments = equipments;
    }

    public int getBottleCount() {
        return bottleCount;
    }

    public int getEquipmentCount() {
        return equipmentCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addABottle(int id, String name, int capacity) {
        bottleCount += 1;
        Bottle bottle = new Bottle();
        bottle.setId(id);
        bottle.setName(name);
        bottle.setCapacity(capacity);
        bottles.put(id, bottle);
    }

    public void removeABottle(int id) {
        bottleCount -= 1;
        System.out.print(bottleCount);
        System.out.print(" ");
        System.out.println(bottles.get(id).getName());
        bottles.remove(id);
    }

    public void addAnEquipment(int id, String name, int star) {
        equipmentCount += 1;
        Equipment equipment = new Equipment();
        equipment.setId(id);
        equipment.setName(name);
        equipment.setStar(star);
        equipments.put(id, equipment);
    }

    public void removeAnEquipment(int id) {
        equipmentCount -= 1;
        System.out.print(equipmentCount);
        System.out.print(" ");
        System.out.println(equipments.get(id).getName());
        equipments.remove(id);
    }

    public void upgradeAnEquipment(int id) {
        int old = equipments.get(id).getStar();
        equipments.get(id).setStar(old + 1);
        System.out.print(equipments.get(id).getName());
        System.out.print(" ");
        System.out.println(old + 1);
    }
}
