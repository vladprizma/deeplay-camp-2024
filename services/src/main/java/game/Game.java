package game;

import board.BoardLogic;
import entity.Board;
import entity.Player;
import enums.Color;
import enums.GameStatus;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private GameLogic gameLogic;
    private BoardLogic boardLogic;

    private String currentPlayerId;
    private Board board;
    private Map<String, Player> players = new HashMap();
    private GameStatus gameState;

    public Game() {
        board = new Board();
        boardLogic = new BoardLogic(board);
        gameLogic = new GameLogic(boardLogic);
        gameLogic.botJoin(players, "1", Color.BLACK);
        gameLogic.botJoin(players, "2", Color.WHITE);
        gameState = gameLogic.gameStarted();
        currentPlayerId = "1";
        playGame();
    }

    public void playGame() {
        while (gameState == GameStatus.IN_PROGRESS) {
            gameLogic.display(board, currentPlayerId, boardLogic);
            if (gameLogic.moveMade(board, players, currentPlayerId, boardLogic)) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                boolean isGameOver = gameLogic.checkForWin(board);
                if (isGameOver) {
                    gameLogic.display(board, currentPlayerId, boardLogic);
                    gameLogic.displayEndGame(board, boardLogic);
                    gameLogic.gameFinished();
                    break;
                }
            } else {
                gameLogic.moveSkipped(currentPlayerId);
            }
            currentPlayerId = gameLogic.playerTurn(currentPlayerId, players);
        }
    }
}
