package io.deeplay.camp.dto;

import io.deeplay.camp.entity.Board;
import io.deeplay.camp.enums.Bots;

public class BotMoveRequest {
    private Board board;
    private int currentPlayerId;
    
    public BotMoveRequest(Board board, int currentPlayerId) {
        this.board = board;
        this.currentPlayerId = currentPlayerId;
    }
    
    public BotMoveRequest() {
        
    }
    
    public Board getBoard() {
        return board;
    }
    
    public void setBoard(Board board) {
        this.board = board;
    }
    
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }
    
    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }
}
