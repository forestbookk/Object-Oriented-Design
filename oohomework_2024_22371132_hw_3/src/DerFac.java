import java.math.BigInteger;
import java.util.HashMap;

public class DerFac implements Factor {
    private Expr expr;
    private Token.Type wholeOp = Token.Type.ADD;

    public DerFac(Expr expr, Token.Type wholeOp) {
        this.wholeOp = wholeOp;
        this.expr = expr;
    }

    public Poly derPoly(Poly rawPoly) {
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        Poly ansPoly = new Poly(monos);

        HashMap<Mono, BigInteger> rawMonos = rawPoly.getMonos();
        for (Mono key : rawMonos.keySet()) {
            ansPoly = ansPoly.addPoly(derMono(key).mulPoly(new NumFac(rawMonos.get(key)).toPoly()));
        }
        return ansPoly;
    }

    public Poly derMono(Mono rawMono) {
        // rawMono Format: x^m * exp(Poly)^n
        HashMap<Poly, BigInteger> rawExps = rawMono.getExps();
        BigInteger m = rawMono.getXindex();
        if (m.compareTo(Const.Zb()) == 0) {
            // m == 0
            if (rawExps.isEmpty()) {
                // m == 0 && exps 空
                return new NumFac(Const.Zb()).toPoly(); // return 0
            } else if (rawExps.size() == 1) {
                // 无需扩展exp
                for (Poly key : rawExps.keySet()) {
                    BigInteger n = rawExps.get(key);
                    if (n.compareTo(Const.Zb()) == 0) {
                        // m == 0 && n == 0
                        return new NumFac(Const.Zb()).toPoly(); // return 0
                    } else {
                        // m == 0 && n >= 1
                        Poly mul1 = new Poly(rawMono, n);
                        Poly mul2 = derPoly(key);
                        return mul1.mulPoly(mul2);
                    }
                }
            } else {
                // 扩展exp: m == 0 & n == 1
                return new Poly(rawMono, Const.Ob()).mulPoly(derPoly(rawMono.expandExps()));
            }
        } else {
            // m >= 1
            if (rawExps.isEmpty()) {
                // m >= 1 && exps 空
                Mono mono = new Mono(m.subtract(Const.Ob()), new HashMap<>());
                HashMap<Mono, BigInteger> mulMonos1 = new HashMap<>();
                mulMonos1.put(mono, m);
                return new Poly(mulMonos1);
            } else if (rawExps.size() == 1) {
                // 无需扩展exp
                for (Poly key : rawExps.keySet()) {
                    BigInteger n = rawExps.get(key);
                    if (n.compareTo(Const.Zb()) == 0) {
                        // m >= 1 && n == 0
                        /*Mono mono = new Mono(m.subtract(Const.Ob()), new HashMap<>());
                        HashMap<Mono, BigInteger> mulMonos1 = new HashMap<>();
                        mulMonos1.put(mono, m);
                        return new Poly(mulMonos1);*/
                        return new Poly(new Mono(m.subtract(Const.Ob()), new HashMap<>()), m);
                    } else {
                        // m >= 1 && n >= 1
                        // return Format: x^(m-1)*exp(Poly)^n * (m+nx*derPoly(Poly))
                        Mono mulMono1 = rawMono.deepClone();
                        mulMono1.setXindex(m.subtract(Const.Ob())); // mul1: x^(m-1)*exp(Poly)^n
                        Poly mul1 = new Poly(mulMono1, Const.Ob());

                        Poly add21 = new NumFac(m).toPoly(); // add21: m
                        Mono tempAdd22 = new Mono(Const.Ob(), new HashMap<>());
                        Poly add22 = new Poly(tempAdd22, n).mulPoly(derPoly(key));
                        // add22: nx*derPoly(Poly)
                        Poly mul2 = add21.addPoly(add22);

                        return mul1.mulPoly(mul2);
                    }
                }
            } else {
                // 扩展exp Format: x^m * exp(Poly) derFormat:x^(m-1)*exp(Poly)*(m+x*derPoly(Poly))
                // m >= 1 && n == 1
                HashMap<Poly, BigInteger> expandExps = new HashMap<>();
                Poly expExpandContent = rawMono.expandExps();
                expandExps.put(expExpandContent, Const.Ob());
                Poly mul1 = new Poly(new Mono(m.subtract(Const.Ob()), expandExps), Const.Ob());

                Poly add21 = new NumFac(m).toPoly(); // m
                Poly xpoly = new Poly(new Mono(Const.Ob(), new HashMap<>()), Const.Ob());
                Poly mul2 = add21.addPoly(xpoly.mulPoly(derPoly(expExpandContent)));

                return mul1.mulPoly(mul2);
            }
        }
        return new Poly(new HashMap<>());
    }

    public Poly toPoly() {
        return derPoly(this.expr.toPoly()).mulPoly(wholeOp == Token.Type.ADD ?
                new NumFac(Const.Ob()).toPoly() : new NumFac(Const.mOb()).toPoly());
    }

    public String getClassName() {
        return "DerFac";
    }
}
