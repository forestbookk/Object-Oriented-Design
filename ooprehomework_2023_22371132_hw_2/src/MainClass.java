import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        ArrayList<ArrayList<String>> inputInfo = new ArrayList<>();
        Scanner cin = new Scanner(System.in);
        int n = Integer.parseInt(cin.nextLine().trim());
        int advId;
        int botId;
        int equId;
        int capacity;
        int star;

        for (int i = 0; i < n; i++) {
            String nextLine = cin.nextLine();
            String[] strings = nextLine.trim().split(" +");
            inputInfo.add(new ArrayList<>(Arrays.asList(strings)));
        }

        HashMap<Integer, Adventurer> adventurers = new HashMap<>();
        for (int i = 0; i < n; i++) {
            switch (inputInfo.get(i).get(0)) {
                case "1": /*add an adventurer*/
                    Adventurer adventurer = new Adventurer();
                    advId = Integer.parseInt(inputInfo.get(i).get(1));
                    adventurer.setId(advId);
                    adventurer.setName(inputInfo.get(i).get(2));
                    adventurers.put(advId, adventurer);
                    break;
                case "2": /*add a bottle*/
                    advId = Integer.parseInt(inputInfo.get(i).get(1));
                    botId = Integer.parseInt(inputInfo.get(i).get(2));
                    capacity = Integer.parseInt(inputInfo.get(i).get(4));
                    adventurers.get(advId).addABottle(botId, inputInfo.get(i).get(3), capacity);
                    break;
                case "3": /*remove a bottle*/
                    advId = Integer.parseInt(inputInfo.get(i).get(1));
                    botId = Integer.parseInt(inputInfo.get(i).get(2));
                    adventurers.get(advId).removeABottle(botId);
                    break;
                case "4": /*add an equipment*/
                    advId = Integer.parseInt(inputInfo.get(i).get(1));
                    equId = Integer.parseInt(inputInfo.get(i).get(2));
                    star = Integer.parseInt(inputInfo.get(i).get(4));
                    adventurers.get(advId).addAnEquipment(equId, inputInfo.get(i).get(3), star);
                    break;
                case "5": /*remove an equipment*/
                    advId = Integer.parseInt(inputInfo.get(i).get(1));
                    equId = Integer.parseInt(inputInfo.get(i).get(2));
                    adventurers.get(advId).removeAnEquipment(equId);
                    break;
                case "6": /*upgrade an equipment*/
                    advId = Integer.parseInt(inputInfo.get(i).get(1));
                    equId = Integer.parseInt(inputInfo.get(i).get(2));
                    adventurers.get(advId).upgradeAnEquipment(equId);
                    break;
                default:
                    break;
            }
        }
    }
}
