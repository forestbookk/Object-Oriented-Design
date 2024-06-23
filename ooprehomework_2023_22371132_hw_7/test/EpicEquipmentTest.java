import org.junit.Test;

import static org.junit.Assert.*;

public class EpicEquipmentTest {

    @Test
    public void setRatio() {
        EpicEquipment epicEquipment = new EpicEquipment(8, "ni", 38483, 4234, 5.25);
        epicEquipment.setRatio(9);

    }
}