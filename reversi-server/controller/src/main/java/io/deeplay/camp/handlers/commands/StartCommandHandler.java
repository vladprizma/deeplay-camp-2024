package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.enums.GameStatus;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.game.GameService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * CommandHandler for starting a game.
 * <p>
 * This handler is responsible for processing commands to start a game. It allows finding or creating a game session,
 * setting up the game logic, and notifying the player about the start of the game. It also logs the process and handles
 * any unexpected errors that may occur.
 * </p>
 */
public class StartCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(StartCommandHandler.class);
    private static final int MAX_WAIT_TIME_MS = 300000; // Maximum wait time in milliseconds

    /**
     * Handles the command to start a game.
     * <p>
     * This method checks if the user is logged in, finds or creates a game session, sets up the game logic, and notifies
     * the player about the start of the game. In case of errors, appropriate messages are sent to the client and the error
     * is logged.
     * </p>
     *
     * @param message     The command message.
     * @param mainHandler The main handler managing the session.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, InterruptedException {
        if (!mainHandler.isLogin()) {
            mainHandler.sendMessageToClient("Please login or register.");
            return;
        }

        logger.info("User {} is attempting to start a game.", mainHandler.getUser().getId());
        var msg = message.split(" ");
        var isBot = false;
        if (msg.length > 1) {
            if (Objects.equals(msg[1], "--bot")) isBot = true;
        }

        var result = SessionManager.getInstance().findOrCreateSession(mainHandler, mainHandler.getUser(), isBot);
        mainHandler.setSession(result.getGameSession());

        int waitTime = 0;
        while (mainHandler.getSession().getGameState() == GameStatus.NOT_STARTED && waitTime < MAX_WAIT_TIME_MS) {
            Thread.sleep(1000);
            waitTime += 1000;
        }

        if (mainHandler.getSession().getGameState() == GameStatus.NOT_STARTED) {
            mainHandler.sendMessageToClient("Failed to start the game. Please try again later.");
            logger.info("User {}: Game start failed due to timeout.", mainHandler.getUser().getId());
            return;
        }

        mainHandler.setBoardLogic(new BoardService(result.getGameSession().getBoard()));
        mainHandler.setGameLogic(new GameService(mainHandler.getBoardLogic()));

        logger.info("User {}: The enemy was found. The game begins...", mainHandler.getUser().getId());

        if (!SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getPlayer2().getIsBot()) {
            var opponent = SessionManager.getInstance().getOpponent(mainHandler);

            mainHandler.sendMessageToClient(String.format("session::%d %s %d %d %s %d",
                    opponent.getId(), opponent.getUserPhoto(), opponent.getRating(),
                    opponent.getMatches(), opponent.getUsername(), opponent.getRating()));
        } else {
            var opponent = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getPlayer2();

            mainHandler.sendMessageToClient(String.format("session-bot::%d %s %d %d %s %d",
                    opponent.getId(), opponent.getUserPhoto(), opponent.getRating(),
                    opponent.getMatches(), opponent.getUsername(), opponent.getRating()));
        }

        var userId = mainHandler.getUser().getId();
        int playerNumber = (userId == mainHandler.getSession().getPlayer1().getId()) ? 1 : 2;

        mainHandler.getGameLogic().display(playerNumber, mainHandler.getBoardLogic());
    }
}