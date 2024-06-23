import org.junit.Test;

import static org.junit.Assert.*;

public class ReinforcedBottleTest {

    @Test
    public void setRatio() {
        ReinforcedBottle reinforcedBottle = new ReinforcedBottle(2384, "jskdf", 234, 34, 34);
        reinforcedBottle.setRatio(234);
        RecoverBottle recoverBottle = new RecoverBottle(234, "ji", 24, 24, 234);
        recoverBottle.setRatio(234);
    }
}