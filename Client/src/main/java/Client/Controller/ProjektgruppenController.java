package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Projektgruppe;
import Client.Modell.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProjektgruppenController {
    @FXML
    private Label pglisteseitentitel_label;
    @FXML
    private Button pgErstellen_btn;
    @FXML
    private TextField suchen_txtfield;
    @FXML
    private TableView<Projektgruppe> pgListe_tableview;
    @FXML
    private TableColumn<Projektgruppe, String> pgTitel_col;
    @FXML
    private TableColumn<Projektgruppe, Integer> nrMitglieder_col;
    @FXML
    private TableColumn<Projektgruppe, Long> pgId_col;
    @FXML
    private Label beitretenPgTitel_label;
    @FXML
    private Label beitretenLvTitel_label;
    @FXML
    private Button beitreten_btn;
    @FXML
    private Button zurueck_btn;
    @FXML
    private Label erstellenLvTitel_label;
    @FXML
    private TextField pgTitel_txtfield;
    @FXML
    private TableView<Student> studentenliste_tableview;
    @FXML
    private TableColumn<Student, Boolean> checkbox_col;
    @FXML
    private TableColumn<Student, String> studentenname_col;
    @FXML
    private TableColumn<Student, Integer> matrnr_col;
    @FXML
    private Button erstellen_btn;


    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;

    public void setPageTitel(String titel) {
        pglisteseitentitel_label.setText(titel);
    }

    public void setErstellenLvTitel_label(String lehrveranstaltungstitel) {
        erstellenLvTitel_label.setText("Lehrveranstaltung " + lehrveranstaltungstitel);
    }

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }



    // "Neue Projektgruppe erstellen"-Button auf der Seite der Projektgruppenliste
    public void pgListeErstellenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        Layout neuePGerstellen = new Layout("projektgruppeErstellen.fxml", (Stage) pgErstellen_btn.getScene().getWindow(), nutzer);


        if (neuePGerstellen.getController() instanceof ProjektgruppenController) {
            ((ProjektgruppenController) neuePGerstellen.getController()).setErstellenLvTitel_label(lehrveranstaltung.getTitel());
        }

    }

    public void beitretenPressedButton(ActionEvent actionEvent) {
    }

    // "Zurück zur Projektgruppenliste"-Button auf der Projektgruppenbeitrittsseite
    public void zurueckPressedButton(ActionEvent actionEvent) {
    }

    // "Projektgruppe erstellen"-Button auf der Seite zur Erstellung einer neuen Projektgruppe
    public void erstellenPressedButton(ActionEvent actionEvent) {
    }
}
