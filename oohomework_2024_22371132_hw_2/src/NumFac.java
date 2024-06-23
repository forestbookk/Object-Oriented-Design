import java.math.BigInteger;
import java.util.HashMap;

public class NumFac implements Factor {
    private BigInteger num;

    public BigInteger getNum() {
        return num;
    }

    public NumFac(BigInteger num) {
        this.num = num;
    }

    public Poly toPoly() {
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        Mono mono = new Mono(Const.Zb(), new HashMap<>()); // x^0 && exps.size = 0
        monos.put(mono, num);
        return new Poly(monos);
    }

    public String getClassName() {
        return "NumFac";
    }

}
