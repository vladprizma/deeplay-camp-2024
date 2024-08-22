package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BotStrategy {
    public final int id;
    public final String name;

    protected BotStrategy(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic);

    abstract List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic);
}
