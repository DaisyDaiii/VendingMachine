package vendingMachine.views;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class Controller implements Initializable {
    protected MachineWindow window;

    @Override
    public void initialize(URL location, ResourceBundle resources){
    }
    public void setApp(MachineWindow window){
        this.window = window;
    }

}
