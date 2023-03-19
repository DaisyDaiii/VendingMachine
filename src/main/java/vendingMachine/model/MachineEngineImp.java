package vendingMachine.model;

import vendingMachine.database.DatabaseToolkit;
import vendingMachine.model.MachineEngine;
import vendingMachine.model.account.User;
import vendingMachine.model.snack.*;
import vendingMachine.model.warehouse.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.lang.Math;

/***
 * Concrete class for vendingMachine
 */
public class MachineEngineImp implements MachineEngine {
    List<Warehouse> warehouses = new ArrayList<Warehouse>();
    DatabaseToolkit dbt;

    Cashier cashier = new Cashier();
    String username;
    String role;
    String payMethod = "none";
    List<Order> shoppingCart = new ArrayList<Order>();
    boolean paymentsuccess = false;
    public MachineEngineImp(DatabaseToolkit dbt) {
        this.dbt = dbt;
        username = null;
        role = null;
        init();
    }

    private void init() {
        ArrayList<ArrayList<String>> dates = dbt.getItemInfo();
        for (ArrayList<String> ls : dates) {
            Snack s = new Snack(Integer.parseInt(ls.get(0)), ls.get(1), Integer.parseInt(ls.get(3)), Double.parseDouble(ls.get(4)), ls.get(2));
            addSnack(s);
        }
    }


    /**
     * @param snack a kind of snack
     * @return if success
     */

