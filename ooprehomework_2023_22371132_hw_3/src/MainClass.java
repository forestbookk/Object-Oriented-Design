import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        ArrayList<ArrayList<String>> l = new ArrayList<>();
        Scanner cin = new Scanner(System.in);
        int n = Integer.parseInt(cin.nextLine().trim());
        for (int i = 0; i < n; i++) {
            String nextLine = cin.nextLine();
            String[] strings = nextLine.trim().split(" +");
            l.add(new ArrayList<>(Arrays.asList(strings)));
        }
        HashMap<Integer, Adventurer> advMap = new HashMap<>();
        for (int i = 0; i < l.size(); i++) {
            int a = Integer.parseInt(l.get(i).get(1));//advId
            switch (l.get(i).get(0)) {
                case "1":
                    Adventurer adventurer = new Adventurer(a, l.get(i).get(2));
                    advMap.put(a, adventurer);
                    break;
                case "2":
                    advMap.get(a).addABottle(l.get(i).get(2), l.get(i).get(3), l.get(i).get(4));
                    break;
                case "3":
                    advMap.get(a).removeABottle(l.get(i).get(2));
                    break;
                case "4":
                    advMap.get(a).addAnEquipment(l.get(i).get(2), l.get(i).get(3), l.get(i).get(4));
                    break;
                case "5":
                    advMap.get(a).removeAnEquipment(l.get(i).get(2));
                    break;
                case "6":
                    advMap.get(a).upgradeAnEquipment(l.get(i).get(2));
                    break;
                case "7":
                    advMap.get(a).addAFood(l.get(i).get(2), l.get(i).get(3), l.get(i).get(4));
                    break;
                case "8":
                    advMap.get(a).removeAFood(l.get(i).get(2));
                    break;
                case "9":
                    advMap.get(a).carryAnEquipment(l.get(i).get(2));
                    break;
                case "10":
                    advMap.get(a).carryABottle(l.get(i).get(2));
                    break;
                case "11":
                    advMap.get(a).carryAFood(l.get(i).get(2));
                    break;
                case "12":
                    advMap.get(a).useABottle(l.get(i).get(2));
                    break;
                case "13":
                    advMap.get(a).useAFood(l.get(i).get(2));
                    break;
                default:
                    break;
            }
        }
    }
}
