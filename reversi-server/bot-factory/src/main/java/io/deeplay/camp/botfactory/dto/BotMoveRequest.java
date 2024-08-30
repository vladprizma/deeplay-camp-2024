// BotMoveRequest.java
package io.deeplay.camp.botfactory.dto;

import io.deeplay.camp.botfactory.model.Board;

public class BotMoveRequest {
    private Board board;
    private int currentPlayerId;
    
    public BotMoveRequest(Board board, int currentPlayerId) {
        this.board = board;
        this.currentPlayerId = currentPlayerId;
    }
    
    public Board getBoard() {
        return board;
    }
    
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }
}