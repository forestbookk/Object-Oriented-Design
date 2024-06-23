import org.junit.Test;

import static org.junit.Assert.*;

public class ChildTest {

    @Test
    public void subMoney() {
        Child child = new Child(20);
        child.subMoney(5);
        assert(child.getMoney() == 15);
    }

    @Test
    public void addOneFruit() {
        Child child = new Child(20);
        child.addOneFruit("apple");
        assert(child.getAppleCount() == 1);
        child.addOneFruit("banana");
        assert(child.getBananaCount() == 1);
    }

    @Test
    public void eatOneFruit() {
    }

    @Test
    public void buyFromStore() {
    }

    @Test
    public void getMoney() {
    }
}