package Client.Controller.NutzerProfil;

import Client.Controller.Auth.LoginController;
import Client.Controller.Freundschaft.FreundesListeController;
import Client.Controller.Freundschaft.FreundschaftsAnfragenController;
import Client.Controller.Lehrveranstaltung.LehrveranstaltungBeitretenController;
import Client.Controller.Lehrveranstaltung.LehrveranstaltungsuebersichtsseiteController;
import Client.Controller.Thema.ThemaErstellenController;
import Client.Controller.Thema.ThemaÜbersichtController;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserprofilController {
    @FXML
    public VBox kurse;
    @FXML
    public VBox themen;
    @FXML
    public Label themenText;
    @FXML
    public ScrollPane themenListe;
    @FXML
    public Button neuesThema;
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
    public Label strasseLabel;
    @FXML
    public Label hausnummerLabel;
    @FXML
    public Label stadtLabel;
    @FXML
    public Label plzLabel;
    @FXML
    private Button anfrage;
    @FXML
    private Label lehrstuhlOderMatrNrTextLabel;
    @FXML
    private Button meineAnfragen;
    @FXML
    private Label forschungsgebietOderStudienfachTextLabel;

    private Object vergleichNutzer;
    private Object eigenerNutzer;
    private Object user;

    @FXML
    public Button abmeldenButton;
    @FXML
    private AnchorPane pane;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void initialize() {
        profil.setVisible(false);
    }

    public void nutzerprofilAufrufen (Object eigenerNutzer, Object vergleichNutzer) {
        this.eigenerNutzer = eigenerNutzer;
        this.vergleichNutzer = vergleichNutzer;

        byte[] profilbildArray = null;
        if(eigenerNutzer == vergleichNutzer){
            if(eigenerNutzer instanceof Student){
                profilbildArray = ((Student) eigenerNutzer).getNutzer().getProfilbild();
            }else if(eigenerNutzer instanceof Lehrender){
                profilbildArray = ((Lehrender) eigenerNutzer).getNutzerId().getProfilbild();
            }
        }else{
            if(vergleichNutzer instanceof Student){
                profilbildArray = ((Student) vergleichNutzer).getNutzer().getProfilbild();
            }else if(vergleichNutzer instanceof Lehrender){
                profilbildArray = ((Lehrender) vergleichNutzer).getNutzerId().getProfilbild();
            }
        }
        if(profilbildArray != null) {
            Image img = new Image(new ByteArrayInputStream(profilbildArray),150,150,true,true);
            ImageView imgView = new ImageView(img);
            imgView.setLayoutX(50.00);
            imgView.setLayoutY(56.00);
            pane.getChildren().add(imgView);
        }

        if(eigenerNutzer == vergleichNutzer) {
            //Diese If-Bedingung tritt ein, wenn der Nutzer sich selbst aufruft
            anfrage.setVisible(false);
            meineAnfragen.setVisible(true);
            //Freundschaftsliste.setVisible(true);
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
                ThemenAufrufen(vergleichNutzer,eigenerNutzer);
                number.setText( "" + ((Lehrender) eigenerNutzer).getNutzerId().getHausnummer());
                lehrstuhlOderMatrNrTextLabel.setText("Lehrstuhl");
                forschungsgebietOderStudienfachTextLabel.setText("Forschungsgebiet");
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

                number.setText( "" + ((Student) eigenerNutzer).getNutzer().getHausnummer());
                lehrstuhlOderMatrNrTextLabel.setText("Matrikelnummer");
                forschungsgebietOderStudienfachTextLabel.setText("Studienfach");
                themenText.setVisible(false);
                themenListe.setVisible(false);
                neuesThema.setVisible(false);
            }
        }

        // Diese else If tritt ein, wenn der Nutzer auf einen anderen Nutzer klickt
        else if (eigenerNutzer != vergleichNutzer) {
            //Freundschaftsliste.setVisible(false);
            meineAnfragen.setVisible(false);
            long nutzer_id =0;
            long profil_id =0;

            if (eigenerNutzer instanceof Lehrender) {
                // Sicht eines Lehrenden auf anderen Lehrenden
                if (vergleichNutzer instanceof Lehrender) {
                    username.setText(((Lehrender) vergleichNutzer).getNutzerId().getVorname() +" "+ ((Lehrender) vergleichNutzer).getNutzerId().getNachname());
                    mailadresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getEmail());
                    lehrstuhl_oder_matr.setText(((Lehrender) vergleichNutzer).getLehrstuhl());
                    forschungsgebiet_studienfach.setText(((Lehrender) vergleichNutzer).getForschungsgebiet());
                    plz.setText(String.valueOf(((Lehrender) vergleichNutzer).getNutzerId().getPlz()));
                    adresse.setText(((Lehrender) vergleichNutzer).getNutzerId().getStrasse());
                    city.setText(((Lehrender) vergleichNutzer).getNutzerId().getStadt());
                    KurseAufrufen(vergleichNutzer);
                    ThemenAufrufen(vergleichNutzer,eigenerNutzer);
                    //abmeldenButton.setVisible(false);
                    number.setText( "" + ((Lehrender) vergleichNutzer).getNutzerId().getHausnummer());
                    lehrstuhlOderMatrNrTextLabel.setText("Lehrstuhl");
                    forschungsgebietOderStudienfachTextLabel.setText("Forschungsgebiet");
                    anfrage.setVisible(false);
                    //Freundschaftsliste.setVisible(false);
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

                    //abmeldenButton.setVisible(false);
                    number.setText( "" + ((Student) vergleichNutzer).getNutzer().getHausnummer());
                    lehrstuhlOderMatrNrTextLabel.setText("Matrikelnummer");
                    forschungsgebietOderStudienfachTextLabel.setText("Studienfach");
                    anfrage.setVisible(false);
                    //Freundschaftsliste.setVisible(false);
                    themenText.setVisible(false);
                    themenListe.setVisible(false);
                    neuesThema.setVisible(false);
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
                    ThemenAufrufen(vergleichNutzer,eigenerNutzer);
                    stadtLabel.setVisible(false);
                    strasseLabel.setVisible(false);
                    hausnummerLabel.setVisible(false);
                    plzLabel.setVisible(false);
                    plz.setVisible(false);
                    adresse.setVisible(false);
                    number.setVisible(false);
                    city.setVisible(false);
                    //abmeldenButton.setVisible(false);
                    lehrstuhlOderMatrNrTextLabel.setText("Lehrstuhl");
                    forschungsgebietOderStudienfachTextLabel.setText("Forschungsgebiet");
                    anfrage.setVisible(false);
                }
                //Sicht eines Studenten auf das Profil eines Studenten
                else if(vergleichNutzer instanceof Student) {
                    nutzer_id = ((Student)eigenerNutzer).getNutzer().getId();
                    profil_id = ((Student)vergleichNutzer).getNutzer().getId();
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/freundschaft/profil/status/"+nutzer_id+"&"+profil_id)).build();

                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        if(response.body().equals("true")){
                            anfrage.setVisible(false);
                        }
                        else {
                            anfrage.setVisible(true);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    username.setText(((Student) vergleichNutzer).getNutzer().getVorname() +" "+ ((Student) vergleichNutzer).getNutzer().getNachname());
                    mailadresse.setText(((Student) vergleichNutzer).getNutzer().getEmail());
                    KurseAufrufen(vergleichNutzer);

                    stadtLabel.setVisible(false);
                    strasseLabel.setVisible(false);
                    hausnummerLabel.setVisible(false);
                    plzLabel.setVisible(false);
                    plz.setVisible(false);
                    adresse.setVisible(false);
                    number.setVisible(false);
                    city.setVisible(false);
                    //abmeldenButton.setVisible(false);
                    lehrstuhlOderMatrNrTextLabel.setVisible(false);
                    lehrstuhl_oder_matr.setVisible(false);
                    forschungsgebietOderStudienfachTextLabel.setText("Studienfach");
                    forschungsgebiet_studienfach.setText(((Student) vergleichNutzer).getStudienfach());
                    themenText.setVisible(false);
                    themenListe.setVisible(false);
                    neuesThema.setVisible(false);
                }
            }
        }
    }

    public void profilBearbeiten(ActionEvent actionEvent) {
        Stage stage = (Stage) profil.getScene().getWindow();
        Layout editieren = null;
        layout.instanceLayout("Nutzerprofil_veraendern.fxml");
        ((EditierenController) layout.getController()).setLayout(layout);
    }

    public void freundschaftsAnfrageWeiterleitung(ActionEvent actionEvent){
        Stage stage = (Stage) profil.getScene().getWindow();
        Layout anfragen = null;
        layout.instanceLayout("freundschaftsAnfragen.fxml");
        if (layout.getController() instanceof FreundschaftsAnfragenController) {
            ((FreundschaftsAnfragenController) layout.getController()).setLayout(layout);
            ((FreundschaftsAnfragenController) layout.getController()).setNutzerInstanz(eigenerNutzer);
        }
    }

    public void FreundeslisteWeiterleitung(ActionEvent actionEvent){
        Stage stage = (Stage) profil.getScene().getWindow();
        Layout freunde = null;
        layout.instanceLayout("freundesliste.fxml");
        if (layout.getController() instanceof FreundesListeController) {
            ((FreundesListeController) layout.getController()).setLayout(layout);
            ((FreundesListeController) layout.getController()).setNutzerInstanz(eigenerNutzer);
        }
    }


    public void ThemenAufrufen(Object user,Object anfragenderNutzer) {
        this.user = user;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        long nutzerId = 0;
        if(anfragenderNutzer instanceof Lehrender){
            nutzerId = ((Lehrender) anfragenderNutzer).getNutzerId().getId();
        }else if(anfragenderNutzer instanceof Student){
            nutzerId = ((Student) anfragenderNutzer).getNutzer().getId();
        }

        if (user instanceof Lehrender) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/themen/alle/" + ((Lehrender) user).getNutzerId().getId()+"/"+nutzerId)).build();
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<Thema> Themen = mapper.readValue(response.body(), new TypeReference<List<Thema>>() {});
            for(Thema thema : Themen) {
                Label them = new Label();
                them.setPadding(new Insets(10,10,10,10));
                them.setFont(new Font(20));
                them.setText(thema.getTitel());
                them.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        layout.instanceLayout("ThemaÜbersicht.fxml");
                        ((ThemaÜbersichtController) layout.getController()).setLayout(layout);
                        ((ThemaÜbersichtController) layout.getController()).setThema(thema);
                    }
                });
                themen.getChildren().add(them);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            List<TeilnehmerListe> meineKurse = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListe>>() {});
            for(TeilnehmerListe teilnehmerListe : meineKurse) {
                Label kurs = new Label();
                kurs.setPadding(new Insets(10,10,10,10));
                kurs.setFont(new Font(20));
                kurs.setText(teilnehmerListe.getLehrveranstaltung().getTitel());
                kurs.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        redirectToCourseOverview(teilnehmerListe.getLehrveranstaltung().getId());
                    }
                });
                kurse.getChildren().add(kurs);
            }

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
            ObjectMapper mapper = new ObjectMapper();
            Lehrveranstaltung lehrveranstaltung = mapper.readValue(response.body(), Lehrveranstaltung.class);
            HttpResponse<String> memberResponse;
            if (eigenerNutzer instanceof Lehrender) {
                long lehrId = ((Lehrender) eigenerNutzer).getNutzerId().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/beitreten/check/"+ lehrveranstaltungId + "&"+lehrId)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                if(memberResponse.body().equals("true")){
                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(eigenerNutzer,lehrveranstaltung);
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                }
                else {
                    layout.instanceLayout("lehrveranstaltungBeitreten.fxml");
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLayout(layout);
                }
            }else if (eigenerNutzer instanceof Student) {

                long id = ((Student) eigenerNutzer).getNutzer().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

                if(memberResponse.body().equals("true")){
                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(eigenerNutzer,lehrveranstaltung);
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                }
                else{
                    layout.instanceLayout("lehrveranstaltungBeitreten.fxml");
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLayout(layout);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abmeldenPressedButton(ActionEvent actionEvent) {
        actionEvent.consume();
        layout.instanceAuth("login.fxml");
        layout.setNutzer(null);
        ((LoginController) layout.getController()).setLayout(layout);
    }

    public void freunschafts_anfrage(ActionEvent actionEvent) {
        actionEvent.consume();
        long anfrage_id = ((Student) eigenerNutzer).getNutzer().getId();
        long angefragter_id = ((Student) vergleichNutzer).getNutzer().getId();

        CloseableHttpClient client = HttpClients.createDefault();
        String url = "http://localhost:8080/freundschaft/anfrage";
        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();
        entity.setCharset(StandardCharsets.UTF_8);
        entity.addTextBody("anfragender_id",String.valueOf(angefragter_id), ContentType.create("long/plain", MIME.UTF8_CHARSET));
        entity.addTextBody("angefragter_id",String.valueOf(anfrage_id),ContentType.create("text/plain", MIME.UTF8_CHARSET));

        HttpEntity requestEntity = entity.build();
        post.setEntity(requestEntity);

        try {
            CloseableHttpResponse response = client.execute(post);
            HttpEntity responseEntity = response.getEntity();
            String result = EntityUtils.toString(responseEntity);
            layout.instanceLayout("userprofile.fxml");
            ((UserprofilController) layout.getController()).setLayout(layout);
            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(eigenerNutzer,vergleichNutzer);

        } catch (IOException e) {
            e.printStackTrace();
        }
}

    public void NeuesThema(ActionEvent actionEvent) {
        //Weiterleitung zu Neues Thema Erstellen
        layout.instanceLayout("ThemaErstellen.fxml");
        ((ThemaErstellenController) layout.getController()).setLayout(layout);
    }
}
