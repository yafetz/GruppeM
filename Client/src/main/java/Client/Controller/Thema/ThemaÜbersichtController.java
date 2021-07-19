package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.Student;
import Client.Modell.Thema;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ThemaÜbersichtController {
    @FXML
    public Button literaturHinzufuegen;
    @FXML
    public Label titel;
    @FXML
    public Label beschreibung;
    @FXML
    public VBox literaturliste;

    public Layout layout;
    public Thema thema;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        if(layout.getNutzer() instanceof Student){
            literaturHinzufuegen.setVisible(false);
        }
    }

    public void setThema(Thema thema){
        titel.setText(thema.getTitel());
        beschreibung.setText(thema.getBeschreibung());
        this.thema = thema;
        //Lade alle Literaturen

    }

    public void LadeAlleLiteraturenZumThema(){
    }

    public void LiteraturHinzufuegen(ActionEvent actionEvent) {
        //Weiterleitung zu Literatur auswahl Seite
        layout.instanceLayout("LiteraturHinzufügen.fxml");
        ((LiteraturHinzufügenController) layout.getController()).setLayout(layout);
        ((LiteraturHinzufügenController) layout.getController()).setThema(thema);
    }
}
