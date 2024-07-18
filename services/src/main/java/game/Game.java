package game;

import board.BoardLogic;
import entity.Board;
import entity.Player;
import enums.Color;
import enums.GameStatus;

import java.util.HashMap;
import java.util.Map;

public class Game {

    public GameLogic gameLogic;
    public BoardLogic boardLogic;

    public String currentPlayerId;
    public Board board;
    public Map<String, Player> players = new HashMap();
    public GameStatus gameState;

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
            if (gameLogic.moveMade(players, currentPlayerId, boardLogic)) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                gameLogic.moveSkipped(currentPlayerId);
            }
            boolean isGameOver = gameLogic.checkForWin(board);
            if (isGameOver) {
                gameLogic.display(board, currentPlayerId, boardLogic);
                gameLogic.displayEndGame(boardLogic);
                gameLogic.gameFinished();
                gameState = GameStatus.FINISHED;
                break;
            }
            currentPlayerId = gameLogic.playerTurn(currentPlayerId, players);
        }
    }

    public GameStatus getGameState() {
        return gameState;
    }
}
