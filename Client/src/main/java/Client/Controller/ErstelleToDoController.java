package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Projektgruppe;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ErstelleToDoController {
    @FXML
    private Button createTodo;
    @FXML
    public TextField responsibility_field;
    @FXML
    public TextField deadline_field;
    @FXML
    public TextField todo_titel;
    private Lehrveranstaltung lehrveranstaltung;
    private Projektgruppe projektgruppe;

    public void pressedCreateTodo(ActionEvent actionEvent) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String url = "http://localhost:8080/todo/create";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();

            ObjectMapper mapper = new ObjectMapper();


            entity.addTextBody("questions", questionsJson);
            entity.addTextBody("titel", quiz_titel.getText());
            entity.addTextBody("lehrenderId", String.valueOf(((Lehrender) nutzer).getId()));
            entity.addTextBody("lehrveranstaltungsId", String.valueOf(lehrveranstaltung.getId()));

            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLayout(Layout layout) {
    }

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung=lehrveranstaltung;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe=projektgruppe;
    }
}
