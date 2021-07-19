package Client.Controller.Freundschaft;

import Client.Layouts.Layout;
import Client.Modell.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FreundschaftsAnfragenController {
    @FXML
    private TableView<Nutzer> anfragen_tabelle;
    @FXML
    private TableColumn<String, String> vornamen_column;
    @FXML
    private TableColumn<String, String> nachnamen_column;

    private Object nutzerInstanz;

    private Layout layout;

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public void populateTableView() {
        long id = 0;
        if (nutzerInstanz instanceof Lehrender) {
            id= ((Lehrender) nutzerInstanz).getNutzerId().getId();
        }
        if (nutzerInstanz instanceof Student) {
            id= ((Student) nutzerInstanz).getNutzer().getId();
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/freundschaft/meine/" + id)).build();

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonObject = new JSONArray(response.body());
//            System.out.println("Freundschaftsanfrage Controller json       "+response.body());
//            System.out.println("JSONObject länge     " +jsonObject.length());
            vornamen_column.setCellValueFactory(new PropertyValueFactory<>("vorname"));
            nachnamen_column.setCellValueFactory(new PropertyValueFactory<>("nachname"));

            for(int i=0;i<jsonObject.length();i++){
                JSONObject nutzer= jsonObject.getJSONObject(i).getJSONObject("anfragender_nutzer");
                Nutzer nutzer1 = new Nutzer();
                nutzer1.setVorname(nutzer.getString("vorname"));
                nutzer1.setNachname(nutzer.getString("nachname"));
                nutzer1.setId(nutzer.getInt("id"));
                anfragen_tabelle.getItems().add(nutzer1);

            }
            addButtonToTable("Akzeptieren");
            addButtonToTable("Ablehnen");

        } catch (IOException e) {
            System.out.println("Error 1");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Error 1");
            e.printStackTrace();
        }
    }

    private void addButtonToTable(String methode) {
        TableColumn<Nutzer, Void> colBtn = new TableColumn(methode);

        Callback<TableColumn<Nutzer, Void>, TableCell<Nutzer, Void>> cellFactory = new Callback<TableColumn<Nutzer, Void>, TableCell<Nutzer, Void>>() {
            @Override
            public TableCell<Nutzer, Void> call(final TableColumn<Nutzer, Void> param) {
                final TableCell<Nutzer, Void> cell = new TableCell<Nutzer, Void>() {

                    private final Button btn = new Button(methode);

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Object data = getTableView().getItems().get(getIndex());
                          if(btn.getText().equals("Akzeptieren")){

//                              System.out.println("Akzeptieren gedrückt");

                              long id =0;
                              if(nutzerInstanz instanceof Lehrender){
                                  id=((Lehrender)nutzerInstanz).getId();

                              }
                              if(nutzerInstanz instanceof Student){
                                  id= ((Student)nutzerInstanz).getNutzer().getId();

                              }
                              long id2= getTableRow().getItem().getId();


                              HttpClient client = HttpClient.newHttpClient();
                              HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/freundschaft/meine/akzeptieren/" + id2+"&"+id)).build();

                              HttpResponse<String> response = null;
                              try {
                                  response = client.send(request, HttpResponse.BodyHandlers.ofString());

//                                  System.out.println("Akzeptieren response:    "+response.body());


                                  Stage stage = (Stage) anfragen_tabelle.getScene().getWindow();

                                  layout.instanceLayout("freundschaftsAnfragen.fxml");
                                  if (layout.getController() instanceof FreundschaftsAnfragenController) {
                                      ((FreundschaftsAnfragenController) layout.getController()).setLayout(layout);
                                      ((FreundschaftsAnfragenController) layout.getController()).setNutzerInstanz(nutzerInstanz);
                                  }

                              } catch (IOException e) {
                                  e.printStackTrace();
                              } catch (InterruptedException e) {
                                  e.printStackTrace();
                              }
                          }
                            if(btn.getText().equals("Ablehnen")){

//                                System.out.println("Ablehnen gedrückt");
                                long id =0;
                                if(nutzerInstanz instanceof Lehrender){
                                    id=((Lehrender)nutzerInstanz).getId();

                                }
                                if(nutzerInstanz instanceof Student){
                                    id= ((Student)nutzerInstanz).getNutzer().getId();

                                }
                                long id2= getTableRow().getItem().getId();

                                HttpClient client = HttpClient.newHttpClient();
                                HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/freundschaft/meine/delete/" + id2+"&"+id)).DELETE().build();

                                HttpResponse<String> response = null;
                                try {
                                    response = client.send(request, HttpResponse.BodyHandlers.ofString());

//                                    System.out.println("Ablehnen response:    "+response.body());

                                    Stage stage = (Stage) anfragen_tabelle.getScene().getWindow();

                                    Layout anfragen = null;
                                    layout.instanceLayout("freundschaftsAnfragen.fxml");
                                    if (layout.getController() instanceof FreundschaftsAnfragenController) {
                                        ((FreundschaftsAnfragenController) layout.getController()).setLayout(layout);
                                        ((FreundschaftsAnfragenController) layout.getController()).setNutzerInstanz(nutzerInstanz);

                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

//                            System.out.println("selectedData2: " + btn);
//                            System.out.println(getTableRow().getItem().getVorname());
//                            System.out.println("DATA type " +getTableRow().getItem().getId());
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
        anfragen_tabelle.getColumns().add(colBtn);
    }

    public void setNutzerInstanz (Object nutzerInstanz){
            this.nutzerInstanz = nutzerInstanz;
            populateTableView();
        }
    }

