import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

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
        String opFactor = "";
        int cntSub = 0;
        while (lexer.NotEnd() && (lexer.getToken().getType() == Token.Type.ADD
                || lexer.getToken().getType() == Token.Type.SUB)) {
            if (lexer.getToken().getType() == Token.Type.SUB) {
                cntSub++;
            }
            lexer.move(); // token update
        }
        if ((cntSub & 1) == 1) {
            opFactor = "-";
        }
        if (lexer.getToken().getType() == Token.Type.DER) {
            return parseDerFac(opFactor);
        } else if (lexer.getToken().getType() == Token.Type.CONS) {
            return parseConsFac(opFactor);
        } else if (lexer.getToken().getType() == Token.Type.VARI) {
            return parseVariFac(opFactor);
        } else if (lexer.getToken().getType() == Token.Type.LPAREN) {
            return parseParenFac(opFactor);
        } else {
            return parseExfFac(opFactor); // if(lexer.getToken().getType() ==Token.Type.EXF)
        }
    }

    public Factor parseDerFac(String opFactor) {
        // 求导因子
        lexer.move(); // skip dx
        lexer.move(); // skip lparen
        Expr derExpr = parseExpr();
        lexer.move(); // skip rparen
        return new DerFac(derExpr, Objects.equals(opFactor, "-") ? Token.Type.SUB : Token.Type.ADD);
    }

    public Factor parseConsFac(String opFactor) {
        // Factor is NumFac
        String num = lexer.getToken().getContent();
        lexer.move(); // skip cons
        if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
            skipPow();
            BigInteger index = lexer.NotEnd() ?
                    new BigInteger(lexer.getToken().getContent()) : Const.Ob(); // read index
            lexer.move(); // skip index
            return new PowFac(new BigInteger(opFactor + "1"), index, num, null, false);
        } else {
            return new NumFac(new BigInteger(opFactor + num));
        }
    }

    public Factor parseVariFac(String opFactor) {
        // Factor is VariFac
        String base = lexer.getToken().getContent();
        lexer.move(); // skip x & read index
        BigInteger index = Const.Ob();
        if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
            skipPow();
            index = new BigInteger(lexer.getToken().getContent());
            lexer.move();// skip index
        }
        return new PowFac(new BigInteger(opFactor + "1"), index, base, null, false);
    }

    public Factor parseParenFac(String opFactor) {
        lexer.move(); // skip lparen
        Expr expr = parseExpr();
        lexer.move(); // skip rparen
        if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
            skipPow();
            BigInteger index = lexer.NotEnd() ?
                    new BigInteger(lexer.getToken().getContent()) : Const.Ob();
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

    public Factor parseExfFac(String opFactor) {
        lexer.move(); // skip exp
        lexer.move(); // skip lparen
        Factor factor = parseFactor();
        BigInteger contentIndex = Const.Ob();
        NumFac content = new NumFac(Const.Ob());
        if (factor.getClassName().equals("NumFac")) {
            content = (NumFac) factor;
            contentIndex = content.getNum();
        }

        lexer.move(); // skip rparen
        BigInteger index = contentIndex; // default value: 1
        if (lexer.NotEnd() && lexer.getToken().getType() == Token.Type.EXP) {
            skipPow(); // skip ^ & (+)
            index = new BigInteger(lexer.getToken().getContent()).multiply(contentIndex);
            lexer.move(); // skip index
        }
        if (index.compareTo(Const.Zb()) == 0) {
            return new NumFac(new BigInteger(opFactor + "1"));
        } else {
            if (factor.getClassName().equals("NumFac")) {
                content.setNum(index);
                index = Const.Ob();
            }
            return new Exf(new BigInteger(opFactor + "1"), factor, index);
        }
    }

    public void skipPow() {
        lexer.move(); // skip ^
        if (lexer.getToken().getType() == Token.Type.ADD) {
            lexer.move(); // skip +
        }
    }
}
