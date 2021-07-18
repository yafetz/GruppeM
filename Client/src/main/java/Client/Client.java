package Client;

import Client.Controller.Auth.LoginController;
import Client.Layouts.Layout;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {

            stage.setTitle("SEP");
            FXMLLoader loader = new FXMLLoader();
            Layout layout = new Layout();
            layout.setStage(stage);
            layout.instanceAuth("login.fxml");
            ((LoginController) layout.getController()).setLayout(layout);
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
