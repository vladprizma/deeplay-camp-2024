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
        mainHandler.sendMessageToClient("get-board::" + boardNotation);
        logger.info("Board information sent to client successfully");

    }
}