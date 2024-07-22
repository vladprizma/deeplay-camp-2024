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

public class TCPClient implements Client {

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
    }

    private void initializeResources() throws IOException {
        socket = new Socket(serverIp, serverPort);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void sendRequest(Request request) throws IOException {
        writer.write(request.toString());
        writer.newLine();
        writer.flush();

        String response = reader.readLine();
        if (response != null) {
            logger.info("Сервер ответил: " + response);
        } else {
            logger.info("Сервер не ответил на запрос.");
        }
    }
}