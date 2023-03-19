package vendingMachine.views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import vendingMachine.model.Order;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class CheckoutController extends Controller {

    @FXML
    private AnchorPane main;

    @FXML
    private Label confirmpurchase;

    @FXML
    private ListView checkoutView;

    @FXML
    private Text time;

    Timer timer;
    private int counter;

    @FXML
    private TextField totalSpend;
    private ObservableList<Order> ls = FXCollections.observableArrayList();
    @FXML
    public void showHistoryAction(ActionEvent actionEvent) throws IOException {


        timer.cancel();
    }

    @FXML
    public void keepShoppingAction(ActionEvent actionEvent) throws IOException {
        window.setFxml("/buyer.fxml");

        timer.cancel();
    }

    @FXML
    public void logout() throws IOException {
        window.getEngine().insertHistory("user cancelled");

        timer.cancel(); // Need this because, it needs to terminate the timer, else timer runs forever and even if go back to login page, the application timer still runs indefinately and application can't close.
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }

    @FXML
    public void checkoutAction(ActionEvent actionEvent) throws IOException {
        //TODO: make transaction here
        window.setFxml("/payment.fxml");

        timer.cancel();
    }

    @Override
    public void setApp(MachineWindow window) {
        super.setApp(window);
        checkoutView.setItems(ls);
        ls.addAll(window.getEngine().getShoppingCart());
        totalSpend.setText(String.format("$ %.2f",window.getEngine().getTotalPrice()));
        totalSpend.setDisable(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){

        main.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timer.cancel();
                startTimeoutTimer();
            }
        });

        startTimeoutTimer();
    }

    public void startTimeoutTimer() {
        timer = new Timer();
        counter = 60;
        TimerTask task = new TimerTask() {
            public void run() {
                counter -= 1;
                time.setText(String.valueOf(counter));
                if (counter == -1) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            window.getEngine().insertHistory("timeout");
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
            }
        };
        timer.scheduleAtFixedRate(task, 1000, 1000);
    }
}
