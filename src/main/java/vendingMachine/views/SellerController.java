package vendingMachine.views;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import vendingMachine.model.snack.Snack;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SellerController extends Controller {

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
    private ChoiceBox category_choice;
    @FXML
    private ChoiceBox updateList;
    @FXML
    private TextField itemName;
    @FXML
    private TextField newText;
    @FXML
    private TabPane itemPane;
    @FXML
    private AnchorPane reportPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateList.setValue("select");
        updateList.getItems().addAll("select","code", "name", "category","quantity","price");
        category_choice.setValue("select");
        category_choice.getItems().addAll("select", "Drinks","Chocolates","Chips","Candies");
        itemPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                deleteError.setText("");
                updateError.setText("");
                insertError.setText("");
                updateList.setValue("select");
            }
        }
        );
    }
    @FXML
    public void itemButtonAction(ActionEvent event) throws IOException {
        updateList.setValue("select");
        category_choice.setValue("select");
        reportPane.setVisible(false);
        itemPane.setVisible(true);
    }
    @FXML
    public void reportAction(ActionEvent event) throws IOException {
        updateList.setValue("select");
        category_choice.setValue("select");
        itemPane.setVisible(false);
        reportPane.setVisible(true);
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
        String quantity = quantityText.getText();
        if(name.isEmpty() || code.isEmpty() || price.isEmpty() || quantity.isEmpty()){
            insertError.setText("Text cannot be empty");
            return;
        }
        String category = (String) category_choice.getValue();
        if(category.equals("select")){
            insertError.setText("Wrong select attribute");
            return;
        }
        int flag = window.getEngine().insertItem(Integer.parseInt(code), name, (String) category_choice.getValue(), Integer.parseInt(quantity), Double.parseDouble(price));
        if(flag==0){
            insertError.setText("Insert fails");
        } else if(flag>0){
            insertError.setText("Insert success");
            insertError.setFill(Paint.valueOf("#12d31b"));
            window.getEngine().addSnack(Integer.parseInt(code),Integer.parseInt(quantity));
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
        category_choice.setValue("select");
        quantityText.clear();
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
        itemName.clear();
        newText.clear();
        updateList.setValue("select");
    }

    @FXML
    public void logOutAction(ActionEvent event) throws IOException {
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }
}
