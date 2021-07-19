package Client.Controller.Gruppenmitglieder;

import Client.Controller.NutzerProfil.UserprofilController;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

public class GruppenmitgliederController {
    @FXML
    private TableColumn<Nutzer, String> vorname;
    @FXML
    private TableColumn<Nutzer, String> nachname;
    @FXML
    private TableView<Nutzer> tabelle;

    private Projektgruppe projektgruppe;
    private Lehrveranstaltung lehrveranstaltung;

    private Layout layout;
    private Object nutzerId;

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public Object getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(Object nutzerId) {
        this.nutzerId = nutzerId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public void zeigeMitglieder(){

        /*HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/gruppenmitglieder/"+projektgruppe.getId())).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<Gruppenmitglied> mitglieder = mapper.readValue(response.body(), new TypeReference<List<Gruppenmitglied>>() {});
            vorname.setCellValueFactory(new PropertyValueFactory<Gruppenmitglied,String>("vorname"));
            nachname.setCellValueFactory(new PropertyValueFactory<Gruppenmitglied,String>("nachname"));


            vorname.setCellFactory(tablecell -> {
                TableCell<Gruppenmitglied, String> cell = new TableCell<Gruppenmitglied, String>() {
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

                            }
                        }
                );
                return cell;
            });

            nachname.setCellFactory(tablecell -> {
                TableCell<Gruppenmitglied, String> cell = new TableCell<Gruppenmitglied, String>() {
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

                            }
                        }
                );
                return cell;
            });

            ObservableList<Gruppenmitglied> obsLv = FXCollections.observableList(mitglieder);
            tabelle.setItems(obsLv);
        } catch (IOException e) {
            System.out.println("ERROR HIER");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("ERROR DA"); // xD
            e.printStackTrace();
        }*/
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
                        if ( ((Student) nutzerId).getId() == vergleichNutzer.getId() ) {
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, nutzerId);
                        } else {
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerId, vergleichNutzer);
                        }
                    } else {
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
    public void populateTableView() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/gruppenmitglieder/"+projektgruppe.getId())).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray jsonObject = new JSONArray(response.body());


            vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
            nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));





            for(int i=0;i<jsonObject.length();i++){
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("student").getJSONObject("nutzerId");


                Nutzer nutzer1 = new Nutzer();

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

                tabelle.getItems().add(nutzer1);
            }

            vorname.setCellFactory(tablecell -> {
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

                            }
                        }
                );

                return cell;
            });
            nachname.setCellFactory(tablecell -> {
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

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe=projektgruppe;
    }
}
