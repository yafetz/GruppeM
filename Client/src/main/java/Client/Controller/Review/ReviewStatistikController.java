package Client.Controller.Review;

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

    @FXML private TableColumn<AntwortTableData, Integer> antwortNrCol;
    @FXML private TableColumn<AntwortTableData, String> antwortCol;
    @FXML private TableColumn<AntwortTableData, Integer> anzahlCol;
    @FXML private TableColumn<AntwortTableData, Double> prozentCol;
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
    private Long angeklickteFrageId = (long) -1;

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
        if (angeklickteFrageId != -1) {
            populateAntwortenTableView(angeklickteFrageId);
        }
    }

    public void bestandenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        teilnehmerLabel.setText("Nur Teilnehmer, die bestanden haben");
        if (angeklickteFrageId != -1) {
            populateAntwortenTableView(angeklickteFrageId);
        }
    }

    public void durchgefallenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        teilnehmerLabel.setText("Nur Teilnehmer, die nicht bestanden haben");
        if (angeklickteFrageId != -1) {
            populateAntwortenTableView(angeklickteFrageId);
        }
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
                                angeklickteFrageId = cell.getTableRow().getItem().getId();
                                populateAntwortenTableView(angeklickteFrageId);

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

                //hier wird angefragt, wie oft die Antwort gewählt wurde, je nachdem welche Teilnehmer betrachtet werden

                if (teilnehmerLabel.getText().contentEquals("Alle Teilnehmer")) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/teilnehmerAlle/antwortId=" + antwortId)).build();
                }
                else if (teilnehmerLabel.getText().contentEquals("Nur Teilnehmer, die bestanden haben")) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/teilnehmerBestanden/antwortId=" + antwortId)).build();
                }
                else if (teilnehmerLabel.getText().contentEquals("Nur Teilnehmer, die nicht bestanden haben")) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/teilnehmerNichtBestanden/antwortId=" + antwortId)).build();
                }
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int anzahl = Integer.valueOf(response.body());

                //hier wird angefragt, wie oft die Frage insgesamt beantwortet wurde, je nachdem welche Teilnehmer betrachtet werden

                if (teilnehmerLabel.getText().contentEquals("Alle Teilnehmer")) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/teilnehmerAlle/frageId=" + frageId)).build();
                }
                else if (teilnehmerLabel.getText().contentEquals("Nur Teilnehmer, die bestanden haben")) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/teilnehmerBestanden/frageId=" + frageId)).build();
                }
                else if (teilnehmerLabel.getText().contentEquals("Nur Teilnehmer, die nicht bestanden haben")) {
                    request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/teilnehmerNichtBestanden/frageId=" + frageId)).build();
                }
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                int anzahlInsgesamt = Integer.valueOf(response.body());


                double prozent = (int)( (  ((double)anzahl/anzahlInsgesamt)*100)  *100) /100.0;

                AntwortTableData eintrag = new AntwortTableData(i+1, antwortenliste.get(i).getAnswer(), anzahl, prozent);
                tableData.add(eintrag);
            };


            antwortNrCol.setCellValueFactory(new PropertyValueFactory<AntwortTableData,Integer>("number"));
            antwortCol.setCellValueFactory(new PropertyValueFactory<AntwortTableData,String>("antwort"));
            anzahlCol.setCellValueFactory(new PropertyValueFactory<AntwortTableData,Integer>("anzahl"));
            prozentCol.setCellValueFactory(new PropertyValueFactory<AntwortTableData,Double>("prozent"));

            ObservableList<AntwortTableData> obsList = FXCollections.observableList(tableData);
            reviewAntwortenTableView.setItems(obsList);

            ObservableList<PieChart.Data> piechartdata = FXCollections.observableArrayList();
            for(int i = 0; i < tableData.size(); i++) {
                piechartdata.add(new PieChart.Data(tableData.get(i).getAntwort(), tableData.get(i).getProzent()));
            }
            antwortenPieChart.setData(piechartdata);
            antwortenPieChart.setLabelsVisible(true);
            antwortenPieChart.setLabelLineLength(10);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public class AntwortTableData {
        private int number;
        private String antwort;
        private int anzahl;
        private double prozent;

        public AntwortTableData(int number, String antwort, int anzahl, double prozent) {
            this.number = number;
            this.antwort = antwort;
            this.anzahl = anzahl;
            this.prozent = prozent;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getAntwort() {
            return antwort;
        }

        public void setAntwort(String antwort) {
            this.antwort = antwort;
        }

        public int getAnzahl() {
            return anzahl;
        }

        public void setAnzahl(int anzahl) {
            this.anzahl = anzahl;
        }

        public double getProzent() {
            return prozent;
        }

        public void setProzent(double prozent) {
            this.prozent = prozent;
        }
    }
}
