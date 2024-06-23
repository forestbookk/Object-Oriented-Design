import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Expr implements Factor {
    private ArrayList<Term> terms = new ArrayList<>();

    private ArrayList<Token> ops = new ArrayList<>();

    private Token.Type wholeOp = Token.Type.ADD;

    public void setWholeOp(Token.Type wholeOp) {
        this.wholeOp = wholeOp;
    }

    public Expr(ArrayList<Term> terms, ArrayList<Token> ops) {
        this.terms.addAll(terms);
        this.ops.addAll(ops);
    }

    public Poly toPoly() {
        HashMap<Mono, BigInteger> monos = new HashMap<>();
        Poly poly = new Poly(monos);
        for (int i = 0; i < terms.size(); i++) {
            Poly termPoly = terms.get(i).toPoly();
            if (ops.get(i).getType() == wholeOp) { // same
                poly = poly.addPoly(termPoly);
            } else { // different
                poly = poly.subPoly(termPoly);
            }
        }
        return poly;
    }

    public String getClassName() {
        return "Expr";
    }
}
