import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = Integer.parseInt(scan.nextLine());
        Const.getInstance().setFunDefNum(n);
        for (int i = 0; i < n; i++) {
            Const.getInstance().addFunDef(scan.nextLine());
        }
        Lexer lexer = new Lexer(scan.nextLine());
        Parser parser = new Parser(lexer);
        System.out.println(parser.parseExpr().toPoly().toString());
        //parser.parseExpr().toPoly();
        //Const.getInstance().printFunDef();
        //lexer.printTokens();
    }
}

