import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Lexer lexer = new Lexer(scan.nextLine());
        Parser parser = new Parser(lexer);
        //System.out.println(parser.parseExpr().toPoly().toString());
        parser.parseExpr().toPoly();

    }
}

