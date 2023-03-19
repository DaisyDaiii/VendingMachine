package vendingMachine.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class Transaction {
    public String username;
    public Timestamp timestamp;
    public HashMap<String, Integer> items;
    public double paid;
    public double change;
    public String payMethod;
    public String status;
  
    public Transaction(String username, HashMap<String, Integer> items, double paid, double change, String payMethod) {
        this.username = username;
        this.items = new HashMap<>(items);
        this.paid = paid;
        this.change = change;
        this.payMethod = payMethod;
        Date date = new Date();
        this.timestamp = new Timestamp(date.getTime());
        this.status = "unpaied";
    }

  
    public void done(){
        this.status = "done";
    }

    public void cancelTimeOut() {
        this.status = "timeout";
    }

    public void cancelChangeNotValid() {
        this.status = "change not available";
    }

    public void cancelUser() {
        this.status = "user cancelled";
    }

    public String getStatus() {
        return this.status;

    }

    public String getUsername() {
        return this.username;
    }

    public HashMap<String, Integer> getItems() {
        return this.items;
    }

    public double getPaid() {
        return this.paid;
    }

    public double getChange() {
        return this.change;
    }

    public String getPayMethod() {
        return this.payMethod;
    }

    public String getTimeStampString() {
        return this.timestamp.toString();
    }

    public Timestamp getTimeStamp() {
        return this.timestamp;
    }
}
