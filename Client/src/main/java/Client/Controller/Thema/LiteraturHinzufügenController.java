package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.Student;
import Client.Modell.Thema;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LiteraturHinzufügenController {

    @FXML
    public Label thema_titel;
    @FXML
    public TableView Literatur_noch_vorhanden_table;
    @FXML
    public TableColumn literatur_noch_vorhanden_titel_column;
    @FXML
    public Button addLiteratur;
    @FXML
    public TableView literatur_jetzt_table;
    @FXML
    public TableColumn literatur_jetzt_titel_column;
    @FXML
    public Button bibtex_button;

    public Layout layout;
    public Thema thema;

    public void setThema(Thema thema){
        this.thema = thema;
        thema_titel.setText(thema.getTitel());
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void LiteraturHinzufügen(ActionEvent actionEvent) {
    }

    public void BibtexFileUpload(ActionEvent actionEvent) {
        layout.instanceLayout("LiteraturUpload.fxml");
        ((LiteraturUploadController) layout.getController()).setLayout(layout);
        ((LiteraturUploadController) layout.getController()).setThema(thema);
    }
}
