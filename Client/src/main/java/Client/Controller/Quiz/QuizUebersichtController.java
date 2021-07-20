package Client.Controller.Quiz;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class QuizUebersichtController {
    @FXML
    public Button XMLQuizButton;
    @FXML
    public Label quizerstellen_LvTitel_Label;
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
                XMLQuizButton.setVisible(false);
            }

        }
        quizzeAufrufen();
    }

    public void quizzeAufrufen() {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/quiz/alle/"+ lehrveranstaltung.getId())).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            List<Quiz> quiz = mapper.readValue(response.body(), new TypeReference<List<Quiz>>() {});

            quizTitel.setCellValueFactory(new PropertyValueFactory<Quiz,String>("titel"));

                quizTitel.setCellFactory(tablecell -> {
                            TableCell<Quiz, String> cell = new TableCell<Quiz, String>() {
                                @Override
                                protected void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    setText(empty ? null : item);
                                }
                            };
                            cell.setCursor(Cursor.HAND);
                            cell.setOnMouseClicked(e -> {
                                if (!cell.isEmpty()) {
                                    //Weiterleitung zu Quiz bearbeiten
                                    if(nutzer instanceof Student) {
                                        layout.instanceLayout("quizBearbeiten.fxml");
                                        ((QuizBearbeitenController) layout.getController()).setQuiz(cell.getTableRow().getItem());
                                        ((QuizBearbeitenController) layout.getController()).setNutzer(nutzer);
                                        ((QuizBearbeitenController) layout.getController()).setUpQuiz();
                                    }
                                    else if(nutzer instanceof Lehrender) {
                                        layout.instanceLayout("statistik.fxml");
                                        ((TeststatistikController) layout.getController()).setQuiz(cell.getTableRow().getItem());
                                        ((TeststatistikController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                                        ((TeststatistikController) layout.getController()).quizname_Label.setText("\"" + cell.getTableRow().getItem().getTitel() + "\"");
                                        ((TeststatistikController) layout.getController()).setNutzer(nutzer);
                                        ((TeststatistikController) layout.getController()).setLayout(layout);
                                        ((TeststatistikController) layout.getController()).teilnahme();
                                        ((TeststatistikController) layout.getController()).showPieChart();
                                        ((TeststatistikController) layout.getController()).populateTableviewVersuch();
                                        ((TeststatistikController) layout.getController()).populateTableviewKorrekt();
                                    }
                                }
                            });
                            return cell;
                        });

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
        ((CreateQuizController) layout.getController()).setLayout(layout);
        ((CreateQuizController) layout.getController()).setNutzer(nutzer,lehrveranstaltung);
        ((CreateQuizController) layout.getController()).quiz_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel());
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void pressedXMLQuiz(ActionEvent actionEvent) {
        layout.instanceLayout("quizXMLUpload.fxml");
        ((QuizXMLUploadController) layout.getController()).setLayout(layout);
        ((QuizXMLUploadController) layout.getController()).setNutzerInstanz(nutzer);
        ((QuizXMLUploadController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
    }
}
