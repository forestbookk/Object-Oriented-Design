import java.math.BigInteger;
import java.util.HashMap;

public class PowFac implements Factor {
    private boolean isExpr;

    private Expr expr;

    private BigInteger coe;

    private final int ind;

    private String base;

    private Token.Type wholeOp = Token.Type.ADD;

    public void setWholeOp(Token.Type wholeOp) {
        this.wholeOp = wholeOp;
    }

    public PowFac(BigInteger coe, int ind, String base, Expr expr, boolean isExpr) {
        this.coe = coe;
        this.ind = ind;
        this.base = base;
        this.expr = expr;
        this.isExpr = isExpr;
    }

    public Poly toPoly() {
        HashMap<String, BigInteger> monos = new HashMap<>();
        if (coe.compareTo(Const.Zb()) == 0) {
            monos.put("1", Const.Zb());
        } else if (ind == 0) {
            if (wholeOp == Token.Type.ADD && coe.compareTo(Const.Zb()) > 0) {
                // 全正
                monos.put("1", Const.Ob());
            } else if (wholeOp == Token.Type.SUB && coe.compareTo(Const.Zb()) < 0) {
                // 全负
                monos.put("1", Const.Ob());
            } else {
                //异号
                monos.put("1", Const.mOb());
            }
        } else if (isExpr) {
            // ExprPow
            // Attention: wholeOp
            Poly exprPow = new Poly(new HashMap<>());
            Poly quickPow = expr.toPoly();
            Poly temp = new Poly(new HashMap<>());
            temp.getMonos().putAll(quickPow.getMonos());
            // TODO
            int i = ind;
            while (i >= 1) {
                if ((i & 1) == 1) { // 011:3 1原身 2平方
                    exprPow.mulPoly(quickPow);
                }
                quickPow.mulPoly(temp);
                temp.getMonos().clear();
                temp.getMonos().putAll(quickPow.getMonos());
                i >>= 1;
            }
            if (wholeOp == Token.Type.SUB) {
                Poly poly = new Poly(new HashMap<>());
                poly.subPoly(exprPow);
                return poly;
            } else {
                return exprPow;
            }
        } else if (base.charAt(0) <= '9' && base.charAt(0) >= '0') {
            // NumPow
            monos.put("1", wholeOp == Token.Type.ADD ? coe.multiply(new BigInteger(base).pow(ind)) :
                    coe.multiply(Const.mOb()).multiply(new BigInteger(base).pow(ind)));
        } else {
            // VariPow
            // Attention: wholeOp
            monos.put(base + "^" + ind, wholeOp == Token.Type.ADD ? coe :
                    Const.Zb().subtract(coe));
        }
        return new Poly(monos);
    }

    //test
    /*public void printFactor() {
        System.out.println(coe.toString() + "*" + base + "^" + String.valueOf(ind));
    }*/
}
