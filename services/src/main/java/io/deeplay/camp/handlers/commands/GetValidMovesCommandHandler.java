package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handler for processing get valid moves commands.
 * <p>
 * This handler is responsible for retrieving the valid moves for the current player
 * and sending them to the client. It ensures that the session and board information
 * are correctly retrieved and formatted before being sent.
 * </p>
 */
public class GetValidMovesCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetValidMovesCommandHandler.class);

    /**
     * Handles the get valid moves command.
     * <p>
     * This method validates the input parameters, retrieves the session and board information,
     * determines the valid moves for the current player, and sends them to the client. It also logs
     * the process and handles any unexpected errors that may occur.
     * </p>
     *
     * @param message     the message received from the client, should not be null
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException          if an unexpected error occurs during the handling process
     * @throws SQLException         if a database access error occurs
     * @throws InterruptedException if the thread is interrupted while handling the command
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

        logger.info("Handling get valid moves command for sessionId: {}", mainHandler.getSession().getSessionId());

        var boardLogic = mainHandler.getBoardLogic();
        var user = mainHandler.getUser().getId();

        // Determine the player number
        if (user == mainHandler.getSession().getPlayer1().getId()) {
            user = 1;
        } else if (user == mainHandler.getSession().getPlayer2().getId()) {
            user = 2;
        } else {
            logger.warn("User {} is not part of the current session", user);
            mainHandler.sendMessageToClient("User is not part of the current session.");
            return;
        }

        var validMoves = boardLogic.getValidMoves(user);
        var msg = "get-valid-moves::" + validMoves;

        mainHandler.sendMessageToClient(msg);
        logger.info("Valid moves for user {} sent successfully", user);
    }
}