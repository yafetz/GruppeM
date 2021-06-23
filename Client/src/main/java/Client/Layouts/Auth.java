package Client.Layouts;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Auth {
    private Object Controller;
    private Stage stage;
    private AnchorPane container;
    public Auth() {
        container = new AnchorPane();
        container.setStyle("-fx-background-color: linear-gradient(to left bottom, #bfe3e5, #7ebed2, #4797c5, #2e6db2, #413e92);");

    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void ChangeFxml(String view_path){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(view_path));
        AnchorPane gui = null;
        try {
            gui = (AnchorPane) loader.load();
            gui.setBackground(Background.EMPTY);
            container.getChildren().setAll(gui);
            Controller = loader.getController();
            Scene scene = new Scene(container);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setFullScreen(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
