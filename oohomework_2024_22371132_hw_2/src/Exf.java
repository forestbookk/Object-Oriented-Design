import java.math.BigInteger;
import java.util.HashMap;

public class Exf implements Factor {
    private BigInteger coe;
    private Factor content;
    private BigInteger index;

    public Exf(BigInteger coe, Factor content, BigInteger index) {
        this.coe = coe;
        this.content = content;
        this.index = index;
    }

    public Poly toPoly() {
        Poly contentPoly = content.toPoly();
        HashMap<Poly, BigInteger> exps = new HashMap<>();
        exps.put(contentPoly, index);//TODO
        Mono mono = new Mono(Const.Zb(), exps);
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        monos.put(mono, coe);
        return new Poly(monos);
    }

    public String getClassName() {
        return "Exf";
    }

}
