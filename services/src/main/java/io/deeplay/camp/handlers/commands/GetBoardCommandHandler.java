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
 * <p>
 * This handler is responsible for retrieving the current state of the game board
 * and sending it to the client. It ensures that the session and board information
 * are correctly retrieved and formatted before being sent.
 * </p>
 */
public class GetBoardCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetBoardCommandHandler.class);
    private static final String GET_BOARD_PREFIX = "get-board::";

    /**
     * Handles the get board command.
     * <p>
     * This method validates the input parameters, retrieves the session and board information,
     * formats the board state, and sends it to the client. It also logs the process and
     * handles any unexpected errors that may occur.
     * </p>
     *
     * @param message     the message received from the client, should not be null or empty
     * @param mainHandler the main handler managing the session, should not be null
     * @throws IOException          if an unexpected error occurs during the handling process
     * @throws SQLException         if a database access error occurs
     * @throws InterruptedException if the thread is interrupted while handling the command
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        // Validate input parameters
        if (message == null || message.isEmpty()) {
            logger.error("Message is null or empty");
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        if (mainHandler == null) {
            logger.error("MainHandler is null");
            throw new IllegalArgumentException("MainHandler cannot be null");
        }

        logger.info("Handling get board command for sessionId: {}", mainHandler.getSession().getSessionId());

        try {
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
                mainHandler.sendMessageToClient(GET_BOARD_PREFIX + boardNotation + "::2::2");
            } else {
                if (userId == session.getPlayer1().getId()) {
                    userId = 1;
                } else if (userId == session.getPlayer2().getId()) {
                    userId = 2;
                } else {
                    mainHandler.sendMessageToClient("You are not a player in this session.");
                }
                String boardState = mainHandler.getBoardLogic().getBoardStateDTO(userId);
                mainHandler.sendMessageToClient(GET_BOARD_PREFIX + boardState + "::2::2");
            }

            logger.info("Board information sent to client successfully");

        } catch (Exception e) {
            logger.error("Unexpected error occurred while handling get board command", e);
            throw new IOException("Unexpected error occurred", e);
        }
    }
}