import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    private int cur = 0; //外部查询tokens所用索引
    private ArrayList<Token> tokens = new ArrayList<Token>();

    public void printTokens() {
        for (int i = 0; i < tokens.size(); i++) {
            System.out.print(tokens.get(i).getContent());
        }
        System.out.print("\n");
    }

    public Lexer(String input) {
        // Remove Space and \t
        String deSp = input.replaceAll(" ", "");
        String deHy = deSp.replaceAll("\t", "");
        addToken(deHy);
    }

    public void addToken(String input) {
        int pos = 0;
        while (pos < input.length()) { //Token流
            if (input.charAt(pos) == '+') {
                tokens.add(new Token(Token.Type.ADD, "+"));
                pos++;
            } else if (input.charAt(pos) == '-') {
                tokens.add(new Token(Token.Type.SUB, "-"));
                pos++;
            } else if (input.charAt(pos) == '*') {
                tokens.add(new Token(Token.Type.MUL, "*"));
                pos++;
            } else if (input.charAt(pos) == '(') {
                tokens.add(new Token(Token.Type.LPAREN, "("));
                pos++;
            } else if (input.charAt(pos) == ')') {
                tokens.add(new Token(Token.Type.RPAREN, ")"));
                pos++;
            } else if (input.charAt(pos) == '^') {
                tokens.add(new Token(Token.Type.EXP, "^"));
                pos++;
            } else if (isL(input.charAt(pos))) {
                StringBuilder str = new StringBuilder();
                while (pos < input.length() && isL(input.charAt(pos))) {
                    str.append(input.charAt(pos));
                    pos++;
                }
                String letters = str.toString();
                if (letters.equals("dx")) {
                    tokens.add(new Token(Token.Type.DER, letters));
                } else if (letters.equals("exp")) {
                    tokens.add(new Token(Token.Type.EXF, letters));
                } else if (letters.equals("f") || letters.equals("g") || letters.equals("h")) {
                    pos += callUdf(input, letters, pos);
                } else {
                    tokens.add(new Token(Token.Type.VARI, str.toString()));
                }
            } else if (isD(input.charAt(pos))) {
                StringBuilder strNumber = new StringBuilder();
                while (pos < input.length() && isD(input.charAt(pos))) {
                    strNumber.append(input.charAt(pos));
                    pos++;
                }
                tokens.add(new Token(Token.Type.CONS, String.valueOf(strNumber)));
            } else {
                System.out.println("Lexer.Input.NonExpected.");
            }
        }
    }

    public int callUdf(String str, String funName, int pos) {
        int afterPos = pos;
        tokens.add(new Token(Token.Type.LPAREN, "("));
        afterPos++; // skip lparen
        ArrayList<String> rps = new ArrayList<>();
        StringBuilder rp = new StringBuilder("(");
        for (int lparenNum = 1; afterPos < str.length(); afterPos++) {
            if (str.charAt(afterPos) == '(') {
                lparenNum++;
                rp.append(str.charAt(afterPos));
            } else if (str.charAt(afterPos) == ')') {
                lparenNum--;
                if (lparenNum == 0) {
                    rp.append(")");
                    rps.add(rp.toString());
                    afterPos++; // skip rparen
                    break;
                } else {
                    rp.append(str.charAt(afterPos));
                }
            } else if (str.charAt(afterPos) == ',' && lparenNum == 1) {
                rp.append(")");
                rps.add(rp.toString());
                rp = new StringBuilder("(");
            } else {
                rp.append(str.charAt(afterPos));
            }
        }
        ArrayList<String> funInfo = Const.getInstance().getFunDef().get(funName);
        String fps = funInfo.get(0);
        HashMap<String, String> paraMap = new HashMap<>();
        for (int i = 0; i < fps.length(); i++) {
            paraMap.put(String.valueOf(fps.charAt(i)), rps.get(i));
        }
        String funExpr = funInfo.get(1);
        StringBuilder repFunExpr = new StringBuilder();
        for (int i = 0; i < funExpr.length(); ) {
            StringBuilder letters = new StringBuilder();
            if (isL(funExpr.charAt(i))) {
                while (i < funExpr.length() && isL(funExpr.charAt(i))) {
                    letters.append(funExpr.charAt(i));
                    i++;
                }
            } else {
                repFunExpr.append(funExpr.charAt(i));
                i++;
            }
            if (letters.length() > 0) {
                if (paraMap.containsKey(letters.toString())) {
                    repFunExpr.append(paraMap.get(letters.toString()));
                } else {
                    repFunExpr.append(letters);
                }
            }
        }
        //System.out.println(repFunExpr);
        addToken(repFunExpr.toString());

        tokens.add(new Token(Token.Type.RPAREN, ")"));
        return afterPos - pos;
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
