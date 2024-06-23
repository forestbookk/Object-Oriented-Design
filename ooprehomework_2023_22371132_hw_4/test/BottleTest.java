import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class BottleTest {
    @Test
    public void getId() {
        Bottle bottle = new Bottle();
        bottle.setId(4);
        assertEquals (bottle.getId(), 4);
    }

    @Test
    public void setId() {
        Bottle bottle = new Bottle();
        bottle.setId(46);
        assertEquals (bottle.getId(), 46);
    }

    @Test
    public void getName() {
        Bottle bottle = new Bottle();
        bottle.setName("89");
        assertEquals (bottle.getName(), "89");
    }

    @Test
    public void setName() {
        Bottle bottle = new Bottle();
        bottle.setName("89");
        assertEquals (bottle.getName(), "89");
    }

    @Test
    public void getCapacity() {
        Bottle bottle = new Bottle();
        bottle.setCapacity(46);
        assertEquals (bottle.getCapacity(), 46);
    }

    @Test
    public void setCapacity() {
        Bottle bottle = new Bottle();
        bottle.setCapacity(866);
        assertEquals (bottle.getCapacity(), 866);
    }

}