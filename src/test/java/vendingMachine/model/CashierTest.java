package vendingMachine.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CashierTest {

    @Test
    public void testsimplePay(){
        Cashier cashier = new Cashier();
        cashier.getInsertMoney().get(0).increase();
        cashier.pay(40);
        List<Cash> ls = cashier.returnCash();
        Assert.assertEquals(cashier.currentValue(ls),10.0,2);

        cashier.getInsertMoney().get(0).increase();
        cashier.pay(46);
        ls = cashier.returnCash();
        Assert.assertEquals(cashier.currentValue(ls),4.0,2);

        cashier.getInsertMoney().get(0).increase();
        cashier.pay(46.5);
        ls = cashier.returnCash();
        Assert.assertEquals(cashier.currentValue(ls),3.5,2);

        cashier.getInsertMoney().get(0).increase();
        assert(!cashier.pay(51));
        ls = cashier.returnCash();
        Assert.assertEquals(cashier.currentValue(ls),50,2);
    }

    @Test
    public void totalValueTest(){
        Cashier cashier = new Cashier();
        Assert.assertEquals(cashier.totalValue(),0,2);
        cashier.getInsertMoney().get(0).increase();
        Assert.assertEquals(cashier.totalValue(),50,2);
        cashier.getInsertMoney().get(1).increase();
        Assert.assertEquals(cashier.totalValue(),70,2);
    }


    @After
    public void reset(){
        Cashier cashier = new Cashier();
        cashier.reset();
    }

}
