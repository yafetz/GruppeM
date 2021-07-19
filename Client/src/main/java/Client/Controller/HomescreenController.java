package Client.Controller;

import Client.Controller.Chat.ChatController;
import Client.Controller.Lehrveranstaltung.LehrveranstaltungBeitretenController;
import Client.Controller.Lehrveranstaltung.LehrveranstaltungErstellenController;
import Client.Controller.Lehrveranstaltung.LehrveranstaltungsuebersichtsseiteController;
import Client.Controller.NutzerProfil.UserprofilController;
import Client.Layouts.Layout;
import Client.Modell.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class HomescreenController {

    @FXML private ScrollPane freundelistescrollpane;
    @FXML private Label meineFreundeLabel;
    @FXML private TableView<Lehrveranstaltung> meineLv;
    @FXML private TableColumn<Lehrveranstaltung, Long> col_LvId;
    @FXML private TableColumn<Lehrveranstaltung, String> col_LvTitel;
    @FXML private TableColumn<Lehrveranstaltung, String> col_LvSemester;
    @FXML private TableColumn<Lehrveranstaltung, String> col_LvArt;
    @FXML private TableColumn<Lehrveranstaltung, String> col_LvLehrende;
    @FXML public Button addCourseBtn;
    @FXML private TableView<Freundschaft> freundeTabelle;
    @FXML private TableColumn<Freundschaft, String> vorname;
    @FXML private TableColumn<Freundschaft, String> nachname;

    private Object nutzerInstanz;
    private long nutzerId;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setNutzerInstanz(layout.getNutzer());
    }

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
        populateMeineKurseTableview();
        if (nutzerInstanz instanceof Student) {
            populateFreundeslisteTableView();
        } else if (nutzerInstanz instanceof  Lehrender) {
            freundeTabelle.setVisible(false);
            meineFreundeLabel.setVisible(false);
            freundelistescrollpane.setVisible(false);
        }
        if (nutzerInstanz instanceof Student) {
//            System.out.println("nutzerInstanz ist Instanz von Student");
            addCourseBtn.setVisible(false);
        } else if (nutzerInstanz instanceof Lehrender) {
//            System.out.println("nutzerInstanz ist Instanz von Lehrender");
        }
    }

    public void populateMeineKurseTableview() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;

        if (nutzerInstanz instanceof Lehrender) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Lehrender) nutzerInstanz).getNutzerId().getId())).build();
        }
        if (nutzerInstanz instanceof Student) {
            request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/meine/nutzerId=" + ((Student) nutzerInstanz).getNutzer().getId())).build();
        }
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            mapping data in response.body() to a list of teilnehmerliste-objects
            ObjectMapper mapper = new ObjectMapper();

//            System.out.println(response.body());

            List<TeilnehmerListe> teilnehmerListe = mapper.readValue(response.body(), new TypeReference<List<TeilnehmerListe>>() {});
            List<Lehrveranstaltung> lehrveranstaltungen = new LinkedList<>();

            for(TeilnehmerListe teilnehmerListe1 : teilnehmerListe) {
                lehrveranstaltungen.add(teilnehmerListe1.getLehrveranstaltung());
            }

            List<Lehrveranstaltung> neueListe = new ArrayList<>();
            List<Integer> jahrSommer = new ArrayList<>();
            List<Integer> jahrWinter = new ArrayList<>();
            List<Lehrveranstaltung> sommer = new ArrayList<>();
            List<Lehrveranstaltung> winter = new ArrayList<>();
            if(!lehrveranstaltungen.isEmpty()) {

                for (int i = 0; i < lehrveranstaltungen.size(); i++) {
                    String semester = lehrveranstaltungen.get(i).getSemester();
                    if (semester.contains("/")) {
                        String win=semester.replace("WiSe ", "");
                        String[] zweiteKomponente = win.split("/");
                        jahrWinter.add(Integer.valueOf(zweiteKomponente[1]));
                        winter.add(lehrveranstaltungen.get(i));
                    } else {
                        String som = semester.replace("SoSe ", "");
                        jahrSommer.add(Integer.valueOf(som));
                        sommer.add(lehrveranstaltungen.get(i));
                    }
                }



                if (winter != null && sommer != null) {
                    for (int i = 0; i < winter.size(); i++) {
                        winter.get(i).setJahr(jahrWinter.get(i));
                    }
                    winter.sort(Comparator.comparing(Lehrveranstaltung::getJahr).reversed());

                    for (int i = 0; i < sommer.size(); i++) {
                        sommer.get(i).setJahr(jahrSommer.get(i));
                    }
                    sommer.sort(Comparator.comparing(Lehrveranstaltung::getJahr).reversed());

                    for (int i = 0; i < sommer.size(); i++) {
                        winter = sort(winter, sommer.get(i));
                    }
                    neueListe=winter;
                }
                else if (winter != null && sommer == null) {
                    for (int i = 0; i < winter.size(); i++) {
                        winter.get(i).setJahr(jahrWinter.get(i));
                    }
                    winter.sort(Comparator.comparing(Lehrveranstaltung::getJahr).reversed());
                    neueListe = winter;
                }
                else if (winter == null && sommer != null) {
                    for (int i = 0; i < sommer.size(); i++) {
                        sommer.get(i).setJahr(jahrSommer.get(i));
                    }
                    sommer.sort(Comparator.comparing(Lehrveranstaltung::getJahr).reversed());
                    neueListe = sommer;
                } else {
                    neueListe = null;
                }
            }






