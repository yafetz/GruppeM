package Client.Controller;

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
import javafx.stage.Stage;
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

    long id=0;

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



            for(int i=0;i<jsonObject.length();i++){
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("angefragter_nutzer");


                Nutzer nutzer1 = new Nutzer();
                nutzer1.setVorname(nutzer.getString("vorname"));
                nutzer1.setNachname(nutzer.getString("nachname"));
                nutzer1.setId(nutzer.getInt("id"));
                freundeTabelle.getItems().add(nutzer1);

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
                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
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
                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
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
                Layout userprofil = new Layout("userprofile.fxml", (Stage) freundeTabelle.getScene().getWindow(), nutzerInstanz);

                if (userprofil.getController() instanceof UserprofilController) {
                    if(nutzerInstanz instanceof  Student){
                        if(((Student) nutzerInstanz).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(nutzerInstanz, nutzerInstanz);
                        }else{
                            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                    }
                }

            } else if (response.body().contains("forschungsgebiet")) {
                Lehrender vergleichNutzer = mapper.readValue(response.body(), Lehrender.class);
                Layout userprofil = new Layout("userprofile.fxml", (Stage) freundeTabelle.getScene().getWindow(), nutzerInstanz);

                if (userprofil.getController() instanceof UserprofilController) {
                    if(nutzerInstanz instanceof  Lehrender){
                        if(((Lehrender) nutzerInstanz).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(nutzerInstanz, nutzerInstanz);
                        }else{
                            ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) userprofil.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
