package Client.Controller;

import Client.Controller.AlleKurseController;
import Client.Controller.MeineKurseController;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedList;
import java.util.List;

public class UserprofilController {
    @FXML
    private Label username;
    @FXML
    private Label mailadresse;

    @FXML
    private Label plz;
    @FXML
    private Label adresse;
    @FXML
    private Label lehrstuhl_oder_matr;
    @FXML
    private Label forschungsgebiet_studienfach;
    @FXML
    private Label number;
    @FXML
    private Label city;
    @FXML
    private Button profil;
    @FXML
    private TableView<Lehrveranstaltung> courseCol;
    @FXML
    public TableColumn<Lehrveranstaltung, String> myCourses;




    private Object vergleichNutzer;
    private Object eigenerNutzer;

    private Object user;




    public void initialize() {
        profil.setVisible(false);
    }








    public void nutzerprofilAufrufen (Object eigenerNutzer, Object vergleichNutzer) {
        this.eigenerNutzer = eigenerNutzer;
        this.vergleichNutzer = vergleichNutzer;

        if(eigenerNutzer == vergleichNutzer) {
            //Diese If-Bedingung tritt ein, wenn der Nutzer sich selbst aufruft

            // Sicht eines Lehrenden auf sein eigenes Profil
            if (eigenerNutzer instanceof Lehrender) {
                username.setText(((Lehrender) eigenerNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) eigenerNutzer).getNutzerId().getNachname());
                mailadresse.setText(((Lehrender) eigenerNutzer).getNutzerId().getEmail());
                lehrstuhl_oder_matr.setText(((Lehrender) eigenerNutzer).getLehrstuhl());
                forschungsgebiet_studienfach.setText(((Lehrender) eigenerNutzer).getForschungsgebiet());
                plz.setText(String.valueOf(((Lehrender) eigenerNutzer).getNutzerId().getPlz()));
                adresse.setText(((Lehrender) eigenerNutzer).getNutzerId().getStrasse());
                city.setText(((Lehrender) eigenerNutzer).getNutzerId().getStadt());
                profil.setVisible(true);
                KurseAufrufen(eigenerNutzer);


            }
            // Sicht eines Studenten auf sein eigenes Profil
            else if(eigenerNutzer instanceof Student) {
                username.setText(((Student) eigenerNutzer).getNutzer().getVorname() +" "+ ((Student) eigenerNutzer).getNutzer().getNachname());
                mailadresse.setText(((Student) eigenerNutzer).getNutzer().getEmail());
                lehrstuhl_oder_matr.setText(String.valueOf(((Student) eigenerNutzer).getMatrikelnummer()));
                forschungsgebiet_studienfach.setText(((Student) eigenerNutzer).getStudienfach());
                plz.setText(String.valueOf(((Student) eigenerNutzer).getNutzer().getPlz()));
                adresse.setText(((Student) eigenerNutzer).getNutzer().getStrasse());
                city.setText(((Student) eigenerNutzer).getNutzer().getStadt());
                profil.setVisible(true);
                KurseAufrufen(eigenerNutzer);

            }


        }
        // Diese else If tritt ein, wenn der Nutzer auf einen anderen Nutzer klickt
        else if (eigenerNutzer != vergleichNutzer) {
            if (eigenerNutzer instanceof Lehrender) {
                // Sicht eines Lehrenden auf anderen Lehrenden
                if (vergleichNutzer instanceof Lehrender) {
                    username.setText(((Lehrender) vergleichNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) vergleichNutzer).getNutzerId().getNachname());
                    mailadresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getEmail());
                    lehrstuhl_oder_matr.setVisible(false);
                    forschungsgebiet_studienfach.setText(((Lehrender) vergleichNutzer).getForschungsgebiet());
                    plz.setText(String.valueOf(((Lehrender) vergleichNutzer).getNutzerId().getPlz()));
                    adresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getStrasse());
                    city.setText(((Lehrender) vergleichNutzer).getNutzerId().getStadt());
                    KurseAufrufen(vergleichNutzer);
                }
                //Sicht eines Lehrenden auf das Profil eines Studenten
                else if(vergleichNutzer instanceof Student) {
                    username.setText(((Student) vergleichNutzer).getNutzer().getVorname() +" "+ ((Student) vergleichNutzer).getNutzer().getNachname());
                    mailadresse.setText(((Student) vergleichNutzer).getNutzer().getEmail());
                    lehrstuhl_oder_matr.setText(String.valueOf(((Student) vergleichNutzer).getMatrikelnummer()));
                    forschungsgebiet_studienfach.setText(((Student) vergleichNutzer).getStudienfach());
                    plz.setText(String.valueOf(((Student) vergleichNutzer).getNutzer().getPlz()));
                    adresse.setText(((Student) vergleichNutzer).getNutzer().getStrasse());
                    city.setText(((Student) vergleichNutzer).getNutzer().getStadt());
                    KurseAufrufen(vergleichNutzer);




                }
            }

            else if(eigenerNutzer instanceof Student) {
                // Sicht eines Studenten auf das Profil eines Lehrenden
                if (vergleichNutzer instanceof Lehrender) {
                    username.setText(((Lehrender) vergleichNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) vergleichNutzer).getNutzerId().getNachname());
                    mailadresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getEmail());
                    lehrstuhl_oder_matr.setText(((Lehrender) vergleichNutzer).getLehrstuhl());
                    forschungsgebiet_studienfach.setText(((Lehrender) vergleichNutzer).getForschungsgebiet());
                    KurseAufrufen(vergleichNutzer);

                }
                //Sicht eines Studenten auf das Profil eines Studenten
                else if(vergleichNutzer instanceof Student) {
                    username.setText(((Student) vergleichNutzer).getNutzer().getVorname() +" "+ ((Student) vergleichNutzer).getNutzer().getNachname());
                    mailadresse.setText(((Student) vergleichNutzer).getNutzer().getEmail());
                    KurseAufrufen(vergleichNutzer);

                }

            }
        }



    }

    public void profilBearbeiten(ActionEvent actionEvent) {
        Stage stage = (Stage) profil.getScene().getWindow();
        Layout editieren = null;
            editieren = new Layout("Nutzerprofil_verändern.fxml", stage,eigenerNutzer);
            if (editieren.getController() instanceof EditierenController) {
                ((EditierenController) editieren.getController()).setNutzer(eigenerNutzer);

            }
    }


    public void KurseAufrufen(Object user) {
        this.user = user;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if (user instanceof Lehrender) {

            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Lehrender) user).getNutzerId().getId())).build();
        }
        if (user instanceof Student) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Student) user).getNutzer().getId())).build();
            System.out.println(((Student) user).getId());
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<TeilnehmerListe> kurse = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListe>>() {});
            List<Lehrveranstaltung> lehrveranstaltungen = new LinkedList<>();

            for(TeilnehmerListe teilnehmerListe1 : kurse) {
                lehrveranstaltungen.add(teilnehmerListe1.getLehrveranstaltung());
            }


            myCourses.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("titel"));


