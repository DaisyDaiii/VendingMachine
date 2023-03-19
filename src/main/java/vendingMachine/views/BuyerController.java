package vendingMachine.views;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import vendingMachine.model.Order;
import vendingMachine.model.Transaction;
import vendingMachine.model.snack.Snack;

import java.util.List;
import java.util.Timer;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class BuyerController extends Controller {

//    DatabaseToolkit dbt;
//    MachineEngine machineEngine;

//    @FXML
//    private Button startshopping;

    @FXML
    private AnchorPane main;

    @FXML
    private Label checkouterrormessage;

    @FXML
    private Button checkout;

    @FXML
    private ListView trolleylistview;

    @FXML
    private Text totalPrice;

    @FXML
    private TableView itemstable;

    @FXML
    private Text lastfivepurchase;

    @FXML
    private Separator separator;

    @FXML
    private TabPane tabpane;

//    @FXML
//    private Tab candies;
//    @FXML
//    private Tab chips;
//    @FXML
//    private Tab chocolates;
//    @FXML
//    private Tab drinks;

    @FXML
    private Text time;

    private int timeoutCounter;
    Timer timeoutTimer;
//    private boolean flag = false;

    private Integer selectedSnackAmount = 1;

//    HashMap<String, ArrayList<>> trolley;
//    HashMap<String, Integer> trolleyItemCounter;
//    HashMap<String, String> trolley;

    private ObservableList<Snack> ls = FXCollections.observableArrayList();
    private ObservableList<Order> orders = FXCollections.observableArrayList();

    private int counter;

    private List<String> lastFiveItemsToShow;
    private String lastFiveContent = "";

    @FXML
    public void checkoutAction(ActionEvent actionEvent) throws IOException {

        if (orders.isEmpty()) {
            checkouterrormessage.setVisible(true);
            Timer timer = new Timer();
            counter = 1;
            TimerTask task = new TimerTask() {
                public void run() {
                    counter -= 1;
                    if (counter == -1) {
                        checkouterrormessage.setVisible(false);
                        timer.cancel();
                    }
                }
            };

            timer.scheduleAtFixedRate(task, 1000, 1000);

        } else {
            checkouterrormessage.setVisible(false);
            window.setFxml("/Checkout.fxml");

            timeoutTimer.cancel();
        }
    }

    @FXML
    public void logOutAction(ActionEvent event) throws IOException {
        if(!orders.isEmpty()){
            window.getEngine().insertHistory("user cancelled");
        }
        timeoutTimer.cancel(); // Need this because, it needs to terminate the timer, else timer runs forever and even if go back to login page, the application timer still runs indefinately and application can't close.
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }

    public void tabChangeInvoker(int n) {
        if (n == 0) {
            setTable("Candies");
        } else if (n == 1) {
            setTable("Chips");
        } else if (n == 2) {
            setTable("Chocolates");
        } else if (n == 3) {
            setTable("Drinks");
        }
    }

    public void setTable(String type) {
        ls.clear();
        ls.addAll(window.getEngine().getSnackLs(type));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startTimeoutTimer();


        checkouterrormessage.setTextFill(Color.RED);
        checkouterrormessage.setVisible(false);

        itemstable.setItems(ls);
        Table.setSnackColumn(itemstable);
        trolleylistview.setItems(orders);
        tabpane.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> tabChangeInvoker((int)newValue)
        );

    }

    public void setApp(MachineWindow window){
        this.window = window;
        setTable("Candies");

        String currentUser = window.getEngine().getUsername();
        this.lastFiveItemsToShow = window.getEngine().getLastFive(currentUser);

        for (int i = 0; i < this.lastFiveItemsToShow.size(); i++) {
            if (i == this.lastFiveItemsToShow.size() - 1) {
                // Last index, no comma
                lastFiveContent += this.lastFiveItemsToShow.get(i);
            } else {
                lastFiveContent += this.lastFiveItemsToShow.get(i);
                lastFiveContent += ", ";
            }
        }

        lastfivepurchase.setText(lastFiveContent);

        main.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeoutTimer.cancel();
                startTimeoutTimer();
            }
        });


        itemstable.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                Snack rowSelected = (Snack) itemstable.getSelectionModel().getSelectedItem();
                if(rowSelected == null){
                    return;
                }
                this.window.getEngine().addOrder(rowSelected.makeOrder());
                orders.clear();
                orders.addAll(this.window.getEngine().getShoppingCart());
                tabChangeInvoker(tabpane.getSelectionModel().getSelectedIndex());
                totalPrice.setText(Double.toString(window.getEngine().getTotalPrice()));
            }
        });
        trolleylistview.setOnMouseClicked((MouseEvent event)-> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                Order rowSelected = (Order) trolleylistview.getSelectionModel().getSelectedItem();
                if(rowSelected == null){
                    return;
                }
                rowSelected.amountDecrease();
                orders.clear();
                orders.addAll(this.window.getEngine().getShoppingCart());
                tabChangeInvoker(tabpane.getSelectionModel().getSelectedIndex());
                totalPrice.setText(Double.toString(window.getEngine().getTotalPrice()));
            }
        });
        orders.clear();
        orders.addAll(this.window.getEngine().getShoppingCart());
        totalPrice.setText(Double.toString(window.getEngine().getTotalPrice()));
    }

    public void startTimeoutTimer() {
        timeoutTimer = new Timer();
        timeoutCounter = 60;
        TimerTask task = new TimerTask() {
            public void run() {
                time.setText(String.valueOf(timeoutCounter));
                timeoutCounter -= 1;

                if (timeoutCounter == -1) {
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
                    timeoutTimer.cancel();
                }
            }
        };
        timeoutTimer.scheduleAtFixedRate(task, 1000, 1000);
    }
}