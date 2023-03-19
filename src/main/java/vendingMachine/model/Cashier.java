package vendingMachine.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Cashier {
    List<Cash> cashes= new ArrayList<Cash>();
    /**
     * used to remember the money that will back to customer when it should return the money
     */
    List<Cash> returnCash = new ArrayList<Cash>();
    private double totalInsert = 0;
    public Cashier(){
        try {
            loadcash();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        refreshReturnMoney();
    }

    private void loadcash() throws FileNotFoundException {
        File f = new File("src/main/resources/database/Cashes");
        Scanner s = new Scanner(f);
        while (s.hasNextLine()) {
            String temp = s.nextLine();
            String[] strings = temp.split(" ");
            cashes.add(new Cash(Double.parseDouble(strings[0]),Integer.parseInt(strings[1])));
        }
        s.close();
    }

    public void saveCash() {
        File f = new File("src/main/resources/database/Cashes");
        String writen = "";
        for(Cash c: cashes){
            //System.out.println(c.toString());
            String[] holder = c.toString().split(" cash of ");

            String temp = String.format("%s %s",holder[1].replace( "$"," ").strip(),holder[0]);
            writen += temp;
            if(c != cashes.get(cashes.size()-1)){
                writen+="\n";
            }
        }
        try {
            FileWriter w = new FileWriter(f);
            w.write(writen);
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void reset(){
        for(Cash c:cashes){
            c.setAmount(20);
        }
        saveCash();
    }

    private void refreshReturnMoney(){
        returnCash.clear();
        returnCash.add(new Cash(50,0));
        returnCash.add(new Cash(20,0));
        returnCash.add(new Cash(10,0));
        returnCash.add(new Cash(5,0));
        returnCash.add(new Cash(2,0));
        returnCash.add(new Cash(1,0));
        returnCash.add(new Cash(0.5,0));
        returnCash.add(new Cash(0.2,0));
        returnCash.add(new Cash(0.1,0));
        returnCash.add(new Cash(0.05,0));
    }


    public List<Cash> getCashes(){
        return this.cashes;
    }

    public double totalValue(){
        double total = 0;
        for(Cash c: returnCash){
            total+=c.getValue() * c.getAmount();
        }
        return total;
    }

    private void takeInputMoneyIntoMachine(){
        for(Cash c: cashes){
            for(Cash input: returnCash){
                if(Math.round(c.getValue()*100) == Math.round(input.getValue()*100)){
                    //System.out.println(c.getValue());
                    c.setAmount(c.getAmount()+input.getAmount());
                }
            }
        }
        returnCash.clear();
    }


    /**
     * pay with cash
     * @param totalPrice the money need to pay
     * @return if payment success
     */
    public boolean pay(double totalPrice){
        this.totalInsert = totalValue();
        double remain = totalValue() - totalPrice;
        //System.out.println(remain);
        if(remain < 0){
            return false;
        }
        List<Cash> readyToReturn = new ArrayList<Cash>();
        for(Cash c : cashes){
            // keep using the biggest cash to change
            remain =Math.round(remain * 100.0) / 100.0;
            while(remain >= c.getValue() && c.getAmount()>0){
                c.setAmount(c.getAmount()-1);
                remain -= c.getValue();

                readyToReturn.add(new Cash(c.getValue(),1));

            }

        }
        // allow the uncertainty for 0.01
        if(remain > 0.01){
            for(Cash c : cashes){
                // keep using the biggest cash to change
                for(Cash r: cashes){
                    if(r.getValue() == c.getValue()){
                        c.increase();
                    }
                }
            }
            return false;
        }
        takeInputMoneyIntoMachine();
        returnCash.addAll(readyToReturn);
        saveCash();
        return true;
    }

    /**
     * use to get the change money
     * @return a list of change money
     */
    public List<Cash> returnCash(){
        List<Cash> ls = new ArrayList<Cash>();
        ls.addAll(returnCash);
        returnCash.clear();
        refreshReturnMoney();
        return ls;
    }

    public List<Cash> getInsertMoney(){
        return this.returnCash;
    }


    public double currentValue(List<Cash> cashes){
        double total = 0;
        for(Cash c : cashes){
            total += c.getValue()*c.getAmount();
        }

        return total;
    }

    public double getInsert(){
        return totalInsert;
    }
}
