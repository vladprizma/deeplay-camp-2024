package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.enums.Bots;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BotStrategy {
    public final int id;
    public final String name;
    public final Bots bot;

    protected BotStrategy(int id, String name, Bots bot) {
        this.id = id;
        this.name = name;
        this.bot = bot;
    }

    public abstract Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic);

    public abstract List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic);
}
