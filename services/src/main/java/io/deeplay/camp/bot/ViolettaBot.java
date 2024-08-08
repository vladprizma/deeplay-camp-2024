package io.deeplay.camp.bot;

import entity.Tile;
import io.deeplay.camp.board.BoardLogic;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViolettaBot extends BotStrategy {
    protected ViolettaBot(int id, String name) {
        super(id, name);
    }

    @Override
    public Tile getMakeMove(int currentPlayerId, @NotNull BoardLogic boardLogic) {
        return null;
    }

    @Override
    List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardLogic boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}
