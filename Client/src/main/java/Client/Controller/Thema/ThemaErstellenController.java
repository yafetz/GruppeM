package Client.Controller.Thema;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

public class ThemaErstellenController {
    @FXML
    public TextField titel;
    @FXML
    public TextArea beschreibung;
    @FXML
    public ComboBox lehrveranstaltungen;
    @FXML
    public Button neuesThema;

    private Layout layout;
    private int selectedLvId;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        //Lade alle Lehrveranstaltungen
        ladeLehrveranstaltungen(lehrveranstaltungen);
    }

    private void ladeLehrveranstaltungen(ComboBox lehveranstaltungen) {
        HttpClient client = HttpClient.newHttpClient();
        long nutzerId = 0;
        if( layout.getNutzer() instanceof Lehrender){
            nutzerId = ((Lehrender) layout.getNutzer()).getNutzerId().getId();
        }else if(layout.getNutzer() instanceof Student){
            nutzerId = ((Student) layout.getNutzer()).getNutzer().getId();
        }
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/allelehrveranstaltungen/"+nutzerId)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<TeilnehmerListe> lehrveranstaltungen = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListe>>() {});
            ObservableList<TeilnehmerListe> obsLv = FXCollections.observableList(lehrveranstaltungen);
            lehveranstaltungen.setItems(obsLv);
            lehveranstaltungen.setConverter(new StringConverter() {
                @Override
                public String toString(Object object) {
                    if(object != null) {
                        if(object instanceof TeilnehmerListe) {
                            return ((TeilnehmerListe) object).getLehrveranstaltung().getTitel();
                        }else if(object instanceof Lehrveranstaltung){
                            return ((Lehrveranstaltung) object).getTitel();
                        }
                        return "";
                    }else{
                        return "";
                    }
                }

                @Override
                public Object fromString(String string) {
                    return lehveranstaltungen.getItems().stream().filter(new Predicate() {
                        @Override
                        public boolean test(Object o) {
                            if(o instanceof TeilnehmerListe){
                                return ((TeilnehmerListe)o).getLehrveranstaltung().getTitel().equals(string);
                            }else if(o instanceof Lehrveranstaltung){
                                return ((Lehrveranstaltung) o).getTitel().equals(string);
                            }
                            return false;
                        }
                    });
                }
            });
            lehveranstaltungen.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if(newValue != null){
                        if(newValue instanceof TeilnehmerListe){
                            selectedLvId = ((TeilnehmerListe) newValue).getLehrveranstaltung().getId();
                        }else if(newValue instanceof Lehrveranstaltung) {
                            selectedLvId = ((Lehrveranstaltung) newValue).getId();
                        }
                    }
                }
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void NeuesThema(ActionEvent actionEvent) {
        if(titel.getText() != "" && beschreibung.getText() != "" && selectedLvId != 0 ){
            long nutzerId = 0;
            if( layout.getNutzer() instanceof Lehrender){
                nutzerId = ((Lehrender) layout.getNutzer()).getNutzerId().getId();
            }else if(layout.getNutzer() instanceof Student){
                nutzerId = ((Student) layout.getNutzer()).getNutzer().getId();
            }
            //Sende Daten an den Server
            try (CloseableHttpClient client1 = HttpClients.createDefault()) {

                String url = "http://localhost:8080/themen/neuesThema";
                HttpPost post = new HttpPost(url);
                MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                entity.setCharset(StandardCharsets.UTF_8);
                entity.addTextBody("titel",titel.getText(), ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("beschreibung",beschreibung.getText(),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("lehrveranstaltungId",String.valueOf(selectedLvId),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                entity.addTextBody("nutzerId",String.valueOf(nutzerId), ContentType.create("text/plain", MIME.UTF8_CHARSET));

                HttpEntity requestEntity = entity.build();
                post.setEntity(requestEntity);

                try (CloseableHttpResponse response1 = client1.execute(post)) {
                    HttpEntity responseEntity = response1.getEntity();
                    String result = EntityUtils.toString(responseEntity);
                    if(result != "") {
                    //Weiterleitung zur Themenübersichtsseite
                    ObjectMapper om = new ObjectMapper();
                    Thema t = om.readValue(result, new TypeReference<Thema>() {});
                    layout.instanceLayout("ThemaÜbersicht.fxml");
                    ((ThemaÜbersichtController) layout.getController()).setLayout(layout);
                    ((ThemaÜbersichtController) layout.getController()).setThema(t);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Alert fehler = new Alert(Alert.AlertType.ERROR);
            fehler.setTitle("Eingegebene Daten sind falsch!");
            fehler.setContentText("Sie müssen eine Lehrveranstaltung auswählen und einen Titel und eine Beschreibung hinzufügen!");
            fehler.showAndWait();
        }
    }
}
