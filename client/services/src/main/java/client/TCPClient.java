package client;

import request.Request;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class TCPClient implements Client {
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
            System.out.println("Сервер ответил: " + response);
        } else {
            System.out.println("Сервер не ответил на запрос.");
        }
    }
}