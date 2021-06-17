package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Quiz;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class QuizUebersichtController {
    @FXML
    private Label title;
    @FXML
    private Button createQuizButton;
    private Object nutzer;

    @FXML
    private TableView<Quiz> quizTable;
    @FXML
    public TableColumn<Quiz, String> quizTitel;


    private Lehrveranstaltung lehrveranstaltung;
    private Layout layout;
    private Object user;


    public void quizSeiteAufrufen(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung = lehrveranstaltung;


        if (nutzer != null) {
            if (nutzer instanceof Lehrender) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());


            } else if (nutzer instanceof Student) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
                createQuizButton.setVisible(false);
            }

        }
    }
    public void quizzeAufrufen(Object user) {
         this.user = user;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if (user instanceof Lehrender) {

            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Lehrender) user).getNutzerId().getId())).build();
        }
        if (user instanceof Student) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Student) user).getNutzer().getId())).build();
            System.out.println(((Student) user).getId());
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            java.util.List<Quiz> quiz = mapper.readValue(response.body(), new TypeReference<>() {});




            quizTitel.setCellValueFactory(new PropertyValueFactory<Quiz,String>("titel"));


//            Angelehnt an: https://stackoverflow.com/questions/35562037/how-to-set-click-event-for-a-cell-of-a-table-column-in-a-tableview
            quizTitel.setCellFactory(tablecell -> {
                TableCell<Quiz, String> cell = new TableCell<>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {

                            }
                        }
                );
                return cell;
            });

//            ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<Quiz> kursListe = FXCollections.observableList(quiz);
            quizTable.setItems(kursListe);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


        public void setNutzer (Object nutzer){
        }

        public void pressedCreateQuizButton (ActionEvent actionEvent) {
            actionEvent.consume();
            layout.instanceLayout("createQuiz.fxml");
            ((CreateQuizController) layout.getController()).setNutzer(nutzer,lehrveranstaltung);
        }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
}
