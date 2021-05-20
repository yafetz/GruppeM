package Client.Controller;

import Client.Layouts.Layout;
import javafx.fxml.FXML;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LehrveranstaltungsuebersichtsseiteController {
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;

    @FXML
    private Button teilnehmerListe;
    private Object lehrveranstaltung;
    private Object nutzer;

    @FXML
    private void teilnehmerListe(ActionEvent event){

        Layout lehrveranstaltungBeitreten = new Layout("teilnehmerliste.fxml", (Stage) teilnehmerListe.getScene().getWindow(),nutzer);
        if(lehrveranstaltungBeitreten.getController() instanceof TeilnehmerListeController){
            long veranstaltungId = ((Lehrveranstaltung) lehrveranstaltung).getId();


            ((TeilnehmerListeController) lehrveranstaltungBeitreten.getController()).setId(veranstaltungId);
            ((TeilnehmerListeController) lehrveranstaltungBeitreten.getController()).setNutzerInstanz(nutzer);
            ((TeilnehmerListeController)  lehrveranstaltungBeitreten.getController()).setLehrveranstaltung(((Lehrveranstaltung) lehrveranstaltung));

        }
    }



    public void initialize() {
    }

    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        Stage stage = (Stage) materialUpload.getScene().getWindow();
        Layout homeScreen = null;
        homeScreen = new Layout("lehrmaterialUpload.fxml", stage, nutzer);
        if (homeScreen.getController() instanceof LehrmaterialController) {
            ((LehrmaterialController) homeScreen.getController()).setNutzerInstanz(nutzer);
            ((LehrmaterialController) homeScreen.getController()).setLehrveranstaltung(lehrveranstaltung);
        }
    }

    public void uebersichtsseiteAufrufen(Object nutzer, Object lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung= lehrveranstaltung;

        if (nutzer !=null) {
            if (nutzer instanceof Lehrender) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
                materialUpload.setText("Lehrmaterial hochladen");
            }
            else if(nutzer instanceof Student) {
                //title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
                materialUpload.setVisible(false);
            }

        }

    }
}
