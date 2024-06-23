import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AdventurerTest {

    @Test
    public void sellABottle() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        Bottle bottle1 = new Bottle(123, "Mine", 88, 289728);
        adventurer.addABottle(123, bottle1);
        adventurer.sellABottle(123);
        Bottle bottle2 = new Bottle(123, "Mine", 88, 289728);
        adventurer.addABottle(123, bottle2);
        adventurer.carryABottle(123);
        Bottle bottle3 = new Bottle(12, "Mine", 88, 289728);
        adventurer.addABottle(12, bottle3);
        adventurer.carryABottle(12);
        adventurer.useABottle(false, "hhhh");
        adventurer.carryABottle(12);
        adventurer.sellABottle(123);
        adventurer.carryABottle(98459845);
        adventurer.sellABottle(12);
        Bottle bottle4 = new RecoverBottle(123, "tired", 2938, 238754, 8.888);
        adventurer.addABottle(123, bottle4);
        adventurer.carryABottle(123);
        adventurer.useABottle(false, "tired");
        adventurer.useABottle(false, "tired");
        Bottle bottle5 = new ReinforcedBottle(1234, "tred", 284, 234873, 9.234);
        adventurer.addABottle(1234, bottle5);
        adventurer.carryABottle(1234);
        adventurer.useABottle(true, "tred");
    }

    @Test
    public void sellAnEquipment() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        Equipment equipment1 = new Equipment(1112, "Divine", 4, 87347);
        adventurer.addAnEquipment(1112, equipment1);
        adventurer.sellAnEquipment(1112);
        Equipment equipment2 = new Equipment(111, "Divine", 4, 8347);
        adventurer.addAnEquipment(111, equipment2);
        Equipment equipment3 = new Equipment(11132, "Divine", 8989, 87347);
        adventurer.addAnEquipment(11132, equipment3);
        adventurer.carryAnEquipment(111);
        adventurer.carryAnEquipment(111);
        adventurer.carryAnEquipment(11132);
        adventurer.upgradeAnEquipment(111);
        adventurer.sellAnEquipment(111);
        adventurer.carryAnEquipment(23478);
        Equipment equipment4 = new EpicEquipment(34234, "color", 348578, 435, 4.3458);
        adventurer.addAnEquipment(34234, equipment4);
        adventurer.carryAnEquipment(34234);
        adventurer.useAnEquipment(8978, false, "color");
        Equipment equipment5 = new CritEquipment(987, "always", 2385782, 238743, 23478);
        adventurer.addAnEquipment(987, equipment5);
        adventurer.carryAnEquipment(987);
        adventurer.useAnEquipment(845, true, "always");
    }

    @Test
    public void removeAFood() {
        int advId = 888;
        String advName = "恭喜发财";
        Adventurer adventurer = new Adventurer(advId, advName);
        Food food1 = new Food(799, "asdf", 23, 23234);
        adventurer.addAFood(799, food1);
        adventurer.removeAFood(799);
        Food food2 = new Food(8455, "ajufha", 34728, 2575);
        adventurer.addAFood(799, food2);
        adventurer.carryAFood(799);
        Food food3 = new Food(99, "eventHorizon", 28358, 28344385);
        adventurer.addAFood(99, food3);
        adventurer.carryAFood(99);
        adventurer.carryAFood(99);
        adventurer.removeAFood(799);
        adventurer.carryAFood(23758457);
        adventurer.useAFood("eventHorizon");
        adventurer.useAFood("be");
    }

    @Test
    public void carryAnEquipment() {
    }

    @Test
    public void carryABottle() {
    }

    @Test
    public void carryAFood() {
    }

    @Test
    public void useAnEquipment() {

    }

    @Test
    public void useABottle() {
    }

    @Test
    public void useAFood() {
    }

    @Test
    public void storeLog0() {
    }

    @Test
    public void loadLog() {
        FightLog fightLog = new FightLog();
        Adventurer a1 = new Adventurer(6, "relax");
        Equipment equipment1 = new Equipment(888888, "equName", 10, 328478);
        a1.addAnEquipment(888888, equipment1);
        a1.carryAnEquipment(888888);
        Adventurer a2 = new Adventurer(8, "kome");
        Adventurer a3 = new Adventurer(10, "THANK_YOU");
        Equipment equipment2 = new Equipment(66666, "PACE", 5, 28374);
        a3.addAnEquipment(66666, equipment2);
        a3.carryAnEquipment(66666);
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
        fightLog.storeLog(3, 8, advInFight, in);
        a1.loadLog(Boolean.FALSE);
        a2.loadLog(Boolean.FALSE);
        a1.loadLog(Boolean.TRUE);
    }


    @Test
    public void employAnAdventurer() {

    }

    @Test
    public void calculateSumCommodity() {
        Adventurer adventurer = new Adventurer(123, "name");
        Adventurer employee = new Adventurer(145, "hello");
        adventurer.employAnAdventurer(145, employee);
        adventurer.employAnAdventurer(145, employee);
        adventurer.getCommodityName(145);
        Food food1 = new Food(799, "asdf", 23, 23234);
        adventurer.addAFood(799, food1);
        adventurer.getCommodityName(799);
        Equipment equipment4 = new EpicEquipment(34234, "color", 348578, 435, 4.3458);
        adventurer.addAnEquipment(34234, equipment4);
        Equipment equipment2 = new Equipment(111, "Divine", 4, 8347);
        adventurer.addAnEquipment(111, equipment2);
        adventurer.getCommodityName(111);
        Equipment equipment3 = new Equipment(11132, "Divine", 8989, 87347);
        adventurer.addAnEquipment(11132, equipment3);
        Bottle bottle3 = new Bottle(12, "Mine", 88, 289728);
        adventurer.addABottle(12, bottle3);
        adventurer.getCommodityName(12);
        adventurer.carryABottle(12);
        adventurer.calculateSumCommodity();
        adventurer.searchMaxPrice();

        Food food = new Food(1, "fooname", 34, 14);
        adventurer.addAFood(1, food);
        adventurer.carryAFood(1);
        Bottle bottle1 = new Bottle(2, "botName1", 1243, 2355);
        adventurer.addABottle(2, bottle1);
        adventurer.carryABottle(2);
        Bottle bottle2 = new ReinforcedBottle(3, "botName2", 13434, 134324, 3.235235);
        adventurer.addABottle(3, bottle2);
        adventurer.carryABottle(3);
        bottle3 = new RecoverBottle(4, "botName3", 23545, 2343254, 23873);
        adventurer.addABottle(4, bottle3);
        adventurer.carryABottle(4);
        Equipment equipment1 = new Equipment(5, "equName1", 23543, 235234);
        adventurer.addAnEquipment(5, equipment1);
        adventurer.carryAnEquipment(5);
        equipment2 = new CritEquipment(6, "equName2", 2342, 23423, 234324);
        adventurer.addAnEquipment(6, equipment2);
        adventurer.carryAnEquipment(6);
        equipment3 = new EpicEquipment(7, "equName3", 2343, 235245, 234.328);
        adventurer.addAnEquipment(7, equipment3);
        adventurer.carryAnEquipment(7);

        adventurer.sellAllItems();
        CommandUtil cmd = new CommandUtil();
        String[] type = {"Food", "RegularBottle", "ReinforcedBottle", "RecoverBottle", "RegularEquipment", "CritEquipment", "EpicEquipment"};
        for (int i = 0; i < type.length; i++) {
            cmd.setType(type[i]);
            cmd.setItemId(i + 10);
            cmd.setName("name");
            cmd.setCritical(14);
            cmd.setRatio(1.34);
            adventurer.buyAnItem(cmd);
        }


    }

    @Test
    public void searchMaxPrice() {
    }

    @Test
    public void getCommodityName() {
    }
}