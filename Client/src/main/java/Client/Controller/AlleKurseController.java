package Client.Controller;


import Client.Layouts.Layout;
import Client.Modell.Lehrender;
import Client.Modell.Lehrveranstaltung;
import Client.Modell.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.List;

public class AlleKurseController{

    @FXML
    private TableView<Lehrveranstaltung> alleLv;
    @FXML
    private TableColumn<Lehrveranstaltung, Long> col_LvId;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvTitel;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvSemester;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvArt;
    @FXML
    private TableColumn<Lehrveranstaltung, String> col_LvLehrende;
    @FXML
    private TextField suchtext;
    @FXML
    private Button suchen;

    private Object nutzerInstanz;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
        setNutzerInstanz(layout.getNutzer());
    }

    public void initialize() {
    }

    public void Suchen(ActionEvent actionEvent) {
        try (CloseableHttpClient client1 = HttpClients.createDefault()) {

            String url = "http://localhost:8080/lehrveranstaltung/suchen";
            HttpPost post = new HttpPost(url);
            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.setCharset(StandardCharsets.UTF_8);
            entity.addTextBody("titel",suchtext.getText());
            HttpEntity requestEntity = entity.build();
            post.setEntity(requestEntity);

            try (CloseableHttpResponse response = client1.execute(post)) {
                HttpEntity responseEntity = response.getEntity();
                String result = EntityUtils.toString(responseEntity);
                alleLv.getItems().removeAll();
                ObjectMapper mapper = new ObjectMapper();
                List<Lehrveranstaltung> lehrveranstaltungen = mapper.readValue(result, new TypeReference<List<Lehrveranstaltung>>() {});

                col_LvId.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,Long>("id"));
                col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("titel"));
                col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("semester"));
                col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("art"));
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
//            ObservableList is required to populate the table alleLv using .setItems() :
                ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(lehrveranstaltungen);
                alleLv.setItems(obsLv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void populateTableView() {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/all")).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

//            mapping data in response.body() to a list of lehrveranstaltung-objects
            ObjectMapper mapper = new ObjectMapper();
            List<Lehrveranstaltung> lehrveranstaltungen = mapper.readValue(response.body(), new TypeReference<List<Lehrveranstaltung>>() {});

            col_LvId.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,Long>("id"));
            col_LvTitel.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("titel"));
            col_LvSemester.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("semester"));
            col_LvArt.setCellValueFactory(new PropertyValueFactory<Lehrveranstaltung,String>("art"));
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
//            ObservableList is required to populate the table alleLv using .setItems() :
            ObservableList<Lehrveranstaltung> obsLv = FXCollections.observableList(lehrveranstaltungen);
            alleLv.setItems(obsLv);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
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
                if(memberResponse.body().equals("true")){
                    layout.instanceLayout("lehrveranstaltungsuebersichtsseite.fxml");
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).setLayout(layout);
                    ((LehrveranstaltungsuebersichtsseiteController) layout.getController()).uebersichtsseiteAufrufen(nutzerInstanz,lehrveranstaltung);

                }
                else {
                    layout.instanceLayout("lehrveranstaltungBeitreten.fxml");
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLehrveranstaltung(lehrveranstaltung);
                    ((LehrveranstaltungBeitretenController) layout.getController()).setLayout(layout);
                }
            }else if (nutzerInstanz instanceof Student) {

                long id = ((Student) nutzerInstanz).getNutzer().getId();
                request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/lehrveranstaltung/beitreten/check/" + lehrveranstaltungId +"&"+ id)).build();
                memberResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

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

    public Object getNutzerInstanz() {
        return nutzerInstanz;
    }

    public void setNutzerInstanz(Object nutzerInstanz) {
        this.nutzerInstanz = nutzerInstanz;
        populateTableView();
    }

}
