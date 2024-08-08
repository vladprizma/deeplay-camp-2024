package io.deeplay.camp.handlers.commands;

import entity.SessionMessage;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CommandHandler для отправки сообщений в чат сессии.
 * Позволяет отправлять сообщения всем участникам сессии.
 */
public class SendSessionChatCommandHandler implements CommandHandler {

    private static final Logger logger = Logger.getLogger(SendSessionChatCommandHandler.class.getName());

    /**
     * Обрабатывает команду для отправки сообщения в чат сессии.
     *
     * @param message     Сообщение команды.
     * @param mainHandler Основной обработчик, управляющий сессией.
     * @throws IOException            В случае ошибки ввода-вывода.
     * @throws SQLException           В случае ошибки SQL.
     * @throws InterruptedException   В случае прерывания потока.
     */
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        logger.info("Handling send session chat command");

        if (mainHandler.getSession() == null) {
            String errorMsg = "No active session found.";
            logger.warning(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        String[] parts = message.split(MainHandler.splitRegex, 2);
        if (parts.length < 2) {
            String errorMsg = "Invalid message format.";
            logger.warning(errorMsg);
            mainHandler.sendMessageToClient(errorMsg);
            return;
        }

        SessionManager.getInstance().sendSessionMessage(mainHandler, parts[1]);
        List<SessionMessage> sessionChat = mainHandler.getSession().getSessionChat();

        StringBuilder sb = new StringBuilder();
        for (SessionMessage messageChat : sessionChat) {
            sb.append("::").append(messageChat.getUsername()).append(" ").append(messageChat.getMsg());
        }

        String msg = "send-message-session-chat" + sb.toString();
        SessionManager.getInstance().sendMessageToAllInSession(mainHandler, msg);

        logger.info("Session chat message sent successfully.");
    }
}