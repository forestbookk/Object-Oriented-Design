import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CommandUtilTest {

    @Test
    public void setPartLog() {
        ArrayList<String> partLog = new ArrayList<>();
        CommandUtil commandUtil = new CommandUtil();
        commandUtil.setPartLog(partLog);
        commandUtil.setMmPeo(13);
        commandUtil.setKkLog(15);
        commandUtil.setCritical(19);
        commandUtil.setDate("sjidfjs");
        commandUtil.getDate();
        commandUtil.getMmPeo();
        commandUtil.getKkLog();
        commandUtil.getCritical();
        commandUtil.setAdvNameFight(partLog);
        commandUtil.getAdvNameFight();
    }

    @Test
    public void setCritical() {
    }

    @Test
    public void setMmPeo() {
    }

    @Test
    public void setKkLog() {
    }

    @Test
    public void setDate() {
    }

    @Test
    public void getMmPeo() {
    }

    @Test
    public void getKkLog() {
    }

    @Test
    public void getAdvNameFight() {
    }

    @Test
    public void setAdvNameFight() {
    }
}