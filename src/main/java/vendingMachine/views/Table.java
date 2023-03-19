package vendingMachine.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import vendingMachine.model.account.User;
import vendingMachine.model.snack.Snack;

import java.util.List;
/** used to draw a table
 * */
public class Table {

//    /**
//     *
//     * @param ls a list of snack
//     * @return table view in snackname, price, amount
//     */
//    public static TableView getSnackTable(List<Snack> ls){
//
//        TableView table = new TableView();
//        ObservableList<Snack> data = FXCollections.observableArrayList();
//        data.addAll(ls);
//        table.setItems(data);
//
//        setSnackColumn(table);
//        return table;
//    }

    public static void setSnackColumn(TableView table){
        TableColumn code = new TableColumn<>("Code");
        code.setCellValueFactory(new PropertyValueFactory<Snack, String>("id"));
        TableColumn snack = new TableColumn<>("Snack");
        snack.setCellValueFactory(new PropertyValueFactory<Snack, String>("name"));
        TableColumn price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<Snack, String>("price"));
        TableColumn amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<Snack, String>("amount"));
        TableColumn type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<Snack, String>("type"));

        table.getColumns().addAll(code,snack,price,amount,type);
    }


//    /**
//     *
//     * @param ls a list of User
//     * @return  a table view of users
//     */
//    public static TableView getAccountTable(List<User> ls){
//        TableView table = new TableView();
//        ObservableList<User> data = FXCollections.observableArrayList();
//        data.addAll(ls);
//        table.setItems(data);
//        setUserColumn(table);
//        return table;
//    }
    public static void setUserColumn(TableView table){
        TableColumn username = new TableColumn<>("Username");
        username.setCellValueFactory(new PropertyValueFactory<Snack, String>("username"));
        TableColumn role = new TableColumn<>("Role");
        role.setCellValueFactory(new PropertyValueFactory<Snack, String>("role"));
//        TableColumn password = new TableColumn<>("Password");
//        password.setCellValueFactory(new PropertyValueFactory<Snack, String>("password"));
        table.getColumns().addAll(username,role);
    }


    public static void setCashColumn(TableView table){

        TableColumn value = new TableColumn<>("Value");
        value.setCellValueFactory(new PropertyValueFactory<Snack, String>("value"));

        TableColumn amount = new TableColumn<>("Amount");
        amount.setCellValueFactory(new PropertyValueFactory<Snack, String>("amount"));

        table.getColumns().addAll(value,amount);
    }

}
