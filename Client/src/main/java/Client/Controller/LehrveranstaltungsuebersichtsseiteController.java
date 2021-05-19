package Client.Controller;

import Client.Layouts.Layout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class LehrveranstaltungsuebersichtsseiteController {
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;
    @FXML
    private TableColumn<Lehrmaterial, String> teachMat;
    @FXML
    private TableView<Lehrmaterial> material;
   // @FXML
    //private TableView test;

    private Lehrveranstaltung lehrkurs;

    private Object lehrveranstaltung;
    private Object nutzer;


    public void getMaterial(Lehrveranstaltung lehrkurs) {
        this.lehrkurs=lehrkurs;
        long id = lehrkurs.getId();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrmaterial/" + id)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());




            ObjectMapper mapper = new ObjectMapper();
            System.out.println(response.body());

            List<Lehrmaterial> lehrmaterial = mapper.readValue(response.body(), new TypeReference<List<Lehrmaterial>>() {});



            teachMat.setCellValueFactory(new PropertyValueFactory<Lehrmaterial,String>("titel"));



            teachMat.setCellFactory(tablecell -> {
                TableCell<Lehrmaterial, String> cell = new TableCell<Lehrmaterial, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                //redirectToCourseOverview(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

//            ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<Lehrmaterial> obsLv = FXCollections.observableList(lehrmaterial);
            material.setItems(obsLv);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {


    }


    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        event.consume();
        Stage stage = (Stage) materialUpload.getScene().getWindow();
        Layout uploadScreen = null;
        uploadScreen = new Layout("lehrmaterialUpload.fxml", stage,nutzer);
        if (uploadScreen.getController() instanceof LehrmaterialController) {
            //((LehrmaterialController) uploadScreen.getController()).setNutzerInstanz(nutzer);
        }

    }

    public void uebersichtsseiteAufrufen(Object nutzer, Object lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung= lehrveranstaltung;


        if (nutzer !=null) {
            if (nutzer instanceof Lehrender) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
               // materialUpload.setText("Lehrmaterial hochladen")
                getMaterial((Lehrveranstaltung) lehrveranstaltung);

            }
            else if(nutzer instanceof Student) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
                materialUpload.setVisible(false);
                getMaterial((Lehrveranstaltung) lehrveranstaltung);
            }

        }
        System.out.println("hello2325");


    }

    public void downloadMaterial (ActionEvent download) {




    }



}