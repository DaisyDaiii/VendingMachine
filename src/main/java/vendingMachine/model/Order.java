package vendingMachine.model;

import vendingMachine.model.snack.Snack;

public class Order {
    int amount;
    double price;
    int id;
    String name;
    Snack snack;
    public Order(Snack s, int id, String name, double price, int amount){
        this.snack = s;
        this.amount = amount;
        this.price = price;
        this.id = id;
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice(){
        return price*amount;
    }

    public String getName() {
        return name;
    }

    public void amountIncrease(int i){
        this.amount+= i;
    }
    public void amountDecrease(){
        if(amount == 0)
        {
            return;
        }
        this.amount--;
        this.snack.setAmount(snack.getAmount()+1);
    }

    public String toString(){
        return String.format("%s , amount: %d",name,amount);
    }
}
