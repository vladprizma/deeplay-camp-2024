package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.enums.Bots;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.List;

public class RandomBot extends BotStrategy {

    public RandomBot(int id, String name, Bots bot) {
        super(id, name, bot);
    }

    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);

        if (allTiles.isEmpty()) {
            return null;
        }

        SecureRandom secureRandom = new SecureRandom();
        return allTiles.get(secureRandom.nextInt(allTiles.size()));
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}
