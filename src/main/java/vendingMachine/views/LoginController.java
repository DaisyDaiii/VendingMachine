package vendingMachine.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class LoginController extends Controller{
    @FXML
    private Text errorBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void loginButtonAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(window.getEngine().login(username,password)){
            errorBox.setText("login success");
            String name = window.getEngine().checkRole(username);
            if(name.equals("admin")){
                window.setFxml("/new_admin.fxml");
            }
            else if(name.equals("cashier")){
                window.setFxml("/new_cashier.fxml");
            }
            else if(name.equals("seller")){
                window.setFxml("/new_seller.fxml");
            }
            else if(name.equals("customer")) {
                BuyerController buyerController = new BuyerController();
                window.setFxml("/buyer.fxml");
            }
        }else{
            errorBox.setText("username or password wrong");
        }
        usernameField.clear();
        passwordField.clear();

    }
    @FXML
    public void registerButtonAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.isEmpty() || password.isEmpty()){
            errorBox.setText("Text cannot be empty");
            return;
        }
        int flag = window.getEngine().register(username, password, "customer");
        if(flag>0){
            errorBox.setText("Register success");
            window.setFxml("/buyer.fxml");
        }
        else if(flag==-1){
            errorBox.setText("Account already exist!");
        }
        else if(flag==0){
            errorBox.setText("Invalid username or password!");
        }
        usernameField.clear();
        passwordField.clear();
    }
    @FXML
    public void visitorButtonAction(ActionEvent event) throws IOException {
        window.getEngine().anonymousLogin();
        window.setFxml("/buyer.fxml");
    }
}
