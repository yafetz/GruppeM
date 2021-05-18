package Client;

import Client.Layouts.Auth;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("SEP");
            FXMLLoader loader = new FXMLLoader();
            Auth login = new Auth("login.fxml",stage);
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
