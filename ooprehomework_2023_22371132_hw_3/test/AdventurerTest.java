import org.junit.Test;

import static org.junit.Assert.*;

public class AdventurerTest {


    @Test
    public void addABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addABottle("123", "Mine", "88");
        assertEquals(adventurer.getBottleCount(), 1);
    }

    @Test
    public void removeABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addABottle("123", "Mine", "88");
        adventurer.removeABottle("123");
        assertEquals(adventurer.getBottleCount(), 0);
    }

    @Test
    public void addAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAnEquipment("111", "Divine", "4");
        assertEquals(adventurer.getEquipmentCount(), 1);
    }

    @Test
    public void removeAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAnEquipment("111", "Divine", "4");
        adventurer.removeAnEquipment("111");
        assertEquals(adventurer.getEquipmentCount(), 0);
    }

    @Test
    public void upgradeAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAnEquipment("111", "Divine", "4");
        adventurer.upgradeAnEquipment("111");
        assertEquals(advId, 888);
    }

    @Test
    public void addAFood() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("799", "eventHorizon", "100");
        assertEquals(adventurer.getFoodCount(), 1);
    }

    @Test
    public void removeAFood() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("799", "eventHorizon", "100");
        adventurer.removeAFood("799");
        assertEquals(adventurer.getFoodCount(), 0);
    }

    @Test
    public void carryAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAnEquipment("111", "Divine", "5");
        adventurer.carryAnEquipment("111");
        assertTrue(adventurer.getItemsInBag().contains(111));
    }

    @Test
    public void carryABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addABottle("666", "Lips", "6");
        adventurer.carryABottle("666");
        assertTrue(adventurer.getItemsInBag().contains(666));
    }

    @Test
    public void carryAFood() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("688", "hello", "10");
        adventurer.carryAFood("688");
        assertTrue (adventurer.getItemsInBag().contains(688));
    }

    @Test
    public void useABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addABottle("666", "Lips", "6");
        adventurer.carryABottle("666");
        adventurer.useABottle("Lips");
        assertEquals (adventurer.getHitPoint(), 506);
        adventurer.useABottle("Lips");
        assertEquals (adventurer.getHitPoint(), 506);
    }

    @Test
    public void useAFood() {
        int advId = 333;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("688", "hello", "10");
        adventurer.carryAFood("688");
        adventurer.useAFood("hello");
        assertEquals (adventurer.getLevel(), 11);
        assertEquals (adventurer.getFoodCount(), 0);
    }
}