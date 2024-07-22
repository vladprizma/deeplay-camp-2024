package entity;

import enums.GameStatus;

import java.util.HashMap;
import java.util.Map;

public class GameSession {
    public String currentPlayerId;
    public Board board;
    public Player player1;
    public Player player2;
    public GameStatus gameState = GameStatus.NOT_STARTED;
    public String sessionId;
    
    public int getPlayersCount() {
        var count = 0;
        
        if (player1 != null) count += 1;
        if (player2 != null) count += 1;
        
        return count;
    }
    
//    public Game() {
//        board = new Board();
//        boardLogic = new BoardLogic(board);
//        gameLogic = new GameLogic(boardLogic);
//        gameLogic.botJoin(players, "1", Color.BLACK);
//        gameLogic.botJoin(players, "2", Color.WHITE);
//        gameState = gameLogic.gameStarted();
//        currentPlayerId = "1";
//        playGame();
//    }

//    public void playGame() {
//        while (gameState == GameStatus.IN_PROGRESS) {
//            gameLogic.display(board, currentPlayerId, boardLogic);
//            if (gameLogic.moveMade(players, currentPlayerId, boardLogic)) {
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            } else {
//                gameLogic.moveSkipped(currentPlayerId);
//            }
//            boolean isGameOver = gameLogic.checkForWin(board);
//            if (isGameOver) {
//                gameLogic.display(board, currentPlayerId, boardLogic);
//                gameLogic.displayEndGame(boardLogic);
//                gameLogic.gameFinished();
//                gameState = GameStatus.FINISHED;
//                break;
//            }
//            currentPlayerId = gameLogic.playerTurn(currentPlayerId, players);
//        }
//    }

    public GameStatus getGameState() {
        return gameState;
    }
}
