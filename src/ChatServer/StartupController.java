package ChatServer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartupController
{

    @FXML
    TextField portInput;

    @FXML
    public void openServer()
    {
        boolean canParse = true;
        try
        {
            Integer.parseInt(portInput.getText());
        }
        catch (NumberFormatException e)
        {
            canParse = false;
        }
        if (canParse)
        {
            portInput.getScene().getWindow().hide();
            try
            {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Server.fxml"));
                Parent root = loader.load();

                ServerController controller = loader.getController();
                controller.initiate(primaryStage, Integer.parseInt(portInput.getText()));

                primaryStage.setTitle("Chat Server");
                primaryStage.setScene(new Scene(root, 300, 275));
                primaryStage.show();
            }
            catch (IOException e)
            {
                System.err.println(e.getMessage());
            }
        }
        else
        {
            portInput.setText("Port must be a number!");
        }
    }

}
