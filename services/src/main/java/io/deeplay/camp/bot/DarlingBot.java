package io.deeplay.camp.bot;

import entity.Tile;
import io.deeplay.camp.board.BoardLogic;

import java.util.List;

public class DarlingBot extends BotStrategy {
    protected DarlingBot(int id, String name) {
        super(id, name);
    }

    @Override
    public Tile getMakeMove(int currentPlayerId, BoardLogic boardLogic) {
        return null;
    }

    @Override
    List<Tile> getAllValidMoves(int currentPlayerId, BoardLogic boardLogic) {
        return List.of();
    }
}
