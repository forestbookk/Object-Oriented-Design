import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class Poly {
    private HashMap<String, BigInteger> monos;

    public HashMap<String, BigInteger> getMonos() {
        return monos;
    }

    public Poly(HashMap<String, BigInteger> monos) {
        this.monos = monos;
    }

    private StringBuilder ans = new StringBuilder();

    public String toString() {
        for (Iterator<Map.Entry<String, BigInteger>> it = this.monos.entrySet().iterator();
             it.hasNext(); ) {
            Map.Entry<String, BigInteger> item = it.next();
            if (item.getValue().compareTo(Const.Zb()) > 0) {
                append(item.getKey(), item.getValue());
                it.remove();
                break;
            }
        }
        if (this.monos.isEmpty()) {
            return ans.toString();
        }

        for (String key : this.monos.keySet()) {
            BigInteger coe = this.monos.get(key);
            ans.append(coe.compareTo(Const.Zb()) >= 0 ? "+" : "");
            append(key, this.monos.get(key));
        }

        String subAddZ = ans.toString().replaceAll("\\+0", "");
        String subSubZ = subAddZ.replaceAll("-0", "");
        if (subSubZ.isEmpty()) {
            return "0";
        } else {
            return subSubZ.charAt(0) == '+' ? subSubZ.substring(1) : subSubZ;
        }
    }

    public void append(String key, BigInteger coe) {
        String[] s = key.split("\\^");
        if (coe.equals(Const.Zb())) {
            ans.append("0");
        } else if (key.equals("1")) {
            ans.append(coe);
        } else if (Objects.equals(coe, Const.Ob())) {
            ans.append(s[1].equals("1") ? s[0] : key);
        } else if (Objects.equals(coe, Const.mOb())) {
            ans.append(s[1].equals("1") ? "-" + s[0] : "-" + key);
        } else {
            if (s[1].equals("1")) {
                ans.append(coe).append("*").append(s[0]);
            } else {
                ans.append(coe).append("*").append(key);
            }
        }
    }

    public void addPoly(Poly other) {
        if (this.monos.isEmpty()) {
            this.monos.putAll(other.getMonos());
        } else {
            HashMap<String, BigInteger> newMonos = new HashMap<>(this.monos);
            for (String key2 : other.monos.keySet()) {
                if (this.monos.containsKey(key2)) {
                    newMonos.put(key2, this.monos.get(key2).add(other.getMonos().get(key2)));
                } else {
                    newMonos.put(key2, other.getMonos().get(key2));
                }
            }
            this.monos.clear();
            this.monos.putAll(newMonos);
        }
    }

    public void subPoly(Poly other) {
        if (this.monos.isEmpty()) {
            for (String key2 : other.monos.keySet()) {
                this.monos.put(key2, new BigInteger("-1").multiply(other.getMonos().get(key2)));
            }
        } else {
            HashMap<String, BigInteger> newMonos = new HashMap<>(this.monos);
            for (String key2 : other.monos.keySet()) {
                if (this.monos.containsKey(key2)) {
                    newMonos.put(key2, this.monos.get(key2).subtract(other.getMonos().get(key2)));
                } else {
                    newMonos.put(key2, new BigInteger("-1").multiply(other.getMonos().get(key2)));
                }
            }
            this.monos.clear();
            this.monos.putAll(newMonos);
        }
    }

    public void mulPoly(Poly other) {
        if (this.monos.isEmpty()) {
            this.monos.putAll(other.getMonos());
        } else {
            if (this.monos.size() == 1 && this.monos.containsValue(Const.Zb())) {
                return;
            }
            HashMap<String, BigInteger> newMonos = new HashMap<>();
            for (String key1 : this.monos.keySet()) {
                for (String key2 : other.monos.keySet()) {
                    String[] s1 = key1.split("\\^");
                    String[] s2 = key2.split("\\^");
                    // Determine key only for sole variable
                    String tempKey = "";
                    if (s1[0].equals("1") && s2[0].equals("1")) {
                        tempKey = "1";
                    } else if (s1[0].equals(s2[0])) {
                        tempKey = s1[0] + "^" + (Integer.parseInt(s1[1]) + Integer.parseInt(s2[1]));
                    } else if (key1.equals("1") || key2.equals("1")) {
                        tempKey = key1.equals("1") ? key2 : key1;
                    } else {
                        tempKey = s1[0].compareTo(s2[0]) < 0 ? (key1 + "*" + key2)
                                : (key2 + "*" + key1);
                    }
                    if (newMonos.containsKey(tempKey)) {
                        BigInteger newCoe = newMonos.get(tempKey);
                        newMonos.remove(tempKey);
                        newMonos.put(tempKey, newCoe.add(monos.get(key1).multiply(
                                other.getMonos().get(key2))));
                    } else {
                        newMonos.put(tempKey, monos.get(key1).multiply(other.getMonos().get(key2)));
                    }
                }
            }
            this.monos.clear();
            this.monos.putAll(newMonos);
        }
    }
}