//            set property of each column of the tableview
            col_LvId.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,Long>("id"));
            col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Titel"));
            col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Semester"));
            col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("Art"));
            col_LvLehrende.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("lehrenderName"));

//            Angelehnt an: https://stackoverflow.com/questions/35562037/how-to-set-click-event-for-a-cell-of-a-table-column-in-a-tableview
            col_LvTitel.setCellFactory(tablecell -> {
                TableCell<Lehrveranstaltung, String> cell = new TableCell<Lehrveranstaltung, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToCourseOverview(cell.getTableRow().getItem().getId());
                            }
                        }
                );
                return cell;
            });
            col_LvTitel.setCellFactory(tablecell -> {
                TableCell<Lehrveranstaltung, String> cell = new TableCell<Lehrveranstaltung, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToCourseOverview(cell.getTableRow().getItem().getId());

                            }
                        }
                );
                return cell;
            });
            col_LvSemester.setCellFactory(tablecell -> {
                TableCell<Lehrveranstaltung, String> cell = new TableCell<Lehrveranstaltung, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToCourseOverview(cell.getTableRow().getItem().getId());

                            }
                        }
                );
                return cell;
            });
            col_LvArt.setCellFactory(tablecell -> {
                TableCell<Lehrveranstaltung, String> cell = new TableCell<Lehrveranstaltung, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToCourseOverview(cell.getTableRow().getItem().getId());

                            }
                        }
                );
                return cell;
            });
            col_LvLehrende.setCellFactory(tablecell -> {
                TableCell<Lehrveranstaltung, String> cell = new TableCell<Lehrveranstaltung, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                redirectToCourseOverview(cell.getTableRow().getItem().getId());

                            }
                        }
                );
                return cell;
            });
            // ObservableList is required to populate the table meineLv using .setItems()
            ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(neueListe);
            meineLv.setItems(obsLv);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public List<Lehrveranstaltung> sort(List<Lehrveranstaltung> list, Lehrveranstaltung var) {
        List<Lehrveranstaltung> neu = new ArrayList<Lehrveranstaltung>(list.size() + 1);
        int i = 0;
        while ((i < list.size()) && (list.get(i).getJahr() > var.getJahr())) {
            neu.add(list.get(i));
            i++;
        }
        neu.add(var);
        while (i < list.size()) {
            neu.add(list.get(i));
            i++;
        }
        return neu;
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
            if (nutzerInstanz instanceof Lehrender) {
                long lehrId = ((Lehrender) nutzerInstanz).getNutzerId().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/beitreten/check/"+ lehrveranstaltungId + "&"+lehrId)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println("Instanz Lehrender "+memberResponse.body());

                if(memberResponse.body().equals("true")) {
                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);

                }
                else {
                    layout.instanceLayout("lehrveranstaltungBeitreten.fxml");
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLayout(layout);
                }
            }
            if (nutzerInstanz instanceof Student) {

                long id = ((Student) nutzerInstanz).getNutzer().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

//                System.out.println("Student Instanz "+memberResponse.body());

                if(memberResponse.body().equals("true")){
                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);

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

    public void addCourse(ActionEvent actionEvent) {
        layout.instanceLayout("lehrveranstaltungErstellen.fxml");
        ((LehrveranstaltungErstellenController) layout.getController()).setLayout(layout);
    }

    public void populateFreundeslisteTableView(){
        if (nutzerInstanz instanceof Student) {
            nutzerId = ((Student)nutzerInstanz).getNutzer().getId();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/freundschaft/"+nutzerId)).build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            List<Freundschaft> freundschaften = mapper.readValue(response.body(), new TypeReference<List<Freundschaft>>() {});

            for(int i=0;i<freundschaften.size();i++){
                long nutzerid= ((Student)nutzerInstanz).getNutzer().getId();
                if(freundschaften.get(i).isStatus()){

                    if(freundschaften.get(i).getAnfragender_nutzer().getId()==nutzerid){
                        vorname.setCellValueFactory(new PropertyValueFactory<>("angefragter_nutzer_vorname"));
                        nachname.setCellValueFactory(new PropertyValueFactory<>("angefragter_nutzer_nachname"));
                        freundeTabelle.getItems().add(freundschaften.get(i));
                    }
                    else {
                        vorname.setCellValueFactory(new PropertyValueFactory<>("anfragender_nutzer_vorname"));
                        nachname.setCellValueFactory(new PropertyValueFactory<>("anfragender_nutzer_nachname"));
                        freundeTabelle.getItems().add(freundschaften.get(i));
                    }
                }
            }

            addButtonToTable("Chat");

            vorname.setCellFactory(tablecell -> {
                TableCell<Freundschaft, String> cell = new TableCell<Freundschaft, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                Freundschaft f = cell.getTableRow().getItem();
                                long nutzerid= ((Student)nutzerInstanz).getNutzer().getId();
                                if(f.getAnfragender_nutzer().getId() == nutzerid) {
                                    redirectToUserprofile(f.getAngefragter_nutzer().getId());
                                }else{
                                    redirectToUserprofile(f.getAnfragender_nutzer().getId());
                                }
//                                System.out.println("id vom angeklickten nutzer aus tabelle: " + cell.getTableRow().getItem().getId());
                            }
                        }
                );

                return cell;
            });
            nachname.setCellFactory(tablecell -> {
                TableCell<Freundschaft, String> cell = new TableCell<Freundschaft, String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        setText(empty ? null : item);
                    }
                };
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(e -> {
                            if (!cell.isEmpty()) {
                                Freundschaft f = cell.getTableRow().getItem();
                                long nutzerid= ((Student)nutzerInstanz).getNutzer().getId();
                                if(f.getAnfragender_nutzer().getId() == nutzerid) {
                                    redirectToUserprofile(f.getAngefragter_nutzer().getId());
                                }else{
                                    redirectToUserprofile(f.getAnfragender_nutzer().getId());
                                }
                            }
                        }
                );

                return cell;
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable(String methode) {
        TableColumn<Freundschaft, Void> colBtn = new TableColumn(methode);

        Callback<TableColumn<Freundschaft, Void>, TableCell<Freundschaft, Void>> cellFactory = new Callback<TableColumn<Freundschaft, Void>, TableCell<Freundschaft, Void>>() {
            @Override
            public TableCell<Freundschaft, Void> call(final TableColumn<Freundschaft, Void> param) {
                final TableCell<Freundschaft, Void> cell = new TableCell<Freundschaft, Void>() {

                    private final Button btn = new Button(methode);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            //Weiterleiten zum Chat
                            long chatId = getTableRow().getItem().getChat().getId();
                            layout.instanceLayout("chat.fxml");
                            ((ChatController) layout.getController()).setLayout(layout);
                            ((ChatController) layout.getController()).setChatraumid((int) chatId);
                            ((ChatController) layout.getController()).setNutzer(nutzerInstanz);
                            ((ChatController) layout.getController()).scheduler();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        freundeTabelle.getColumns().add(colBtn);
    }

    public void redirectToUserprofile(Integer teilnehmerId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/teilnehmer/teilnehmerId=" + teilnehmerId)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            if (response.body().contains("matrikelnummer")) {
                Student vergleichNutzer = mapper.readValue(response.body(), Student.class);
                layout.instanceLayout("userprofile.fxml");
                if (layout.getController() instanceof UserprofilController) {
                    if(nutzerInstanz instanceof  Student){
                        if(((Student) nutzerInstanz).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, nutzerInstanz);
                        }else{
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                    }
                }

            } else if (response.body().contains("forschungsgebiet")) {
                Lehrender vergleichNutzer = mapper.readValue(response.body(), Lehrender.class);
                layout.instanceLayout("userprofile.fxml");
                if (layout.getController() instanceof UserprofilController) {
                    if(nutzerInstanz instanceof  Lehrender){
                        if(((Lehrender) nutzerInstanz).getId() == vergleichNutzer.getId()){
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, nutzerInstanz);
                        }else{
                            ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                        }
                    }else{
                        ((UserprofilController) layout.getController()).nutzerprofilAufrufen(nutzerInstanz, vergleichNutzer);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
