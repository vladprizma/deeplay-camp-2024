package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardLogic;

public class ViolettaBot implements BotStrategy {
    @Override
    public boolean makeMove(int currentPlayerId, BoardLogic boardLogic) {
        return false;
    }
}
