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

/**
 * Handles communication with a connected client.
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameSession session;
    private Player player;

    /**
     * Constructor for ClientHandler.
     *
     * @param socket the socket connected to the client
     */
    public ClientHandler(Socket socket) {
        this.socket = socket;

        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main logic for handling the client's connection.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            SessionManager.getInstance().addHandler(this);

            logger.info("Ожидание сессии.");
            sendMessageToClient("Ожидание сессии.");

            var result = SessionManager.getInstance().findOrCreateSession(this);

            session = result.getGameSession();
            player = result.getPlayer();

            while (session.getGameState() == GameStatus.NOT_STARTED) {
                Thread.sleep(1000);
            }

            logger.info(player.getId() + ": The enemy was found. The game begins...");
            sendMessageToClient(player.getId() + ": The enemy was found. The game begins...");

            String message;

            while ((message = in.readLine()) != null) {
                logger.info(player.getId() + ": " + message);
                if (message.equals("disconnect")) {
                    closeConnection();
                } else if (message.equals("pause")) {
                    SessionManager.getInstance().sendMessageToOpponent(this, session, player.getId() + " start pause");
                } else {
                    logger.info("Empty request");
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    /**
     * Sends a message to the client.
     *
     * @param msg the message to send
     */
    public void sendMessageToClient(String msg) {
        out.println(msg);
    }

    /**
     * Gets the player associated with this handler.
     *
     * @return the player
     */
    public Player getHandlerPlayer() {
        return player;
    }

    /**
     * Gets the session associated with this handler.
     *
     * @return the game session
     */
    public GameSession getHandlerSession() {
        return session;
    }

    /**
     * Gets the socket associated with this handler.
     *
     * @return the socket
     */
    public Socket getHandlerSocket() {
        return socket;
    }

    /**
     * Closes the connection and cleans up resources.
     */
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
