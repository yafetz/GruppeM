package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TeilnehmerListeController {

    @FXML
    private TableView<Nutzer> teilnehmerTabelle;

    @FXML
    private TableColumn<Nutzer, String> Vorname;

    @FXML
    private TableColumn<Nutzer, String> Nachname;

    @FXML
    private TableColumn<Nutzer, String> Rolle;
    long id;
    private Object nutzerId;
    private Lehrveranstaltung lehrveranstaltung;


    public void populateTableView(){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/"+id)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            JSONArray jsonObject = new JSONArray(response.body());



           Vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
           Nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));

            Rolle.setCellValueFactory(new PropertyValueFactory<>("rolle"));


            // ObjectMapper mapper = new ObjectMapper();
           // List<TeilnehmerListeController> nutzerList = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListeController>>() {});

            for(int i=0;i<jsonObject.length();i++){
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("nutzerId");
                System.out.println(nutzer.get("vorname")+ " "+ nutzer.get("nachname"));

                Nutzer nutzer1 = new Nutzer();
                if (nutzerId instanceof Lehrender){
                    if(nutzer.get("vorname").equals(((Lehrender)nutzerId).getVorname()) && nutzer.get("nachname").equals(((Lehrender)nutzerId).getNachname())){
                        nutzer1.setRolle(nutzer.getString("rolle")+" (Ich)");


                    }
                    else{
                        nutzer1.setRolle(nutzer.getString("rolle"));
                    }

                }

                if (nutzerId instanceof Student){
                    if(nutzer.get("vorname").equals(((Student)nutzerId).getVorname()) && nutzer.get("nachname").equals(((Student)nutzerId).getNachname())){
                        nutzer1.setRolle(nutzer.getString("rolle")+" (Ich)");


                    }
                    else{
                        nutzer1.setRolle(nutzer.getString("rolle"));
                    }

                }

                nutzer1.setVorname(nutzer.getString("vorname"));
                nutzer1.setNachname(nutzer.getString("nachname"));

                teilnehmerTabelle.getItems().add(nutzer1);
            }

            Vorname.setCellFactory(tablecell -> {
                TableCell<Nutzer, String> cell = new TableCell<Nutzer, String>(){
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
            if (nutzerId instanceof Lehrender) {
                long lehrId = ((Lehrender) nutzerId).getNutzerId().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/"+ lehrveranstaltungId + "&"+lehrId)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println("Instanz Lehrender "+memberResponse.body());

                if(memberResponse.body().equals("true")){
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) teilnehmerTabelle.getScene().getWindow(),nutzerId);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerId,lehrveranstaltung);
                    }
                }
                else {
                    System.out.println("LehrveranstaltungsId   "+lehrveranstaltungId);
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) teilnehmerTabelle.getScene().getWindow(),nutzerId);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerId,lehrveranstaltung);
                    }
                }
            }
            if (nutzerId instanceof Student) {

                long id = ((Student) nutzerId).getNutzer().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println("Student Instanz "+memberResponse.body());

                if(memberResponse.body().equals("true")){
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) teilnehmerTabelle.getScene().getWindow(),nutzerId);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerId,lehrveranstaltung);
                    }
                }
                else{
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) teilnehmerTabelle.getScene().getWindow(),nutzerId);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(nutzerId,lehrveranstaltung);
                    }
                }
            }

//            System.out.println(lehrveranstaltung.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getNutzer() {
        return nutzerId;
    }

    public void setNutzerInstanz(Object nutzer) {
        this.nutzerId = nutzer;
        System.out.println(id);
        populateTableView();

    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }
}
