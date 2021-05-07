package Client.Controller;

import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Userprofil {
    @FXML
    private Label username;
    @FXML
    private Label mailadresse;
    @FXML
    private Label matr;
    @FXML
    private Label plz;
    @FXML
    private Label adresse;
    @FXML
    private Label lehrstuhl;
    @FXML
    private Label forschungsgebiet;
    @FXML
    private Label city;
    @FXML
    private Label number;

    private Student student;
    private Lehrender lehrender;



    public void initialize() {
    }
     public void setStudent(Student student) {
        this.student = student;
         username.setText(student.getNutzer().getVorname() + " " + student.getNutzer().getNachname());
         mailadresse.setText(student.getNutzer().getEmail());
         matr.setText(String.valueOf(student.getMatrikelnummer()));
         plz.setText(String.valueOf(student.getNutzer().getPlz()));
         adresse.setText(student.getNutzer().getStrasse());
         number.setText(String.valueOf(student.getNutzer().getHausnummer()));
         lehrstuhl.setVisible(false);
         forschungsgebiet.setVisible(false);

     }
    public void setLehrender(Lehrender lehrender) {
        this.lehrender = lehrender;
        username.setText(lehrender.getNutzer().getVorname() + " " + lehrender.getNutzer().getNachname());
        mailadresse.setText(lehrender.getNutzer().getEmail());
        matr.setVisible(false);
        plz.setText(String.valueOf(lehrender.getNutzer().getPlz()));
        adresse.setText(lehrender.getNutzer().getStrasse());
        number.setText(String.valueOf(lehrender.getNutzer().getHausnummer()));
        lehrstuhl.setText(lehrender.getLehrstuhl());
        forschungsgebiet.setText(lehrender.getForschungsgebiet());

    }


    public void meineKurseAufrufen(ActionEvent actionEvent) {
    }

    public void alleKurseAufrufen(ActionEvent actionEvent) {
    }
}
