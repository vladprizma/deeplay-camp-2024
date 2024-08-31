package io.deeplay.camp.botfactory.dto;

import io.deeplay.camp.botfactory.model.Tile;

/**
 * DTO for handling bot move responses.
 */
public class BotMoveResponse {
    private Tile move;

    /**
     * Constructs a new BotMoveResponse with the specified move.
     *
     * @param move The move made by the bot.
     */
    public BotMoveResponse(Tile move) {
        this.move = move;
    }

    /**
     * Gets the move made by the bot.
     *
     * @return The move made by the bot.
     */
    public Tile getMove() {
        return move;
    }

    /**
     * Sets the move made by the bot.
     *
     * @param move The new move made by the bot.
     */
    public void setMove(Tile move) {
        this.move = move;
    }
}