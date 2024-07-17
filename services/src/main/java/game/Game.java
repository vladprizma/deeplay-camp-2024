package game;

import entity.Board;
import entity.Player;
import enums.Color;
import enums.GameStatus;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private GameLogic gameLogic;

    private String currentPlayerId; // ID текущего игрока
    private Board board;
    private Map<String, Player> players = new HashMap(); // Хранение игроков по ID
    private GameStatus gameState;

    public Game() {
        board = new Board();
        gameLogic = new GameLogic();
        gameLogic.botJoin(players, "1", Color.BLACK);
        gameLogic.botJoin(players, "2", Color.WHITE);
        gameState = gameLogic.gameStarted();
        currentPlayerId = "1";
        playGame();
    }

    public void playGame() {
        while (gameState == GameStatus.IN_PROGRESS) {
            gameLogic.display(board, currentPlayerId);
            if (gameLogic.moveMade(board, players, currentPlayerId)) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                boolean isGameOver = gameLogic.checkForWin(board);
                if (isGameOver) {
                    gameLogic.display(board, currentPlayerId);
                    gameLogic.displayEndGame(board);
                    gameLogic.gameFinished();
                    break;
                }

            }
            currentPlayerId = gameLogic.playerTurn(currentPlayerId, players);
        }
    }
}
