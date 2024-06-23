import java.math.BigInteger;
import java.util.ArrayList;

public class Parser {

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        ArrayList<Term> terms = new ArrayList<>();
        ArrayList<Token> ops = new ArrayList<>();
        Token.Type type = lexer.getToken().getType();
        if (lexer.NotEnd() && (type == Token.Type.ADD || type == Token.Type.SUB)) {
            ops.add(lexer.getToken());
            lexer.move();
        } else {
            ops.add(new Token(Token.Type.ADD, "+"));
        }
        terms.add(parseTerm());
        while (lexer.NotEnd() && (lexer.getToken().getType() == Token.Type.ADD
                || lexer.getToken().getType() == Token.Type.SUB)) {
            ops.add(lexer.getToken());
            lexer.move(); // token update
            terms.add(parseTerm());
        }
        return new Expr(terms, ops);
    }

    public Term parseTerm() {
        ArrayList<Factor> factors = new ArrayList<>();
        factors.add(parseFactor());
        while (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.MUL) {
            lexer.move(); // skip mul
            factors.add(parseFactor());
        }
        return new Term(factors);
    }

    public Factor parseFactor() {
        int cntSub = 0;
        while (lexer.NotEnd() && (lexer.getToken().getType() == Token.Type.ADD
                || lexer.getToken().getType() == Token.Type.SUB)) {
            if (lexer.getToken().getType() == Token.Type.SUB) {
                cntSub++;
            }
            lexer.move(); // token update
        }
        String opFactor = ""; // only for const and vari
        if ((cntSub & 1) == 1) {
            opFactor = "-";
        }
        if (lexer.getToken().getType() == Token.Type.CONS) {
            // Factor is Constant
            String num = lexer.getToken().getContent();
            lexer.move(); // skip cons
            if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
                skipPow();
                int index = lexer.NotEnd() ?
                        Integer.parseInt(lexer.getToken().getContent()) : 1; // read index
                lexer.move(); // skip index
                return new PowFac(new BigInteger(opFactor + "1"), index, num, null, false);
            } else {
                return new NumFac(new BigInteger(opFactor + num));
            }
        } else if (lexer.getToken().getType() == Token.Type.VARI) {
            // Factor is Varialbe
            String base = lexer.getToken().getContent();
            lexer.move(); // skip x & read index
            int index = 1;
            if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
                skipPow();
                index = Integer.parseInt(lexer.getToken().getContent());
                lexer.move();// skip index
            }
            return new PowFac(new BigInteger(opFactor + "1"), index, base, null, false);
        } else {
            // Factor is Expr
            lexer.move(); // skip lparen
            Expr expr = parseExpr();
            lexer.move(); // skip rparen
            if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
                skipPow();
                int index = lexer.NotEnd() ? Integer.parseInt(lexer.getToken().getContent()) : 1;
                lexer.move(); // skip index
                PowFac powFac = new PowFac(new BigInteger("1"), index, null, expr, true);
                if (opFactor.equals("-")) {
                    powFac.setWholeOp(Token.Type.SUB);
                }
                return powFac;
            } else {
                if (opFactor.equals("-")) {
                    expr.setWholeOp(Token.Type.SUB);
                }
                return expr;
            }
        }
    }

    public void skipPow() {
        lexer.move(); // skip ^
        if (lexer.getToken().getType() == Token.Type.ADD) {
            lexer.move(); // skip +
        }
    }
}
