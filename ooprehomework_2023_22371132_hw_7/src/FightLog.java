import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FightLog {
    private ArrayList<ArrayList<String>> fightLog = new ArrayList<>();
    private ArrayList<Integer> advBefHitPoint = new ArrayList<>();

    public Boolean ao(ArrayList<String> l, HashMap<String, Adventurer> n, ArrayList<Adventurer> a) {
        String advName = l.get(3);
        if (!n.containsKey(advName)) {
            //非法冒险者 日志无效
            System.out.println("Fight log error");
            return Boolean.FALSE;
        }
        //合法冒险者
        String equName = l.get(4);

        for (int j = 0; j < a.size(); j++) {
            if (a.get(j).getName().equals(advName)) {
                continue;
            }
            int hitPoint0 = a.get(j).getHitPoint();
            int healthPointDec = n.get(advName).useAnEquipment(hitPoint0, Boolean.TRUE, equName);
            if (healthPointDec < 0) {
                //非法武器, 日志无效
                return Boolean.FALSE;
            }

            int hitPointAfterFight = a.get(j).getHitPoint() - healthPointDec;
            a.get(j).setHitPoint(hitPointAfterFight);
            System.out.print(hitPointAfterFight + " ");

        }
        System.out.print("\n");
        return Boolean.TRUE;
    }

    public Boolean so(ArrayList<String> l, HashMap<String, Adventurer> n, ArrayList<Adventurer> a) {
        String advName1 = l.get(3);
        String advName2 = l.get(4);
        if (!n.containsKey(advName1) || !n.containsKey(advName2)) {
            //至少1非法冒险者，日志无效
            System.out.println("Fight log error");
            return Boolean.FALSE;
        }
        //2合法冒险者
        String equName = l.get(5);
        int hitPoint0 = n.get(advName2).getHitPoint();
        int healthPointDec = n.get(advName1).useAnEquipment(hitPoint0, Boolean.TRUE, equName);
        if (healthPointDec < 0) {
            //非法武器，日志无效
            return Boolean.FALSE;
        }
        //合法武器
        int hitPointAfterFight = n.get(advName2).getHitPoint() - healthPointDec;
        n.get(advName2).setHitPoint(hitPointAfterFight);
        System.out.println(n.get(advName2).getId() + " " + hitPointAfterFight);


        return Boolean.TRUE;
    }

    public Boolean bo(ArrayList<String> l, HashMap<String, Adventurer> n, ArrayList<Adventurer> a) {
        String advName = l.get(3);
        if (!n.containsKey(advName)) {
            //非法冒险者，日志无效
            System.out.println("Fight log error");
            return Boolean.FALSE;
        }
        //合法冒险者
        String botName = l.get(4);
        if (n.get(advName).useABottle(Boolean.TRUE, botName)) {
            //合法药水
            return Boolean.TRUE;
        } else {
            //非法药水
            return Boolean.FALSE;
        }
    }

    public void storeLog(int m, int k, ArrayList<Adventurer> advInFight, ArrayList<String> in) {
        storeHitPoint(advInFight);
        HashMap<String, Adventurer> advNameInFight = new HashMap<>();
        for (int i = 0; i < m; i++) {
            advNameInFight.put(advInFight.get(i).getName(), advInFight.get(i));
        }
        for (int i = 0; i < k; i++) {
            String pat = "(\\d{4})/(1[012]|0[1-9])-([^\\s-@#]{1,39})@#-([^\\s-@#]{1,39})";
            Pattern pattern3 = Pattern.compile(pat);
            Matcher matcher3 = pattern3.matcher(in.get(i));
            if (matcher3.find()) {
                //1vN pattern
                ArrayList<String> logAnalysis = new ArrayList<>();
                logAnalysis.add("ao");
                for (int j = 1; j <= 4; j++) {
                    logAnalysis.add(matcher3.group(j));
                }
                if (ao(logAnalysis, advNameInFight, advInFight)) {
                    fightLog.add(logAnalysis);
                    advNameInFight.get(logAnalysis.get(3)).storeLog1(logAnalysis);
                    for (int j = 0; j < m; j++) {
                        if (advInFight.get(j).getName().equals(logAnalysis.get(3))) {
                            continue;
                        }
                        advInFight.get(j).storeLog0(logAnalysis);
                    }
                }
                continue;
            }
            pat = "(\\d{4})/(1[012]|0[1-9])-([^\\s-@#]{1,39})@([^\\s-@#]{1,39})-([^\\s-@#]{1,39})";
            Pattern pattern2 = Pattern.compile(pat);
            Matcher matcher2 = pattern2.matcher(in.get(i));
            if (matcher2.find()) {
                //1v1 pattern
                ArrayList<String> logAnalysis = new ArrayList<>();
                logAnalysis.add("so");
                for (int j = 1; j <= 5; j++) {
                    logAnalysis.add(matcher2.group(j));
                }
                if (so(logAnalysis, advNameInFight, advInFight)) {
                    fightLog.add(logAnalysis);
                    advNameInFight.get(logAnalysis.get(3)).storeLog1(logAnalysis);
                    advNameInFight.get(logAnalysis.get(4)).storeLog0(logAnalysis);
                }
                continue;
            }
            //YYYY/MM-{adv_name_1}-{name}
            pat = "(\\d{4})/(1[012]|0[1-9])-([^\\s-@#]{1,39})-([^\\s-@#]{1,39})";
            Pattern pattern1 = Pattern.compile(pat);
            Matcher matcher1 = pattern1.matcher(in.get(i));
            if (matcher1.find()) {
                ArrayList<String> logAnalysis = new ArrayList<>();
                logAnalysis.add("bo");
                for (int j = 1; j <= 4; j++) {
                    logAnalysis.add(matcher1.group(j));
                }
                if (bo(logAnalysis, advNameInFight, advInFight)) {
                    fightLog.add(logAnalysis);
                }
            }
        }
        helpEmployer(advInFight);
    }

    public void loadLogOnDate(String date) {
        if (this.fightLog.isEmpty()) {
            System.out.println("No Matched Log");
            return;
        }
        String[] str = date.split("/");
        int count = 0;
        for (ArrayList<String> logAnalysis : this.fightLog) {
            String dateLog = logAnalysis.get(1) + "/" + logAnalysis.get(2);
            if ((logAnalysis.get(1).equals(str[0])) && (logAnalysis.get(2).equals(str[1]))) {
                count++;
                String advName1 = logAnalysis.get(3);
                System.out.print(str[0] + "/" + str[1] + " " + advName1 + " ");
                if (logAnalysis.get(0).equals("ao")) {
                    System.out.println("AOE-attacked with " + logAnalysis.get(4));
                } else if (logAnalysis.get(0).equals("so")) {
                    String advName2 = logAnalysis.get(4);
                    System.out.println("attacked " + advName2 + " with " + logAnalysis.get(5));
                } else if (logAnalysis.get(0).equals("bo")) {
                    System.out.println("used " + logAnalysis.get(4));
                } else {
                    System.out.println("FightLog loadLogOnDate error");
                }
            } else if (date.compareTo(dateLog) < 0) {
                break;
            }
        }
        if (count == 0) {
            System.out.println("No Matched Log");
        }
    }

    public void storeHitPoint(ArrayList<Adventurer> advInFight) {
        System.out.println("Enter Fight Mode");
        advBefHitPoint = new ArrayList<>();
        for (Adventurer adv : advInFight) {
            advBefHitPoint.add(adv.getHitPoint());
        }
    }

    public void helpEmployer(ArrayList<Adventurer> advInFight) {
        for (int i = 0; i < advInFight.size(); i++) {
            int currentHitPoint = advInFight.get(i).getHitPoint();
            if (currentHitPoint <= advBefHitPoint.get(i) / 2) {
                int a = advBefHitPoint.get(i) - currentHitPoint;
                Adventurer adv = advInFight.get(i);
                Iterator<Adventurer> employees = adv.getEmployees().values().iterator();
                while (employees.hasNext()) {
                    Adventurer employee = employees.next();
                    long employeeMoney = employee.getMoney();
                    long aidFund = a * 10000L;
                    if (employeeMoney >= aidFund) {
                        employee.setMoney(-aidFund);
                        advInFight.get(i).setMoney(aidFund);
                    } else {
                        employee.setMoney(-employeeMoney);
                        advInFight.get(i).setMoney(employeeMoney);
                    }
                }
            }

        }
    }
}
