import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EquipmentTest {
    @Test
    public void setId() {
        Equipment equipment = new Equipment();
        equipment.setId(3);
        assertEquals (equipment.getId(), 3);
    }

    @Test
    public void getId() {
        Equipment equipment = new Equipment();
        equipment.setId(10);
        assertEquals (equipment.getId(), 10);
    }

    @Test
    public void getName() {
        Equipment equipment = new Equipment();
        equipment.setName("area");
        assertEquals (equipment.getName(), "area");
    }

    @Test
    public void setName() {
        Equipment equipment = new Equipment();
        equipment.setName("area");
        assertEquals (equipment.getName(), "area");
    }

    @Test
    public void getStar() {
        Equipment equipment = new Equipment();
        int star = 8888;
        equipment.setStar(star);
        assertEquals (equipment.getStar(), star);
    }

    @Test
    public void setStar() {
        Equipment equipment = new Equipment();
        int star = 9;
        equipment.setStar(star);
        assertEquals (equipment.getStar(), star);
    }


}