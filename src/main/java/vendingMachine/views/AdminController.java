package vendingMachine.views;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import vendingMachine.model.Cash;
import vendingMachine.model.account.User;
import vendingMachine.model.snack.Snack;
import javafx.scene.control.Tab;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController extends Controller {
    @FXML
    private TextField addNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private ChoiceBox roleChoice;
    @FXML
    private TextField deleteNameField;
    @FXML
    private Text errorBox;
    @FXML
    private Text addError;
    @FXML
    private TabPane userPane;
    @FXML
    private TabPane itemPane;


    @FXML
    private TextField deleteItem;
    @FXML
    private Text deleteError;
    @FXML
    private Text updateError;
    @FXML
    private Text insertError;
    @FXML
    private TextField nameInsertText;
    @FXML
    private TextField codeText;
    @FXML
    private TextField priceText;
    @FXML
    private TextField quantityText;
    @FXML
    private ChoiceBox categoryChoice;
    @FXML
    private ChoiceBox updateList;
    @FXML
    private TextField itemName;
    @FXML
    private TextField newText;

    @FXML
    private AnchorPane reportPane;

    @FXML
    private AnchorPane moneyPane;
    @FXML
    private ChoiceBox moneyList;
    @FXML
    private TextField moneyText;
    @FXML
    private Text moneyBox;


    public void clearError() {
        deleteError.setText("");
        updateError.setText("");
        insertError.setText("");
        errorBox.setText("");
        addError.setText("");
        moneyBox.setText("");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roleChoice.setValue("select");
        roleChoice.getItems().addAll("select","seller", "cashier", "owner");
        updateList.setValue("select");
        updateList.getItems().addAll("select","code", "name", "category","quantity","price");
        moneyList.setValue("select");
        moneyList.getItems().addAll("select","50.0", "20.0","10.0","5.0","2.0","1.0","0.5","0.2","0.1","0.05");

        categoryChoice.setValue("select");
        categoryChoice.getItems().addAll("select","Candies", "Chips", "Chocolates", "Drinks");
        userPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                addError.setText("");
                errorBox.setText("");
                roleChoice.setValue("select");
            }
        }
        );
        itemPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                deleteError.setText("");
                updateError.setText("");
                insertError.setText("");
                updateList.setValue("select");

                categoryChoice.setValue("select");
            }
        }
        );
    }

    @FXML
    public void userButtonAction(ActionEvent event) throws IOException{
        this.clearError();
        roleChoice.setValue("select");
        updateList.setValue("select");
        moneyList.setValue("select");
        categoryChoice.setValue("select");
        itemPane.setVisible(false);
        reportPane.setVisible(false);
        moneyPane.setVisible(false);
        userPane.setVisible(true);
    }
    @FXML
    public void itemButtonAction(ActionEvent event) throws IOException {
        this.clearError();
        roleChoice.setValue("select");
        updateList.setValue("select");
        moneyList.setValue("select");
        categoryChoice.setValue("select");
        userPane.setVisible(false);
        moneyPane.setVisible(false);
        reportPane.setVisible(false);
        itemPane.setVisible(true);
    }
    @FXML
    public void reportAction(ActionEvent event) throws IOException {
        this.clearError();
        roleChoice.setValue("select");
        updateList.setValue("select");
        moneyList.setValue("select");
        categoryChoice.setValue("select");
        userPane.setVisible(false);
        itemPane.setVisible(false);
        moneyPane.setVisible(false);
        reportPane.setVisible(true);
    }
    @FXML
    private void moneyButtonAction(ActionEvent event) throws IOException {
        this.clearError();
        roleChoice.setValue("select");
        updateList.setValue("select");
        moneyList.setValue("select");
        categoryChoice.setValue("select");
        userPane.setVisible(false);
        itemPane.setVisible(false);
        reportPane.setVisible(false);
        moneyPane.setVisible(true);
    }
    @FXML
    public void logOutAction(ActionEvent event) throws IOException {
        this.clearError();
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }
    @FXML
    public void deleteButtonAction(ActionEvent event) throws IOException {
        errorBox.setFill(Paint.valueOf("#e11818"));
        String name = deleteNameField.getText();
        int num = window.getEngine().deleteAccount(name);
        if( num == 1){
            errorBox.setText("Delete success");
            errorBox.setFill(Paint.valueOf("#12d31b"));
        }
        else if(num == 0){
            errorBox.setText("Cannot find account");
        }
        else if(num == -1){
            errorBox.setText("Delete fails");
        }
        else if(num == -2){
            errorBox.setText("Root account cannot be deleted");
        }
        deleteNameField.clear();
    }
    @FXML
    public void addButtonAction(ActionEvent event) throws IOException {
        addError.setFill(Paint.valueOf("#e11818"));
        String name = addNameField.getText();
        String password = passwordField.getText();
        String role = (String) roleChoice.getValue();
        if(role.equals("select")){
            addError.setText("Wrong role");
            return;
        }
        int flag = window.getEngine().register(name,password,role);
        if(flag==-1){
            addError.setText("Account already exist");
        }
        else if(flag>0){
            addError.setText("Add success");
            addError.setFill(Paint.valueOf("#12d31b"));
        }
        else if(flag==0){
            addError.setText("Add fails");
        }
        roleChoice.setValue("select");
        addNameField.clear();
        passwordField.clear();
    }
    @FXML
    public void deleteItemAction(ActionEvent event) throws IOException {
        deleteError.setFill(Paint.valueOf("#e11818"));
        String name = deleteItem.getText();
        int flag = window.getEngine().deleteItem(name);
        if(flag == 0){
            deleteError.setText("Cannot find item");
        }
        else if(flag == -1) {
            deleteError.setText("Delete fails");
        }
        else if(flag > 0) {
            deleteError.setText("Delete success");
            deleteError.setFill(Paint.valueOf("#12d31b"));
        }
        deleteItem.clear();
    }
    @FXML
    public void insertButtonAction(ActionEvent event) throws IOException {
        insertError.setFill(Paint.valueOf("#e11818"));
        String name = nameInsertText.getText();
        String code = codeText.getText();
        String price = priceText.getText();

        String category = (String) categoryChoice.getValue();
        String quantity = quantityText.getText();

        if(category.equals("select")){
            insertError.setText("Wrong category");
            return;
        }

        if(name.isEmpty() || code.isEmpty() || price.isEmpty() || category.isEmpty() || quantity.isEmpty()){
            insertError.setText("Text cannot be empty");
            return;
        }
        int flag = window.getEngine().insertItem(Integer.parseInt(code), name, category, Integer.parseInt(quantity), Double.parseDouble(price));
        if(flag==0){
            insertError.setText("Insert fails");
        } else if(flag>0){
            insertError.setText("Insert success");
            insertError.setFill(Paint.valueOf("#12d31b"));
        } else if(flag==-1){
            insertError.setText("Code already exist");
        } else if(flag==-2){
            insertError.setText("Name already exist");
        } else if(flag==-3){
            insertError.setText("category not exist");
        } else if(flag==-4){
            insertError.setText("quantity not valid");
        }

        nameInsertText.clear();
        codeText.clear();
        priceText.clear();
        quantityText.clear();
        categoryChoice.setValue("select");
        //should be one more : price invalid
    }
    @FXML
    public void updateButtonAction(ActionEvent event) throws IOException {
        updateError.setFill(Paint.valueOf("#e11818"));
        String name = itemName.getText();
        String newT = newText.getText();
        if(name.isEmpty() || newT.isEmpty()){
            updateError.setText("Text cannot be empty");
            return;
        }
        String changeItem = (String) updateList.getValue();
        if(changeItem.equals("select")){
            updateError.setText("Wrong select attribute");
            return;
        }
        int temp = -2;
        if(changeItem.equals("category")){
            temp = window.getEngine().updateItemCategory(name, newT);
        }
        else if(changeItem.equals("name")){
            temp = window.getEngine().updateItemName(name, newT);
        }
        else if(changeItem.equals("code")){
            temp = window.getEngine().updateItemCode(name, Integer.parseInt(newT));
        }
        else if(changeItem.equals("quantity")){
            temp = window.getEngine().updateItemQuantity(name, Integer.parseInt(newT));
        }
        else if(changeItem.equals("price")){
            temp = window.getEngine().updateItemPrice(name, Double.parseDouble(newT));
        }
        if(temp == 0){
            updateError.setText("Cannot find item");
        }
        else if(temp == -1){
            updateError.setText("Update fails");
        }
        else if(temp > 0){
            updateError.setText("Update success");
            updateError.setFill(Paint.valueOf("#12d31b"));
        }
        updateList.setValue("select");
        itemName.clear();
        newText.clear();
    }

    @FXML
    public void modifyButtonAction(ActionEvent event) throws IOException {
        String note = (String) moneyList.getValue();
        String amount = moneyText.getText();
        if(amount.isEmpty()){
            moneyBox.setText("Text cannot be empty");
            moneyBox.setFill(Paint.valueOf("#e11818"));
            return;
        }
        if(note.equals("select")){
            moneyBox.setText("wrong note");
            moneyBox.setFill(Paint.valueOf("#e11818"));
            return;
        }
        for(Cash c: window.getEngine().getCashes()){
            if(c.getValue()==Double.parseDouble(note)){
                c.setAmount(Integer.parseInt(amount));
            }
        }
        window.getEngine().getCashier().saveCash();
        moneyList.setValue("select");
        moneyBox.setText("modify success");
        moneyBox.setFill(Paint.valueOf("#12d31b"));
    }
}
