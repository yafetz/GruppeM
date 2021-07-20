package Client.Controller;

import Client.Controller.Auth.LoginController;
import Client.Layouts.Layout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class SettingController {
    @FXML
    private TextField Stunde;
    @FXML
    private TextField Minute;
    @FXML
    private DatePicker date;
    @FXML
    private Button back;
    @FXML
    private Button einstellen;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void backPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("login.fxml");
        ((LoginController) layout.getController()).setLayout(layout);
    }

    public void einstellenPressedButton(ActionEvent actionEvent) {
        String datum = "";
        String monat = "";
        if (date.getValue() == null) {

            String jahr = String.valueOf(LocalDateTime.now().getYear());

            if (LocalDateTime.now().getMonthValue()>= 0 &&LocalDateTime.now().getMonthValue() <= 9) {
                monat = "0"+String.valueOf(LocalDateTime.now().getMonthValue());
            }
            else {
                monat = String.valueOf(LocalDateTime.now().getMonthValue());
            }
            String tag = String.valueOf(LocalDateTime.now().getDayOfMonth());
            datum = jahr + "-" + monat + "-" + tag;
        }
        else {
            datum = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        String hour= "";
        String minute= " ";


        //Stunde der Uhrzeit richtig formatieren
        if(Stunde.getText().equals("Stunde")) {
            if (LocalDateTime.now().getHour()>=0 && LocalDateTime.now().getHour() <= 9) {
                hour = "0"+ String.valueOf(LocalDateTime.now().getHour());
            }
            else {
                hour = String.valueOf(LocalDateTime.now().getHour());
            }
        }
        else {
            if (Integer.valueOf(Stunde.getText()) >= 0 && Integer.valueOf(Stunde.getText()) <= 24) {
                if (Integer.valueOf(Stunde.getText())>=0 && Integer.valueOf(Stunde.getText())<= 9) {
                    hour = "0" +Stunde.getText();
                }
                else {
                    hour = Stunde.getText();
                }
            } else {
                //Alert
            }
        }

//Minute der Uhrzeit richtig formatieren
        if(Minute.getText().equals("Minute")) {
            if (LocalDateTime.now().getMinute()>= 0 && LocalDateTime.now().getMinute()<= 9) {
                minute ="0"+ String.valueOf(LocalDateTime.now().getMinute());
            }
            else {
                minute = String.valueOf(LocalDateTime.now().getMinute());
            }
        }
        else {
            if (Integer.valueOf(Minute.getText()) >= 0 && Integer.valueOf(Minute.getText()) <= 59) {
                if (Integer.valueOf(Minute.getText()) >= 0 && Integer.valueOf(Minute.getText())<= 9) {
                    minute = "0"+ Minute.getText();
                }
                else {
                    minute = Minute.getText();
                }
            } else {
                //Alert
            }
        }

        String datumunduhrzeit = datum + "T" + hour + ":" + minute + ":00Z" ; //"2021-12-22T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(datumunduhrzeit), ZoneId.of("UTC"));
        LocalDateTime aktuell = LocalDateTime.now(clock);


        layout.instanceAuth("login.fxml");
        ((LoginController) layout.getController()).setLayout(layout);
        ((LoginController) layout.getController()).setJetzt(aktuell);



    }
}
