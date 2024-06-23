import org.junit.Test;

public class EquipmentTest {

    @Test
    public void getId() {
        Equipment equipment = new Equipment();
        equipment.setId(21374849);
        assert (equipment.getId() == 21374849);
    }

    @Test
    public void setId() {
        Equipment equipment = new Equipment();
        equipment.setId(88888888);
        assert (equipment.getId() == 88888888);
    }

    @Test
    public void getName() {
        Equipment equipment = new Equipment();
        equipment.setName("nihaoawomendejinghua");
        assert (equipment.getName() == "nihaoawomendejinghua");
    }

    @Test
    public void setName() {
        Equipment equipment = new Equipment();
        equipment.setName("[[[oii382udnfsufefue8f");
        assert (equipment.getName() == "[[[oii382udnfsufefue8f");
    }

    @Test
    public void getStar() {
        Equipment equipment = new Equipment();
        equipment.setStar(88888888);
        equipment.setStar(8899);
        assert (equipment.getStar() == 8899);
    }

    @Test
    public void setStar() {
        Equipment equipment = new Equipment();
        equipment.setStar(99999);
        equipment.setStar(1);
        assert (equipment.getStar() == 1);
    }
}