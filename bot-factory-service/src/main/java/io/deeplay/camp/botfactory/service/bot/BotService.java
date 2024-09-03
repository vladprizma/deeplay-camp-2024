package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.darling.DarlingBotStrategy;
import io.deeplay.camp.botfactory.service.bot.viola.ExpectiMaxBot;
import io.deeplay.camp.botfactory.service.bot.viola.MiniMaxBot;
import io.deeplay.camp.botfactory.service.bot.viola.MonteCarloBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotService {

    private static final Logger logger = LoggerFactory.getLogger(BotService.class);
    // ленивая инициализация ботов, котоыре не используются
    private BotStrategy botStrategy;
    private final MiniMaxBot miniMaxBot;
    private final ExpectiMaxBot expectiMaxBot;
    private final MonteCarloBot monteCarloBot;
    // разнести на сервисы
    @Autowired
    public BotService(@Qualifier("darlingBotStrategy") BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
        this.miniMaxBot = new MiniMaxBot(1, "MiniMaxBot", 3);
        this.expectiMaxBot = new ExpectiMaxBot(2, "ExpectiMaxBot", 3);
        this.monteCarloBot = new MonteCarloBot(3, "MonteCarloBot", 1000);
    }
    
    public void setMiniMaxDarlingStrategy() {
        this.botStrategy = new DarlingBotStrategy();
    }

    public void setMiniMaxBotStrategy() {
        this.botStrategy = miniMaxBot;
    }

    public void setExpectiMaxBotStrategy() {
        this.botStrategy = expectiMaxBot;
    }

    public void setMonteCarloBotStrategy() {
        this.botStrategy = monteCarloBot;
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