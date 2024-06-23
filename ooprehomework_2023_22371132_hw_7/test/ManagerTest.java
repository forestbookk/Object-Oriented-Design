import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ManagerTest {

    @Test
    public void handleOp() {
        ArrayList<String> inputInfo = new ArrayList<>();
        //---------------------------------------------//
        inputInfo.add("1 475424 irdk4");
        inputInfo.add("1 228482 m1LsOwtB");
        inputInfo.add("18 475424 228482");
        inputInfo.add("18 475424 228482");
        inputInfo.add("1 596449 ?Iqo7");
        inputInfo.add("1 423793 SdOt7S");
        inputInfo.add("18 475424 423793 ");
        inputInfo.add("1 703842 SK1%YEH7+");
        inputInfo.add("1 394266 %x/7m4");
        inputInfo.add("1 242939 h5*bExkqCa");
        inputInfo.add("1 819577 $L^Hnu");
        inputInfo.add("1 473486 KVh+duF/M");
        inputInfo.add("1 533608 rEn/ggD");
        inputInfo.add("4 242939 224622 g7!hPvz 2 77 RegularEquipment");
        inputInfo.add("2 242939 498410 ooVYkf* 59 900 RegularBottle");
        inputInfo.add("6 242939 224622");
        inputInfo.add("7 703842 50866 6&2F+WVfAV4 1 7877");
        inputInfo.add("4 228482 822130 c1+O%s 4 88 EpicEquipment 2");
        inputInfo.add("11 703842 50866");
        inputInfo.add("2 473486 190219 ooVYkf* 41 900 ReinforcedBottle 9.99");
        inputInfo.add("20 473486");
        inputInfo.add("2 242939 267332 ?+drO3ka 19 900 RecoverBottle 9.99");
        inputInfo.add("2 242939 524967 br3Ggo=w 87 900 RegularBottle");
        inputInfo.add("2 423793 236536 ooVYkf* 64 900 ReinforcedBottle 9.99");
        inputInfo.add("7 596449 117687 ru4gXgU11 5 89898");
        inputInfo.add("2 423793 162299 ooVYkf* 58 900 ReinforcedBottle 9.99");
        inputInfo.add("2 596449 217306 ooVYkf* 98 283439 ReinforcedBottle 9.99");
        inputInfo.add("10 423793 162299");
        inputInfo.add("7 242939 673587 s1R?o?mc3915 4 7675");
        inputInfo.add("2 423793 261571 ooVYkf* 11 98 ReinforcedBottle 9.99");
        inputInfo.add("21 423793 261571");
        inputInfo.add("9 242939 224622");
        inputInfo.add("2 475424 72289 br3Ggo=w 79 98 ReinforcedBottle 9.99");
        inputInfo.add("2 475424 225852 ?+drO3ka 29 98 RegularBottle");
        inputInfo.add("4 703842 695759 8^F=t 0 88 EpicEquipment 2.77675 ");
        inputInfo.add("21 703842 695759");
        inputInfo.add("7 242939 617303 1Txupg!DG21 5 96");
        inputInfo.add("4 475424 106785 8^F=t 4 88 EpicEqui pment 2.90");
        inputInfo.add("6 228482 822130");
        inputInfo.add("10 242939 524967");
        inputInfo.add("4 228482 64390 8^F=t 3 2384 RecoverBottle 89.234");
        inputInfo.add("1 960543 fP1Ztl5Y");
        inputInfo.add("4 423793 628000 c1+O%s 1 2347 RegularBottle");
        inputInfo.add("10 473486 190219");
        inputInfo.add("6 228482 822130");
        inputInfo.add("10 475424 225852");
        inputInfo.add("9 228482 64390");
        inputInfo.add("9 703842 695759");
        inputInfo.add("10 242939 498410");
        inputInfo.add("7 819577 498904 SD7cf35 3 2384");
        inputInfo.add("13 819577 SD7cf35");
        inputInfo.add("8 819577 498904");
        inputInfo.add("10 242939 524967");
        inputInfo.add("20 819577");
        inputInfo.add("2 819577 123 hello 2348 53548 RegularBottle");
        inputInfo.add("12 819577 123");
        inputInfo.add("3 819577 123");
        inputInfo.add("22 819577");
        inputInfo.add("23 819577 123 hello RegularBottle");
        //---------------------------------------------//
        int n = inputInfo.size();
        MyScanner myScanner = new MyScanner();
        Manager manager = new Manager();
        for (int i = 0; i < n; i++) {
            String[] str = inputInfo.get(i).trim().split(" +");
            CommandUtil cmdUtil = new CommandUtil();
            cmdUtil.setOperator(Integer.parseInt(str[0]));
            if (cmdUtil.getOperator() == 1) {
                myScanner.scanCmd1(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 2) {
                myScanner.scanCmd2(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 3) {
                myScanner.scanCmd3(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 4) {
                myScanner.scanCmd4(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 5) {
                myScanner.scanCmd5(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 6) {
                myScanner.scanCmd5(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 7) {
                myScanner.scanCmd7(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 8) {
                myScanner.scanCmd8(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 9) {
                myScanner.scanCmd5(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 10) {
                myScanner.scanCmd3(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 11) {
                myScanner.scanCmd8(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 12) {
                myScanner.scanCmd1(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 13) {
                myScanner.scanCmd1(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 14) {
                myScanner.scanCmdE(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 15) {
                myScanner.scanCmdF(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 16) {
                myScanner.scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 17) {
                myScanner.scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 18) {
                myScanner.scanCmdI(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 19) {
                myScanner.scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 20) {
                myScanner.scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 21) {
                myScanner.scanCmdI(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 22) {
                myScanner.scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 23) {
                myScanner.scanCmdJ(str, cmdUtil);
            } else {
                System.out.println("MyScanner Error");
            }
            manager.getCmdUtilArray().add(cmdUtil);
        }
        manager.handleOp();
    }

    @Test
    public void readFightLog() {
        Manager manager = new Manager();
        CommandUtil a1 = new CommandUtil();
        a1.setAdvId(123);
        a1.setName("T2d&=W*r");
        manager.addAnAdventurer(a1);
        CommandUtil a2 = new CommandUtil();
        a2.setAdvId(134);
        a2.setName("Z8^oK");
        manager.addAnAdventurer(a2);

        CommandUtil c1 = new CommandUtil();
        c1.setAdvId(123);
        c1.setItemId(145);
        c1.setType("RegularBottle");
        c1.setPrice(28525);
        c1.setName("nini");
        c1.setItemFeature(2355);
        manager.addABottle(c1);
        manager.sellABottle(c1);
        manager.useABottle(c1);

        CommandUtil cmd = new CommandUtil();
        cmd.setMmPeo(2);
        cmd.setKkLog(10);
        ArrayList<String> advName = new ArrayList<>();
        advName.add("T2d&=W*r");
        advName.add("Z8^oK");
        cmd.setAdvNameFight(advName);
        ArrayList<String> partLog = new ArrayList<>();
        partLog.add("2000/08-T2d&=W*r@Z8^oK-&KRUBIN8v3");
        partLog.add("2000/09-T2d&=W*r@#-h+Kcb*");
        partLog.add("2000/09-Z8^oK@#-*4euMoHk1");
        partLog.add("2000/10-T2d&=W*r-dy/CUi");
        partLog.add("2000/11-Z8^oK-dy/CUi");
        partLog.add("2000/11-T2d&=W*r-dy/CUi");
        partLog.add("2000/12-Z8^oK@#-g7y%dpH");
        partLog.add("2001/01-T2d&=W*r@Z8^oK-1OchE");
        partLog.add("2001/02-T2d&=W*r@Z8^oK-a8dC5f3");
        partLog.add("2001/02-T2d&=W*r-dy/CUi");
        cmd.setPartLog(partLog);

        manager.readFightLog(cmd);
        cmd.setDate("2001/02");
        manager.loadLogOnDate(cmd);
        cmd.setAdvId(123);
        manager.loadAdvLog0(cmd);
        manager.loadAdvLog1(cmd);
        manager.calculateSumPrice(cmd);
    }
}