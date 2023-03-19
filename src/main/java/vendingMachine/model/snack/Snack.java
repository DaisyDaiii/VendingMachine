package vendingMachine.model.snack;


import vendingMachine.model.Order;

public class Snack{
    String name;
    int amount;
    double price;
    int id;
    String type;
    public Snack(int id,String name, int amount, double price, String type){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }
    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name) {this.name = name;}
    public int getAmount(){
        return amount;
    }
    public void setAmount(int amount){
        this.amount = amount;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {this.type = type;}
    public Order makeOrder(){
        if(amount == 0){
            return new Order(this,this.id,this.name,this.price,0);
        }
        Order o = new Order(this,this.id,this.name,this.price,1);
        this.amount--;
        return o;
    }
}