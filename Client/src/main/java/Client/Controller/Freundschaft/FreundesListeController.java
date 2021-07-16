package Client.Controller.Freundschaft;

import Client.Controller.NutzerProfil.UserprofilController;
import Client.Layouts.Layout;
import Client.Modell.Lehrender;
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

public class FreundesListeController {

    @FXML
    private TableView<Nutzer> freundeTabelle;
    @FXML
    private TableColumn<Nutzer, String> vorname;
    @FXML
    private TableColumn<Nutzer, String> nachname;

    private Object nutzerInstanz;
    private Layout layout;

    long id=0;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void populateTableView(){
        if(nutzerInstanz instanceof Student){
            id=((Student)nutzerInstanz).getNutzer().getId();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/freundschaft/"+id)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonObject = new JSONArray(response.body());
            vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
            nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
//            System.out.println(" Freundesliste json "+jsonObject);

            for(int i=0;i<jsonObject.length();i++){
                long nutzerid= ((Student)nutzerInstanz).getNutzer().getId();
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("anfragender_nutzer");
                JSONObject nutzer2= jsonObject.getJSONObject(i).getJSONObject("angefragter_nutzer");
                JSONObject jsonObject1 = jsonObject.getJSONObject(0);
//                System.out.println("JSON OBJEKT1  "+jsonObject1);
                if(jsonObject1.get("status").equals(true)){

                    if(nutzer.getLong("id")==nutzerid){

//                        System.out.println("If abfrage");

                        Nutzer nutzer_2 = new Nutzer();
                        nutzer_2.setVorname(nutzer2.getString("vorname"));
                        nutzer_2.setNachname(nutzer2.getString("nachname"));
                        nutzer_2.setId(nutzer2.getInt("id"));
                        freundeTabelle.getItems().add(nutzer_2);
                    }
                    else{
//                        System.out.println("else abfrage");
                        Nutzer nutzer1 = new Nutzer();
                        nutzer1.setVorname(nutzer.getString("vorname"));
                        nutzer1.setNachname(nutzer.getString("nachname"));
                        nutzer1.setId(nutzer.getInt("id"));
                        freundeTabelle.getItems().add(nutzer1);
                    }
                }
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
//                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
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

    public void setNutzerInstanz (Object nutzerInstanz){
        this.nutzerInstanz = nutzerInstanz;
        populateTableView();
    }


    public Object getNutzerInstanz() {
        return nutzerInstanz;
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
                if (layout.getController() instanceof UserprofilController) {
                    if(nutzerInstanz instanceof  Student){
                        if(((Student) nutzerInstanz).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, nutzerInstanz);
                        }else{
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                    }
                }

            } else if (response.body().contains("forschungsgebiet")) {
                Lehrender vergleichNutzer = mapper.readValue(response.body(), Lehrender.class);
                layout.instanceLayout("userprofile.fxml");
                if (layout.getController() instanceof UserprofilController) {
                    if(nutzerInstanz instanceof  Lehrender){
                        if(((Lehrender) nutzerInstanz).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, nutzerInstanz);
                        }else{
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
