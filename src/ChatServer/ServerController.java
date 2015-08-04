package ChatServer;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerController
{
    ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(2);
    ArrayList<Socket> clients = new ArrayList<Socket>();

    @FXML
    TextArea chatLog;

    public void initiate(Stage stage, int port)
    {
        try
        {
            final ServerSocket server = new ServerSocket(port);
            chatLog.appendText("Listening to port " + server.getLocalPort() + "\n");

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
                        clients.add(server.accept());
                    }
                    catch (IOException e)
                    {
                        chatLog.appendText(e.getMessage());
                    }
                }
            }, 0, 100, TimeUnit.MILLISECONDS);


            schedulePool.scheduleAtFixedRate(new Runnable()
            {
                @Override
                public void run()
                {
                    for (int i = 0; i < clients.size(); i++)
                    {
                        try
                        {
                            DataInputStream inputStream = new DataInputStream(clients.get(i).getInputStream());
                            if (inputStream.available() > 0)
                            {
                                String timeStamp = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm:ss a"));
                                String s = inputStream.readUTF();
                                chatLog.appendText(timeStamp + " " + s + "\n");
                                for (int j = 0; j < clients.size(); j++)
                                {
                                    DataOutputStream outputStream = new DataOutputStream(clients.get(j).getOutputStream());
                                    outputStream.writeUTF(timeStamp + " " + s);
                                }
                            }
                        }
                        catch (IOException e)
                        {
                            chatLog.appendText(e.getMessage());
                        }
                    }
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
        }
        catch (IOException e)
        {
            chatLog.appendText(e.getMessage());
        }
    }
}