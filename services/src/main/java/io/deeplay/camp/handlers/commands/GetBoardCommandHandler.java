package io.deeplay.camp.handlers.commands;

import dto.BoardDTO;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handler for processing get board commands.
 */
public class GetBoardCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetBoardCommandHandler.class);

    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        // Validate input parameters
        if (message == null || mainHandler == null) {
            logger.error("Message or MainHandler is null");
            throw new IllegalArgumentException("Message or MainHandler cannot be null");
        }

        // Retrieve the session and board information
        var session = SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId());
        if (session == null) {
            logger.error("Session not found for sessionId: {}", mainHandler.getSession().getSessionId());
            mainHandler.sendMessageToClient("Session not found");
            return;
        }

        BoardDTO boardDTO = new BoardDTO(session.getBoard());
        String boardNotation = boardDTO.boardToClient();

        // Send the board notation to the client
        var userId = mainHandler.getUser().getId();

        var currentPlayerId = session.getCurrentPlayerId();
        if (currentPlayerId != userId) {
            mainHandler.sendMessageToClient("get-board::" + boardNotation + "::2" + "::2");
        } else {
            if (userId == session.getPlayer1().getId()) {
                userId = 1;
            } else if (userId == session.getPlayer2().getId()) {
                userId = 2;
            } else {
                mainHandler.sendMessageToClient("You are not a player in this session.");
            }
            String boardState = mainHandler.getBoardLogic().getBoardStateDTO(userId);

            mainHandler.sendMessageToClient("get-board::" + boardState + "::2" + "::2");
        }

        logger.info("Board information sent to client successfully");
    }
}