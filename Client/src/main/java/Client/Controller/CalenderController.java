package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.*;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.function.Predicate;

public class CalenderController {

    @FXML
    private CalendarView cv;
    private Layout layout;
    private Object nutzer;
    private int selectedLvId;

    public void setNutzer(Object nutzer){
        this.nutzer = nutzer;
    }

    public void Initilaize(){
        LadeAlleTermine();
        ChangeEreignisPopUp();
    }

    private void LadeAlleTermine() {
        HttpClient client = HttpClient.newHttpClient();
        long nutzerId = 0;
        if( layout.getNutzer() instanceof Lehrender ){
            nutzerId = ((Lehrender) layout.getNutzer()).getNutzerId().getId();
            System.out.println(nutzerId);
        }else if(layout.getNutzer() instanceof Student){
            nutzerId = ((Student) layout.getNutzer()).getNutzer().getId();
        }
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/kalender/alleTermine/"+nutzerId)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Termin> termine = mapper.readValue(response.body(), new TypeReference<List<Termin>>() {});
            for(int i = 0; i < termine.size(); i++){
                Entry e = cv.createEntryAt(termine.get(i).getVon().atZone(ZoneId.systemDefault()));
                e.setTitle(termine.get(i).getTitel());
                LocalDate endDate = LocalDate.of(termine.get(i).getBis().getYear(),termine.get(i).getBis().getMonth(),termine.get(i).getBis().getDayOfMonth());
                LocalTime endTime = LocalTime.of(termine.get(i).getBis().getHour(),termine.get(i).getBis().getMinute());
                e.changeEndDate(endDate);
                e.changeEndTime(endTime);
                e.setUserObject(termine.get(i));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){

    }

    public void ChangeEreignisPopUp(){
        cv.setEntryDetailsPopOverContentCallback(new Callback<DateControl.EntryDetailsPopOverContentParameter, Node>() {
            @Override
            public Node call(DateControl.EntryDetailsPopOverContentParameter param) {
                Object termin = param.getEntry().getUserObject();
                VBox panel = new VBox();
                TextField terminName = new TextField();
                terminName.setText(param.getEntry().getTitle());
                terminName.setPadding(new Insets(10,10,10,10));

                HBox vonDateTime = new HBox();

                TextField vonHour = new TextField();
                makeTextFieldOnlyNumbers(vonHour);
                vonHour.setText(String.valueOf(param.getEntry().getStartTime().getHour()) );

                Label divider = new Label();
                divider.setText(":");

                TextField vonMinutes = new TextField();
                makeTextFieldOnlyNumbers(vonMinutes);
                vonMinutes.setText(String.valueOf(param.getEntry().getStartTime().getMinute()) );

                DatePicker von = new DatePicker();
                von.setPadding(new Insets(10,10,10,10));
                von.setValue(param.getEntry().getStartDate());

                vonDateTime.getChildren().add(von);
                vonDateTime.getChildren().add(vonHour);
                vonDateTime.getChildren().add(divider);
                vonDateTime.getChildren().add(vonMinutes);

                HBox bisDateTime = new HBox();

                TextField bisHour = new TextField();
                makeTextFieldOnlyNumbers(bisHour);
                bisHour.setText(String.valueOf(param.getEntry().getStartTime().getHour()) );

                TextField bisMinutes = new TextField();
                makeTextFieldOnlyNumbers(bisMinutes);
                bisMinutes.setText(String.valueOf(param.getEntry().getStartTime().getMinute()) );

                DatePicker bis = new DatePicker();
                bis.setPadding(new Insets(10,10,10,10));
                bis.setValue(param.getEntry().getEndDate());

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
                if(termin instanceof Termin){
                    if(termin != null){
                        lehveranstaltungen.setValue(((Termin) termin).getLehrveranstaltung());
                    }
                }
                TextField ReminderValue = new TextField();

                makeTextFieldOnlyNumbers(ReminderValue);
                if(termin instanceof Termin){
                    if(termin != null){
                        ReminderValue.setText(String.valueOf(((Termin) termin).getReminderValue()));
                    }else{
                        ReminderValue.setText("0");
                    }
                }else{
                    ReminderValue.setText("0");
                }
                ReminderValue.setText("0");
                ComboBox reminderDropdown = new ComboBox();
                if(termin instanceof Termin){
                    if(termin != null){
                        reminderDropdown.setValue(((Termin) termin).getReminderArt());
                    }else{
                        reminderDropdown.setValue("Kein");
                    }
                }else{
                    reminderDropdown.setValue("Kein");
                }
                reminderDropdown.getItems().add("Kein");
                reminderDropdown.getItems().add("Minuten");
                reminderDropdown.getItems().add("Stunden");
                reminderDropdown.getItems().add("Tage");
                HBox reminder = new HBox();

                ComboBox reminderShow = new ComboBox();

                if(termin instanceof Termin){
                  if(termin != null){
                      reminderShow.setValue(((Termin) termin).getReminderShow());
                  }else{
                      reminderShow.setValue("Email");
                  }
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
                            System.out.println(vonMinutes.getText().length() + " " + vonMinutes.getText());
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
                            neuenTerminhinzufügen(terminName.getText(),VonDateTime,BisDateTime,ReminderValue.getText(),reminderDropdown.getValue().toString(),reminderShow.getValue().toString());
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
            System.out.println("von: " + von +" bis: "+ bis);
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
                System.out.println("Sende Nachricht");
                if (result.equals("OK")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    cv.createEntryAt(ZonedDateTime.from(LocalDateTime.parse(von, formatter)));
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
            System.out.println(nutzerId);
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
                        System.out.println(selectedLvId);
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
