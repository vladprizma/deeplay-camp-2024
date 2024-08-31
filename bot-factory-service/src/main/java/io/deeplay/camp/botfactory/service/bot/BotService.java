// BotService.java
package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotService {

    private BotStrategy botStrategy;

    @Autowired
    public BotService(@Qualifier("darlingBotStrategy") BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }

    public void setBotStrategy(BotStrategy botStrategy) {
        this.botStrategy = botStrategy;
    }

    public Tile getBotMove(int currentPlayerId, BoardService boardService) {
        return botStrategy.getMove(currentPlayerId, boardService);
    }

    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardService) {
        return botStrategy.getAllValidMoves(currentPlayerId, boardService);
    }
}