import org.junit.Test;

public class BottleTest {

    @Test
    public void getId() {
        Bottle bottle = new Bottle();
        bottle.setId(21374849);
        assert (bottle.getId() == 21374849);
    }

    @Test
    public void setId() {
        Bottle bottle = new Bottle();
        bottle.setId(21374849);
        bottle.setId(11);
        assert (bottle.getId() == 11);
    }

    @Test
    public void getName() {
        Bottle bottle = new Bottle();
        bottle.setName("nihaoawomendejinghua");
        assert (bottle.getName() == "nihaoawomendejinghua");
    }

    @Test
    public void setName() {
        Bottle bottle = new Bottle();
        bottle.setName("你好精华");
        assert (bottle.getName().equals("你好精华"));
    }

    @Test
    public void getCapacity() {
        Bottle bottle = new Bottle();
        bottle.setCapacity(20);
        assert (bottle.getCapacity() == 20);
    }

    @Test
    public void setCapacity() {
        Bottle bottle = new Bottle();
        bottle.setCapacity(1010101010);
        assert (bottle.getCapacity() == 1010101010);
    }
}