package game;

import entity.Board;
import entity.Player;
import entity.Tile;
import enums.GameStatus;
import enums.PlayerType;

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
        int[] move = getCurrentPlayerMove(players.get(currentPlayerId).isAI());
        int x = move[0];
        int y = move[1];
        board.setPiece(x, y, Integer.parseInt(currentPlayerId));
        return true;
    }

    public void setCurrentPlayer(String playerId) {
        if (players.containsKey(playerId)) {
            currentPlayerId = playerId;
        } else {
            throw new IllegalArgumentException("Player with ID " + playerId + " does not exist.");
        }
    }

    public int[] getCurrentPlayerMove(PlayerType isAI) {
        int[] move;
        if (isAI == PlayerType.BOT) {
            move = getBotMove();
        } else {
            move = getUserMove();
        }

        if (board.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId))) {
            return new int[]{move[0], move[1]};
        } else {
            System.out.println("Данных ход невозможен, попробуйте еще раз.");
            getCurrentPlayerMove(isAI);
        }
        return new int[]{move[0], move[1]};
    }

    // not work, dont check.
    public int[] getBotMove() {
        Tile[] tiles = new Tile[64];
        short tileCount = 0;
        long blackValidMoves = board.getBlackValidMoves();
        long whiteValidMoves = board.getWhiteValidMoves();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                long validMoves = (Integer.parseInt(currentPlayerId) == 1) ? blackValidMoves : whiteValidMoves;
                long mask = 1L << (x + 8 * y);
                if ((validMoves & mask) != 0) {
                    tiles[tileCount] = new Tile(players.get(currentPlayerId).getColor(), x, y);
                    tileCount++;
                }
            }
        }

        Tile selectedTile = tiles[(int) (Math.random() * tileCount)];

        return new int[]{selectedTile.getX(), selectedTile.getY()};
    }

    public int[] getUserMove() {
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

    public void displayScore() {
        int[] score = board.score();
        String scoreText = " Score: " + score[0] + " : " + score[1] + " ";
        int textLength = scoreText.length();
        printTopBorder(textLength);
        System.out.println("│" + scoreText + "│");
        printBottomBorder(textLength);
    }

    private static void printTopBorder(int length) {
        System.out.print("┌");
        for (int i = 0; i < length; i++) {
            System.out.print("─");
        }
        System.out.println("┐");
    }

    private static void printBottomBorder(int length) {
        System.out.print("[");
        for (int i = 0; i < length; i++) {
            System.out.print("─");
        }
        System.out.println("]");
    }

    public void displayEndGame() {
        int[] score = board.score();
        String scoreText;

        if (score[0] > score [1]) {
            scoreText = " Black wins! ";
        } else if (score[0] < score [1]) {
            scoreText = " White wins! ";
        } else {
            scoreText = " draw ";
        }

        int textLength = scoreText.length();
        printTopBorder(textLength);
        System.out.println("│" + scoreText + "│");
        printBottomBorder(textLength);
    }

    public boolean checkForWin() {
        int[] score = board.score();
        boolean endGame =  (score[0] + score[1] == 64) ? true : false;
        return endGame;
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
