import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Poly {
    private HashMap<Mono, BigInteger> monos;

    public HashMap<Mono, BigInteger> getMonos() {
        return monos;
    }

    public Poly(HashMap<Mono, BigInteger> monos) {
        this.monos = monos;
    }

    @Override
    public String toString() {
        HashMap<Mono, BigInteger> cloneMap = monosDeepClone(this.monos);
        HashMap<String, BigInteger> ansParts = new HashMap<>();
        for (Mono key : cloneMap.keySet()) {
            if (cloneMap.get(key).compareTo(Const.Zb()) == 0) {
                continue;
            }
            String newKey = key.toString();
            if (ansParts.containsKey(newKey)) {
                ansParts.put(newKey, ansParts.get(newKey).add(cloneMap.get(key)));
            } else {
                ansParts.put(newKey, cloneMap.get(key));
            }
        }

        StringBuilder ans = new StringBuilder();
        for (Iterator<Map.Entry<String, BigInteger>> it = ansParts.entrySet().iterator();
             it.hasNext(); ) {
            Map.Entry<String, BigInteger> item = it.next();
            if (item.getValue().compareTo(Const.Zb()) > 0) {
                String coe = item.getValue().toString();
                if (!item.getKey().equals("1")) {
                    if (!coe.equals("1")) {
                        ans.append(coe).append("*");
                    }
                    ans.append(item.getKey());
                } else {
                    ans.append(coe); // Constant
                }
                it.remove();
                break;
            }
        }

        if (ansParts.isEmpty()) {
            return (ans.length() == 0) ? "0" : ans.toString();
        }

        for (String key : ansParts.keySet()) {
            BigInteger coe = ansParts.get(key);
            if (coe.compareTo(Const.Zb()) == 0) {
                continue;
            }
            ans.append(coe.compareTo(Const.Zb()) >= 0 ? "+" : "");
            if (key.equals("1")) {
                // Constant
                ans.append(coe);
            } else if (coe.compareTo(Const.Ob()) == 0) {
                // coe == 1
                ans.append(key);
            } else if (coe.compareTo(Const.mOb()) == 0) {
                // coe == -1
                ans.append("-").append(key);
            } else {
                ans.append(coe).append("*").append(key);
            }
        }

        String subAddZ = ans.toString().replaceAll("\\+0", "");
        String subSubZ = subAddZ.replaceAll("-0", "");
        if (subSubZ.isEmpty()) {
            return "0";
        } else {
            return subSubZ.charAt(0) == '+' ? subSubZ.substring(1) : subSubZ;
        }
    }

    public Poly addPoly(Poly other) {
        HashMap<Mono, BigInteger> ans;
        if (this.monos.isEmpty()) {
            ans = monosDeepClone(other.getMonos());
        } else {
            ans = monosDeepClone(this.monos);
            for (Mono otherMono : other.getMonos().keySet()) {
                if (this.monos.containsKey(otherMono)) {
                    ans.put(otherMono.deepClone(),
                            this.monos.get(otherMono).add(other.getMonos().get(otherMono)));
                } else {
                    ans.put(otherMono.deepClone(), other.getMonos().get(otherMono));
                }
            }
        }
        return new Poly(ans);
    }

    public Poly subPoly(Poly other) {
        HashMap<Mono, BigInteger> ans = new HashMap<>();
        if (this.monos.isEmpty()) {
            for (Mono otherMono : other.monos.keySet()) {
                ans.put(otherMono.deepClone(),
                        Const.mOb().multiply(other.getMonos().get(otherMono)));
            }
        } else {
            ans = monosDeepClone(this.monos);
            for (Mono otherMono : other.getMonos().keySet()) {
                if (this.monos.containsKey(otherMono)) {
                    ans.put(otherMono.deepClone(),
                            this.monos.get(otherMono).subtract(other.getMonos().get(otherMono)));
                } else {
                    ans.put(otherMono.deepClone(),
                            Const.mOb().multiply(other.getMonos().get(otherMono)));
                }
            }
        }
        return new Poly(ans);
    }

    public Poly mulPoly(Poly other) {
        HashMap<Mono, BigInteger> ans = new HashMap<>();
        if (this.monos.isEmpty()) {
            ans = monosDeepClone(other.getMonos());
        } else {
            for (Mono mono1 : this.monos.keySet()) {
                for (Mono mono2 : other.getMonos().keySet()) {
                    Mono monoProduct = mono1.multiply(mono2);
                    BigInteger plusCoe = this.monos.get(mono1).
                            multiply(other.getMonos().get(mono2));
                    if (ans.containsKey(monoProduct)) {
                        ans.put(monoProduct, ans.get(monoProduct).add(plusCoe));
                    } else {
                        ans.put(monoProduct, plusCoe);
                    }
                }
            }
        }
        return new Poly(ans);
    }

    public HashMap<Mono, BigInteger> monosDeepClone(HashMap<Mono, BigInteger> ms) {
        HashMap<Mono, BigInteger> cloneMap = new HashMap<>();
        for (Mono key : ms.keySet()) {
            cloneMap.put(key.deepClone(), ms.get(key));
        }
        return cloneMap;
    }

    public Poly deepClone() {
        HashMap<Mono, BigInteger> cloneMap = new HashMap<>();
        for (Mono mono : this.monos.keySet()) {
            cloneMap.put(mono.deepClone(), this.monos.get(mono));
        }
        return new Poly(cloneMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poly)) {
            return false;
        }
        Poly poly = (Poly) o;
        HashMap<Mono, BigInteger> monos1 = new HashMap<>(this.monos);
        HashMap<Mono, BigInteger> monos2 = new HashMap<>(poly.getMonos());
        Iterator<Map.Entry<Mono, BigInteger>> it1 = monos1.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<Mono, BigInteger> entry1 = (Map.Entry) it1.next();
            Iterator<Map.Entry<Mono, BigInteger>> it2 = monos2.entrySet().iterator();
            int flag = 0;
            while (it2.hasNext()) {
                Map.Entry<Mono, BigInteger> entry2 = (Map.Entry) it2.next();
                if (entry1.getKey().equals(entry2.getKey()) &&
                        entry1.getValue().equals(entry2.getValue())) {
                    it1.remove();
                    it2.remove();
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                return false;
            }
        }
        return true;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (Mono mono : monos.keySet()) {
            result += prime * result + mono.hashCode() + monos.get(mono).hashCode();
        }
        return result;
    }
}

