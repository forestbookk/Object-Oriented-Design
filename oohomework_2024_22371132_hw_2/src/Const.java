import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Const {
    private static Const instance = new Const();

    private Const() {
    }

    public static Const getInstance() {
        return instance;
    }

    private int funDefNum;

    public void setFunDefNum(int funDefNum) {
        this.funDefNum = funDefNum;
    }

    private HashMap<String, ArrayList<String>> funDef = new HashMap<>();

    public HashMap<String, ArrayList<String>> getFunDef() {
        return funDef;
    }

    public void addFunDef(String input) {
        String deSp = input.replaceAll(" ", "");
        String def = deSp.replaceAll("\t", "");
        String funName = def.substring(0, 1);
        StringBuilder fps = new StringBuilder();
        fps.append(def.charAt(2));
        for (int i = 3; def.charAt(i) != ')'; i++) {
            if (def.charAt(i) != ',') {
                fps.append(def.charAt(i));
            }
        }
        String regex = "=.+";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(def);
        if (m.find()) {
            ArrayList<String> funFPsExpr = new ArrayList<>();
            funFPsExpr.add(fps.toString());
            funFPsExpr.add(m.group().substring(1));
            this.funDef.put(funName, funFPsExpr);
        }
    }

    public void printFunDef() {
        for (String key : this.funDef.keySet()) {
            System.out.println(key + " " + funDef.get(key).get(0) + " " + funDef.get(key).get(1));
        }
    }

    private static final BigInteger zero = new BigInteger("0");

    public static BigInteger Zb() { // BigInteger Zero
        return zero;
    }

    private static final BigInteger one = new BigInteger("1");

    public static BigInteger Ob() {
        return one;
    }

    private static final BigInteger mOne = new BigInteger("-1");

    public static BigInteger mOb() {
        return mOne;
    }
}
