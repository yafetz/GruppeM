package Client.Controller;

import Client.Controller.AlleKurseController;
import Client.Controller.MeineKurseController;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class UserprofilController {
    @FXML
    private Label username;
    @FXML
    private Label mailadresse;
    @FXML
    private Label plz;
    @FXML
    private Label adresse;
    @FXML
    private Label lehrstuhl_oder_matr;
    @FXML
    private Label forschungsgebiet_studienfach;
    @FXML
    private Label number;
    @FXML
    private Label city;


    private Student student;
    private Lehrender lehrender;

    private Object nutzerInstanz;

    private Object vergleichNutzer;
    private Object eigenerNutzer;


    public void initialize() {
    }

    public void nutzerprofilAufrufen (Object eigenerNutzer, Object vergleichNutzer) {
        this.eigenerNutzer = eigenerNutzer;
        this.vergleichNutzer = vergleichNutzer;

        if(vergleichNutzer == eigenerNutzer) {
            //Diese If-Bedingung tritt ein, wenn der Nutzer sich selbst aufruft

            // Sicht eines Lehrenden auf sein eigenes Profil
            if (eigenerNutzer instanceof Lehrender) {
                username.setText(((Lehrender) eigenerNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) eigenerNutzer).getNutzerId().getNachname());
                mailadresse.setText(((Lehrender) eigenerNutzer).getNutzerId().getEmail());
                lehrstuhl_oder_matr.setText(((Lehrender) eigenerNutzer).getLehrstuhl());
                forschungsgebiet_studienfach.setText(((Lehrender) eigenerNutzer).getForschungsgebiet());
                plz.setText(String.valueOf(((Lehrender) eigenerNutzer).getNutzerId().getPlz()));
                adresse.setText(((Lehrender) eigenerNutzer).getNutzerId().getStrasse());
                city.setText(((Lehrender) eigenerNutzer).getNutzerId().getStadt());
            }

            // Sicht eines Studenten auf sein eigenes Profil
            else if(eigenerNutzer instanceof Student) {
                username.setText(((Student) eigenerNutzer).getNutzer().getVorname() +" "+ ((Student) eigenerNutzer).getNutzer().getNachname());
                mailadresse.setText(((Student) eigenerNutzer).getNutzer().getEmail());
                lehrstuhl_oder_matr.setText(String.valueOf(((Student) eigenerNutzer).getMatrikelnummer()));
                forschungsgebiet_studienfach.setText(((Student) eigenerNutzer).getStudienfach());
                plz.setText(String.valueOf(((Student) eigenerNutzer).getNutzer().getPlz()));
                adresse.setText(((Student) eigenerNutzer).getNutzer().getStrasse());
                city.setText(((Student) eigenerNutzer).getNutzer().getStadt());
            }
        }

        // Diese else If tritt ein, wenn der Nutzer auf einen anderen Nutzer klickt
        else if (vergleichNutzer != eigenerNutzer) {
            if (eigenerNutzer instanceof Lehrender) {
                // Sicht eines Lehrenden auf anderen Lehrenden
                if (vergleichNutzer instanceof Lehrender) {
                    username.setText(((Lehrender) vergleichNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) vergleichNutzer).getNutzerId().getNachname());
                    mailadresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getEmail());
                    lehrstuhl_oder_matr.setText(((Lehrender) vergleichNutzer).getLehrstuhl());
                    forschungsgebiet_studienfach.setText(((Lehrender) vergleichNutzer).getForschungsgebiet());
                    plz.setText(String.valueOf(((Lehrender) vergleichNutzer).getNutzerId().getPlz()));
                    adresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getStrasse());
                    city.setText(((Lehrender) vergleichNutzer).getNutzerId().getStadt());
                }
                //Sicht eines Lehrenden auf das Profil eines Studenten
                else if(vergleichNutzer instanceof Student) {
                    username.setText(((Student) vergleichNutzer).getNutzer().getVorname() +" "+ ((Student) vergleichNutzer).getNutzer().getNachname());
                    mailadresse.setText(((Student) vergleichNutzer).getNutzer().getEmail());
                    lehrstuhl_oder_matr.setText(String.valueOf(((Student) vergleichNutzer).getMatrikelnummer()));
                    forschungsgebiet_studienfach.setText(((Student) vergleichNutzer).getStudienfach());
                    plz.setText(String.valueOf(((Student) vergleichNutzer).getNutzer().getPlz()));
                    adresse.setText(((Student) vergleichNutzer).getNutzer().getStrasse());
                    city.setText(((Student) vergleichNutzer).getNutzer().getStadt());

                }
            }
            else if(eigenerNutzer instanceof Student) {
                // Sicht eines Studenten auf das Profil eines Lehrenden
                if (vergleichNutzer instanceof Lehrender) {
                    username.setText(((Lehrender) vergleichNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) vergleichNutzer).getNutzerId().getNachname());
                    mailadresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getEmail());
                    lehrstuhl_oder_matr.setText(((Lehrender) vergleichNutzer).getLehrstuhl());
                    forschungsgebiet_studienfach.setText(((Lehrender) vergleichNutzer).getForschungsgebiet());
                }
                //Sicht eines Studenten auf das Profil eines Studenten
                else if(vergleichNutzer instanceof Student) {
                    username.setText(((Student) vergleichNutzer).getNutzer().getVorname() +" "+ ((Student) vergleichNutzer).getNutzer().getNachname());
                    mailadresse.setText(((Student) vergleichNutzer).getNutzer().getEmail());
                }
            }
        }
    }


    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
    }
}
