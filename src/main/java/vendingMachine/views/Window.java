package vendingMachine.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;


import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import vendingMachine.model.MachineEngine;
import javafx.fxml.FXMLLoader;
/**
 * prototype of every window, extend it when create new window
 * */
public class Window extends Scene {
    MachineWindow window;
    public Window(Parent root, double width, double height, MachineWindow window) {
        super(root, width, height);
        this.window = window;
        setFrame();
    }


    /**
     * overrite this function to make your layout
     */
    protected void setFrame(){
    }

}
