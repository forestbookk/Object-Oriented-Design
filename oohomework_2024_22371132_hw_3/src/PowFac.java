import java.math.BigInteger;
import java.util.HashMap;

public class PowFac implements Factor {
    private boolean isExpr;

    public Expr getExpr() {
        return expr;
    }

    private Expr expr;

    private BigInteger coe;

    private BigInteger ind;

    public BigInteger getInd() {
        return ind;
    }

    public void setInd(BigInteger ind) {
        this.ind = ind;
    }

    private String base;

    private Token.Type wholeOp = Token.Type.ADD;

    public void setWholeOp(Token.Type wholeOp) {
        this.wholeOp = wholeOp;
    }

    public PowFac(BigInteger coe, BigInteger ind, String base, Expr expr, boolean isExpr) {
        this.coe = coe;
        this.ind = ind;
        this.base = base;
        this.expr = expr;
        this.isExpr = isExpr;
    }

    public Poly toPoly() {
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        if (coe.compareTo(Const.Zb()) == 0) {
            Mono mono = new Mono(Const.Zb(), new HashMap<>()); // Const
            monos.put(mono, Const.Zb());
        } else if (ind.compareTo(Const.Zb()) == 0) { // ind == 0
            Mono mono = new Mono(Const.Zb(), new HashMap<>()); // Const
            if (wholeOp == Token.Type.ADD && coe.compareTo(Const.Zb()) > 0) {
                // 全正
                monos.put(mono, Const.Ob());
            } else if (wholeOp == Token.Type.SUB && coe.compareTo(Const.Zb()) < 0) {
                // 全负
                monos.put(mono, Const.Ob());
            } else {
                // 异号
                monos.put(mono, Const.mOb());
            }
        } else if (isExpr) {
            // ExprPow
            // Attention: wholeOp
            Poly exprPow = new Poly(new HashMap<>());
            Poly quickPow = expr.toPoly();
            String i = ind.toString(2);
            while (!i.isEmpty()) { // i >= 1
                if (i.charAt(i.length() - 1) == '1') { //(i & 1) == 1 011:3 1原身 2平方
                    exprPow = exprPow.mulPoly(quickPow);
                }
                quickPow = quickPow.mulPoly(quickPow);
                i = i.substring(0, i.length() - 1);
            }
            if (wholeOp == Token.Type.SUB) {
                Poly poly = new Poly(new HashMap<>());
                poly = poly.subPoly(exprPow);
                return poly;
            } else {
                return exprPow;
            }
        } else if (base.charAt(0) <= '9' && base.charAt(0) >= '0') {
            // NumPow
            BigInteger exp = Const.Ob();
            BigInteger quickPow = new BigInteger(base);
            String i = ind.toString(2);
            while (new BigInteger(i, 2).compareTo(Const.Ob()) >= 0) { // i >= 1
                if (i.charAt(i.length() - 1) == 1) { //(i & 1) == 1 011:3 1原身 2平方
                    exp = exp.multiply(quickPow);
                }
                quickPow = quickPow.multiply(quickPow);
                i = i.substring(0, i.length() - 1);
            }
            Mono mono = new Mono(Const.Zb(), new HashMap<>()); // Const
            monos.put(mono, wholeOp == Token.Type.ADD ?
                    coe.multiply(exp) : coe.multiply(Const.mOb()).multiply(exp));
        } else {
            // VariPow
            // Attention: wholeOp
            Mono mono = new Mono(ind, new HashMap<>()); // x^ind
            monos.put(mono, wholeOp == Token.Type.ADD ? coe :
                    Const.Zb().subtract(coe));
        }
        return new Poly(monos);
    }

    public String getClassName() {
        return "PowFac";
    }

}
