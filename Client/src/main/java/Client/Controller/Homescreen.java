package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import org.kordamp.bootstrapfx.BootstrapFX;

public class Homescreen {

    @FXML
    private Button meineKurse;
    @FXML
    private Button alleKurse;
    @FXML
    private Button nutzerName;

    public void initialize() {
        meineKurse.setText("Meine Kurse");

    }



    public void meineKurseAufrufen(ActionEvent event) {
        //TODO

    }

    public void alleKurseAufrufen(ActionEvent event) {
        //TODO
    }

    public void eigeneProfilSeiteAufrufen(ActionEvent event) {
        //TODO
    }

}

