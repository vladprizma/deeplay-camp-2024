package io.deeplay.camp.botfactory.service.bot;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.BoardService;

import java.util.List;

public abstract class BotStrategy {
    public final int id;
    public final String name;

    protected BotStrategy(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract Tile getMove(int currentPlayerId, BoardService boardLogic);

    public abstract List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic);
}
