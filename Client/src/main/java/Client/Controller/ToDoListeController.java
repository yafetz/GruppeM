package Client.Controller;

import Client.Layouts.Layout;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Projektgruppe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;

public class ToDoListeController {
    @FXML
    private Button createTodo;
    @FXML
    private TableColumn todo;
    @FXML
    private TableColumn responsibility;
    @FXML
    private TableColumn deadline;
    @FXML
    private TableView table;



    private Lehrveranstaltung lehrveranstaltung;
    private Layout layout;
    private Projektgruppe projektgruppe;


    public void setLehrveranstaltung(Lehrveranstaltung lehrveranstaltung) {
        this.lehrveranstaltung= lehrveranstaltung;
    }


    public void setLayout(Layout layout) {
        this.layout = layout;

    }

    public void setProjektgruppe(Projektgruppe projektgruppe) {
        this.projektgruppe=projektgruppe;
    }


    public void populateTableView() {
    }

    public void pressedCreateTodo(ActionEvent actionEvent) {
        layout.instanceLayout("toDoErstellen.fxml");
        ((ErstelleToDoController) layout.getController()).setLayout(layout);
        ((ErstelleToDoController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
        ((ErstelleToDoController) layout.getController()).setProjektgruppe(projektgruppe);

    }
}
