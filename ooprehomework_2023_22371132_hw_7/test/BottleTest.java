import org.junit.Test;

import static org.junit.Assert.*;

public class BottleTest {

    @Test
    public void getRatio() {
        Bottle bottle = new Bottle(234, "jsif", 252453, 346);
        bottle.setRatio(3.23452);
        bottle.getRatio();
    }

    @Test
    public void setRatio() {
    }
}