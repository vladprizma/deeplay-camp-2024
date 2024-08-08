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
    public Tile getMakeMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        return null;
    }

    @Override
    List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return List.of();
    }
}
