package vendingMachine.model;

public class Cash {
    final double value;
    int amount;
    public Cash(double value, int amount){
        this.value = value;
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {this.amount = amount;}

    public double getValue() {
        return value;
    }

    public void increase(){
        amount++;
    }

    public void decrease(){
        amount--;
    }


    @Override
    public String toString(){
        return String.format("%d cash of %.2f$",amount,value);
    }
}
