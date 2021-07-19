package Client.Controller.ToDo;

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
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ErstelleToDoController {
    @FXML
    private DatePicker deadline;
    @FXML
    private ComboBox<Gruppenmitglied> gruppenmitglieder;
    @FXML
    private TextField todo_titel;
    @FXML
    private Button createTodo;
    @FXML
    private CheckBox done;


    private Object nutzer;
    private Layout layout;



    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;
    private int selectedGruppenmitglied= 0;

    public void setLayout(Layout layout) {
        this.layout= layout;

    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung=lehrveranstaltung;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe=projektgruppe;
    }
    public Object getNutzer() {
        return nutzer;
    }

    public void setNutzer(Object nutzer) {
        this.nutzer = nutzer;
    }


    public void pressedCreateTodo(ActionEvent actionEvent) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String url = "http://localhost:8080/todo/create";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();

            ObjectMapper mapper = new ObjectMapper();

            entity.addTextBody("datum", deadline.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            entity.addTextBody("titel", todo_titel.getText());
            entity.addTextBody("verantwortliche", String.valueOf(gruppenmitglieder.getSelectionModel().getSelectedItem().getStudent().getVorname()));

            entity.addTextBody("projektgruppeId",String.valueOf(projektgruppe.getId()));
            entity.addTextBody("nutzerId",String.valueOf(( gruppenmitglieder.getSelectionModel().getSelectedItem().getId() )));

            if (done.isSelected()== true) {
                entity.addTextBody("erledigt", "fertig");
            }
            else if((done.isSelected()== false)) {
                entity.addTextBody("erledigt", "in Bearbeitung");
            }


            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
            }

            layout.instanceLayout("toDoListe.fxml");
            ((ToDoListeController) layout.getController()).setLayout(layout);
            ((ToDoListeController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
            ((ToDoListeController) layout.getController()).setProjektgruppe(projektgruppe);
            ((ToDoListeController) layout.getController()).populateTableView();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public void ladeGruppenmitglieder() {
        HttpClient client = HttpClient.newHttpClient();
        long nutzerId = 0;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/gruppenmitglieder/"+projektgruppe.getId())).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Gruppenmitglied> mitglied = mapper.readValue(response.body(), new TypeReference<List<Gruppenmitglied>>() {});
            ObservableList<Gruppenmitglied> obsLv = FXCollections.observableList(mitglied);
            gruppenmitglieder.setItems(obsLv);
            gruppenmitglieder.setConverter(new StringConverter() {
                @Override
                public String toString(Object object) {
                    if(object != null) {
                        return ((Gruppenmitglied) object).getStudent().getVorname();
                    }else{
                        return "";
                    }
                }

                @Override
                public Object fromString(String string) {
                    return gruppenmitglieder.getItems().stream().filter(ap ->
                            ((Gruppenmitglied)ap).getStudent().getVorname().equals(string)).findFirst().orElse(null);
                }
            });
            gruppenmitglieder.valueProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    if(newValue != null){
                        selectedGruppenmitglied = ((Gruppenmitglied) newValue).getId();
//                        System.out.println(selectedGruppenmitglied);
                    }
                }
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}
