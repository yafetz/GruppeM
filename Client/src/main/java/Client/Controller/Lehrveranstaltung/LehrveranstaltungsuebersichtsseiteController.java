package Client.Controller.Lehrveranstaltung;

import Client.Controller.Review.CreateReviewController;
import Client.Controller.Liste.StudentenListeController;
import Client.Controller.Liste.TeilnehmerListeController;
import Client.Controller.ProjektGruppe.ProjektgruppenController;
import Client.Controller.Quiz.QuizUebersichtController;
import Client.Controller.Review.CreateReviewController;
import Client.Controller.Review.ReviewBearbeitenController;
import Client.Controller.Review.ReviewStatistikController;
import Client.Layouts.Layout;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;

public class LehrveranstaltungsuebersichtsseiteController {
    @FXML
    private Button bearbeiten;
    @FXML
    private Button projektgruppe_btn;

    @FXML
    private Button reviewStatistikBtn;
    @FXML
    private Button reviewBtn;
    @FXML
    private Label title;
    @FXML
    private Button materialUpload;
    @FXML
    private TableColumn<Lehrmaterial, String> teachMat;
    @FXML
    private TableView<Lehrmaterial> material;
    @FXML
    private Button teilnehmerListe;
    private Lehrveranstaltung lehrveranstaltung;
    private Object nutzer;
    @FXML
    private Button studentenliste;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;

    }

    private Review review;

    public void Studenliste(ActionEvent actionEvent) {
        layout.instanceLayout("studentenListe.fxml");
        ((StudentenListeController) layout.getController()).setLayout(layout);
        ((StudentenListeController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
    }

    public void checkIfReviewed(Lehrveranstaltung lehrveranstaltungid, long nutzerid){
        long lehrid = lehrveranstaltungid.getId();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/check/user/"+nutzerid+"&"+lehrid)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Checkifrevied response " +response.body());

            if (response.body().equals("true")){
                System.out.println("Bereits reviewed");
                reviewBtn.setVisible(false);
            }
            else {
                checkThreshold(lehrveranstaltung, ((Student)nutzer).getNutzer().getId());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void checkThreshold(Lehrveranstaltung lehrveranstaltung1, long nutzerid){
        long lehrid = lehrveranstaltung1.getId();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/threshold/" +nutzerid+"&"+lehrid)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Threshold "+ response.body());
            if (response.body().equals("true")){
                System.out.println("Threshold erreicht");

                reviewBtn.setVisible(true);
            }
            else {
                System.out.println("Threshold nicht erreicht");
                reviewBtn.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void createReview(){

        layout.instanceLayout("reviewCreate.fxml");
        ((CreateReviewController) layout.getController()).setLayout(layout);
        ((CreateReviewController) layout.getController()).reviewSeiteAufrufen(nutzer, lehrveranstaltung);
        ((CreateReviewController) layout.getController()).review_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel());



    }

    @FXML
    private void teilnehmerListe(ActionEvent event){
            layout.instanceLayout("teilnehmerListe.fxml");
            long veranstaltungId = ((Lehrveranstaltung) lehrveranstaltung).getId();
            ((TeilnehmerListeController) layout.getController()).setId(veranstaltungId);
            ((TeilnehmerListeController) layout.getController()).setLayout(layout);
            ((TeilnehmerListeController) layout.getController()).setLehrveranstaltung(((Lehrveranstaltung) lehrveranstaltung));
    }

    public void getReview( Lehrveranstaltung lehrveranstaltungsid){

        long Lehrid = lehrveranstaltungsid.getId();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/" +Lehrid)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            if(response.body().isEmpty()){

            }
            else {
                review = mapper.readValue(response.body(), new TypeReference<Review>() {
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void getMaterial(Lehrveranstaltung lehrkurs) {
        this.lehrveranstaltung=lehrkurs;
        long id = lehrkurs.getId();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrmaterial/" + id)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JSONArray alleLehrmaterialien = new JSONArray(response.body());
            //List<Lehrmaterial> lehrmaterial = mapper.readValue(response.body(), new TypeReference<List<Lehrmaterial>>() {});
            teachMat.setCellValueFactory(new PropertyValueFactory<>("titel"));
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
                                int lehrmaterialId = cell.getTableRow().getItem().getId().intValue();
                                try {
                                    Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/sep","root","");
                                    PreparedStatement pstmt = connection.prepareStatement("select datei from lehrmaterial WHERE id LIKE "+lehrmaterialId);
                                    ResultSet rs = pstmt.executeQuery();
                                    String home = System.getProperty("user.home");
                                    File file = new File(home+"/Downloads/" + alleLehrmaterialien.getJSONObject(cell.getTableRow().getIndex()).getString("titel").replace(" ","_").replace("?",""));
                                    FileOutputStream fo = new FileOutputStream(file);
                                    rs.next();
                                    Blob datei = rs.getBlob("datei");
                                    IOUtils.write(datei.getBinaryStream().readAllBytes(),fo);
                                    fo.close();
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Erfolgreich heruntergladen!");
                                    alert.setHeaderText("Ihre Lehrmaterialien wurden erfolgreich heruntergeladen!");
                                    alert.setContentText("Sie können Ihr Lernmaterial unter ihrem Downloads Ordner finden! Sie müssen das Programm schließen bevor Sie ihre Datei öffnen können. Sonst kann es passieren das ihr Betriebssystem ihnen Probleme macht!");
                                    alert.showAndWait();

                                } catch (IOException | SQLException exception) {
                                    exception.printStackTrace();
                                }
                            }
                        }
                );
                return cell;
            });

//            ObservableList is required to populate the table alleLv using .setItems() :
            for(int i = 0; i < alleLehrmaterialien.length(); i++){
                Lehrmaterial l = new Lehrmaterial();
                l.setId(alleLehrmaterialien.getJSONObject(i).getLong("id"));
                l.setTitel(alleLehrmaterialien.getJSONObject(i).getString("titel"));
                material.getItems().add(l);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
    }

    @FXML
    private void materialUploadPressedButton(ActionEvent event) {
        event.consume();
        layout.instanceLayout("lehrmaterialUpload.fxml");
        ((LehrmaterialController) layout.getController()).setLayout(layout);
        ((LehrmaterialController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LehrmaterialController) layout.getController()).setModus("Lehrmaterial");

    }

    public void uebersichtsseiteAufrufen(Object nutzer, Lehrveranstaltung lehrveranstaltung) {
        this.nutzer = nutzer;
        this.lehrveranstaltung= lehrveranstaltung;

        if (nutzer != null) {
            if (nutzer instanceof Lehrender) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());
               // materialUpload.setText("Lehrmaterial hochladen")
                getMaterial((Lehrveranstaltung) lehrveranstaltung);
                checkForReview(lehrveranstaltung);
            }
            else if(nutzer instanceof Student) {
                title.setText(((Lehrveranstaltung) lehrveranstaltung).getTitel());

                materialUpload.setVisible(false);
                studentenliste.setVisible(false);
                bearbeiten.setVisible(false);
                reviewBtn.setVisible(false);
                getMaterial((Lehrveranstaltung) lehrveranstaltung);
                checkForReview(lehrveranstaltung);

            }
        }
    }


    public void checkForReview(Lehrveranstaltung lehrveranstaltung1){

        this.lehrveranstaltung=lehrveranstaltung1;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/check/" +lehrveranstaltung.getId())).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("RESPONSEBODY     "+response.body());
            if(response.body().equals("false")){
                if (nutzer instanceof Lehrender) {
                    reviewBtn.setVisible(true);
                }
                else {
                    reviewBtn.setVisible(false);
                }



            }
            else {
                if(nutzer instanceof Student) {
                    checkIfReviewed(lehrveranstaltung, ((Student) nutzer).getNutzer().getId());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void projektgruppePressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceLayout("projektgruppenliste.fxml");
        ((ProjektgruppenController) layout.getController()).setLayout(layout);
        ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
        ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ProjektgruppenController) layout.getController()).populateTableView();
        ((ProjektgruppenController) layout.getController()).setPGListeSeitentitel(lehrveranstaltung.getTitel());

    }

    public void quizPressed(ActionEvent actionEvent) {
        layout.instanceLayout("quizUebersicht.fxml");
        ((QuizUebersichtController) layout.getController()).setLayout(layout);
        ((QuizUebersichtController) layout.getController()).quizSeiteAufrufen(nutzer,lehrveranstaltung);
        ((QuizUebersichtController) layout.getController()).quizerstellen_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel() );
    }

    public void reviewPressed(ActionEvent actionEvent) {
        System.out.println("REviewPressed  "+ lehrveranstaltung.getId());
        getReview(lehrveranstaltung);
        if (nutzer instanceof Lehrender) {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/review/check/" +lehrveranstaltung.getId())).build();
            HttpResponse<String> response;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("RESPONSEBODY     "+response.body());
                if(response.body().equals("true")){
                    layout.instanceLayout("reviewStatistikAlle.fxml");
                    ((ReviewStatistikController) layout.getController()).setLayout(layout);
                    ((ReviewStatistikController) layout.getController()).setNutzer(nutzer);
                    ((ReviewStatistikController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((ReviewStatistikController) layout.getController()).populateFragenTableView();

                }
                else {
                    layout.instanceLayout("reviewCreate.fxml");
                    ((CreateReviewController) layout.getController()).setLayout(layout);
                    ((CreateReviewController) layout.getController()).reviewSeiteAufrufen(nutzer, lehrveranstaltung);
                    ((CreateReviewController) layout.getController()).review_LvTitel_Label.setText("Lehrveranstaltung " + lehrveranstaltung.getTitel());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        else if (nutzer instanceof Student){
            layout.instanceLayout("reviewBearbeiten.fxml");
            ((ReviewBearbeitenController) layout.getController()).setLayout(layout);
            ((ReviewBearbeitenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
            ((ReviewBearbeitenController) layout.getController()).setNutzer(nutzer);
            //System.out.println("lvübersichtsseitecontroller review: " + review.getTitel());
            ((ReviewBearbeitenController) layout.getController()).setReview(review);

        }
    }

    public void bearbeitenPressedButton(ActionEvent actionEvent) {
        layout.instanceLayout("lehrveranstaltungBearbeiten.fxml");
        ((LehrveranstaltungBearbeitenController) layout.getController()).setLayout(layout);
        ((LehrveranstaltungBearbeitenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LehrveranstaltungBearbeitenController) layout.getController()).setNutzer(nutzer);
    }
}