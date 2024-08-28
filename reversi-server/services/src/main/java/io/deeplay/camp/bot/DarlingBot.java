package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DarlingBot extends BotStrategy {
    protected DarlingBot(int id, String name) {
        super(id, name);
    }

    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        return null;
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}
