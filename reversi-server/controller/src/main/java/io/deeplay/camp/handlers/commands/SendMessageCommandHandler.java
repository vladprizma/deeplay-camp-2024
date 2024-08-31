package io.deeplay.camp.handlers.commands;

import io.deeplay.camp.entity.ChatMessage;
import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

/**
 * CommandHandler for sending messages to the public chat.
 * <p>
 * This handler is responsible for processing commands to send messages to the public chat. It validates the input message,
 * adds the message to the chat service, and sends the message to all participants. It also logs the process and handles any
 * unexpected errors that may occur.
 * </p>
 */
public class SendMessageCommandHandler implements CommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(SendMessageCommandHandler.class);
    private final ChatService chatService = new ChatService();

    /**
     * Handles the command to send a message to the public chat.
     * <p>
     * This method validates the input parameters, adds the message to the chat service, and sends the message to all participants.
     * In case of errors, appropriate messages are sent to the client and the error is logged.
     * </p>
     *
     * @param message     The command message.
     * @param mainHandler The main handler managing the session.
     * @throws IOException  If an I/O error occurs.
     * @throws SQLException If a SQL error occurs.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling send message command");

        String[] parts = message.split(MainHandler.splitRegex, 2);
        if (parts.length < 2) {
            String errorMsg = "Invalid message format.";
            logger.warn(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        String chatMessage = parts[1];
        chatService.addMessage(mainHandler.getUser(), chatMessage);
        mainHandler.sendMessageToClient("Message sent successfully.");

        StringBuilder response = new StringBuilder("messages");

        List<ChatMessage> messages = chatService.getAllMessages();
        for (ChatMessage chatMessageItem : messages) {
            response.append("::").append(chatMessageItem.getTimestamp())
                    .append(" ").append(chatMessageItem.getUserId().getUsername())
                    .append(" ").append(chatMessageItem.getMessage());
        }

        SessionManager.getInstance().sendMessageToAll(response.toString());
        logger.info("Chat message sent to all successfully.");
    }
}