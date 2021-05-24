package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import Client.Modell.TeilnehmerListe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class MeineKurseController {

    @FXML
    private TableView<Lehrveranstaltung> meineLv;
    @FXML
    public TableColumn<Lehrveranstaltung, Long> col_LvId;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvTitel;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvSemester;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvArt;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvLehrende;
    @FXML
    private Button addCourse;

    private Object nutzerInstanz;


    public void initialize() {
        if(nutzerInstanz instanceof Student){
            addCourse.setVisible(false);
        }
    }

    public void populateTableView() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if (nutzerInstanz instanceof Lehrender) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Lehrender) nutzerInstanz).getNutzerId().getId())).build();
        }
        if (nutzerInstanz instanceof Student) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Student) nutzerInstanz).getNutzer().getId())).build();
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            mapping data in response.body() to a list of teilnehmerliste-objects
            ObjectMapper mapper = new ObjectMapper();

//            System.out.println(response.body());

            List<TeilnehmerListe> teilnehmerListe = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListe>>() {});
            List<Lehrveranstaltung> lehrveranstaltungen = new LinkedList<>();

            for(TeilnehmerListe teilnehmerListe1 : teilnehmerListe) {
                lehrveranstaltungen.add(teilnehmerListe1.getLehrveranstaltung());
            }

//            set property of each column of the tableview
            col_LvId.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,Long>("id"));
            col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Titel"));
            col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Semester"));
            col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Art"));
            col_LvLehrende.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("lehrenderName"));

//            Angelehnt an: https://stackoverflow.com/questions/35562037/how-to-set-click-event-for-a-cell-of-a-table-column-in-a-tableview
            col_LvTitel.setCellFactory(tablecell -> {
                TableCell<Lehrveranstaltung, String> cell = new TableCell<Lehrveranstaltung, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToCourseOverview(cell.getTableRow().getItem().getId());

                            }
                        }
                );
                return cell;
            });
            // ObservableList is required to populate the table meineLv using .setItems()
            ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(lehrveranstaltungen);
            meineLv.setItems(obsLv);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void redirectToCourseOverview(Integer lehrveranstaltungId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/"+lehrveranstaltungId)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Lehrveranstaltung lehrveranstaltung = mapper.readValue(response.body(), Lehrveranstaltung.class);
//            TODO Weiterleitung zu Übersichtsseite des Kurses
            //  HttpRequest requestisMember = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/"+lehrveranstaltungId)).build();
            //Layout layout = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) namenLink.getScene().getWindow());

//            Platzhalter bis dahin:
            HttpResponse<String> memberResponse;
            if (nutzerInstanz instanceof Lehrender) {
                long lehrId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/"+ lehrveranstaltungId + "&"+lehrId)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println("Instanz Lehrender "+memberResponse.body());

                if(memberResponse.body().equals("true")){
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) meineLv.getScene().getWindow(),nutzerInstanz);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);
                    }
                }
                else {
                    System.out.println("LehrveranstaltungsId   "+lehrveranstaltungId);
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) meineLv.getScene().getWindow(),nutzerInstanz);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);
                    }
                }
            }
            if (nutzerInstanz instanceof Student) {

                long id = ((Student) nutzerInstanz).getNutzer().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println("Student Instanz "+memberResponse.body());

                if(memberResponse.body().equals("true")){
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) meineLv.getScene().getWindow(),nutzerInstanz);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);
                    }
                }
                else{
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) meineLv.getScene().getWindow(),nutzerInstanz);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
        populateTableView();

        if (this.nutzerInstanz !=null) {
            if(this.nutzerInstanz instanceof Student) {
                addCourse.setVisible(false);
            }

        }
    }

    public void AddCourse(ActionEvent actionEvent) {
        Layout erstelleLehrveranstaltung = new Layout("lehrveranstaltungErstellen.fxml",(Stage) addCourse.getScene().getWindow(),nutzerInstanz);
        if(erstelleLehrveranstaltung.getController() instanceof LehrveranstaltungErstellenController) {
            ((LehrveranstaltungErstellenController) erstelleLehrveranstaltung.getController()).setNutzerInstanz(nutzerInstanz);
        }
    }
}
