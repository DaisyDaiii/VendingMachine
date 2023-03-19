package vendingMachine.model.warehouse;

import org.junit.Test;
import vendingMachine.model.snack.Snack;

public class WarehouseTest {


    @Test
    public void getTest(){
        Warehouse wh = new Warehouse("test");
        assert(wh.getType().equals("test"));
    }


    @Test
    public void normalTest(){
        Warehouse wh = new Warehouse("test");
        assert(!wh.addSnack(new Snack(0,"a",2,2,"not test")));
        assert(wh.listSnacks().size() == 0);
        assert(wh.addSnack(new Snack(1,"b",2,2,"test")));
        assert(wh.listSnacks().size() == 1);
//        assert(wh.chooseSnack(1) != null);
//        wh.addSnack(1,1);
//        assert(wh.listSnacks().get(0).getAmount() == 2);
//        assert(wh.chooseSnack(1)!=null);
    }
}
