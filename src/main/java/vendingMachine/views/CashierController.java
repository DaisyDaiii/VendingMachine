package vendingMachine.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import vendingMachine.model.Cash;
import javafx.scene.text.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CashierController extends Controller{

    @FXML
    private ChoiceBox moneyList;
    @FXML
    private TextField numberText;

    @FXML
    private AnchorPane reportPane;
    @FXML
    private AnchorPane moneyPane;
    @FXML
    private Text moneyBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moneyList.setValue("select");
        moneyList.getItems().addAll("select", "50.0", "20.0","10.0","5.0","2.0","1.0","0.5","0.2","0.1","0.05");
    }

    @FXML
    public void manageMoneyButtonAction(ActionEvent event) throws IOException{
        moneyBox.setText("");
        reportPane.setVisible(false);
        moneyPane.setVisible(true);
    }
    @FXML
    public void exportReportButtonAction(ActionEvent event) throws IOException{
        moneyList.setValue("select");
        moneyBox.setText("");
        moneyPane.setVisible(false);
        reportPane.setVisible(true);

    }
    @FXML
    public void logOutButtonAction(ActionEvent event) throws IOException {
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }
    @FXML
    public void modifyButtonAction(ActionEvent event) throws IOException {
        String note = (String) moneyList.getValue();
        if(note.equals("select")){
            moneyBox.setText("wrong note");
            moneyBox.setFill(Paint.valueOf("#e11818"));
            return;
        }
        String amount = numberText.getText();
        if(amount.isEmpty()){
            moneyBox.setText("Text cannot be empty");
            moneyBox.setFill(Paint.valueOf("#e11818"));
            return;
        }
        for(Cash c: window.getEngine().getCashes()){
            if(c.getValue()==Double.parseDouble(note)){
                c.setAmount(Integer.parseInt(amount));
            }
        }
        window.getEngine().getCashier().saveCash();
        numberText.clear();
        moneyList.setValue("select");
        moneyBox.setText("modify success");
        moneyBox.setFill(Paint.valueOf("#12d31b"));
    }

}
