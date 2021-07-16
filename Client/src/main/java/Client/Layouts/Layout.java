package Client.Layouts;

import Client.Controller.*;
import Client.Controller.Chat.ChatController;
import Client.Controller.Kalender.CalenderController;
import Client.Controller.Kurse.AlleKurseController;
import Client.Controller.Kurse.MeineKurseController;
import Client.Controller.NutzerProfil.UserprofilController;
import Client.Modell.Lehrender;
import Client.Modell.Student;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Layout {

    private Object Controller;
    private Button meineKurse = new Button();
    private Button alleKurse = new Button();
    private Button Kalender = new Button();
    private Hyperlink logo = new Hyperlink();
    private Hyperlink namenlink = new Hyperlink();
    private Object Nutzer;
    private Stage stage;
    private AnchorPane container;
    private Pane guiPanel;
    private AnchorPane gui;
    private final Layout layout = this;

    public void setNutzer(Object Nutzer){
        this.Nutzer = Nutzer;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }


    public void instanceAuth(String view_path){
        container = new AnchorPane();
        container.setStyle("-fx-background-color: linear-gradient(to left bottom, #bfe3e5, #7ebed2, #4797c5, #2e6db2, #413e92);");
        ChangeFxml(view_path);
        show();
    }

    private void show(){
        Scene scene = new Scene(container);
        stage.setScene(scene);
        stage.setMaximized(false);
        stage.setFullScreen(false);
        stage.setFullScreenExitHint("");
        stage.show();
    }

    private void ChangeFxml(String view_path){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(view_path));
        try {
            gui = (AnchorPane) loader.load();
            gui.setBackground(Background.EMPTY);
            container.getChildren().setAll(gui);
            Controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void instanceLayout(String view_path){
        container = new AnchorPane();
        container.setStyle("-fx-background-color: linear-gradient(to left bottom, #bfe3e5, #7ebed2, #4797c5, #2e6db2, #413e92);");

        HBox hbox = new HBox();
        hbox.setLayoutX(201.00);
        hbox.prefHeight(67.00);
        hbox.prefWidth(622.00);

        VBox vbox = new VBox();
        vbox.setLayoutY(295.00);
        vbox.prefHeight(780.00);
        vbox.prefWidth(200.00);

        instanziateMeineKurseButton();
        hbox.getChildren().add(meineKurse);
        instanziateAlleKurseButton();
        hbox.getChildren().add(alleKurse);
        instanziateKalenderButton();
        hbox.getChildren().add(Kalender);

        instanziereLogo();

        Pane nutzer = new Pane();
        nutzer.setLayoutX(1090.0);
        nutzer.setLayoutY(10.0);
        nutzer.setPrefHeight(67.0);
        nutzer.setPrefWidth(302.0);

        instanziereNutzer();
        byte[] profilbild = null;
        if(Nutzer instanceof Student){
            profilbild = ((Student) Nutzer).getNutzer().getProfilbild();
        }else if(Nutzer instanceof Lehrender){
            profilbild = ((Lehrender) Nutzer).getNutzerId().getProfilbild();
        }
        Image img = new Image(new ByteArrayInputStream(profilbild),50,50,true,true);
        ImageView imgView = new ImageView(img);
        imgView.setLayoutX(1030.00);
        imgView.setLayoutY(10.00);
        nutzer.getChildren().add(namenlink);

        ChangeFxml(view_path);
        guiPanel = new Pane();
        guiPanel.setLayoutX(200.0);
        guiPanel.setLayoutY(67.0);
        guiPanel.setPrefHeight(1009.0);
        guiPanel.setPrefWidth(1721.0);
        guiPanel.setStyle("-fx-background-color: white; -fx-background-radius: 10 10 10 10;");
        guiPanel.getChildren().add(gui);

        container.getChildren().add(hbox);
        container.getChildren().add(vbox);
        container.getChildren().add(logo);
        container.getChildren().add(guiPanel);
        container.getChildren().add(nutzer);
        container.getChildren().add(imgView);

        show();;
    }

    public Layout(){
    }

    public void startChatSchedule(ChatController chatController){
        Timeline timer = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                chatController.LadeNeueNachrichten();
                            }
                        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public void instanziateMeineKurseButton(){
        meineKurse.setText("Meine Kurse");
        meineKurse.setId("meineKurse");
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                if(getController() instanceof ChatController){
                    ((ChatController) getController()).t.stop();
                }
                instanceLayout("meineKurse.fxml");
                ((MeineKurseController) getController()).setLayout(layout);
            }
        };
        meineKurse.setPrefHeight(67.00);
        meineKurse.setPrefWidth(100.00);
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
                instanceLayout("alleKurse.fxml");
                ((AlleKurseController) getController()).setLayout(layout);
            }
        };
        alleKurse.setPrefHeight(67.00);
        alleKurse.setPrefWidth(100.00);
        alleKurse.setCursor(Cursor.HAND);
        alleKurse.setFont(new Font("System Bold",12.0));
        alleKurse.setOnAction(alleKurseHandler);
        alleKurse.setAlignment(Pos.CENTER);
    }

    public void instanziateKalenderButton(){
        Kalender.setText("Kalender");
        Kalender.setId("kalender");
        EventHandler<ActionEvent> KalenderHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                instanceLayout("Calender.fxml");
                ((CalenderController) getController()).setLayout(layout);
                ((CalenderController) getController()).Initilaize();
            }
        };
        Kalender.setPrefHeight(67.00);
        Kalender.setPrefWidth(100.00);
        Kalender.setCursor(Cursor.HAND);
        Kalender.setFont(new Font("System Bold",12.0));
        Kalender.setOnAction(KalenderHandler);
        Kalender.setAlignment(Pos.CENTER);
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
                instanceLayout("homescreen.fxml");
                ((HomescreenController) getController()).setLayout(layout);
            }
        };
        logo.setOnAction(logoHandler);
    }

    public void instanziereNutzer(){
        if ( Nutzer instanceof Lehrender) {
            namenlink.setText( ((Lehrender) Nutzer).getNutzerId().getName() );
        } else if ( Nutzer instanceof Student) {
            namenlink.setText( ((Student) Nutzer).getNutzer().getName() );
        }

        namenlink.setFont(new Font("System Bold",24.0));
        namenlink.setStyle("-fx-text-fill: white");
        namenlink.setCursor(Cursor.HAND);
        EventHandler<ActionEvent> nutzerHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                instanceLayout("userprofile.fxml");
                ((UserprofilController) getController()).setLayout(layout);
                ((UserprofilController) getController()).nutzerprofilAufrufen(Nutzer,Nutzer);
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

    public Object getNutzer() {
        return Nutzer;
    }
}
