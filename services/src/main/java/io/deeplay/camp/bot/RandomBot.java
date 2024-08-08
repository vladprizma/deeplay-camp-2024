package io.deeplay.camp.bot;

import entity.Tile;
import io.deeplay.camp.board.BoardLogic;

import java.security.SecureRandom;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomBot extends BotStrategy {

    public RandomBot(int id, String name) {
        super(id, name);
    }

    @Override
    public Tile getMakeMove(int currentPlayerId, BoardLogic boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);

        if (allTiles.isEmpty()) {
            return null;
        }

        SecureRandom secureRandom = new SecureRandom();
        return allTiles.get(secureRandom.nextInt(allTiles.size()));
    }

    @Override
    List<Tile> getAllValidMoves(int currentPlayerId, BoardLogic boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}
