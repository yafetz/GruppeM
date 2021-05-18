package Client.Controller;


import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.ResourceBundle;

public class AlleKurseController implements Initializable {

    @FXML
    public ImageView profilBild;
    @FXML
    public Hyperlink namenLink;
    @FXML
    private TableView<Lehrveranstaltung> alleLv;
    @FXML
    private TableColumn<Lehrveranstaltung, Long> col_LvId;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvTitel;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvSemester;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvArt;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvLehrende;
    @FXML
    private Button alleKurse;
    @FXML
    private Button meineKurse;

    private Object nutzerInstanz;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void populateTableView() {
        System.out.println(nutzerInstanz);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/all")).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Populate Tableview "+ response.body());

//            mapping data in response.body() to a list of lehrveranstaltung-objects
            ObjectMapper mapper = new ObjectMapper();
            List<Lehrveranstaltung> lehrveranstaltungen = mapper.readValue(response.body(), new TypeReference<List<Lehrveranstaltung>>() {});

            col_LvId.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,Long>("id"));
            col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("titel"));
            col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("semester"));
            col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("art"));
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

//            ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(lehrveranstaltungen);
            alleLv.setItems(obsLv);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void redirectToCourseOverview(Integer lehrveranstaltungId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/"+lehrveranstaltungId)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());
            ObjectMapper mapper = new ObjectMapper();
            Lehrveranstaltung lehrveranstaltung = mapper.readValue(response.body(), Lehrveranstaltung.class);
//            TODO Weiterleitung zu Übersichtsseite des Kurses
          //  HttpRequest requestisMember = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/"+lehrveranstaltungId)).build();
            //Layout layout = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) namenLink.getScene().getWindow());

//            Platzhalter bis dahin:
            Layout lehrenderLayout = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) namenLink.getScene().getWindow());
            if(lehrenderLayout.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                ((LehrveranstaltungsuebersichtsseiteController) lehrenderLayout.getController()).setNutzer(nutzerInstanz);
                ((LehrveranstaltungsuebersichtsseiteController) lehrenderLayout.getController()).setLehrveranstaltung(lehrveranstaltung);
                ((LehrveranstaltungsuebersichtsseiteController) lehrenderLayout.getController()).uebersichtsseiteAufrufen(nutzerInstanz, lehrveranstaltung);
            }
//
//            HttpResponse<String> memberResponse;
//            if (nutzerInstanz instanceof Lehrender) {
//                long lehrId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
//                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/"+ lehrveranstaltungId + "&2")).build();
//                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//                if(memberResponse.equals("true")){
//
//                    Layout lehrenderLayout = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) namenLink.getScene().getWindow());
//                    if(lehrenderLayout.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
//                        ((LehrveranstaltungsuebersichtsseiteController) lehrenderLayout.getController()).uebersichtsseiteAufrufen(nutzerInstanz, lehrveranstaltung);
//                    }
//                }
//                else {
//                    Layout lehrenderLayout = new Layout("lehrveranstaltungBeitreten.fxml", (Stage) namenLink.getScene().getWindow());
//
//                }
//            }
//            if (nutzerInstanz instanceof Student) {
//
//                long id = ((Student) nutzerInstanz).getNutzer().getId();
//                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
//                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//                if(memberResponse.body().equals("true")){
//
//                    Layout studentLayout = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) namenLink.getScene().getWindow());
//                    if(studentLayout.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
//                        ((LehrveranstaltungsuebersichtsseiteController) studentLayout.getController()).uebersichtsseiteAufrufen(nutzerInstanz, lehrveranstaltung);
//                    }
//                }
//                else{
//                    Layout studentLayout = new Layout("lehrveranstaltungBeitreten.fxml", (Stage) namenLink.getScene().getWindow());
//                }
//
//            }


            System.out.println(lehrveranstaltung.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void meineKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) meineKurse.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("meineKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            MeineKurseController meineKurseController = loader.getController();
            meineKurseController.setNutzerInstanz(nutzerInstanz);
            Scene scene = new Scene(root);
            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
            scene.getStylesheets().add(homescreencss);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void alleKurseAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) alleKurse.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("alleKurse.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            AlleKurseController alleKurseController = loader.getController();
            alleKurseController.setNutzerInstanz(nutzerInstanz);
            Scene scene = new Scene(root);
            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
            scene.getStylesheets().add(homescreencss);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void eigeneProfilSeiteAufrufen(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) namenLink.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("userprofile.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            UserprofilController userprofilController = loader.getController();
            userprofilController.nutzerprofilAufrufen(nutzerInstanz,nutzerInstanz);
            Scene scene = new Scene(root);
            String homescreencss = getClass().getClassLoader().getResource("css/login.css").toExternalForm();
            scene.getStylesheets().add(homescreencss);
            stage.setScene(scene);
            stage.setMaximized(false);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
        populateTableView();
    }

}
