package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.andrey.KaylebeeBotMyFunc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AndreyBotService {
    private static final Logger logger = LoggerFactory.getLogger(DarlingBotService.class);

    private BotStrategy botStrategy;

    @Autowired
    public AndreyBotService(@Qualifier("andreyBotStrategy") BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }
    
    public void setMiniMaxAndreyStrategy() {
        this.botStrategy = new KaylebeeBotMyFunc();
    }
    
    @Async
    public CompletableFuture<Tile> getBotMove(int currentPlayerId, BoardService boardService) {
        logger.info("Getting bot move for player ID: {}", currentPlayerId);

        if (botStrategy == null) {
            logger.error("BotStrategy is not set");
            throw new IllegalStateException("BotStrategy cannot be null");
        }

        Tile move = botStrategy.getMove(currentPlayerId, boardService);
        logger.info("Bot move determined: {}", move);
        return CompletableFuture.completedFuture(move);
    }
    
    @Async
    public CompletableFuture<List<Tile>> getAllValidMoves(int currentPlayerId, BoardService boardService) {
        logger.info("Getting all valid moves for player ID: {}", currentPlayerId);

        if (botStrategy == null) {
            logger.error("BotStrategy is not set");
            throw new IllegalStateException("BotStrategy cannot be null");
        }

        List<Tile> moves = botStrategy.getAllValidMoves(currentPlayerId, boardService);
        logger.info("All valid moves determined: {}", moves);
        return CompletableFuture.completedFuture(moves);
    }
}