//            Angelehnt an: https://stackoverflow.com/questions/35562037/how-to-set-click-event-for-a-cell-of-a-table-column-in-a-tableview
            myCourses.setCellFactory(tablecell -> {
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
                                kurseAufrufen(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

//            ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<Lehrveranstaltung> kursListe = FXCollections.observableList(lehrveranstaltungen);
            courseCol.setItems(kursListe);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void kurseAufrufen(Integer lehrveranstaltungId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/"+lehrveranstaltungId)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            Lehrveranstaltung lehrveranstaltung = mapper.readValue(response.body(), Lehrveranstaltung.class);

            HttpResponse<String> memberResponse;
            if (user instanceof Lehrender) {
                long lehrId = ((Lehrender) user).getNutzerId().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/"+ lehrveranstaltungId + "&"+lehrId)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());



                if(memberResponse.body().equals("true")){
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) courseCol.getScene().getWindow(),user);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(user,lehrveranstaltung);
                    }
                }
                else {
                    System.out.println("LehrveranstaltungsId   "+lehrveranstaltungId);
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) courseCol.getScene().getWindow(),user);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(user,lehrveranstaltung);
                    }
                }
            }
            if (user instanceof Student) {

                long id = ((Student) user).getNutzer().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                if(memberResponse.body().equals("true")){
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) courseCol.getScene().getWindow(),user);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(user,lehrveranstaltung);
                    }
                }
                else{
                    Layout lehrveranstaltungBeitreten = new Layout("lehrveranstaltungsuebersichtsseite.fxml", (Stage) courseCol.getScene().getWindow(),user);
                    if(lehrveranstaltungBeitreten.getController() instanceof LehrveranstaltungsuebersichtsseiteController){
                        ((LehrveranstaltungsuebersichtsseiteController) lehrveranstaltungBeitreten.getController()).uebersichtsseiteAufrufen(user,lehrveranstaltung);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

