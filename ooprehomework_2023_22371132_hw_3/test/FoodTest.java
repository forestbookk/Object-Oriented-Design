import org.junit.Test;

import static org.junit.Assert.*;

public class FoodTest {

    @Test
    public void setId() {
        Food food = new Food();
        food.setId(13);
        assertEquals(food.getId(), 13);
    }

    @Test
    public void getId() {
        Food food = new Food();
        food.setId(13);
        assertEquals(food.getId(), 13);
    }

    @Test
    public void setName() {
        Food food = new Food();
        food.setName("13");
        assertEquals(food.getName(), "13");
    }

    @Test
    public void getName() {
        Food food = new Food();
        food.setName("138");
        assertEquals(food.getName(), "138");
    }

    @Test
    public void setEnergy() {
        Food food = new Food();
        food.setEnergy(66);
        assertEquals(food.getEnergy(), 66);
    }

    @Test
    public void getEnergy() {
        Food food = new Food();
        food.setEnergy(66);
        assertEquals(food.getEnergy(), 66);
    }


}