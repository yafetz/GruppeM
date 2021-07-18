package Client.Controller.ToDo;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Projektgruppe;
import Client.Modell.ToDoItem;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

public class ToDoListeController {
    @FXML
    private Button createTodo;
    @FXML
    private TableColumn<ToDoItem, String> todo;
    @FXML
    private TableColumn<ToDoItem, String>responsibility;
    @FXML
    private TableColumn<ToDoItem, String> deadline;
    @FXML
    private TableColumn<ToDoItem, String> finished;
    @FXML
    private TableView<ToDoItem> table;
    private Object nutzerId;

    public Object getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(Object nutzerId) {
        this.nutzerId = nutzerId;
    }

    private Lehrveranstaltung lehrveranstaltung;
    private Layout layout;
    private Projektgruppe projektgruppe;

    public ToDoItem getItem() {
        return item;
    }

    public void setItem(ToDoItem item) {
        this.item = item;
    }

    private ToDoItem item;

    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung= lehrveranstaltung;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe=projektgruppe;
    }


    public void pressedCreateTodo(ActionEvent actionEvent) {
        layout.instanceLayout("toDoErstellen.fxml");
        ((ErstelleToDoController) layout.getController()).setLayout(layout);
        ((ErstelleToDoController) layout.getController()).setNutzer(nutzerId);
        ((ErstelleToDoController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ErstelleToDoController) layout.getController()).setProjektgruppe(projektgruppe);
        ((ErstelleToDoController) layout.getController()).ladeGruppenmitglieder();
    }

    public void populateTableView() {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/todo/"+ projektgruppe.getId())).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());


            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            java.util.List<ToDoItem> todos = mapper.readValue(response.body(), new TypeReference<List<ToDoItem>>() {});

            todo.setCellValueFactory(new PropertyValueFactory<ToDoItem,String>("titel"));
            responsibility.setCellValueFactory(new PropertyValueFactory<ToDoItem,String>("verantwortliche"));
            deadline.setCellValueFactory(new PropertyValueFactory<ToDoItem,String>("deadline"));
            finished.setCellValueFactory(new PropertyValueFactory<ToDoItem,String>("erledigt"));


//            Angelehnt an: https://stackoverflow.com/questions/35562037/how-to-set-click-event-for-a-cell-of-a-table-column-in-a-tableview
            todo.setCellFactory(tablecell -> {
                TableCell<ToDoItem, String> cell = new TableCell<ToDoItem, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(javafx.scene.Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                bearbeiten(cell.getTableRow().getItem());
                            }
                        }
                );
                return cell;
            });
            responsibility.setCellFactory(tablecell -> {
                TableCell<ToDoItem, String> cell = new TableCell<ToDoItem, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(javafx.scene.Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {

                            }
                        }
                );
                return cell;
            });
            deadline.setCellFactory(tablecell -> {
                TableCell<ToDoItem, String> cell = new TableCell<ToDoItem, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(javafx.scene.Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {

                            }
                        }
                );
                return cell;
            });

//            ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<ToDoItem> obsLv = FXCollections.observableList(todos);
            table.setItems(obsLv);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void bearbeiten(ToDoItem todoitem) {
        layout.instanceLayout("todoBearbeiten.fxml");
        ((TodoBearbeitenController) layout.getController()).setLayout(layout);
        ((TodoBearbeitenController) layout.getController()).setToDoItem(todoitem);
        ((TodoBearbeitenController) layout.getController()).setNutzer(nutzerId);
        ((TodoBearbeitenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((TodoBearbeitenController) layout.getController()).setProjektgruppe(projektgruppe);
        ((TodoBearbeitenController) layout.getController()).ladeGruppenmitglieder();
    }
}
