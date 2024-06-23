import org.junit.Test;

import static org.junit.Assert.*;

public class AdventurerTest {

    @Test
    public void getId() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.setId(1);
        assert (testAdventurer.getId() == 1);
    }

    @Test
    public void setId() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.setId(2147483646);
        assert (testAdventurer.getId() == 2147483646);
    }

    @Test
    public void getName() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.setName("jiwo3ij9eu;[,.difn3902-=1]'[./,;'p0[;");
        assert (testAdventurer.getName().equals("jiwo3ij9eu;[,.difn3902-=1]'[./,;'p0[;"));
    }

    @Test
    public void setName() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.setName("你好");
        assert (testAdventurer.getName().equals("你好"));
    }

    @Test
    public void addABottle() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.addABottle(13, "nini", 5);
        assert (testAdventurer.getBottleCount() == 1);
    }

    @Test
    public void removeABottle() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.addABottle(13, "nini", 5);
        testAdventurer.addABottle(15, "happy", 4);
        testAdventurer.removeABottle(13);
        assert (testAdventurer.getBottleCount() == 1);
    }

    @Test
    public void addAnEquipment() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.addAnEquipment(123, "irene", 4);
        assert (testAdventurer.getEquipmentCount() == 1);
        testAdventurer.addAnEquipment(999, "seulgi", 2);
        assert (testAdventurer.getEquipmentCount() == 2);
    }

    @Test
    public void removeAnEquipment() {
        Adventurer testAdventurer = new Adventurer();
        assert (testAdventurer.getEquipmentCount() == 0);
        testAdventurer.addAnEquipment(223732, "jk", 5);
        assert (testAdventurer.getEquipmentCount() == 1);
    }

    @Test
    public void upgradeAnEquipment() {
        Adventurer testAdventurer = new Adventurer();
        testAdventurer.addAnEquipment(888, "jiangting", 10);
        testAdventurer.upgradeAnEquipment(888);
        assert (testAdventurer.getEquipments().get(888).getStar() == 11);
    }
}