package vendingMachine.model.warehouse;
import vendingMachine.model.Order;

import vendingMachine.model.snack.Snack;

import java.util.ArrayList;
import java.util.List;

/**
 * */

public class Warehouse {

    String type;
    private List<Snack> snacks;
    public Warehouse(String type){
        this.type = type;

        snacks = new ArrayList<Snack>();
    }
    public List<Snack> listSnacks(){
        return snacks;
    }

    public String getType() {
        return type;
    }

    public boolean addSnack(int id, int amount){
        for(Snack snack: snacks){
            if(snack.getId() == id){
                snack.setAmount(snack.getAmount()+amount);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param snack a new kind of snack;
     * @return if the snack successfully add
     */
    public boolean addSnack(Snack snack){
        if(!snack.getType().equals(this.getType())){
            //System.out.println(String.format("the type is %s",snack.getType()));
            return false;
        }else{
            for(Snack s : snacks){
                if(s.getId() == snack.getId()){
                    s.setAmount(s.getAmount()+snack.getAmount());
                    return true;
                }
            }
            snacks.add(snack);
            return true;
        }
    }

    public boolean deleteSnack(String name) {
        for(Snack snack: snacks){
            if(snack.getName().equals(name)){
                snacks.remove(snack);
                return true;
            }
        }
        return false;
    }
}
