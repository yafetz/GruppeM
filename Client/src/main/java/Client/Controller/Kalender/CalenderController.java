package Client.Controller.Kalender;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class CalenderController {

    @FXML
    private CalendarView cv;
    private Layout layout;
    private Object nutzer;
    private int selectedLvId;
    private List<Termin> alleTermine;
    private List<Termin> reminder = new ArrayList<>();
    private Timeline timer;
    private LocalDateTime datum;

    public void setNutzer(Object nutzer){
        this.nutzer = nutzer;
    }

    public void Initilaize(){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/login/getDate/")).build();
        HttpResponse<String> response2 = null;
        try {
            response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response2.body());

            String str = response2.body().replace("T", " ").replace("\"", "");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            datum = LocalDateTime.parse(str, formatter);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        cv.getDayPage().getYearMonthView().setDate(LocalDate.of(datum.getYear(), datum.getMonth(), datum.getDayOfMonth()));
        LadeAlleTermine();
        ChangeEreignisPopUp();
        ReminderPopUp();
    }


    public void ReminderPopUp(){
        timer  = new Timeline(
                new KeyFrame(Duration.seconds(5),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                LocalDateTime jetzt = datum;
                                for(int i = 0; i < reminder.size(); i++) {
                                    if (reminder.get(i).getReminderShow().equals("PopUp")) {
                                        if (reminder.get(i).getReminderArt().equalsIgnoreCase("Minuten")) {
                                            if (reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMinute() == jetzt.getMinute()
                                                    && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getHour() == jetzt.getHour()
                                                    && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                                                    && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                                                    && reminder.get(i).getVon().minusMinutes(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                                                Alert fehler = new Alert(Alert.AlertType.INFORMATION);
                                                fehler.setTitle("Ihr Termin steht an !");
                                                fehler.setHeaderText("Ihr Termin steht an !");
                                                fehler.setContentText("In " + reminder.get(i).getReminderValue() + " Minuten haben Sie einen Termin: " + reminder.get(i).getTitel());
                                                fehler.show();
                                                reminder.remove(i);
                                            }
                                        } else if (reminder.get(i).getReminderArt().equalsIgnoreCase("Stunden")) {
                                            if (reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getHour() == jetzt.getHour()
                                                    && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                                                    && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                                                    && reminder.get(i).getVon().minusHours(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                                                Alert fehler = new Alert(Alert.AlertType.INFORMATION);
                                                fehler.setTitle("Ihr Termin steht an !");
                                                fehler.setHeaderText("Ihr Termin steht an !");
                                                fehler.setContentText("In " + reminder.get(i).getReminderValue() + " Stunden haben Sie einen Termin: " + reminder.get(i).getTitel());
                                                fehler.show();
                                                reminder.remove(i);
                                            }
                                        } else if (reminder.get(i).getReminderArt().equalsIgnoreCase("Tage")) {
                                            if (reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getDayOfMonth() == jetzt.getDayOfMonth()
                                                    && reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getMonth().equals(jetzt.getMonth())
                                                    && reminder.get(i).getVon().minusDays(reminder.get(i).getReminderValue()).getYear() == jetzt.getYear()) {
                                                Alert fehler = new Alert(Alert.AlertType.INFORMATION);
                                                fehler.setTitle("Ihr Termin steht an !");
                                                fehler.setHeaderText("Ihr Termin steht an !");
                                                fehler.setContentText("In " + reminder.get(i).getReminderValue() + " Tagen haben Sie einen Termin: " + reminder.get(i).getTitel());
                                                fehler.show();
                                                reminder.remove(i);
                                            }
                                        }
                                    }
                                }
                            }
                        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }


    private void LadeAlleTermine() {
        HttpClient client = HttpClient.newHttpClient();
        long nutzerId = 0;
        if( layout.getNutzer() instanceof Lehrender ){
            nutzerId = ((Lehrender) layout.getNutzer()).getNutzerId().getId();
//            System.out.println(nutzerId);
        }else if(layout.getNutzer() instanceof Student){
            nutzerId = ((Student) layout.getNutzer()).getNutzer().getId();
        }
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/kalender/alleTermine/"+nutzerId)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Termin> termine = mapper.readValue(response.body(), new TypeReference<List<Termin>>() {});
            if(alleTermine == null){
                alleTermine = new ArrayList<>();
            }
            for(int i = 0; i < termine.size(); i++){
                    LocalDateTime StartTermin = termine.get(i).getVon();
                    Entry e = cv.createEntryAt(StartTermin.atZone(ZoneId.systemDefault()));
                    e.setTitle(termine.get(i).getTitel());
                    LocalDate endDate = LocalDate.of(termine.get(i).getBis().getYear(), termine.get(i).getBis().getMonth(), termine.get(i).getBis().getDayOfMonth());
                    LocalTime endTime = LocalTime.of(termine.get(i).getBis().getHour(), termine.get(i).getBis().getMinute());

                    LocalDate startDate = LocalDate.of(StartTermin.getYear(), StartTermin.getMonth(), StartTermin.getDayOfMonth());
                    LocalTime startTime = LocalTime.of(StartTermin.getHour(), StartTermin.getMinute());
                    e.changeStartDate(startDate);
                    e.changeStartTime(startTime);
                    e.changeEndDate(endDate);
                    e.changeEndTime(endTime);
                    e.setUserObject(termine.get(i));
                    alleTermine.add(termine.get(i));
                    LocalDateTime jetzt = LocalDateTime.now();
                    if (StartTermin.isAfter(jetzt) && termine.get(i).getReminderShow().equalsIgnoreCase("PopUp")) {
                        if (termine.get(i).getReminderArt().equalsIgnoreCase("Minuten")) {
                            if (StartTermin.minusMinutes(termine.get(i).getReminderValue()).isAfter(jetzt)) {
                                reminder.add(termine.get(i));
                            }
                        } else if (termine.get(i).getReminderArt().equalsIgnoreCase("Stunden")) {
                            if (StartTermin.minusHours(termine.get(i).getReminderValue()).isAfter(jetzt)) {
                                reminder.add(termine.get(i));
                            }
                        } else if (termine.get(i).getReminderArt().equalsIgnoreCase("Tage")) {
                            if (StartTermin.minusDays(termine.get(i).getReminderValue()).isAfter(jetzt)) {
                                reminder.add(termine.get(i));
                            }
                        }
                    }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean CheckIfTerminInAlleTermine(Termin termin, List<Termin> alleTermine) {
        for(int i = 0; i < alleTermine.size(); i++){
            if(termin.getId().equals(alleTermine.get(i).getId())){
                return true;
            }
        }
        return false;
    }

    public void initialize(){

    }

    public void ChangeEreignisPopUp(){
        cv.setEntryDetailsPopOverContentCallback(new Callback<DateControl.EntryDetailsPopOverContentParameter, Node>() {
            @Override
            public Node call(DateControl.EntryDetailsPopOverContentParameter param) {
                Termin termin = (Termin) param.getEntry().getUserObject();
                VBox panel = new VBox();
                TextField terminName = new TextField();
                if(termin != null){
                    terminName.setText(termin.getTitel());
                }else {
                    terminName.setText(param.getEntry().getTitle());
                }
                terminName.setPadding(new Insets(10,10,10,10));

                HBox vonDateTime = new HBox();

                TextField vonHour = new TextField();
                makeTextFieldOnlyNumbers(vonHour);
                if(termin != null){
                    vonHour.setText(String.valueOf(termin.getVon().getHour()));
                }else {
                    vonHour.setText(String.valueOf(param.getEntry().getStartTime().getHour()));
                }
                Label divider = new Label();
                divider.setText(":");

                TextField vonMinutes = new TextField();
                makeTextFieldOnlyNumbers(vonMinutes);
                if(termin != null){
                    vonMinutes.setText(String.valueOf(termin.getVon().getMinute()));
                }else {
                    vonMinutes.setText(String.valueOf(param.getEntry().getStartTime().getMinute()));
                }

                DatePicker von = new DatePicker();
                von.setPadding(new Insets(10,10,10,10));
                if(termin != null){
                    von.setValue(termin.getVon().toLocalDate());
                }else {
                    von.setValue(param.getEntry().getStartDate());
                }

                vonDateTime.getChildren().add(von);
                vonDateTime.getChildren().add(vonHour);
                vonDateTime.getChildren().add(divider);
                vonDateTime.getChildren().add(vonMinutes);

                HBox bisDateTime = new HBox();

                TextField bisHour = new TextField();
                makeTextFieldOnlyNumbers(bisHour);
                if(termin != null){
                    bisHour.setText(String.valueOf(termin.getBis().getHour()));
                }else {
                    bisHour.setText(String.valueOf(param.getEntry().getEndTime().getHour()));
                }

                TextField bisMinutes = new TextField();
                makeTextFieldOnlyNumbers(bisMinutes);
                if(termin != null){
                    bisMinutes.setText(String.valueOf(termin.getBis().getMinute()));
                }else {
                    bisMinutes.setText(String.valueOf(param.getEntry().getEndTime().getMinute()));
                }

                DatePicker bis = new DatePicker();
                bis.setPadding(new Insets(10,10,10,10));
                if(termin != null){
                    bis.setValue(termin.getBis().toLocalDate());
                }else {
                    bis.setValue(param.getEntry().getEndDate());
                }

                bisDateTime.getChildren().add(bis);
                bisDateTime.getChildren().add(bisHour);
                bisDateTime.getChildren().add(divider);
                bisDateTime.getChildren().add(bisMinutes);

                Label vonLabel = new Label();
                vonLabel.setLabelFor(von);
                vonLabel.setText("von: ");

                Label bisLabel = new Label();
                bisLabel.setLabelFor(bis);
                bisLabel.setText("bis: ");

                Label nameLabel = new Label();
                nameLabel.setLabelFor(terminName);
                nameLabel.setText("Termin Name: ");

                Label reminderLabel = new Label();
                nameLabel.setLabelFor(terminName);
                nameLabel.setText("Reminder: ");

                Label terminerstellenLabel = new Label();
                nameLabel.setLabelFor(terminName);
                nameLabel.setText("");

                Label lvLabel = new Label();
                lvLabel.setText("Lehveranstaltung: ");

                ComboBox lehveranstaltungen = new ComboBox();

                ladeLehrveranstaltungen(lehveranstaltungen);
                    if(termin != null){
                        lehveranstaltungen.setValue(((Termin) termin).getLehrveranstaltung());
                }
                TextField ReminderValue = new TextField();

                makeTextFieldOnlyNumbers(ReminderValue);
                    if(termin != null){
                        ReminderValue.setText(String.valueOf(((Termin) termin).getReminderValue()));
                    }else{
                        ReminderValue.setText("0");
                    }
                ComboBox reminderDropdown = new ComboBox();
                    if(termin != null){
                        reminderDropdown.setValue(((Termin) termin).getReminderArt());
                    }else{
                        reminderDropdown.setValue("Kein");
                    }
                reminderDropdown.getItems().add("Kein");
                reminderDropdown.getItems().add("Minuten");
                reminderDropdown.getItems().add("Stunden");
                reminderDropdown.getItems().add("Tage");
                HBox reminder = new HBox();

                ComboBox reminderShow = new ComboBox();

                  if(termin != null){
                      reminderShow.setValue(((Termin) termin).getReminderShow());
                  }else{
                      reminderShow.setValue("Email");
                  }

                reminderShow.getItems().add("Email");
                reminderShow.getItems().add("PopUp");

                Button TerminErstellen = new Button();
                TerminErstellen.setText("Termin Erstellen/Bearbeiten");
                TerminErstellen.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //Alle Rückgabe Werte austesten
                        if(selectedLvId != 0) {
                            String VonDateTime = "";
                            String BisDateTime = "";
//                            System.out.println(vonMinutes.getText().length() + " " + vonMinutes.getText());
                            String vonM = vonMinutes.getText();
                            String vonH = vonHour.getText();
                            String bisM = bisMinutes.getText();
                            String bisH = bisHour.getText();
                            if(vonMinutes.getText().length() == 1){
                                vonM += "0";
                            }
                            if(vonHour.getText().length() == 1){
                                vonH = "0"+vonH;
                            }
                            VonDateTime = von.getValue().toString() + " "+ vonH+":"+ vonM + ":00";
                            if(bisMinutes.getText().length() == 1){
                                bisM += "0";
                            }
                            if(bisHour.getText().length() == 1){
                                bisH = "0"+bisH;
                            }
                            BisDateTime = bis.getValue().toString() + " "+ bisH+":"+ bisM + ":00";

                            //System.out.println( ((Lehrender) layout.getNutzer()).getNutzerId().getId());
                            //Post Anfrage an den Server um den Termin zu erstellen
                            if(termin == null) {
                                neuenTerminhinzufügen(terminName.getText(), VonDateTime, BisDateTime, ReminderValue.getText(), reminderDropdown.getValue().toString(), reminderShow.getValue().toString());
                            }else{
                                //Bearbeiten
                                TerminBearbeiten(String.valueOf(termin.getId()),terminName.getText(), VonDateTime, BisDateTime, ReminderValue.getText(), reminderDropdown.getValue().toString(), reminderShow.getValue().toString());
                            }
                            param.getEntry().removeFromCalendar();
                        }else{
                            Alert fehler = new Alert(Alert.AlertType.ERROR);
                            fehler.setTitle("Eingegebene Daten sind falsch!");
                            fehler.setContentText("Sie müssen eine Lehrveranstaltung auswählen!");
                            fehler.showAndWait();
                        }
                    }
                });

                reminder.getChildren().add(ReminderValue);
                reminder.getChildren().add(reminderDropdown);
                panel.setPadding(new Insets(10,10,10,10));
                panel.getChildren().add(nameLabel);
                panel.getChildren().add(terminName);
                panel.getChildren().add(vonLabel);
                panel.getChildren().add(vonDateTime);
                panel.getChildren().add(bisLabel);
                panel.getChildren().add(bisDateTime);
                panel.getChildren().add(lvLabel);
                panel.getChildren().add(lehveranstaltungen);
                panel.getChildren().add(reminderLabel);
                panel.getChildren().add(reminder);
                panel.getChildren().add(reminderShow);
                panel.getChildren().add(terminerstellenLabel);
                panel.getChildren().add(TerminErstellen);
                return panel;
            }
        });
    }

    private void neuenTerminhinzufügen(String titel, String von, String bis, String reminderValue, String reminderArt, String reminderShow){
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {
            String url = "http://localhost:8080/kalender/neuerTermin";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
//            System.out.println("von: " + von +" bis: "+ bis);
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("titel", titel );
            entity.addTextBody("von", von);
            entity.addTextBody("bis", bis);
            entity.addTextBody("lehrveranstaltungsId", String.valueOf(selectedLvId) );
            entity.addTextBody("reminderValue", reminderValue );
            entity.addTextBody("reminderArt", reminderArt);
            entity.addTextBody("reminderShow", reminderShow);
            if(layout.getNutzer() instanceof Student){
                entity.addTextBody("nutzer", String.valueOf(((Student) layout.getNutzer()).getNutzer().getId()));
            }else if(layout.getNutzer() instanceof Lehrender){
                entity.addTextBody("nutzer", String.valueOf(((Lehrender) layout.getNutzer()).getNutzerId().getId()));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);
            try (CloseableHttpResponse response = client1.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
//                System.out.println("Sende Nachricht");
                if (result.equals("OK")) {
                    timer.stop();
                    layout.instanceLayout("Calender.fxml");
                    ((CalenderController) layout.getController()).setLayout(layout);
                    ((CalenderController) layout.getController()).Initilaize();

                }
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Fehler!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler 2!");
        }
    }

    private void TerminBearbeiten(String id,String titel, String von, String bis, String reminderValue, String reminderArt, String reminderShow){
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {
            String url = "http://localhost:8080/kalender/Terminbearbeiten";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("id", id );
            entity.addTextBody("titel", titel );
            entity.addTextBody("von", von);
            entity.addTextBody("bis", bis);
            entity.addTextBody("lehrveranstaltungsId", String.valueOf(selectedLvId) );
            entity.addTextBody("reminderValue", reminderValue );
            entity.addTextBody("reminderArt", reminderArt);
            entity.addTextBody("reminderShow", reminderShow);
            if(layout.getNutzer() instanceof Student){
                entity.addTextBody("nutzer", String.valueOf(((Student) layout.getNutzer()).getNutzer().getId()));
            }else if(layout.getNutzer() instanceof Lehrender){
                entity.addTextBody("nutzer", String.valueOf(((Lehrender) layout.getNutzer()).getNutzerId().getId()));
            }
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);
            try (CloseableHttpResponse response = client1.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
//                System.out.println("Sende Nachricht");
                if (result.equals("OK")) {
                    timer.stop();
                    layout.instanceLayout("Calender.fxml");
                    ((CalenderController) layout.getController()).setLayout(layout);
                    ((CalenderController) layout.getController()).Initilaize();

                }
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Fehler!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler 2!");
        }
    }

    private void ladeLehrveranstaltungen(ComboBox lehveranstaltungen) {
        HttpClient client = HttpClient.newHttpClient();
        long nutzerId = 0;
        if( layout.getNutzer() instanceof Lehrender ){
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

    public Layout getLayout(){
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void makeTextFieldOnlyNumbers(TextField feld){
        feld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    feld.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
