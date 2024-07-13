package listener;

import enums.GameStatus;

public interface ReversiListener {
    boolean moveMade(long playerId, int x, int y);
    boolean moveSkipped(long playerId);

    GameStatus gameFinished();
    GameStatus gameStarted();
    GameStatus gameStopped();
    GameStatus gameResumed();

    boolean scoreUpdated();

    byte playerTurn();

    boolean playerJoin(long playerId);
    boolean playerLeave(long playerId);
    boolean playerDisconnect(long playerId);

}
