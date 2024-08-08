package io.deeplay.camp.bot;

import entity.Tile;
import io.deeplay.camp.board.BoardLogic;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BotStrategy {
    public final int id;
    public final String name;

    protected BotStrategy(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Tile getMakeMove(int currentPlayerId, @NotNull BoardLogic boardLogic);

    abstract List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardLogic boardLogic);
}
