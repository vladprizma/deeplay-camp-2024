package game;

import entity.Board;
import entity.Player;
import enums.GameStatus;

import java.util.Map;
import java.util.Scanner;

public class GameLogic {

    private Board board;
    private Map<String, Player> players;
    private String currentPlayerId;
    private GameStatus gameState;

    public GameLogic(Board board, Map<String, Player> players) {
        this.board = board;
        this.players = players;
        this.gameState = GameStatus.NOT_STARTED;
    }

    public boolean makeMove() {
        int[] move = getCurrentPlayerMove();
        int x = move[0];
        int y = move[1];
        board.setPiece(x, y, Integer.parseInt(currentPlayerId));
        // нужно обработать ситуацию, что по какой-то причине фишка не добавилась.
        return true;
    }

    public void setCurrentPlayer(String playerId) {
        if (players.containsKey(playerId)) {
            currentPlayerId = playerId;
        } else {
            throw new IllegalArgumentException("Player with ID " + playerId + " does not exist.");
        }
    }

    public int[] getCurrentPlayerMove() {

        int[] move = getUserMove();
        // должно работать при правильном isValidMove.
//        if (board.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId))) {
//            return new int[]{move[0], move[1]};
//        } else {
//            throw new IllegalArgumentException("Movement obstructed");
//        }
        return new int[]{move[0], move[1]};

    }

    public static int[] getUserMove() {
        int[] move = new int[2];

        System.out.println("Введите ваш ход (например, 'e2' или 'f6'):");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        if (userInput.length() == 2 &&
                userInput.charAt(0) >= 'a' && userInput.charAt(0) <= 'h' &&
                userInput.charAt(1) >= '1' && userInput.charAt(1) <= '8') {

            // координаты в индексы массива
            move[0] = userInput.charAt(0) - 'a';
            move[1] = Math.abs(userInput.charAt(1) - '8');
        } else {
            System.out.println("Неверный ход. Попробуйте ещё раз.");
            move = getUserMove(); // Рекурсивно запрашиваем ход заново
        }
        return move;
    }

    public void displayBoard() {
        System.out.printf((board.getBoardState(Integer.parseInt(currentPlayerId))).toString());
    }

    public boolean checkForWin() {
        return false;
    }

    public void updateScores() {
    }

    public void playerTurn() {
        if (currentPlayerId == "1") {
            setCurrentPlayer("2");
        } else {
            setCurrentPlayer("1");
        }
    }

    public GameStatus getGameState() {
        return gameState;
    }

    public void setGameState(GameStatus gameState) {
        this.gameState = gameState;
    }
}
