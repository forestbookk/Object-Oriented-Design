import java.util.ArrayList;
import java.util.Scanner;

public class MyScanner {
    private Scanner cin = new Scanner(System.in);

    public void handleInput(ArrayList<CommandUtil> cmdUtilArray) {
        int n = Integer.parseInt(cin.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String nextLine = cin.nextLine();
            String[] str = nextLine.trim().split(" +");
            CommandUtil cmdUtil = new CommandUtil();
            cmdUtil.setOperator(Integer.parseInt(str[0]));
            if (cmdUtil.getOperator() == 1) {
                scanCmd1(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 2) {
                scanCmd2(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 3) {
                scanCmd3(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 4) {
                scanCmd4(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 5) {
                scanCmd5(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 6) {
                scanCmd5(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 7) {
                scanCmd7(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 8) {
                scanCmd8(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 9) {
                scanCmd5(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 10) {
                scanCmd3(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 11) {
                scanCmd8(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 12) {
                scanCmd1(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 13) {
                scanCmd1(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 14) {
                scanCmdE(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 15) {
                scanCmdF(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 16) {
                scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 17) {
                scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 18) {
                scanCmdI(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 19) {
                scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 20) {
                scanCmdG(str, cmdUtil);
            } else if (cmdUtil.getOperator() == 21) {
                scanCmdI(str, cmdUtil);
            } else {
                System.out.println("MyScanner Error");
            }
            cmdUtilArray.add(cmdUtil);
        }
    }

    public void scanCmd1(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        cmdUtil.setName(str[2]);
    }

    public void scanCmd2(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        int botId = Integer.parseInt(str[2]);
        cmdUtil.setItemId(botId);
        cmdUtil.setName(str[3]);
        int capacity = Integer.parseInt(str[4]);
        cmdUtil.setItemFeature(capacity);
        long price = Long.parseLong(str[5]);
        cmdUtil.setPrice(price);
        String type = str[6];
        cmdUtil.setType(type);
        if (type.equals("ReinforcedBottle") || type.equals("RecoverBottle")) {
            double ratio = Double.parseDouble(str[7]);
            cmdUtil.setRatio(ratio);
        }
    }

    public void scanCmd3(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        int botId = Integer.parseInt(str[2]);
        cmdUtil.setItemId(botId);
    }

    public void scanCmd4(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        int equId = Integer.parseInt(str[2]);
        cmdUtil.setItemId(equId);
        cmdUtil.setName(str[3]);
        int star = Integer.parseInt(str[4]);
        cmdUtil.setItemFeature(star);
        long price = Long.parseLong(str[5]);
        cmdUtil.setPrice(price);
        String type = str[6];
        cmdUtil.setType(type);
        if (type.equals("EpicEquipment")) {
            double ratio = Double.parseDouble(str[7]);
            cmdUtil.setRatio(ratio);
        } else if (type.equals("CritEquipment")) {
            int critical = Integer.parseInt(str[7]);
            cmdUtil.setCritical(critical);
        }
    }

    public void scanCmd5(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        int equId = Integer.parseInt(str[2]);
        cmdUtil.setItemId(equId);
    }

    public void scanCmd7(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        int foodId = Integer.parseInt(str[2]);
        cmdUtil.setItemId(foodId);
        cmdUtil.setName(str[3]);
        int energy = Integer.parseInt(str[4]);
        cmdUtil.setItemFeature(energy);
        long price = Long.parseLong(str[5]);
        cmdUtil.setPrice(price);
    }

    public void scanCmd8(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
        int foodId = Integer.parseInt(str[2]);
        cmdUtil.setItemId(foodId);
    }

    public void scanCmdE(String[] str, CommandUtil cmdUtil) {
        int mmPeo = Integer.parseInt(str[1]);
        cmdUtil.setMmPeo(mmPeo);
        int kkLog = Integer.parseInt(str[2]);
        cmdUtil.setKkLog(kkLog);
        ArrayList<String> advNameFight = new ArrayList<>();
        for (int i = 0; i < mmPeo; i++) {
            advNameFight.add(str[3 + i]);
        }
        cmdUtil.setAdvNameFight(advNameFight);
        ArrayList<String> partLog = new ArrayList<>();
        for (int i = 0; i < kkLog; i++) {
            partLog.add(cin.nextLine());
        }
        cmdUtil.setPartLog(partLog);
    }

    public void scanCmdF(String[] str, CommandUtil cmdUtil) {
        cmdUtil.setDate(str[1]);
    }

    public void scanCmdG(String[] str, CommandUtil cmdUtil) {
        int advId = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId);
    }

    public void scanCmdI(String[] str, CommandUtil cmdUtil) {
        int advId1 = Integer.parseInt(str[1]);
        cmdUtil.setAdvId(advId1);
        int advId2 = Integer.parseInt(str[2]);
        cmdUtil.setItemId(advId2);
    }
}
