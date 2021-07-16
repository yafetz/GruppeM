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

 /*   public void populateLiteraturNochVorhanden() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/studteilnehmer/" + lehrveranstaltung.getId())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Student> students = mapper.readValue(response.body(), new TypeReference<List<Student>>() {});

            addCheckBoxToTable("teilnehmer");
            studentenliste_tableview.setEditable(true);
            studentenname_col.setCellValueFactory(new PropertyValueFactory<Literatur, String>("Title"));

            ObservableList<Student> obsStud = FXCollections.observableList(students);
            studentenliste_tableview.setItems(obsStud);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addCheckBoxToTable(String table) {
        TableColumn<Student, Void> colBtn = new TableColumn("Selected");
        Callback<TableColumn<Student, Void>, TableCell<Student, Void>> cellFactory = new Callback<TableColumn<Student, Void>, TableCell<Student, Void>>() {
            @Override
            public TableCell<Student, Void> call(final TableColumn<Student, Void> param) {
                final TableCell<Student, Void> cell = new TableCell<Student, Void>() {
                    private final CheckBox checkBox = new CheckBox();
                    {
                        checkBox.setOnAction((ActionEvent event) -> {
                            Student student = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {        // wenn Häckchen gesetzt wird, füge Student zu der Liste der ausgewählten Studenten hinzu
                                selectedStudentIds.add((long)student.getId());
                            } else {                            // wenn Häckchen entfernt wird, entferne den Studenten von der Liste
                                for (int i = 0; i < selectedStudentIds.size(); i++) {
                                    if (selectedStudentIds.get(i) == student.getId()) {
                                        selectedStudentIds.remove(i);
                                    }
                                }
                            }
                        });
                    }
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(checkBox);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        if (table.equals("teilnehmer")) {
            studentenliste_tableview.getColumns().add(colBtn);
        }
        if (table.equals("neue")) {
            neue_TableView.getColumns().add(colBtn);
        }
    }*/
}
