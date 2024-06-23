import org.junit.Test;

import static org.junit.Assert.*;

public class MyScannerTest {

    @Test
    public void scanCmdE() {
        MyScanner myScanner = new MyScanner();
        String[] str = {"98", "jief"};
        CommandUtil cmd = new CommandUtil();
        myScanner.scanCmdF(str, cmd);
    }

    @Test
    public void scanCmdF() {
    }
}