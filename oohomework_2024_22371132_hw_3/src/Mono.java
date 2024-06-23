import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Mono {

    private BigInteger xindex;

    public BigInteger getXindex() {
        return xindex;
    }

    public void setXindex(BigInteger xindex) {
        this.xindex = xindex;
    }

    private HashMap<Poly, BigInteger> exps;

    public HashMap<Poly, BigInteger> getExps() {
        return exps;
    }

    public Mono(BigInteger xindex, HashMap<Poly, BigInteger> exps) {
        this.xindex = xindex;
        this.exps = exps; // TODO：可能有DeepClone的问题。
    }

    public Mono multiply(Mono other) {
        BigInteger ansxIndex = this.xindex.add(other.getXindex());
        HashMap<Poly, BigInteger> ansExps = new HashMap<>();
        for (Poly poly : this.exps.keySet()) {
            ansExps.put(poly.deepClone(), this.exps.get(poly));
        }
        for (Poly otherPoly : other.getExps().keySet()) {
            if (this.exps.containsKey(otherPoly)) {
                ansExps.put(otherPoly.deepClone(), this.exps.get(otherPoly).
                        add(other.getExps().get(otherPoly)));
            } else {
                ansExps.put(otherPoly.deepClone(), other.getExps().get(otherPoly));
            }
        }
        return new Mono(ansxIndex, ansExps);
    }

    public Mono deepClone() {
        if (exps.isEmpty()) {
            return new Mono(xindex, new HashMap<>());
        } else {
            HashMap<Poly, BigInteger> cloneMap = new HashMap<>();
            for (Poly poly : this.exps.keySet()) {
                cloneMap.put(poly.deepClone(), this.exps.get(poly));
            }
            return new Mono(xindex, cloneMap);
        }
    }

    public boolean isConst() {
        if (xindex.compareTo(Const.Zb()) != 0) {
            return false; // xIndex!=0
        }
        if (exps.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Poly expandExps() {
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        Poly ansExpPoly = new Poly(monos);
        for (Poly keyPoly : exps.keySet()) {
            BigInteger value = exps.get(keyPoly);
            Poly valuePoly = new NumFac(value).toPoly();
            ansExpPoly = ansExpPoly.addPoly(keyPoly.mulPoly(valuePoly));
        }
        return ansExpPoly;
    }

    private boolean isNumFac = false;

    public boolean isFactor(String polyStr) {
        isNumFac = true;
        int i = 0;
        while (i < polyStr.length() && (polyStr.charAt(i) == '+' || polyStr.charAt(i) == '-')) {
            i++;
            for (int j = i; j < polyStr.length(); j++) {
                if (polyStr.charAt(j) > '9' || polyStr.charAt(j) < '0') {
                    isNumFac = false;
                    return false;
                }
            }
        }
        int lparen = 0;
        for (; i < polyStr.length(); i++) {
            if (polyStr.charAt(i) > '9' || polyStr.charAt(i) < '0') {
                isNumFac = false;
            }
            if (polyStr.charAt(i) == '(') {
                lparen++;
            } else if (polyStr.charAt(i) == ')') {
                lparen--;
            } else if (lparen == 0 && (polyStr.charAt(i) == '+' ||
                    polyStr.charAt(i) == '-' || polyStr.charAt(i) == '*')) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (this.isConst()) {
            return "1";
        }
        StringBuilder ans = new StringBuilder();
        if (exps.size() > 1) {
            String expExpandContent = expandExps().toString();
            if (!expExpandContent.equals("0")) {
                ans.append("exp");
                if (isFactor(expExpandContent)) {
                    ans.append("(").append(expExpandContent).append(")");
                } else {
                    ans.append("((").append(expExpandContent).append("))");
                }
            }
        } else if (exps.size() == 1) {
            for (Poly key : exps.keySet()) {
                String expContent = key.toString();
                if (!expContent.equals("0")) {
                    ans.append("exp");
                    if (isFactor(expContent)) {
                        if (isNumFac) {
                            ans.append("(").append(new BigInteger(expContent).
                                    multiply(exps.get(key))).append(")");
                        } else {
                            ans.append("(").append(expContent).append(")");
                        }
                    } else {
                        ans.append("((").append(expContent).append("))");
                    }
                    if (exps.get(key).compareTo(Const.Ob()) > 0 && !isNumFac) {
                        ans.append("^").append(exps.get(key));
                    }
                }
            }
        }

        if (xindex.compareTo(Const.Zb()) != 0) {
            // xindex != 0
            if (ans.length() > 0) {
                ans.append("*");
            }
            ans.append("x");
            if (xindex.compareTo(Const.Ob()) != 0) {
                // xindex != 1
                ans.append("^").append(xindex);
            }
        }
        if (ans.length() == 0) {
            return "1";
        } else {
            return ans.toString();
        }
    }

    @Override
    public boolean equals(Object o) { // ignore Coe
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mono)) {
            return false;
        }
        Mono mono = (Mono) o;
        if (getXindex().compareTo(mono.getXindex()) != 0) {
            return false;
        }
        HashMap<Poly, BigInteger> exps1 = new HashMap<>(getExps());
        HashMap<Poly, BigInteger> exps2 = new HashMap<>(mono.getExps());
        Iterator<Map.Entry<Poly, BigInteger>> it1 = exps1.entrySet().iterator();
        while (it1.hasNext()) {
            Map.Entry<Poly, BigInteger> entry1 = (Map.Entry) it1.next();
            Iterator<Map.Entry<Poly, BigInteger>> it2 = exps2.entrySet().iterator();
            int flag = 0;
            while (it2.hasNext()) {
                Map.Entry<Poly, BigInteger> entry2 = (Map.Entry) it2.next();
                if (entry1.getKey().equals(entry2.getKey())) {
                    if (entry1.getValue().compareTo(entry2.getValue()) == 0) {
                        it1.remove();
                        it2.remove();
                        flag = 1;
                        break;
                    }
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
        result = prime * result + xindex.hashCode();
        for (Poly poly : exps.keySet()) {
            result = prime * result + (poly.getMonos().size()) + poly.hashCode();
        }
        return result;
    }

}
