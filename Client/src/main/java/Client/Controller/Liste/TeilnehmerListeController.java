package Client.Controller.Liste;

import Client.Controller.NutzerProfil.UserprofilController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Nutzer;
import Client.Modell.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TeilnehmerListeController {

    @FXML
    private TableView<Nutzer> teilnehmerTabelle;
    @FXML
    private TableColumn<Nutzer, String> Vorname;
    @FXML
    private TableColumn<Nutzer, String> Nachname;
    @FXML
    private TableColumn<Nutzer, String> Rolle;
    @FXML
    private TableColumn<Nutzer, Integer> teilnehmerid;
    long id;
    private Object nutzerId;
    private Lehrveranstaltung lehrveranstaltung;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setNutzerInstanz(layout.getNutzer());
    }

    public void populateTableView(){

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/"+id)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());

            JSONArray jsonObject = new JSONArray(response.body());

            Vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
            Nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
            teilnehmerid.setCellValueFactory(new PropertyValueFactory<>("id"));
            Rolle.setCellValueFactory(new PropertyValueFactory<>("rolle"));

            for(int i=0;i<jsonObject.length();i++){
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("nutzerId");

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
                nutzer1.setId(nutzer.getInt("id"));

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
                                redirectToUserprofile(cell.getTableRow().getItem().getId());
//                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
                            }
                        }
                );

                return cell;
            });
            Nachname.setCellFactory(tablecell -> {
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
                                redirectToUserprofile(cell.getTableRow().getItem().getId());
//                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
                            }
                        }
                );

                return cell;
            });
            Rolle.setCellFactory(tablecell -> {
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
                                redirectToUserprofile(cell.getTableRow().getItem().getId());
//                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
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

    public void redirectToUserprofile(Integer teilnehmerId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/teilnehmerId=" + teilnehmerId)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            if (response.body().contains("matrikelnummer")) {
                Student vergleichNutzer = mapper.readValue(response.body(), Student.class);
                layout.instanceLayout("userprofile.fxml");
                ((UserprofilController) layout.getController()).setLayout(layout);
                if (layout.getController() instanceof UserprofilController) {
                    if(nutzerId instanceof  Student){
                        if(((Student) nutzerId).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, nutzerId);
                        }else{
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, vergleichNutzer);
                    }
                }

            } else if (response.body().contains("forschungsgebiet")) {
                Lehrender vergleichNutzer = mapper.readValue(response.body(), Lehrender.class);
                layout.instanceLayout("userprofile.fxml");
                ((UserprofilController) layout.getController()).setLayout(layout);
                if (layout.getController() instanceof UserprofilController) {
                    if(nutzerId instanceof  Lehrender){
                        if(((Lehrender) nutzerId).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, nutzerId);
                        }else{
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, vergleichNutzer);
                    }
                }
            }

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
//        System.out.println(id);
        populateTableView();

    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }
}
