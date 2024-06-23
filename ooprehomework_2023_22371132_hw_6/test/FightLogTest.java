import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class FightLogTest {

    @Test
    public void ao() {
        HashMap<String, Adventurer> advNameInFight = new HashMap<>();
        ArrayList<Adventurer> advInFight = new ArrayList<>();
        String log = "2022/09-advName2@#-equName";
    }

    @Test
    public void so() {
    }

    @Test
    public void bo() {
    }

    @Test
    public void storeLog() {
        FightLog fightLog = new FightLog();
        Adventurer a1 = new Adventurer(6, "relax");
        Equipment equipment1 = new Equipment(888888, "equName", 10, 328478);
        a1.addAnEquipment(888888, equipment1);
        a1.carryAnEquipment(888888);
        Adventurer a2 = new Adventurer(8, "kome");
        Bottle bottle1 = new Bottle(666666, "jay", 10000, 7485745);
        a2.addABottle(666666, bottle1);
        a2.carryABottle(666666);
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
        in.add("2022/09-kome-jay");
        in.add("2022/09-sky@travel-rose");
        fightLog.storeLog(3,10,advInFight, in);
    }

    @Test
    public void loadLogOnDate() {
        System.out.println("loadLogOnDate test BEGIN!");
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
        fightLog.loadLogOnDate("2013/09");
        fightLog.storeLog(3,8,advInFight, in);
        System.out.println("***********************");
        System.out.println("loadLogOnDate test BEGIN!");
        String date = "2023/10";
        fightLog.loadLogOnDate(date);
        System.out.println("***********************");
        date = "2022/09";
        fightLog.loadLogOnDate(date);
    }
}