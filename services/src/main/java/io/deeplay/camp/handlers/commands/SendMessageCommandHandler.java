package io.deeplay.camp.handlers.commands;

import entity.ChatMessage;
import io.deeplay.camp.chat.ChatService;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandHandler для отправки сообщений в общий чат.
 * Позволяет отправлять сообщения всем участникам.
 */
public class SendMessageCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(SendMessageCommandHandler.class.getName());
    private final ChatService chatService = new ChatService();

    /**
     * Обрабатывает команду для отправки сообщения в общий чат.
     *
     * @param message Сообщение команды.
     * @param mainHandler Основной обработчик, управляющий сессией.
     * @throws IOException В случае ошибки ввода-вывода.
     * @throws SQLException В случае ошибки SQL.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException {
        logger.info("Handling send message command");

        String[] parts = message.split(MainHandler.splitRegex, 2);
        if (parts.length < 2) {
            String errorMsg = "Invalid message format.";
            logger.warning(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        try {
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
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error handling send message command", e);
            throw e; 
        }
    }
}