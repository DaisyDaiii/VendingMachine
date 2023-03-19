package vendingMachine.views;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import vendingMachine.model.Cash;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javafx.scene.input.KeyEvent;
import vendingMachine.model.Transaction;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PaymentController extends Controller {

    @FXML
    private AnchorPane main;

    @FXML
    private Text cashInfo;
    @FXML
    private AnchorPane cashPane;
    @FXML
    private AnchorPane cardPane;
    @FXML
    private PasswordField number; // Check card password
    @FXML
    private TextField nameField; // Check card name
    @FXML
    private Button cardPay;
    @FXML
    private Text    info;
    @FXML
    private ListView insertCash;
    @FXML
    private Text moneyAmount;
    @FXML
    private Text needToPayText;
    @FXML
    private Button cashPayButton;

    @FXML
    private Label timeoutlabel;

    @FXML
    private Text time;

    private int timeoutCounter;
    Timer timeoutTimer;

    private ObservableList<Cash> ls = FXCollections.observableArrayList();

    @FXML
    public void cashButtonAction(ActionEvent event) throws IOException {
        info.setText("");
        number.clear();
        nameField.clear();
        cardPane.setVisible(false);
        cashPane.setVisible(true);
    }
    @FXML
    public void cardButtonAction(ActionEvent event) throws IOException {
        cashInfo.setText("");
        moneyAmount.setText("0$");
        cashPane.setVisible(false);
        cardPane.setVisible(true);
    }
    @FXML
    public void backButtonAction(ActionEvent event) throws IOException {
        window.setFxml("/Checkout.fxml");

        timeoutTimer.cancel();
    }

    @FXML
    public void logout() throws IOException{
        window.getEngine().insertHistory("user cancelled");

        timeoutTimer.cancel(); // Need this because, it needs to terminate the timer, else timer runs forever and even if go back to login page, the application timer still runs indefinately and application can't close.
        window.getEngine().logout();
        window.setFxml("/login.fxml");
    }


    private boolean checkDreditCard(String number, String name){
        if(number==null||name == null||number.equals("") || name.equals("")){
            return false;
        }
        JSONParser jsonParser = new JSONParser();
        JSONArray mainarray = null;
        try (FileReader reader = new FileReader("credit_cards.json")){
            mainarray = (JSONArray) jsonParser.parse(reader);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<mainarray.size();i++){
            JSONObject o = (JSONObject) mainarray.get(i);
            String recordName = (String)o.get("name");
            String recordNumber = (String)o.get("number");
            if(recordName.equals(name) && recordNumber.equals(number)){
                return true;
            }
        }

        return false;
    }

    private boolean loadExistingUserCardDetails(String name) {
        JSONParser jsonParser = new JSONParser();
        JSONArray mainarray = null;
        try (FileReader reader = new FileReader("credit_cards.json")){
            mainarray = (JSONArray) jsonParser.parse(reader);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<mainarray.size();i++){
            JSONObject o = (JSONObject) mainarray.get(i);
            String recordName = (String)o.get("name");
            String recordNumber = (String)o.get("number");
            if(recordName.equals(name)){
                // TODO
                nameField.setText(name);
                number.setText(recordNumber);
                return true;
            }
        }
        return false;
    }

    private void setMoneyAmount(){
        moneyAmount.setText(String.format("%.2f$",window.getEngine().getCashier().currentValue(ls)));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeoutlabel.toFront();
        time.toFront();
        startTimeoutTimer();
    }

    @Override
    public void setApp(MachineWindow window) {
        super.setApp(window);

        if(window.getEngine().getUsername().equals("anonymous")){
            //do nothing - don't auto fill in customer card details
        }else{
            // save the card info to the user
            loadExistingUserCardDetails(window.getEngine().getUsername()); // Check the username: System.out.println("LOGGED IN AS: " + window.getEngine().getUsername());
        }

        main.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                timeoutTimer.cancel();
                startTimeoutTimer();
            }
        });

        main.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
           @Override
           public void handle(KeyEvent keyEvent) {
               if (keyEvent.getCharacter() != null) {
                   timeoutTimer.cancel();
                   startTimeoutTimer();
               }
           }
       });

        needToPayText.setText(String.format("%.2f$",window.getEngine().getTotalPrice()));
        ls.addAll(window.getEngine().getCashier().getInsertMoney());
        insertCash.setItems(ls);
        insertCash.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton().equals(MouseButton.PRIMARY)) {
                    Cash rowSelected = (Cash) insertCash.getSelectionModel().getSelectedItem();
                    rowSelected.setAmount(rowSelected.getAmount()+1);
                    setMoneyAmount();
                    ls.clear();
                    ls.addAll(window.getEngine().getCashier().getInsertMoney());
                }
            }
        });

        cardPay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(checkDreditCard(number.getText(),nameField.getText())){
                    window.getEngine().checkout(0);
                    info.setText("payment success");
                    if(window.getEngine().getUsername().equals("anonymous")){ // Anonymous
                        //do nothing
                    }else{ // logged in as user
                        //save the card info to the user
                    }
                    //TODO: update the status of snacks in database

                    //jump to the （付款成功/找零）界面
                    try {
                        window.getEngine().PayMethod("card");
                        timeoutTimer.cancel();
                        window.setFxml("/change.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    info.setText("unavailable");
                }
                number.clear();
                nameField.clear();
            }
        });

        cashPayButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(window.getEngine().getCashier().currentValue(ls) < window.getEngine().getTotalPrice()){
                    cashInfo.setText("The amount is not enough");
                    return;
                }
                window.getEngine().checkout(1);
                if(!window.getEngine().ifPaymentSuccess()){
                    window.getEngine().getCashier().getInsertMoney().clear();
                    window.getEngine().getCashier().returnCash();
                    ls.clear();
                    ls.addAll(window.getEngine().getCashier().getInsertMoney());
                    setMoneyAmount();
                    cashInfo.setText("There are no enough change");
                    return;
                }
                try {
                    window.getEngine().PayMethod("cash");
                    timeoutTimer.cancel();
                    window.setFxml("/change.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        setMoneyAmount();
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

    @FXML
    public void namefieldAction(ActionEvent actionEvent) {
    }

    @FXML
    public void passwordfieldAction(ActionEvent actionEvent) {
    }
}
