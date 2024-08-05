package io.deeplay.camp.repository;

import io.deeplay.camp.board.BoardLogic;
import entity.User;
import enums.GameStatus;

public interface ReversiListener {
    boolean moveMade(User user, int currentPlayerId, BoardLogic boardLogic, String move);

    GameStatus gameFinished();
    GameStatus gameStarted();
    GameStatus gameStopped();
    GameStatus gameResumed();

    int[] scoreUpdated();
}
