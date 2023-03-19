package vendingMachine.views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import vendingMachine.model.Cash;
import vendingMachine.model.Transaction;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ChangeController extends Controller{
    @FXML
    private ListView changeList;
    @FXML
    private Text info;
    private int counter;
    private boolean flag = false;

    private ObservableList<Cash> ls = FXCollections.observableArrayList();

    @FXML
    public void backtoLoginAction(ActionEvent event) throws IOException {
        flag = true;
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }

    @FXML
    public void keepShoppingAction(ActionEvent actionEvent) throws IOException {
        flag = true;
        window.setFxml("/buyer.fxml");
    }


    @Override
    public void setApp(MachineWindow window) {
        super.setApp(window);

        ls.addAll(window.getEngine().getCashier().returnCash());
        changeList.setItems(ls);
        if(window.getEngine().ifPaymentSuccess()){ // To show the transaction items, call: System.out.println("ChangeController (transaction item keys):\n" + window.getEngine().transaction().getItems().keySet());
            window.getEngine().donePurchase();
            window.getEngine().getShoppingCart().clear();
        }else{
            window.getEngine().cancelOrder();
            window.getEngine().insertHistory("change not available");
            info.setText("Failed, there is not enough cash for change, but thank you");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Timer timer = new Timer();
        counter = 5;
        TimerTask task = new TimerTask() {
            public void run() {
                counter -= 1;
                if (counter == 0) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            window.getEngine().logout();
                            try {
                                window.setFxml("/login.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    timer.cancel();
                }
                else if(flag == true){
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
}