    public boolean addSnack(Snack snack) {
        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).addSnack(snack)) {
                return true;
            }
        }

        warehouses.add(new Warehouse(snack.getType()));
        return addSnack(snack);
    }

    @Override
    public boolean addSnack(int snackId, int amount) {
        for (Warehouse wh : warehouses) {
            if (wh.addSnack(snackId, amount)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public int register(String username, String password, String role) {
        if (username == null || password == null ) {
            return 0;
        }
        if (username.equals("anonymous")) {
            return 0;
        }
        this.username = username;
        return dbt.createAccount(username, password, role);
    }

    @Override
    public boolean login(String username, String password) {

//        System.out.println(username + "|"+ password);
        if (username == null || password == null || username.equals("") || password.equals("")) {
            return false;
        }
        if (username.equals("anonymous")) {
            return false;
        }
        if (dbt.checkAccount(username, password)) {
            this.username = username;
            return true;
        }
        return false;

    }

    @Override
    public String checkRole(String username) {

        this.role = dbt.getUserRole(username);
        return this.role;

    }

    @Override
    public List<Snack> getSnackLs(String type) {
        for (Warehouse wh : warehouses) {
            if (type.equals(wh.getType())) {
                return wh.listSnacks();
            }
        }
        return null;
    }

    @Override
    public void addOrder(Order order) {
        for(Order o: shoppingCart){
            if(o.getId() == order.getId()){
                o.amountIncrease(order.getAmount());
                return;
            }
        }
        shoppingCart.add(order);
    }


    @Override
    public void anonymousLogin() {
        this.role = "customer";
        this.username = "anonymous";
    }

//    @Override
//    public boolean chooseSnack(int id) {
//        for (Warehouse wh : warehouses) {
//            Order o = wh.chooseSnack(id);
//            if (o != null) {
////                addOrder(o);
//                return true;
//            }
//        }
//        return false;
//    }

//    private void addOrder(Order o) {
//        for (Order order : shoppingCart) {
//            if (o.getId() == order.getId()) {
//                order.amount += o.amount;
//                order.price = o.price;
//                return;
//            }
//        }
//        shoppingCart.add(o);
//    }

    private void decreaseOrder(Order o) {
        while(o.getAmount()!=0){
            o.amountDecrease();
        }
        shoppingCart.remove(o);
    }

    public void cancelOrder() {
        while(shoppingCart.size()!=0){
            decreaseOrder(shoppingCart.get(0));
        }
    }

    @Override
    public List<Cash> checkout(int paymentType) {
        // buy action has been happend in vendingmachine.views.PaymentController
        if(paymentType == 1){
            paymentsuccess = cashier.pay(getTotalPrice());
        }else{
            paymentsuccess = true;
        }
        return cashier.getInsertMoney();
    }

    @Override
    public int deleteAccount(String username) {
        if(username.isEmpty()){
            return 0;
        }
        return dbt.deleteAccount(username);
    }

    @Override
    public int deleteItem(String name) {
        if(name.isEmpty()){
            return 0;
        }
        for(Warehouse w: warehouses){
            w.deleteSnack(name);
        }
        return dbt.deleteItem(name);
    }

    @Override
    public int insertItem(int code, String name, String category, int quantity, double price) {
        int insert = dbt.insertItem(code, name, category, quantity, price);
        Snack snack = new Snack(code,name,quantity,price,category);
        addSnack(snack);
        return insert;
    }

    @Override
    public void logout() {
        this.cashier.returnCash();
        this.cancelOrder();
        this.username = "";
        this.role = "";
        paymentsuccess = false;
    }

    @Override
    public int updateItemQuantity(String name, int quantity) {
        for(Warehouse w: warehouses){
            for(Snack s: w.listSnacks()){
                if(s.getName().equals(name)){
                    s.setAmount(quantity);
                }
            }
        }
        int count = dbt.updateItemQuantity(name, quantity);
        return count;
    }

    @Override
    public int updateItemPrice(String name, double price) {
        for(Warehouse w: warehouses){
            for(Snack s: w.listSnacks()){
                if(s.getName().equals(name)){
                    s.setPrice(price);
                }
            }
        }
        int count = dbt.updateItemPrice(name, price);
        return count;
    }

    @Override
    public int updateItemCode(String name, int code) {
        for(Warehouse w: warehouses){
            for(Snack s: w.listSnacks()){
                if(s.getName().equals(name)){
                    s.setId(code);
                }
            }
        }
        int count = dbt.updateItemCode(name, code);
        return count;
    }

    @Override
    public int updateItemCategory(String name, String category){
//        for(Warehouse w: warehouses){
//            for(Snack s: w.listSnacks()){
//                if(s.getName().equals(name)){
//                    s.setType(category);
//                }
//            }
//        }
        /** it is an interface preserve **/
        //int count = dbt.updateItemCategory(name, category);
        return 1;
    }

    @Override
    public int updateItemName(String name, String newName){
        for(Warehouse w: warehouses){
            for(Snack s: w.listSnacks()){
                if(s.getName().equals(name)){
                    s.setName(newName);
                }
            }
        }
        int count = dbt.updateItemName(name, newName);
        return count;
    }

    @Override
    public List<User> listusers() {
        List<User> ls = new ArrayList<User>();
        ArrayList<ArrayList<String>> sls = dbt.getUserInfo();
        for(ArrayList<String> s : sls){
            ls.add(new User(s.get(0),s.get(2),s.get(1)));
        }
        return ls;
    }

    @Override
    public List<Snack> listAllSnack() {
        List<Snack> ls = new ArrayList<Snack>();
        for(Warehouse w: warehouses){
            ls.addAll(w.listSnacks());
        }
        return ls;
    }

    @Override
    public List<Cash> getCashes(){return this.cashier.getCashes();}

    @Override
    public String getRole(){
        return this.role;
    }

    @Override
    public List<Order> getShoppingCart() {
        for (Order o: shoppingCart) {
            if (shoppingCart.contains(o)) {
                if (o.getAmount() == 0) {
                    this.decreaseOrder(o);
                    break;
                }
            }

        }

        return shoppingCart;
    }

    @Override
    public double getTotalPrice() {
        double total = 0;
        for(Order o:shoppingCart){
            double twoDecimalTotalPrice = Math.round(o.getTotalPrice() * 100.0) / 100.0; // round to two decimal place
            total += twoDecimalTotalPrice;
        }
        return total;
    }

    @Override
    public Cashier getCashier() {
        return cashier;
    }

    @Override
    public boolean ifPaymentSuccess() {
        return paymentsuccess;
    }

    @Override
    public Transaction transaction(){
        String name = this.getUsername();
        List<Order> items_list = this.getShoppingCart();
        HashMap<String, Integer> items = new HashMap<String, Integer>();
        for(int i=0; i < items_list.size();i++){
            Order o = items_list.get(i);
            items.put(o.getName(), o.getAmount());
        }
        String method = this.getPayMethod();
        double paid = this.cashier.getInsert();
        Transaction t = new Transaction(name,items,paid,paid-this.getTotalPrice(),method);
        return t;
    }

    @Override
    public void PayMethod(String method){
        this.payMethod = method;
    }

    public String getPayMethod(){
        return this.payMethod;
    }

    @Override
    public String insertHistory(String status){
        Transaction t = this.transaction();
        if(status.equals("timeout")){
            t.cancelTimeOut();
        }else if(status.equals("change not available")){
            t.cancelChangeNotValid();
        }else if(status.equals("user cancelled")){
            t.cancelUser();
        }
        dbt.insertHistory(t);
        return t.getStatus();
    }

    @Override
    public String donePurchase(){
        Transaction t = this.transaction();
        t.done();
        dbt.donePurchase(t);
        return t.getStatus();
    }

    @Override
    public ArrayList<String> getLastFive(String username) {
        ArrayList<String> lastFiveItemOfUser = dbt.getLastFive(username);
        return lastFiveItemOfUser;
    }

}

