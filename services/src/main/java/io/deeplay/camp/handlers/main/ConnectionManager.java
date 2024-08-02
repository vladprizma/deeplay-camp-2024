package io.deeplay.camp.handlers.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionManager {
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ConnectionManager(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessageToClient(String msg) {
        out.println(msg);
    }

    public BufferedReader getInputReader() {
        return in;
    }

    public Socket getSocket() {
        return socket;
    }

    public void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
