package player;

import entity.Board;
import entity.Player;
import enums.Color;
import enums.PlayerType;

import java.util.Map;
import java.util.Scanner;

public class PlayerService {

    public void addPlayer(Map<String, Player> players, String id, Color color) {
        if (!players.containsKey(id)) {
            Player player = new Player(id, color);
            players.put(id, player);
        }
    }

    public boolean makeMove(Board board, String currentPlayerId) {
        int[] move = getCurrentPlayerMove(board, currentPlayerId);
        int x = move[0];
        int y = move[1];
        board.setPiece(x, y, Integer.parseInt(currentPlayerId));
        return true;
    }

    public int[] getCurrentPlayerMove(Board board, String currentPlayerId) {
        int[] move;
            move = getUserMove();
        if (board.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId))) {
            return new int[]{move[0], move[1]};
        } else {
            System.out.println("Данных ход невозможен, попробуйте еще раз.");
            getCurrentPlayerMove(board, currentPlayerId);
        }
        return new int[]{move[0], move[1]};
    }


    public int[] getUserMove() {
        int[] move = new int[2];

        System.out.println("Введите ваш ход (например, 'e2' или 'f6'):");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();

        if (userInput.length() == 2 &&
                userInput.charAt(0) >= 'a' && userInput.charAt(0) <= 'h' &&
                userInput.charAt(1) >= '1' && userInput.charAt(1) <= '8') {

            move[0] = userInput.charAt(0) - 'a';
            move[1] = Math.abs(userInput.charAt(1) - '8');
        } else {
            System.out.println("Неверный ход. Попробуйте ещё раз.");
            move = getUserMove();
        }
        return move;
    }
}
