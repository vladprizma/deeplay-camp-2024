package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling bot operations and strategies.
 */
@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class);

    private BotStrategy botStrategy;

    /**
     * Constructs a new BotService with the specified bot strategy.
     *
     * @param botStrategy The strategy used by the bot.
     */
    @Autowired
    public BotService(@Qualifier("darlingBotStrategy") BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }

    /**
     * Sets the strategy for the bot.
     *
     * @param botStrategy The new strategy to be used by the bot.
     */
    public void setBotStrategy(BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }

    /**
     * Gets the move for the bot based on the current player ID and board state.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardService The service representing the current state of the board.
     * @return The move made by the bot.
     */
    public Tile getBotMove(int currentPlayerId, BoardService boardService) {
        logger.info("Getting bot move for player ID: {}", currentPlayerId);

        if (botStrategy == null) {
            logger.error("BotStrategy is not set");
            throw new IllegalStateException("BotStrategy cannot be null");
        }

        Tile move = botStrategy.getMove(currentPlayerId, boardService);
        logger.info("Bot move determined: {}", move);
        return move;
    }

    /**
     * Gets all valid moves for the bot based on the current player ID and board state.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardService The service representing the current state of the board.
     * @return A list of all valid moves for the bot.
     */
    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardService) {
        logger.info("Getting all valid moves for player ID: {}", currentPlayerId);

        if (botStrategy == null) {
            logger.error("BotStrategy is not set");
            throw new IllegalStateException("BotStrategy cannot be null");
        }

        List<Tile> moves = botStrategy.getAllValidMoves(currentPlayerId, boardService);
        logger.info("All valid moves determined: {}", moves);
        return moves;
    }
}