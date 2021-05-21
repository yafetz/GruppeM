package Client.Controller;

import Client.Layouts.Auth;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegistrierenController {
    @FXML
    private TextField email;
    @FXML
    private Button registrieren_lehrender;
    @FXML
    private Button registrieren_student;
    @FXML
    private Button zuruek;
    @FXML
    private Button registrieren;
    @FXML
    private TextField nachname;
    @FXML
    private TextField vorname;
    @FXML
    private TextField postleitzahl;
    @FXML
    private TextField stadt;
    @FXML
    private TextField strasse;
    @FXML
    private TextField hausnummer;
    @FXML
    private PasswordField passwort;
    @FXML
    private TextField studienfach;
    @FXML
    private TextField lehrstuhl;
    @FXML
    private TextField forschungsgebiet;
    @FXML
    private CheckBox check_box;


    public void Rollenwechsel(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) registrieren_lehrender.getScene().getWindow();
        Auth register_lehrender = new Auth("Registrieren_Lehrender.fxml",stage);
    }

    public void Registrieren_Student(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) registrieren_student.getScene().getWindow();
        Auth register_student = new Auth("Registrieren_Student.fxml",stage);
    }

    public void Zuruek(ActionEvent actionEvent) {
        actionEvent.consume();
        Stage stage = (Stage) zuruek.getScene().getWindow();
        Auth login = new Auth("login.fxml",stage);
    }

    public void Registrieren(ActionEvent actionEvent) {
        actionEvent.consume();
        String vornameText = vorname.getText();
        String nachnameText = nachname.getText();
        String emailText = email.getText();
        String passwortText = passwort.getText();
        int hausnummerText = Integer.valueOf(hausnummer.getText());
        int plzText = Integer.valueOf(postleitzahl.getText());
        String stadtText = stadt.getText();
        String strasseText = strasse.getText();

       /* @FXML
        private Object args;
        public String EmailValidieren(){
            public static void main(String[] args){
                //    String email1 = "nachname.vorname@stud.uni-due.de";
                //  String email2 = "nachname.vornamestud.uni-due.de"; //invalide:false
                System.out.println("email1= +EmailValidieren(EmailValidieren(Email1):
                System.out.println("email2= +EmailValidieren(EmailValidieren(Email2):
         if(!email.contains ("@") {
            System.out.println("die Eingabe ist korrekt");

            if (email == null || email.isDisabled()) { //also leer oder falsch
                System.out.println("Die Eingabe ist falsch!");
                String emailRegex = "A-Z*@\\.\\.de\\.com";
            Pattern pattern = Pattern.compile(emailRegex);
            if (pattern.matcher(email).matches()){
            System.out.println("Valid") //return true
            } else{
            return invalid;
            }
            if(passwort.size()<8{
            System.out.println("Das passwort ist zu kurz!")
            }}}*/


               // if (!passwort.contains(8))


                    if (registrieren_student == null) {
                        // als Student registrieren
                        String studienfachText = studienfach.getText();
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/register/student/" +
                                        vornameText + "&" + nachnameText + "&" + emailText + "&" +
                                        passwortText + "&" + studienfachText + "&" +
                                        hausnummerText + "&" + plzText + "&" + stadtText + "&" + strasseText + "&Student")).build();
                        HttpResponse<String> response = null;
                        try {
                            response = client.send(request, HttpResponse.BodyHandlers.ofString());
                            String Serverantwort = response.body();
                            System.out.println(Serverantwort);
                            if (Serverantwort.equals("OK")) {
                                //Weiterleitung zur Login Seite
                                Stage stage = (Stage) registrieren.getScene().getWindow();
                                Auth login = new Auth("login.fxml", stage);
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    } else {
                        // als Lehrender registrieren
                        String forschungsgebietText = forschungsgebiet.getText();
                        String lehrstuhlText = lehrstuhl.getText();
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create("http://localhost:8080/register/lehrender/" +
                                        vornameText + "&" + nachnameText + "&" + emailText + "&" +
                                        passwortText + "&" + forschungsgebietText + "&" + lehrstuhlText + "&" +
                                        hausnummerText + "&" + plzText + "&" + stadtText + "&" + strasseText+ "&Lehrender")).build();
                        HttpResponse<String> response = null;
                        try {
                            response = client.send(request, HttpResponse.BodyHandlers.ofString());
                            String Serverantwort = response.body();
                            System.out.println(Serverantwort);
                            if (Serverantwort.equals("OK")) {
                                //Weiterleitung zur Login Seite
                                Stage stage = (Stage) registrieren.getScene().getWindow();
                                Auth login = new Auth("login.fxml", stage);
                            }
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }
   /* @FXML
    private Object args;
    public String EmailValidieren(){
       public static void main(String[] args){
        //    String email = "nachname.vorname@stud.uni-due.de";
          //  String email2 = "nachname.vornamestud.uni-due.de"; //invalide:false

     */
        //public static String validiereEmail(String mail)

//hier Validieren
     /*   if(!email.contains ("@") {
            System.out.println("die Eingabe ist korrekt");
        }

        else (email==null||email.isDisabled()){ //also leer oder falsch
        System.out.println("false"); //return "false";
String EmailRegex = "A-Z*@\\.\\.de\\.com";

    }}}*/

/*
if(!email.containts("@")){
    system.out.println}
*/
