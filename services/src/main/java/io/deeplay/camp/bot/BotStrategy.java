package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardLogic;

public interface BotStrategy {
    boolean makeMove(int currentPlayerId, BoardLogic boardLogic);
}
