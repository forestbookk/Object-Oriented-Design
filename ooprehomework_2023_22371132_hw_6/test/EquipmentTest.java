import org.junit.Test;

import static org.junit.Assert.*;

public class EquipmentTest {

    @Test
    public void setCritical() {
        Equipment equipment = new Equipment(24,"naef", 2385,238478);
        equipment.setCritical(9);
    }
}