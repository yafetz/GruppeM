package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Gruppenmitglied;
import Client.Modell.Projektgruppe;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GruppenmitgliederController {
    @FXML
    private TableColumn<Gruppenmitglied, String> vorname;
    @FXML
    private TableColumn<Gruppenmitglied, String> nachname;
    @FXML
    private TableView<Gruppenmitglied> tabelle;

    private Projektgruppe projektgruppe;

    private Layout layout;

    public void setLayout(Layout layout) {
        this.layout = layout;
    }
    public void zeigeMitglieder(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/projektgruppe/mitgliederliste"+projektgruppe.getId())).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Gruppenmitglied> mitglieder = mapper.readValue(response.body(), new TypeReference<List<Gruppenmitglied>>() {});
            vorname.setCellValueFactory(new PropertyValueFactory<Gruppenmitglied,String>("studentVorname"));
            nachname.setCellValueFactory(new PropertyValueFactory<Gruppenmitglied,String>("studentNachname"));


            vorname.setCellFactory(tablecell -> {
                TableCell<Gruppenmitglied, String> cell = new TableCell<Gruppenmitglied, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                //zurLehrveranstaltungHinzufügen(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

            nachname.setCellFactory(tablecell -> {
                TableCell<Gruppenmitglied, String> cell = new TableCell<Gruppenmitglied, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                //zurLehrveranstaltungHinzufügen(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });

            ObservableList<Gruppenmitglied> obsLv = FXCollections.observableList(mitglieder);
            tabelle.setItems(obsLv);
        } catch (IOException e) {
            System.out.println("ERROR HIER");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("ERROR DA"); // xD
            e.printStackTrace();
        }
    }
}
