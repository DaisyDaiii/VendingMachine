package vendingMachine.model.snack;

import org.junit.Test;

public class SnackTest {

    @Test
    public void amountTest(){
        Snack a = new Snack(0,"test0",1,1,"Drinks");
        assert(a.getAmount() == 1);
        a.setAmount(3);
        assert(a.getAmount() == 3);
    }

    @Test
    public void normalTest(){
        Snack a = new Snack(0,"test0",1,1,"Drinks");
        assert(a.getAmount() == 1);
        assert(a.getId() == 0);
        assert(a.getName().equals("test0"));
        assert(a.getPrice() == 1);
        a.setPrice(2);
        assert(a.getPrice() == 2);
        assert(a.getType().equals("Drinks"));
    }

}
