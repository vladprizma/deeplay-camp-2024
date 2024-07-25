package repository;

import board.BoardLogic;
import entity.User;
import enums.Color;
import enums.GameStatus;

import java.util.Map;

public interface ReversiListener {
    boolean moveMade(User user, int currentPlayerId, BoardLogic boardLogic);

    GameStatus gameFinished();
    GameStatus gameStarted();
    GameStatus gameStopped();
    GameStatus gameResumed();

    int[] scoreUpdated();
}
