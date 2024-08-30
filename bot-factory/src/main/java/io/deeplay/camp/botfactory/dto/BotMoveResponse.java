package io.deeplay.camp.botfactory.dto;

import io.deeplay.camp.botfactory.model.Tile;

public class BotMoveResponse {
    private Tile move;

    public BotMoveResponse(Tile move) {
        this.move = move;
    }
    
    public Tile getMove() {
        return move;
    }
    
    public void setMove(Tile move) {
        this.move = move;
    }
}