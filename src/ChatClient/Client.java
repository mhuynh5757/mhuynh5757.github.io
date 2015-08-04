package ChatClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Startup.fxml"));
        Parent root = loader.load();

        Scene primaryScene = new Scene(root);

        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(primaryScene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
