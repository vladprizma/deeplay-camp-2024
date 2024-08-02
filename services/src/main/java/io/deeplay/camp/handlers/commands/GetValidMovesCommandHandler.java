package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handler for processing get valid moves commands.
 */
public class GetValidMovesCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetValidMovesCommandHandler.class);

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

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