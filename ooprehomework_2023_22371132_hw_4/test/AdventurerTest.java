import org.junit.Test;

import java.util.ArrayList;

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
        adventurer.addABottle("123", "Mine", "88");
        adventurer.carryABottle("123");
        adventurer.addABottle("12", "Mine", "88");
        adventurer.carryABottle("12");
        adventurer.removeABottle("123");
        adventurer.removeABottle("12");
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
        adventurer.addAnEquipment("111", "Divine", "4");
        adventurer.carryAnEquipment("111");
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
        adventurer.addAFood("799", "eventHorizon", "100");
        adventurer.carryAFood("799");
        adventurer.addAFood("99", "eventHorizon", "100");
        adventurer.carryAFood("99");
        adventurer.removeAFood("799");
        adventurer.removeAFood("99");
        assertEquals(adventurer.getFoodCount(), 0);
    }

    @Test
    public void carryAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAnEquipment("111", "Divine", "5");
        adventurer.carryAnEquipment("111");
        adventurer.carryAnEquipment("111");
        assertTrue(adventurer.getItemsInBag().contains(111));
        adventurer.addAnEquipment("9", "Divine", "5");
        adventurer.carryAnEquipment("9");
        adventurer.carryAnEquipment("666666");
    }

    @Test
    public void carryABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addABottle("666", "Lips", "6");
        adventurer.carryABottle("666");
        assertTrue(adventurer.getItemsInBag().contains(666));
        adventurer.addABottle("7999", "Lips", "6");
        adventurer.carryABottle("7999");
    }

    @Test
    public void carryAFood() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("688", "hello", "10");
        adventurer.carryAFood("688");
        adventurer.carryAFood("688");
        assertTrue (adventurer.getItemsInBag().contains(688));
        adventurer.addAFood("68", "hello", "10");
        adventurer.carryAFood("68");
    }

    @Test
    public void useAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAnEquipment("111", "Divine", "5");
        adventurer.carryAnEquipment("111");
        adventurer.useAnEquipment(Boolean.FALSE, "ive");
        adventurer.useAnEquipment(Boolean.TRUE, "ive");
        assertTrue(adventurer.getItemsInBag().contains(111));
        adventurer.addAnEquipment("9", "Divine", "5");
        adventurer.carryAnEquipment("9");
    }

    @Test
    public void useABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addABottle("666", "Lips", "6");
        adventurer.carryABottle("666");
        adventurer.useABottle(Boolean.FALSE,"one");
        adventurer.useABottle(Boolean.TRUE,"one");
        adventurer.useABottle(Boolean.FALSE, "Lips");
        adventurer.useABottle(Boolean.FALSE, "Lips");
    }

    @Test
    public void useAFood() {
        int advId = 333;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("688", "hello", "10");
        adventurer.carryAFood("688");
        adventurer.useAFood("hello");
        adventurer.useAFood("thin");
        adventurer.addAFood("88888", "listen", "10000");
        adventurer.carryAFood("88888");
        adventurer.removeAFood("88888");
        adventurer.useAFood("listen");
    }

    @Test
    public void storeLog0() {
    }

    @Test
    public void loadLog() {
        FightLog fightLog = new FightLog();
        Adventurer a1 = new Adventurer(6, "relax");
        a1.addAnEquipment("888888", "equName", "10");
        a1.carryAnEquipment("888888");
        Adventurer a2 = new Adventurer(8, "kome");
        Adventurer a3 = new Adventurer(10, "THANK_YOU");
        a3.addAnEquipment("66666", "PACE", "5");
        a3.carryAnEquipment("66666");
        ArrayList<Adventurer> advInFight = new ArrayList<>();
        advInFight.add(a1);
        advInFight.add(a2);
        advInFight.add(a3);
        ArrayList<String> in = new ArrayList<>();
        in.add("2022/09-relax@#-equName");
        in.add("2022/09-relax@#-ourName");
        in.add("2022/09-relax@kome-equName1");
        in.add("2022/09-relax@kome-equName");
        in.add("2022/09-THANK_YOU-PACE");
        in.add("2022/09-edge-PACE");
        in.add("2022/09-oneday@kome-equName");
        in.add("2022/09-feeling@#-equName");
        fightLog.storeLog(3,8,advInFight, in);
        a1.loadLog(Boolean.FALSE);
        a2.loadLog(Boolean.FALSE);
        a1.loadLog(Boolean.TRUE);
    }

    @Test
    public void storeLog1() {
    }

    @Test
    public void getLevel() {
        Adventurer adventurer = new Adventurer(101010, "advName");
        int level = adventurer.getLevel();
        assertEquals(level, 1);
    }

    @Test
    public void getEquipments() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        adventurer.addAFood("688", "hello", "10");
        adventurer.carryAFood("688");
        assertTrue (adventurer.getItemsInBag().contains(688));
        adventurer.addAFood("68", "hello", "10");
        adventurer.carryAFood("68");
    }

    @Test
    public void setHitPoint() {
    }

    @Test
    public void getId() {
    }
}