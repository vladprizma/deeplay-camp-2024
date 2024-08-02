package io.deeplay.camp.handlers.commands;

import dto.BoardDTO;
import io.deeplay.camp.handlers.MainHandler;
import io.deeplay.camp.managers.SessionManager;
import io.deeplay.camp.repository.CommandHandler;

import java.io.IOException;
import java.sql.SQLException;

public class GetBoardCommandHandler implements CommandHandler {
    @Override
    public void handle(String message, MainHandler mainHandler) throws IOException, SQLException, InterruptedException {
        BoardDTO boardDTO = new BoardDTO(SessionManager.getInstance().getSession(mainHandler.getSession().getSessionId()).getBoard());
        var boardNotation = boardDTO.boardToClient();
        
        mainHandler.sendMessageToClient("get-board::" + boardNotation);
    }
}
