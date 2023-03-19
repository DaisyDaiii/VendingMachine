package vendingMachine.views;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import vendingMachine.model.MachineEngine;
import java.io.IOException;


/**
 * use to manipulate the windows
 * */
public class MachineWindow{
    MachineEngine engine;
    Pane pane;
    Scene scene;
    Stage stage;

    Parent root;
    int width;
    int height;
    public MachineWindow(MachineEngine engine, int width, int height, Stage primaryStage) throws IOException {
        this.stage = primaryStage;

        this.engine = engine;
        setFxml("/login.fxml");
    }
    public void setFxml(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));
        Pane pane = (Pane)loader.load();
        try {
            ((Controller) loader.getController()).setApp(this);
        }catch(NullPointerException e){}
        setScene(new Scene(pane,getWidth(),getHeight()));

    }
    public void setScene(Scene scene){ this.scene = scene; stage.setScene(scene);}
    public Scene getScene(){ return scene; }
    public MachineEngine getEngine(){return engine;}


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


}
