package Client.Layouts;

import Client.Controller.AlleKurseController;
import Client.Controller.MeineKurseController;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

public class Layout {

    public Layout(String view_path, Stage stage) throws IOException {
        AnchorPane container = new AnchorPane();
        container.setStyle("-fx-background-color: linear-gradient(to left bottom, #bfe3e5, #7ebed2, #4797c5, #2e6db2, #413e92);");
        HBox hbox = new HBox();
        hbox.setLayoutX(201.00);
        hbox.prefHeight(67.00);
        hbox.prefWidth(622.00);
        Button meineKurse = new Button();
        meineKurse.setText("Meine Kurse");
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
            }
        };
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(view_path));
        AnchorPane gui = (AnchorPane) loader.load();
        gui.setBackground(Background.EMPTY);
        meineKurse.setOnAction(buttonHandler);
        meineKurse.setAlignment(Pos.CENTER);
        hbox.getChildren().add(meineKurse);
        container.getChildren().add(gui);
        container.getChildren().add(meineKurse);
        Scene scene = new Scene(container);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.show();
    }
}
