import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private ArrayList<CommandUtil> cmdUtilArray = new ArrayList<>();
    private HashMap<Integer, Adventurer> idAdventurer = new HashMap<>();
    private HashMap<String, Adventurer> nameAdventurer = new HashMap<>();
    private FightLog fightlog = new FightLog();

    public void handleOp() {
        int n = cmdUtilArray.size();
        for (int i = 0; i < n; i++) {
            CommandUtil cmd = cmdUtilArray.get(i);
            if (cmd.getOperator() == 1) {
                addAnAdventurer(cmd);
            } else if (cmd.getOperator() == 2) {
                addABottle(cmd);
            } else if (cmd.getOperator() == 3) {
                sellABottle(cmd);
            } else if (cmd.getOperator() == 4) {
                addAnEquipment(cmd);
            } else if (cmd.getOperator() == 5) {
                sellAnEquipment(cmd);
            } else if (cmd.getOperator() == 6) {
                upgradeAnEquipment(cmd);
            } else if (cmd.getOperator() == 7) {
                addAFood(cmd);
            } else if (cmd.getOperator() == 8) {
                removeAFood(cmd);
            } else if (cmd.getOperator() == 9) {
                carryAnEquipment(cmd);
            } else if (cmd.getOperator() == 10) {
                carryABottle(cmd);
            } else if (cmd.getOperator() == 11) {
                carryAFood(cmd);
            } else if (cmd.getOperator() == 12) {
                useABottle(cmd);
            } else if (cmd.getOperator() == 13) {
                useAFood(cmd);
            } else if (cmd.getOperator() == 14) {
                readFightLog(cmd);
            } else if (cmd.getOperator() == 15) {
                loadLogOnDate(cmd);
            } else if (cmd.getOperator() == 16) {
                loadAdvLog1(cmd);
            } else if (cmd.getOperator() == 17) {
                loadAdvLog0(cmd);
            } else if (cmd.getOperator() == 18) {
                employAnAdventurer(cmd);
            } else if (cmd.getOperator() == 19) {
                calculateSumPrice(cmd);
            } else if (cmd.getOperator() == 20) {
                searchMaxPrice(cmd);
            } else if (cmd.getOperator() == 21) {
                getCommodityName(cmd);
            } else if (cmd.getOperator() == 22) {
                sellAllItems(cmd);
            } else if (cmd.getOperator() == 23) {
                buyAnItem(cmd);
            }
        }
    }

    public void addAnAdventurer(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        String advName = cmd.getName();
        Adventurer adventurer = new Adventurer(advId, advName);
        idAdventurer.put(advId, adventurer);
        nameAdventurer.put(advName, adventurer);
    }

    public void addABottle(CommandUtil cmd) {
        int botId = cmd.getItemId();
        String botName = cmd.getName();
        int capacity = cmd.getItemFeature();
        long price = cmd.getPrice();
        String type = cmd.getType();
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        if (type.equals("RegularBottle")) {
            Bottle bottle = new Bottle(botId, botName, capacity, price);
            adventurer.addABottle(botId, bottle);
        } else if (type.equals("ReinforcedBottle")) {
            double ratio = cmd.getRatio();
            Bottle bottle = new ReinforcedBottle(botId, botName, capacity, price, ratio);

            adventurer.addABottle(botId, bottle);
        } else if (type.equals("RecoverBottle")) {
            double ratio = cmd.getRatio();
            Bottle bottle = new RecoverBottle(botId, botName, capacity, price, ratio);
            adventurer.addABottle(botId, bottle);
        } else {
            System.out.println("Manager.addABottle error");
        }
    }

    public void sellABottle(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        int botId = cmd.getItemId();
        adventurer.sellABottle(botId);
    }

    public void addAnEquipment(CommandUtil cmd) {
        int equId = cmd.getItemId();
        String equName = cmd.getName();
        int star = cmd.getItemFeature();
        long price = cmd.getPrice();
        String type = cmd.getType();
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        if (type.equals("RegularEquipment")) {
            Equipment equipment = new Equipment(equId, equName, star, price);
            adventurer.addAnEquipment(equId, equipment);
        } else if (type.equals("CritEquipment")) {
            int critical = cmd.getCritical();
            Equipment equipment = new CritEquipment(equId, equName, star, price, critical);
            adventurer.addAnEquipment(equId, equipment);
        } else if (type.equals("EpicEquipment")) {
            double ratio = cmd.getRatio();
            Equipment equipment = new EpicEquipment(equId, equName, star, price, ratio);
            adventurer.addAnEquipment(equId, equipment);
        } else {
            System.out.println("Manager.addABottle error");
        }
    }

    public void sellAnEquipment(CommandUtil cmd) {
        idAdventurer.get(cmd.getAdvId()).sellAnEquipment(cmd.getItemId());
    }

    public void upgradeAnEquipment(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.upgradeAnEquipment(cmd.getItemId());
    }

    public void addAFood(CommandUtil cmd) {
        int foodId = cmd.getItemId();
        String name = cmd.getName();
        int energy = cmd.getItemFeature();
        long price = cmd.getPrice();
        Food food = new Food(foodId, name, energy, price);
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.addAFood(foodId, food);
    }

    public void removeAFood(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.removeAFood(cmd.getItemId());
    }

    public void carryAnEquipment(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.carryAnEquipment(cmd.getItemId());
    }

    public void carryABottle(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.carryABottle(cmd.getItemId());
    }

    public void carryAFood(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.carryAFood(cmd.getItemId());
    }

    public void useABottle(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.useABottle(false, cmd.getName());
    }

    public void useAFood(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        Adventurer adventurer = idAdventurer.get(advId);
        adventurer.useAFood(cmd.getName());
    }

    public void readFightLog(CommandUtil cmd) {
        int mmPeo = cmd.getMmPeo();
        int kkLog = cmd.getKkLog();
        ArrayList<String> advNameFight = cmd.getAdvNameFight();
        ArrayList<Adventurer> advInFight = new ArrayList<>();
        for (int i = 0; i < mmPeo; i++) {
            Adventurer adventurer = nameAdventurer.get(advNameFight.get(i));
            advInFight.add(adventurer);
        }
        fightlog.storeLog(mmPeo, kkLog, advInFight, cmd.getPartLog());
    }

    public void loadLogOnDate(CommandUtil cmd) {
        fightlog.loadLogOnDate(cmd.getDate());
    }

    public void loadAdvLog1(CommandUtil cmd) {
        Adventurer adv = idAdventurer.get(cmd.getAdvId());
        adv.loadLog(true);
    }

    public void loadAdvLog0(CommandUtil cmd) {
        Adventurer adv = idAdventurer.get(cmd.getAdvId());
        adv.loadLog(false);
    }

    public void employAnAdventurer(CommandUtil cmd) {
        int advId1 = cmd.getAdvId();
        Adventurer employer = idAdventurer.get(advId1);
        int advId2 = cmd.getItemId();
        Adventurer employee = idAdventurer.get(advId2);
        employer.employAnAdventurer(advId2, employee);
    }

    public void calculateSumPrice(CommandUtil cmd) {
        idAdventurer.get(cmd.getAdvId()).calculateSumCommodity();
    }

    public void searchMaxPrice(CommandUtil cmd) {
        idAdventurer.get(cmd.getAdvId()).searchMaxPrice();
    }

    public void getCommodityName(CommandUtil cmd) {
        int comId = cmd.getItemId();
        idAdventurer.get(cmd.getAdvId()).getCommodityName(comId);
    }

    public void sellAllItems(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        idAdventurer.get(advId).sellAllItems();
    }

    public void buyAnItem(CommandUtil cmd) {
        int advId = cmd.getAdvId();
        idAdventurer.get(advId).buyAnItem(cmd);
    }

    public ArrayList<CommandUtil> getCmdUtilArray() {
        return cmdUtilArray;
    }
}
