package vendingMachine.model;

import static org.junit.Assert.*;
import org.junit.Test;
import vendingMachine.database.DatabaseToolkit;
import vendingMachine.model.account.User;
import vendingMachine.model.snack.Snack;

import java.util.List;

public class MachineEngineImpTest {

    @Test
    public  void LoginTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        assertTrue(engine.login("group1","group1"));
        assertFalse(engine.login("no exist", "group1"));
        assertFalse(engine.login("group1", "Wrong password"));
        assertFalse(engine.login("no exist","Wrong password"));
        assertFalse(engine.login(null,null));
        engine.checkRole("group1");
        assertEquals("admin", engine.getRole());
        engine.logout();
        assertEquals("", engine.getRole());
        engine.anonymousLogin();
    }


    @Test
    public void snackTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        List<Snack> ls = engine.listAllSnack();
        assert(ls!=null);
        assert(ls.size() > 0);
        engine.insertItem(10,"name","good thing",4,5);
        engine.addSnack(1,2);
        assert(engine.deleteItem("name") == 0);
    }
    @Test
    public void userTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        List<User> users = engine.listusers();
        assert(users!=null);
        assert(users.size()>0);
        engine.register("user","pass","customer");
        engine.deleteAccount("user");

    }


//    @Test
//    public void RegisterTest() {
//        DatabaseToolkit kt = new DatabaseToolkit();
//        kt.createConn();
//        MachineEngine engine = new MachineEngineImp(kt);
//        assert(engine.register(null,null,null) == 0);
//        assert(engine.register("anonymous","123",null) == 0);
//    }

    @Test
    public void singleTypeSnackTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        List<Snack> ls = engine.getSnackLs("Drinks");
        assert(ls.get(0).getType().equals("Drinks"));
        List<Snack> ls1 = engine.getSnackLs("nothing");
        assert(ls1 == null);
    }


//    @Test
//    public void updateTest(){
//        DatabaseToolkit kt = new DatabaseToolkit();
//        MachineEngine engine = new MachineEngineImp(kt);
//        engine.insertItem(50,"test","Drinks",2,2);
//        assert(0 < engine.updateItemQuantity("test",1));
//        assert(engine.updateItemCode("test",51) > 0);
//        assert(engine.updateItemName("test", "newname")>0);
//        assert(engine.updateItemPrice("newname",2.0)>0);
//        assert(engine.updateItemQuantity("newname",2)>0);
//    }

    @Test
    public void cashTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        List<Cash> cash = engine.getCashes();
        assert(cash!=null);
        assert(cash.size()>0);
    }

    @Test
    public void listSnackTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        List<Snack> ls = engine.listAllSnack();
        assert(ls!=null);
        assert(ls.size() > 0);
        ls = engine.getSnackLs("Candies");
        assert(ls!=null);
    }

    @Test
    public void TestetTotalPrice(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        engine.getShoppingCart().add(engine.listAllSnack().get(0).makeOrder());
        assertEquals(engine.getTotalPrice(),engine.listAllSnack().get(0).getPrice(),2);
        assertEquals(engine.getShoppingCart().size(),1);
        engine.getShoppingCart().get(0).amountDecrease();
        assertEquals(engine.getShoppingCart().size(),0);
    }

    @Test
    public void beforeCheckoutTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        assert(engine.getCashier()!=null);
        assert(!engine.ifPaymentSuccess());
    }

    @Test
    public void CheckoutTest(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        engine.getShoppingCart().add(engine.listAllSnack().get(0).makeOrder());
        engine.getCashier().getInsertMoney().get(0).increase();
        engine.checkout(1);
        assert(engine.ifPaymentSuccess());

        double moneychange = engine.getCashier().currentValue(engine.getCashier().returnCash());

        assertEquals(moneychange,50-engine.listAllSnack().get(0).getPrice(),2);
    }

    @Test
    public void testCancelOrder(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        engine.getShoppingCart().add(engine.listAllSnack().get(0).makeOrder());
        engine.logout();
    }

    @Test
    public void testupdate(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        engine.updateItemQuantity("Mineral Water",1);
        engine.updateItemPrice("Mineral Water",7.5);
        engine.updateItemCode("Mineral Water",51);
        engine.updateItemName("Mineral Water","Water");
        List<Snack> ls = engine.getSnackLs("Drinks");
        boolean findNewName = false;
        for(Snack s : ls){
            if(s.getName().equals("Water")){
                assert(s.getAmount() == 1);
                assertEquals(s.getPrice(),7.5,2);
                assertEquals(s.getId(),51);
                findNewName = true;
            }
        }
        assert(findNewName);
        kt.initDatabase();
    }

    @Test
    public void Test_transaction(){
        DatabaseToolkit kt = new DatabaseToolkit();
        kt.createConn();
        MachineEngine engine = new MachineEngineImp(kt);
        Transaction t = engine.transaction();
        assert(t.getUsername()==null);
        assert(t.getChange()==0);
        assert(t.getPaid()==0);
        assert(t.getPayMethod()=="none");
        engine.register("test","test","customer");
        engine.PayMethod("cash");
        engine.getShoppingCart().add(engine.listAllSnack().get(0).makeOrder());
        engine.getCashier().getInsertMoney().get(0).increase();
        Transaction t1 = engine.transaction();
        assert(t1.getUsername().equals("test"));
        assert(t1.getPayMethod().equals("cash"));
        String done = engine.donePurchase();
        assert(done.equals("done"));
        String cancel = engine.insertHistory("user cancelled");
        assert(cancel.equals("user cancelled"));
        String cancel1 = engine.insertHistory("change not available");
        assert(cancel1.equals("change not available"));
        String cancel2 = engine.insertHistory("timeout");
        assert(cancel2.equals("timeout"));
        kt.initDatabase();
    }
}
