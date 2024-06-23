import java.math.BigInteger;

public class Const {
    private Const() {
    }

    private static BigInteger zero = new BigInteger("0");

    public static BigInteger Zb() { // BigInteger Zero
        return zero;
    }

    private static BigInteger one = new BigInteger("1");

    public static BigInteger Ob() {
        return one;
    }

    private static BigInteger mOne = new BigInteger("-1");

    public static BigInteger mOb() {
        return mOne;
    }
}
