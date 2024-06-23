import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Term {
    private ArrayList<Factor> factors;

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public Term(ArrayList<Factor> factors) {
        this.factors = factors;
    }

    public Poly toPoly() {
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        Poly poly = new Poly(monos);
        for (int i = 0; i < factors.size(); i++) {
            if (factors.get(i).getClassName().equals("NumFac")) {
                NumFac num = (NumFac) factors.get(i);
                if (num.getNum().compareTo(Const.Zb()) == 0) {
                    return new Poly(new HashMap<>()).mulPoly(num.toPoly());
                }
            }
            Poly facPoly = factors.get(i).toPoly();
            poly = poly.mulPoly(facPoly);
        }
        return poly;
    }

}
