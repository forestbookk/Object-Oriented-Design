import org.junit.Test;

import java.math.BigInteger;
import java.util.HashMap;

import static org.junit.Assert.*;

public class PolyTest {

    @Test
    public void testEquals() {
        HashMap<Poly, BigInteger> exps1 = new HashMap<>();
        Lexer lexer1 = new Lexer("3+x-exp(x)*x+exp(x)^2");
        Parser parser1 = new Parser(lexer1);
        Poly poly1 = parser1.parseExpr().toPoly();
        Lexer lexer2 = new Lexer("x^2");
        Parser parser2 = new Parser(lexer1);
        Poly poly2 = parser2.parseExpr().toPoly();
        exps1.put(poly1,Const.Ob());
        exps1.put(poly2,Const.mOb());

        HashMap<Poly, BigInteger> exps2 = new HashMap<>();
        Lexer lexer3 = new Lexer("3+x-exp(x)*x+exp(x)^2");
        Parser parser3 = new Parser(lexer1);
        Poly poly3 = parser3.parseExpr().toPoly();
        exps2.put(poly3,Const.Ob());

        System.out.println(poly1.equals(poly2));
    }
}