import java.math.BigInteger;
import java.util.HashMap;

public class NumFac implements Factor {
    private BigInteger num;

    public NumFac(BigInteger num) {
        this.num = num;
    }

    public Poly toPoly() {
        HashMap<String, BigInteger> monos = new HashMap<>();
        monos.put("1", num);
        return new Poly(monos);
    }
}
