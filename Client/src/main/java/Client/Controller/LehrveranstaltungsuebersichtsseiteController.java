package Client.Controller;

import Client.Layouts.Layout;
import javafx.fxml.FXML;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LehrveranstaltungsuebersichtsseiteController {
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;
    @FXML
    private TableColumn<Lehrmaterial, String> teachMat;
    @FXML
    private TableView<Lehrmaterial> material;

    private Lehrveranstaltung lehrkurs;

    @FXML
    private Button teilnehmerListe;
    private Object lehrveranstaltung;
    private Object nutzer;

    @FXML
    private void teilnehmerListe(ActionEvent event){

        Layout lehrveranstaltungBeitreten = new Layout("teilnehmerliste.fxml", (Stage) teilnehmerListe.getScene().getWindow(),nutzer);
        if(lehrveranstaltungBeitreten.getController() instanceof TeilnehmerListeController){
            long veranstaltungId = ((Lehrveranstaltung) lehrveranstaltung).getId();


            ((TeilnehmerListeController) lehrveranstaltungBeitreten.getController()).setId(veranstaltungId);
            ((TeilnehmerListeController) lehrveranstaltungBeitreten.getController()).setNutzerInstanz(nutzer);
            ((TeilnehmerListeController)  lehrveranstaltungBeitreten.getController()).setLehrveranstaltung(((Lehrveranstaltung) lehrveranstaltung));

        }
    }



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
                                long lehrmaterialId = cell.getTableRow().getItem().getId();
                                HttpClient client1 = HttpClient.newHttpClient();
                                HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrmaterial/download/" + id)).build();
                                HttpResponse<String> response1;
                                try {
                                    response1 = client.send(request, HttpResponse.BodyHandlers.ofString());
                                    byte[] datei = response1.body().getBytes();
                                    String home = System.getProperty("user.home");
                                    File file = new File(home+"/Downloads/" + "test" + ".pdf");
                                    FileOutputStream fo = new FileOutputStream(file);
                                    fo.write(datei);
                                    fo.close();
                                    System.out.println("Fertig gedownloadet!");

                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
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
            ((LehrmaterialController) uploadScreen.getController()).setNutzerInstanz(nutzer);
            ((LehrmaterialController) uploadScreen.getController()).setLehrveranstaltung(lehrveranstaltung);
            ((LehrmaterialController) uploadScreen.getController()).setModus("Lehrmaterial");
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