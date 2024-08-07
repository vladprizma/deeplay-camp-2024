package client;

import javafx.application.Platform;
import action.Action;
import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.RequestResponse;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPClient implements Client {

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private String serverIp;
    private int serverPort;
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    public static String splitRegex = "::";
    public Action action;

    public void setAction(Action action) {
        this.action = action;
    }

    public TCPClient(String serverIp, int serverPort) throws IOException {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        initializeResources();
        getResponse();
    }

    private void initializeResources() throws IOException {
        socket = new Socket(serverIp, serverPort);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void sendRequest(RequestResponse requestResponse) {
        new Thread(() -> {
            try {
                logger.info("Server request: " + requestResponse);
                writer.write(requestResponse.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                logger.error("Failed to send request: " + requestResponse, e);
            }
        }).start();
    }

    public void getResponse() {
        new Thread(() -> {
            String serverResponse;
            try {
                while ((serverResponse = reader.readLine()) != null) {
                    logger.info("Server response: " + serverResponse);
                    String command = serverResponse.split(splitRegex)[0];
                    switch (command) {
                        case "login":
                        case "register":
                            String finalServerResponse = serverResponse;
                            Platform.runLater(() -> action.handleLoginActionResponse(finalServerResponse.split(splitRegex)[1], finalServerResponse.split(splitRegex)[2]));
                            break;
                        case "User not found.":
                        case "Not unique username.":
                        case "messages":
                        case "Please login or register":
                        case "session-start":
                        case "session-bot":
                        case "get-board":
                        case "session":
                            String finalServerResponse1 = serverResponse;
                            Platform.runLater(() -> action.handleResponseActionResponse(finalServerResponse1));
                            break;
                    }
                }
            } catch (IOException e) {
                logger.error("Failed to read response from server", e);
            }
        }).start();
    }
}