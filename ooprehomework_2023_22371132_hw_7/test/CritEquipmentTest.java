import org.junit.Test;

import static org.junit.Assert.*;

public class CritEquipmentTest {

    @Test
    public void setCritical() {
        CritEquipment critEquipment = new CritEquipment(1, "ni", 989, 8, 86);
        critEquipment.setCritical(87);
        critEquipment.setRatio(8.23847);
        critEquipment.getRatio();
    }
}