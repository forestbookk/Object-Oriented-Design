public class Token {
    public enum Type {
        ADD, SUB, MUL, LPAREN, RPAREN, EXP, CONS, VARI, EXF
    }

    private final Type type;

    public Type getType() {
        return type;
    }

    private final String content;

    public String getContent() {
        return content;
    }

    public Token(Type type, String content) {
        this.type = type;
        this.content = content;
    }
}
