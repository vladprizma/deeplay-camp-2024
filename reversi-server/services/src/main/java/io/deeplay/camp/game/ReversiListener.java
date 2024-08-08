package io.deeplay.camp.game;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.User;
import io.deeplay.camp.enums.GameStatus;

public interface ReversiListener {
    boolean moveMade(User user, int currentPlayerId, BoardService boardLogic, String move);

    GameStatus gameFinished();
    GameStatus gameStarted();
    GameStatus gameStopped();
    GameStatus gameResumed();

    int[] scoreUpdated();
}
