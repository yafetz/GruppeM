package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Lernkarte;
import Client.Modell.Projektgruppe;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LernkartenController {
    @FXML
    public Label frage;
    @FXML
    public Label antwort;
    @FXML
    private Button back;
    @FXML
    private Button previous;
    @FXML
    private Button lösung;
    @FXML
    private Button next;
    @FXML
    private Button create;

    private Layout layout;
    private Object nutzer;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;

    private List<Lernkarte> lernkartenList;
    private Lernkarte currentLernkarte;

    int currentPositionInLernkartenList;
    boolean showLösung;

    public void initLernkartenController() {

        currentPositionInLernkartenList = 0;
        showLösung = false;
        frage.setText("");
        antwort.setText("");
        antwort.setVisible(true);


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lernkarten/getlernkarten/" + projektgruppe.getId())).build();
        HttpResponse<String> response;

        lernkartenList = new ArrayList<>();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            lernkartenList = mapper.readValue(response.body(), new TypeReference<List<Lernkarte>>() {});

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(lernkartenList != null && lernkartenList.size() >= 1){
            currentLernkarte = lernkartenList.get(0);
            frage.setText("Frage: " + currentLernkarte.getFrage());
        } else {
            frage.setText("Es existieren keine Lernkarten");
            antwort.setText("");
        }
    }

    public void ActionLösung() {
        if(currentLernkarte == null)
            return;

        showLösung = !showLösung;
        if(showLösung) {
            antwort.setText("Antwort: " + currentLernkarte.getAntwort());
            antwort.setVisible(true);
        } else {
            antwort.setText("");
            antwort.setVisible(false);
        }
    }

    public void ActionPrevious() {
        if(currentLernkarte == null)
            return;

        if(currentPositionInLernkartenList - 1 < 0)
            return;

        if(currentPositionInLernkartenList - 1 >= 0) {
            currentLernkarte = lernkartenList.get(currentPositionInLernkartenList - 1);
            currentPositionInLernkartenList--;
            initLernKarte(currentLernkarte);
            next.setCancelButton(false);

            if(currentPositionInLernkartenList - 1 < 0) {
                previous.setCancelButton(true);
            }
        }
    }

    public void ActionNext() {
        if(currentLernkarte == null)
            return;

        if(currentPositionInLernkartenList + 1 >= lernkartenList.size())
            return;

        if(currentPositionInLernkartenList + 1 < lernkartenList.size()) {
            currentLernkarte = lernkartenList.get(currentPositionInLernkartenList + 1);
            currentPositionInLernkartenList++;
            initLernKarte(currentLernkarte);
            previous.setCancelButton(false);

            if(currentPositionInLernkartenList + 1 >= lernkartenList.size()) {
                next.setCancelButton(true);
            }
        }
    }

    public void ActionKartenmischen() {
        if(currentLernkarte == null)
            return;

        Collections.shuffle(lernkartenList);
        currentPositionInLernkartenList = 0;
        currentLernkarte = lernkartenList.get(currentPositionInLernkartenList);
        initLernKarte(currentLernkarte);
    }

    public void ActionCreate() {
        layout.instanceLayout("LernkarteErstellen.fxml");
        ((LernKartenErstellenController) layout.getController()).setLayout(layout);
        ((LernKartenErstellenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((LernKartenErstellenController) layout.getController()).setNutzer(nutzer);
        ((LernKartenErstellenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((LernKartenErstellenController) layout.getController()).initLernKartenErstellen();
    }

    public void ActionBack() {
        layout.instanceLayout("projektgruppeUebersicht.fxml");
        ((ProjektgruppenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((ProjektgruppenController) layout.getController()).setLayout(layout);
        ((ProjektgruppenController) layout.getController()).setNutzer(nutzer);
        ((ProjektgruppenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ProjektgruppenController) layout.getController()).populateTableView();
        ((ProjektgruppenController) layout.getController()).setPGListeSeitentitel(lehrveranstaltung.getTitel());
    }

    public void initLernKarte(Lernkarte lernkarte) {
        frage.setText("Frage: " + lernkarte.getFrage());
        antwort.setText("");
        antwort.setVisible(false);
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }

    public Lehrveranstaltung getLehrveranstaltung() {
        return lehrveranstaltung;
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung = lehrveranstaltung;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe = projektgruppe;
    }

}
