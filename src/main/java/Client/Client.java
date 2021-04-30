package Client;

import Client.Model.Student;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.File;

public class Client extends Application {
    public static Student student;
    @Override
    public void start(Stage stage) throws Exception {
        try {
            stage.setTitle("SEP");
            FXMLLoader loader = new FXMLLoader();
            File f = new File("src/main/java/Client/login.fxml");
            loader.setLocation(getClass().getClassLoader().getResource("login.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
