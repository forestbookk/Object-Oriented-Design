import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Term {
    private ArrayList<Factor> factors = new ArrayList<>();

    public Term(ArrayList<Factor> factors) {
        this.factors = factors;
    }

    public Poly toPoly() {
        HashMap<String, BigInteger> monos = new HashMap<>();
        Poly poly = new Poly(monos);
        for (int i = 0; i < factors.size(); i++) {
            //poly.mulPoly(factors.get(i).toPoly());
            //test
            Poly temp1 = factors.get(i).toPoly();
            poly.mulPoly(temp1);
            if (monos.size() == 1 && monos.containsValue(new BigInteger("0"))) {
                return poly; // 单项式0乘任何式子得到0
            }
            //
        }
        return poly;
    }

    //test
    /*public void printTerm() {
        for (int i = 0; i < factors.size(); i++) {
            factors.get(i).printFactor();
            if (i < factors.size() - 1) System.out.println("*");
        }
    }*/
}
