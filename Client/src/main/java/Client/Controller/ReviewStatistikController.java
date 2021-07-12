package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ReviewStatistikController {

    @FXML private TableColumn<ReviewAnswer, Integer> antwortNrCol;
    @FXML private TableColumn<ReviewAnswer, String> antwortCol;
    @FXML private TableColumn<ReviewAnswer, Integer> anzahlCol;
    @FXML private TableColumn<ReviewAnswer, String> prozentCol;
    @FXML private TableColumn<ReviewQuestion, Long> frageIdCol;
    @FXML private TableColumn<ReviewQuestion, String> frageCol;
    @FXML private TableView<ReviewQuestion> reviewFragenTableView;
    @FXML private TableView<AntwortTableData> reviewAntwortenTableView;
    @FXML private PieChart antwortenPieChart;
    @FXML public Label statistikTitelLabel;
    @FXML private Label teilnehmerLabel;
    @FXML private Button alleBtn;
    @FXML private Button bestandenBtn;
    @FXML private Button durchgefallenBtn;

    private Layout layout;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Review review;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
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
        statistikTitelLabel.setText("Bewertungsstatistik der Lehrveranstaltung " + lehrveranstaltung.getTitel());
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public void allePressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        teilnehmerLabel.setText("Alle Teilnehmer");

    }

    public void bestandenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        teilnehmerLabel.setText("Nur Teilnehmer, die bestanden haben");

    }

    public void durchgefallenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        teilnehmerLabel.setText("Nur Teilnehmer, die nicht bestanden haben");

    }

    public void populateFragenTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/alleFragen/" + lehrveranstaltung.getId())).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<ReviewQuestion> fragenliste = mapper.readValue(response.body(), new TypeReference<List<ReviewQuestion>>() {});
            frageIdCol.setCellValueFactory(new PropertyValueFactory<ReviewQuestion,Long>("id") );
            frageCol.setCellValueFactory(new PropertyValueFactory<ReviewQuestion,String>("question"));

            frageCol.setCellFactory(tablecell -> {
                TableCell<ReviewQuestion,String> cell = new TableCell<ReviewQuestion, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                populateAntwortenTableView(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });
            ObservableList<ReviewQuestion> obsLv = FXCollections.observableList(fragenliste);
            reviewFragenTableView.setItems(obsLv);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void populateAntwortenTableView(long frageId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/alleAntworten/" + frageId)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<ReviewAnswer> antwortenliste = mapper.readValue(response.body(), new TypeReference<List<ReviewAnswer>>() {});
            List<AntwortTableData> tableData = new ArrayList<>();
            for (int i = 0; i < antwortenliste.size(); i++) {
                long antwortId = antwortenliste.get(i).getId();


                int anzahl = 0;
                String prozent = "";
                AntwortTableData eintrag = new AntwortTableData(i, antwortenliste.get(i).getAnswer(), anzahl, prozent);
                tableData.add(eintrag);
            };



            ObservableList<AntwortTableData> obsList = FXCollections.observableList(tableData);
            reviewAntwortenTableView.setItems(obsList);


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class AntwortTableData {
        private int number;
        private String answer;
        private int anzahl;
        private String prozent;

        public AntwortTableData(int number, String answer, int anzahl, String prozent) {
            this.number = number;
            this.answer = answer;
            this.anzahl = anzahl;
            this.prozent = prozent;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public int getAnzahl() {
            return anzahl;
        }

        public void setAnzahl(int anzahl) {
            this.anzahl = anzahl;
        }

        public String getProzent() {
            return prozent;
        }

        public void setProzent(String prozent) {
            this.prozent = prozent;
        }
    }
}
