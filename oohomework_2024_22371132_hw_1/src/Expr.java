import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Expr implements Factor {
    private ArrayList<Term> terms = new ArrayList<Term>();

    private ArrayList<Token> ops = new ArrayList<Token>();

    private Token.Type wholeOp = Token.Type.ADD;

    public void setWholeOp(Token.Type wholeOp) {
        this.wholeOp = wholeOp;
    }

    public Expr(ArrayList<Term> terms, ArrayList<Token> ops) {
        this.terms.addAll(terms);
        this.ops.addAll(ops);
    }

    public Poly toPoly() {
        HashMap<String, BigInteger> monos = new HashMap<>();
        Poly poly = new Poly(monos);
        for (int i = 0; i < terms.size(); i++) {
            if (ops.get(i).getType() == wholeOp) { // same
                //poly.addPoly(terms.get(i).toPoly());
                //test
                Poly temp1 = terms.get(i).toPoly();
                poly.addPoly(temp1);
                //
            } else { // different
                //poly.subPoly(terms.get(i).toPoly());
                //test
                Poly temp1 = terms.get(i).toPoly();
                poly.subPoly(temp1);
                //
            }
        }
        return poly;
    }

    //test
    /*public void printExpr() {
        for (int i = 0; i < terms.size(); i++) {
            System.out.println(ops.get(i).getContent());
            terms.get(i).printTerm();
        }
    }

    public void printFactor() {
        printExpr();
    }*/
    /*@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(terms.get(0));
        for (int i = 0; i < ops.size(); i++) {
            sb.append(ops.get(i));
            sb.append(terms.get(i + 1));
        }
        sb.append(")");
        return sb.toString();
    }*/
}
