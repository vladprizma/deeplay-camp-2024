package io.deeplay.camp.botfactory.dto;

import io.deeplay.camp.botfactory.model.Board;

/**
 * DTO for handling bot move requests.
 */
public class BotMoveRequest {
    private Board board;
    private int currentPlayerId;

    /**
     * Constructs a new BotMoveRequest with the specified board and current player ID.
     *
     * @param board The current state of the board.
     * @param currentPlayerId The ID of the current player.
     */
    public BotMoveRequest(Board board, int currentPlayerId) {
        this.board = board;
        this.currentPlayerId = currentPlayerId;
    }

    /**
     * Gets the current state of the board.
     *
     * @return The current state of the board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the current state of the board.
     *
     * @param board The new state of the board.
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Gets the ID of the current player.
     *
     * @return The ID of the current player.
     */
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * Sets the ID of the current player.
     *
     * @param currentPlayerId The new ID of the current player.
     */
    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }
}