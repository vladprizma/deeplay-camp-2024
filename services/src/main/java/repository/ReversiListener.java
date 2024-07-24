package repository;

import board.BoardLogic;
import entity.Board;
import entity.Player;
import enums.Color;
import enums.GameStatus;

import java.util.Map;

public interface ReversiListener {
    boolean moveMade(Map<String, Player> players, String currentPlayerId, BoardLogic boardLogic);
    boolean moveSkipped(String playerId);

    GameStatus gameFinished();
    GameStatus gameStarted();
    GameStatus gameStopped();
    GameStatus gameResumed();

    boolean scoreUpdated();

    String playerTurn(String currentPlayerId, Map<String, Player> players);

    boolean playerJoin(Map<Integer, Player> players, String id, Color color);
    boolean botJoin(Map<String, Player> players, String id, Color color);
    boolean playerLeave(long playerId);
    boolean playerDisconnect(long playerId);

}
