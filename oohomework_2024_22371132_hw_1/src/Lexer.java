import java.util.ArrayList;

public class Lexer {
    private String deHy;
    private int cur = 0; //外部查询tokens所用索引
    private ArrayList<Token> tokens = new ArrayList<Token>();

    public Lexer(String input) {
        // Remove Space and \t
        String deSp = input.replaceAll(" ", "");
        deHy = deSp.replaceAll("\t", "");

        int pos = 0;
        while (pos < deHy.length()) { //Token流
            if (deHy.charAt(pos) == '+') {
                tokens.add(new Token(Token.Type.ADD, "+"));
                pos++;
            } else if (deHy.charAt(pos) == '-') {
                tokens.add(new Token(Token.Type.SUB, "-"));
                pos++;
            } else if (deHy.charAt(pos) == '*') {
                tokens.add(new Token(Token.Type.MUL, "*"));
                pos++;
            } else if (deHy.charAt(pos) == '(') {
                tokens.add(new Token(Token.Type.LPAREN, "("));
                pos++;
            } else if (deHy.charAt(pos) == ')') {
                tokens.add(new Token(Token.Type.RPAREN, ")"));
                pos++;
            } else if (deHy.charAt(pos) == '^') {
                tokens.add(new Token(Token.Type.EXP, "^"));
                pos++;
            } else if (isL(deHy.charAt(pos))) {
                /*tokens.add(new Token(Token.Type.VARI, "x"));*/
                StringBuilder str = new StringBuilder();
                while (pos < deHy.length() && isL(deHy.charAt(pos))) {
                    str.append(deHy.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(Token.Type.VARI, str.toString()));
            } else if (isD(deHy.charAt(pos))) {
                StringBuilder strNumber = new StringBuilder();
                while (pos < deHy.length() && isD(deHy.charAt(pos))) {
                    strNumber.append(deHy.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(Token.Type.CONS, String.valueOf(strNumber)));
            } else {
                System.out.println("Lexer.Input.NonExpected.");
            }
        }
    }

    public void move() {
        cur++;
    }

    public Token getToken() {
        return tokens.get(cur);
    }

    public boolean NotEnd() {
        return cur < tokens.size();
    }

    public boolean isD(char c) { // digit
        return c >= '0' && c <= '9';
    }

    public boolean isL(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

}
