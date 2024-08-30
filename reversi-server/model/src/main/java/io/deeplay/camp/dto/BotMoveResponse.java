package io.deeplay.camp.dto;

import io.deeplay.camp.entity.Tile;

public class BotMoveResponse {
    private Tile move;

    public BotMoveResponse(Tile move) {
        this.move = move;
    }
    
    public BotMoveResponse() {
        
    }
    
    public Tile getMove() {
        return move;
    }
    
    public void setMove(Tile move) {
        this.move = move;
    }
}
