package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.GameSession;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.enums.GameStatus;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandHandler for pausing the current game.
 * <p>
 * This handler is responsible for processing pause game commands from clients. It checks the session and user state,
 * as well as the current game state to determine if the game can be paused. If the game is in progress, it will be paused,
 * and appropriate messages will be sent to the clients.
 * </p>
 */
public class PauseCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(PauseCommandHandler.class.getName());

    /**
     * Handles the command to pause the game.
     * <p>
     * This method checks the session and user state, as well as the current game state. If the game is in progress,
     * it will be paused, and appropriate messages will be sent to the clients. In case of an error, it will be logged,
     * and an error message will be sent to the client.
     * </p>
     *
     * @param message     The command message.
     * @param mainHandler The main handler managing the session.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) {
        logger.info("Handling pause command");

        try {
            var session = mainHandler.getSession();
            var user = mainHandler.getUser();

            if (session == null || user == null) {
                logger.warning("Session or user is null");
                mainHandler.sendMessageToClient("Cannot pause game. Invalid session or user.");
                return;
            }

            if (session.getGameState() == GameStatus.IN_PROGRESS) {
                pauseGame(mainHandler, session, user);
            } else {
                mainHandler.sendMessageToClient("Cannot pause game. Game is not in progress.");
                logger.warning("Cannot pause game. Game is not in progress.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error handling pause command", e);
            mainHandler.sendMessageToClient("Error occurred while pausing the game.");
        }
    }

    /**
     * Pauses the game and sends appropriate messages to the clients.
     *
     * @param mainHandler The main handler managing the session.
     * @param session     The current game session.
     * @param user        The user who initiated the pause.
     */
    private void pauseGame(MainHandler mainHandler, GameSession session, User user) {
        SessionManager.getInstance().sendMessageToOpponent(
                mainHandler,
                session,
                user.getId() + " pause"
        );
        mainHandler.sendMessageToClient("pause");
        logger.info("Game paused by user: " + user.getId());
    }
}