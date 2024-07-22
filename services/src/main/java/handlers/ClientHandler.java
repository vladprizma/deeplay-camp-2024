package handlers;

import entity.GameSession;
import enums.GameStatus;
import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.SessionManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameSession session;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            logger.info("Ожидание сессии.");

            session = SessionManager.getInstance().findOrCreateSession(this);
            
            while (session.gameState == GameStatus.NOT_STARTED) Thread.sleep(1000);
            
            logger.info("Противник нашёлся. Игра начинается...");
            
            String message;
            
            //обработка запросов пользователя
            while ((message = in.readLine()) != null) {
                logger.info(message);
                if (message.equals("bb")) closeConnection();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            logger.info("Игрок отключился.");
            
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
