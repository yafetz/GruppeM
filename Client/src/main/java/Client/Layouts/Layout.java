package Client.Layouts;

import Client.Controller.AlleKurseController;
import Client.Controller.MeineKurseController;
import Client.Controller.UserprofilController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;

public class Layout {

    Object Controller;
    Button meineKurse = new Button();
    Button alleKurse = new Button();
    Hyperlink logo = new Hyperlink();
    Hyperlink namenlink = new Hyperlink();
    Object Nutzer;
    Stage stage;

    public Layout(String view_path, Stage stage, Object Nutzer){
        this.Nutzer = Nutzer;
        this.stage = stage;
        AnchorPane container = new AnchorPane();
        container.setStyle("-fx-background-color: linear-gradient(to left bottom, #bfe3e5, #7ebed2, #4797c5, #2e6db2, #413e92);");

        HBox hbox = new HBox();
        hbox.setLayoutX(201.00);
        hbox.prefHeight(67.00);
        hbox.prefWidth(622.00);

        VBox vbox = new VBox();
        vbox.setLayoutY(295.00);
        vbox.prefHeight(780.00);
        vbox.prefWidth(200.00);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(view_path));
        AnchorPane gui = null;
        try {
            gui = (AnchorPane) loader.load();
        Controller = loader.getController();
        gui.setBackground(Background.EMPTY);

        instanziateMeineKurseButton();
        hbox.getChildren().add(meineKurse);
        instanziateAlleKurseButton();
        hbox.getChildren().add(alleKurse);

        instanziereLogo();

        Pane guiPanel = new Pane();
        guiPanel.setLayoutX(200.0);
        guiPanel.setLayoutY(67.0);
        guiPanel.setPrefHeight(1009.0);
        guiPanel.setPrefWidth(1721.0);
        guiPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10 10 10 10;");
        guiPanel.getChildren().add(gui);

        Pane nutzer = new Pane();
        nutzer.setLayoutX(1090.0);
        nutzer.setLayoutY(10.0);
        nutzer.setPrefHeight(67.0);
        nutzer.setPrefWidth(302.0);

        instanziereNutzer();

        nutzer.getChildren().add(namenlink);

        container.getChildren().add(hbox);
        container.getChildren().add(vbox);
        container.getChildren().add(logo);
        container.getChildren().add(guiPanel);
        container.getChildren().add(nutzer);

        Scene scene = new Scene(container);
        scene.getStylesheets().add("css/layout.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void instanziateMeineKurseButton(){
        meineKurse.setText("Meine Kurse");
        meineKurse.setId("meineKurse");
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Layout meineKurse = new Layout("meineKurse.fxml",stage,Nutzer);
                if(meineKurse.getController() instanceof MeineKurseController){
                    ((MeineKurseController) meineKurse.getController()).setNutzerInstanz(Nutzer);
                }
            }
        };
        meineKurse.setCursor(Cursor.HAND);
        meineKurse.setFont(new Font("System Bold",12.0));
        meineKurse.setOnAction(buttonHandler);
        meineKurse.setAlignment(Pos.CENTER);
    }

    public void instanziateAlleKurseButton(){
        alleKurse.setText("Alle Kurse");
        alleKurse.setId("alleKurse");
        EventHandler<ActionEvent> alleKurseHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Layout alleKurse = new Layout("alleKurse.fxml",stage,Nutzer);
                if(alleKurse.getController() instanceof AlleKurseController){
                    ((AlleKurseController) alleKurse.getController()).setNutzerInstanz(Nutzer);
                }
            }
        };
        alleKurse.setCursor(Cursor.HAND);
        alleKurse.setFont(new Font("System Bold",12.0));
        alleKurse.setOnAction(alleKurseHandler);
        alleKurse.setAlignment(Pos.CENTER);
    }

    public void instanziereLogo(){
        logo.setText("SEP");
        logo.setId("logo");
        logo.setLayoutX(61.0);
        logo.setLayoutY(0.0);
        logo.setFont(new Font("System Bold",49.0));
        logo.setCursor(Cursor.HAND);
        EventHandler<ActionEvent> logoHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Layout homescreen = new Layout("homescreen.fxml",stage,Nutzer);
            }
        };
        logo.setOnAction(logoHandler);
    }

    public void instanziereNutzer(){
        namenlink.setText("Mein Profil");
        namenlink.setFont(new Font("System Bold",25.0));
        namenlink.setCursor(Cursor.HAND);
        EventHandler<ActionEvent> nutzerHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                Layout userprofil = new Layout("userprofile.fxml",stage,Nutzer);
                if(userprofil.getController() instanceof UserprofilController){
                    ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
                }
            }
        };
        namenlink.setOnAction(nutzerHandler);
    }
    public Object getController() {
        return Controller;
    }

    public void setController(Object controller) {
        Controller = controller;
    }
}
