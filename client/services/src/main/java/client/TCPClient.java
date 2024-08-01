package client;

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
        executor.submit(() -> {
            try {
                writer.write(requestResponse.toString());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void getResponse() {
        executor.submit(() -> {
            String serverResponse;
            try {
                while ((serverResponse = reader.readLine()) != null) {
                    logger.info("Server response: " + serverResponse);
                    String command = serverResponse.split(splitRegex)[0];
                    switch (command) {
                        case "login":
                            action.handleLoginActionResponse(serverResponse.split(splitRegex)[1], serverResponse.split(splitRegex)[2]);
                            break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}