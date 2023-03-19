package vendingMachine.model;


import vendingMachine.model.account.User;
import vendingMachine.model.snack.Snack;

import javax.imageio.stream.IIOByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for back-end manipulation. Every frond-end should connect here for interaction.
 * */
public interface MachineEngine {
    /**
     * For seller to add snack into vendingmachine
     * @param snackId id of a kind of snack
     * @return success or not
     */
    boolean addSnack(int snackId, int amount);

    /**
     *
     * @return the login username
     */
    String getUsername();

    /**
     *
     * @param username
     * @param password
     * @return success login or not
     */
    boolean login(String username,String password);

    /**
     *
     * @param username
     * @param password
     * @param role
     * @return
     */
    int register(String username, String password, String role);

    /**
     *
     * @param username
     * @return the role of this username
     */
    String checkRole(String username);

    /**
     *
     * @param username
     * @return
     */
    int deleteAccount(String username);

    /**
     *
     * @param name
     * @return
     */
    int deleteItem(String name);

    /**
     *
     * @param code
     * @param name
     * @param category
     * @param quantity
     * @param price
     * @return
     */
    int insertItem(int code, String name, String category, int quantity, double price);

    /**
     *
     * @param type the type of snack you want
     * @return a list of snack
     */
    List<Snack> getSnackLs(String type);


    /**
     *  add the order to the shopping cart
     * @param order the order we want to add
     */
    void addOrder(Order order);

    /**
     * it is for anonymousLogin
     */
    void anonymousLogin();

//    /**
//     * add a snack into shopping cart with snack id
//     * @param id the id of snack
//     * @return if the snack can be added into the shopping cart
//     */
//    boolean chooseSnack(int id);


    /**
     * pay after choosing snacks, do not use it in checkout button, it is for pay button in payment.fxml
     * @param paymentType : 0 for credit card, 1 for cash pay
     * @return a list of cash
     */
    List<Cash> checkout(int paymentType);

    /**
     * used to logout
     */
    void logout();

    /**
     *
     * @param name snackname
     * @param quantity new quantity
     * @return int    <br>
     * 	  	0 for Item not found <br>
     * 	  	-1 for quantity over limit 15 <br>
     * 	  	bigger than 0 for success
     */
    int updateItemQuantity(String name, int quantity);

    /**
     *
     * @param name snackname
     * @param price new price
     * @return int    <br>
     * 	  	0 for Item not found <br>
     * 	  	-1 Price not valid <br>
     * 	  	bigger than 0 for success
     */
    int updateItemPrice(String name, double price);

    /**
     *
     * @param name snackname
     * @param code new code
     * @return int    <br>
     * 	 	0 for Item not found <br>
     * 	  	-1 code already exist <br>
     * 	  	bigger than 0 for success
     */
    int updateItemCode(String name, int code);

    /**
     *
     * @param name  snackname
     * @param category new category
     * @return int    <br>
     * 	 	0 for Item not found <br>
     * 	 	-1 Category not exist <br>
     * 	  	bigger than 0 for success
     */
    int updateItemCategory(String name, String category);

    /**
     *
     * @param name snack name
     * @param newName new snack name
     * @return int	<br>
     *      0 for Item not found <br>
     * 	    -1 not sure error <br>
     * 	    bigger than 0 for success
     */
    int updateItemName(String name, String newName);


    /**
     * list all users
     * @return a list of users
     */
    List<User> listusers();

    /**
     * list all snack
     * @return a list of all snack
     */
    List<Snack> listAllSnack();

    /**
     * get a list of cash
     * @return a list of cash
     */
    public List<Cash> getCashes();

    /**
     * get current user role
     * @return current user role
     */
    String getRole();

    /**
     *
     * @return a list of order in shopping cart
     */
    List<Order> getShoppingCart();

    /**
     *  get total price in shopping cart
     * @return total price of snack in shoping cart
     */
    double getTotalPrice();

    /**
     *
     * @return the cashier in model
     */
    Cashier getCashier();

    boolean ifPaymentSuccess();

    /**
     * cancel all of order
     */
    void cancelOrder();

    /**
     * This function should be called after purchasing CANCELLED
     * @param status
     * @return
     */
    String insertHistory(String status);

    /**
     * pay method : cash or card
     * @param method
     */
    void PayMethod(String method);

    /**
     * each transaction
     * @return
     */
    Transaction transaction();

    String donePurchase();


    // Access the last five list of items purchased for the user.
    ArrayList<String> getLastFive(String username);

}
