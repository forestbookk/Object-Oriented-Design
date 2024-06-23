import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        HashMap<Integer, Adventurer> advId = new HashMap<>();//建立id和冒险者的映射
        HashMap<String, Adventurer> advName = new HashMap<>();//建立name和冒险者的映射的映射
        FightLog fightLog = new FightLog();
        Scanner cin = new Scanner(System.in);
        int n = Integer.parseInt(cin.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String nextLine = cin.nextLine();
            String[] str = nextLine.trim().split(" +");
            int a = str[0].equals("15") ? 0 : Integer.parseInt(str[1]);//advId
            if (str[0].equals("1")) {
                Adventurer adventurer = new Adventurer(a, str[2]);
                advId.put(a, adventurer);
                advName.put(str[2], adventurer);
            } else if (str[0].equals("2")) {
                advId.get(a).addABottle(str[2], str[3], str[4]);
            } else if (str[0].equals("3")) {
                advId.get(a).removeABottle(str[2]);
            } else if (str[0].equals("4")) {
                advId.get(a).addAnEquipment(str[2], str[3], str[4]);
            } else if (str[0].equals("5")) {
                advId.get(a).removeAnEquipment(str[2]);
            } else if (str[0].equals("6")) {
                advId.get(a).upgradeAnEquipment(str[2]);
            } else if (str[0].equals("7")) {
                advId.get(a).addAFood(str[2], str[3], str[4]);
            } else if (str[0].equals("8")) {
                advId.get(a).removeAFood(str[2]);
            } else if (str[0].equals("9")) {
                advId.get(a).carryAnEquipment(str[2]);
            } else if (str[0].equals("10")) {
                advId.get(a).carryABottle(str[2]);
            } else if (str[0].equals("11")) {
                advId.get(a).carryAFood(str[2]);
            } else if (str[0].equals("12")) {
                advId.get(a).useABottle(Boolean.FALSE, str[2]);
            } else if (str[0].equals("13")) {
                advId.get(a).useAFood(str[2]);
            } else if (str[0].equals("14")) {
                ArrayList<String> partLog = new ArrayList<>();
                for (int j = 0; j < Integer.parseInt(str[2]); j++) {
                    partLog.add(cin.nextLine());
                }
                ArrayList<Adventurer> advInFight = new ArrayList<>();
                for (int j = 0; j < a; j++) {
                    advInFight.add(advName.get(str[j + 3]));
                }
                fightLog.storeLog(a, Integer.parseInt(str[2]), advInFight, partLog);
            } else if (str[0].equals("15")) {
                fightLog.loadLogOnDate(str[1]);
            } else if (str[0].equals("16")) {
                advId.get(a).loadLog(Boolean.TRUE);
            } else if (str[0].equals("17")) {
                advId.get(a).loadLog(Boolean.FALSE);
            } else {
                System.out.println("MainClass inputInfo Error");
            }
        }
    }
}
