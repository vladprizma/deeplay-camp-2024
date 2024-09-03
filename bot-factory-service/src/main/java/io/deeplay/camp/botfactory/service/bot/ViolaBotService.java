package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.viola.ExpectiMaxBot;
import io.deeplay.camp.botfactory.service.bot.viola.MiniMaxBot;
import io.deeplay.camp.botfactory.service.bot.viola.MonteCarloBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ViolaBotService {
    private static final Logger logger = LoggerFactory.getLogger(ViolaBotService.class);

    private BotStrategy botStrategy;

    @Autowired
    public ViolaBotService(@Qualifier("violaBotStrategy") BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }
    
    public void setMiniMaxViolaStrategy() {
        this.botStrategy = new MiniMaxBot();
    }
    
    public void setExpectiMaxViolaStrategy() {
        this.botStrategy = new ExpectiMaxBot(1, "ExpectiMaxBot", 3);
    }
    
    public void setMonteCarloViolaStrategy() {
        this.botStrategy = new MonteCarloBot(1, "MonteCarloBot", 100);
    }
    
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
