package handlers;

import entity.GameSession;
import entity.Player;
import enums.GameStatus;
import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import managers.SessionManager;

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
    private Player player;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            SessionManager.getInstance().addHandler(this);
            
            logger.info("Ожидание сессии.");
            sendMessageToClient("Ожидание сессии.");

            var result = SessionManager.getInstance().findOrCreateSession(this);
            
            session = result.gameSession;
            player = result.player;
            
            while (session.gameState == GameStatus.NOT_STARTED) {
                Thread.sleep(1000);
            }
            
            logger.info("Противник нашёлся. Игра начинается...");
            sendMessageToClient("Противник нашёлся. Игра начинается...");
            
            String message;
            //обработка запросов пользователя
            while ((message = in.readLine()) != null) {
                logger.info(message);
                if (message.equals("disconnect")) closeConnection();
                else if (message.equals("pause")) SessionManager.getInstance().sendMessageToOpponent(this, session, player.getId() + " start pause");
                else logger.info("Empty request");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
    
    public void sendMessageToClient(String msg) {
        out.println(msg);
    }
    
    public Player getHandlerPlayer() {
        return player;
    }
    
    public GameSession getHandlerSession() {
        return session;
    }
    
    public Socket getHandlerSocket() {
        return socket;
    }
    
    //удаление сессий
    private void closeConnection() {
        try {
            logger.info("Игрок отключился.");
            SessionManager.getInstance().deleteHandler(this);
            
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}