package io.deeplay.camp.handlers.commands;

import enums.GameStatus;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandHandler для приостановки текущей игры.
 */
public class PauseCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(PauseCommandHandler.class.getName());

    /**
     * Обрабатывает команду для приостановки игры.
     *
     * @param message Сообщение команды.
     * @param mainHandler Основной обработчик, управляющий сессией.
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
                SessionManager.getInstance().sendMessageToOpponent(
                        mainHandler,
                        session,
                        user.getId() + " pause"
                );
                mainHandler.sendMessageToClient("pause");
                logger.info("Game paused by user: " + user.getId());
            } else {
                mainHandler.sendMessageToClient("Cannot pause game. Game is not in progress.");
                logger.warning("Cannot pause game. Game is not in progress.");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error handling pause command", e);
            mainHandler.sendMessageToClient("Error occurred while pausing the game.");
        }
    }
}