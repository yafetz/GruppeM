package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class LehrveranstaltungBearbeitenController {

@FXML
    private TextField titel;
    @FXML
    private TextField semesters;
    @FXML
    private Button finished;
    @FXML
    private ChoiceBox typVorlesung;

    private Layout layout;
    private Lehrveranstaltung lehrveranstaltung;


    public TextField getTitel() {
        return titel;
    }

    public void setTitel(TextField titel) {
        this.titel = titel;
    }

    public TextField getSemesters() {
        return semesters;
    }

    public void setSemesters(TextField semesters) {
        this.semesters = semesters;
    }

    public Button getFinished() {
        return finished;
    }

    public void setFinished(Button finished) {
        this.finished = finished;
    }

    public ChoiceBox getTypVorlesung() {
        return typVorlesung;
    }

    public void setTypVorlesung(ChoiceBox typVorlesung) {
        this.typVorlesung = typVorlesung;
    }




    public void bearbeitenPressedButton(ActionEvent actionEvent) {
    }
}
