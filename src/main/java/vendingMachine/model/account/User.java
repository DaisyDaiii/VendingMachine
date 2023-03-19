package vendingMachine.model.account;

public class User {

    private String username;
    private String role;
    private String password;
    private String card_name = null;
    private String card_number = null;

    public User(String username, String role, String password){
        this.username = username;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCard_name() {return card_name; }

    public String getCard_number() {return card_number; }

    public void addCard(String name, String number) {
        this.card_name = name;
        this.card_number = number;
    }
}
