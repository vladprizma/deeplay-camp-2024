package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;

import java.util.List;

/**
 * Abstract class representing a strategy for a bot.
 */
public abstract class BotStrategy {
    public final int id;
    public final String name;

    /**
     * Constructs a new BotStrategy with the specified ID and name.
     *
     * @param id The unique identifier for the strategy.
     * @param name The name of the strategy.
     */
    protected BotStrategy(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the move for the bot based on the current player ID and board state.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardLogic The service representing the current state of the board.
     * @return The move made by the bot.
     */
    public abstract Tile getMove(int currentPlayerId, BoardService boardLogic);

    /**
     * Gets all valid moves for the bot based on the current player ID and board state.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardLogic The service representing the current state of the board.
     * @return A list of all valid moves for the bot.
     */
    public abstract List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic);
}