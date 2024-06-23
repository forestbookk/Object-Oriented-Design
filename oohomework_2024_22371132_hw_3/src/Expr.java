import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Expr implements Factor {
    private boolean isDer = false;

    public void setDer(boolean der) {
        isDer = der;
    }

    /*private ArrayList<Term> derTerms = new ArrayList<>();

    public ArrayList<Term> getDerTerms() {
        return derTerms;
    }

    public void setDerTerms(ArrayList<Term> derTerms) {
        this.derTerms = derTerms;
    }*/

    private ArrayList<Term> terms = new ArrayList<>();

    public ArrayList<Term> getTerms() {
        return terms;
    }

    private ArrayList<Token> ops = new ArrayList<>();

    public ArrayList<Token> getOps() {
        return ops;
    }

    private Token.Type wholeOp = Token.Type.ADD;

    public Token.Type getWholeOp() {
        return wholeOp;
    }

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
