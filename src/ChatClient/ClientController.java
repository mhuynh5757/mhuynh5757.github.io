package ChatClient;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientController
{

    @FXML
    TextArea chatLog;
    @FXML
    TextField chatInput;

    ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(1);
    Socket server;
    String name;

    public void initiate(Stage stage, String login, String hostName, int port)
    {
        name = login;
        try
        {
            server = new Socket(hostName, port);
            chatLog.appendText("Connected to server at: " + server.getLocalSocketAddress() + "\nYou are known as: " + name + "\n");

            stage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent windowEvent)
                {
                    System.out.println("Closing program...");
                    try
                    {
                        server.close();
                    }
                    catch (IOException e)
                    {
                        chatLog.appendText(e.getMessage());
                    }
                    schedulePool.shutdownNow();
                }
            });

            schedulePool.scheduleAtFixedRate(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        DataInputStream inputStream = new DataInputStream(server.getInputStream());
                        if (inputStream.available() > 0)
                        {
                            chatLog.appendText(inputStream.readUTF() + "\n");
                        }
                    }
                    catch (IOException e)
                    {
                        chatLog.appendText(e.getMessage());
                    }
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
        }
        catch (IOException e)
        {
            chatLog.appendText(e.getMessage());
        }

    }

    @FXML
    public void sendMessage()
    {
        try
        {
            DataOutputStream outputStream = new DataOutputStream(server.getOutputStream());
            outputStream.writeUTF(name + ": " + chatInput.getText());
            chatInput.setText("");
        }
        catch (IOException e)
        {
            chatLog.appendText(e.getMessage());
        }
    }

}
