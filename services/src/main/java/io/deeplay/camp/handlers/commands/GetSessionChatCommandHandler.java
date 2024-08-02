package io.deeplay.camp.handlers.commands;

import entity.SessionMessage;
import io.deeplay.camp.handlers.main.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;

public class GetSessionChatCommandHandler implements CommandHandler {
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        var sessionChat = mainHandler.getSession().getSessionChat();

        StringBuilder sb = new StringBuilder();
        for (SessionMessage messageChat : sessionChat) {
            sb.append("::").append(messageChat.getUsername()).append(" ").append(messageChat.getMsg());
        }

        var msg = "get-messages-session-chat" + sb.toString();

        SessionManager.getInstance().sendMessageToAllInSession(mainHandler, msg);
    }
}
