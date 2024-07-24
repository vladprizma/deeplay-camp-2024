package client;

import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.Request;
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
    public void sendRequest(Request request) {
        executor.submit(() -> {
            try {
                writer.write(request.toString());
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
                    System.out.println("Server response: " + serverResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}