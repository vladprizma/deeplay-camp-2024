package player;

import board.BoardLogic;
import entity.Player;
import enums.Color;
import io.deeplay.camp.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Scanner;

public class PlayerService {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public void addPlayer(Map<String, Player> players, String id, Color color) {
        if (!players.containsKey(id)) {
            Player player = new Player(id, color);
            players.put(id, player);
        }
    }

    public boolean makeUserMove(String currentPlayerId, BoardLogic boardLogic) {
        logger.info("Введите ваш ход (например, 'e2' или 'f6'):");
        Scanner scanner = new Scanner(System.in);
        return makeMove(scanner.nextLine(), currentPlayerId, boardLogic);
    }

    public boolean makeMove(String userInput, String currentPlayerId, BoardLogic boardLogic) {
        int[] move = getCurrentPlayerMove(userInput, currentPlayerId, boardLogic);
        int x = move[0];
        int y = move[1];
        boardLogic.setPiece(x, y, Integer.parseInt(currentPlayerId));
        return true;
    }

    public int[] getCurrentPlayerMove(String userInput, String currentPlayerId, BoardLogic boardLogic) {
        int[] move;
        move = getUserMove(userInput);
        if (boardLogic.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId))) {
            return new int[]{move[0], move[1]};
        } else {
            logger.info("Данных ход невозможен, попробуйте еще раз.");
            getCurrentPlayerMove(userInput, currentPlayerId, boardLogic);
        }
        return new int[]{move[0], move[1]};
    }

    public int[] getUserMove(String userInput) {
        int[] move = new int[2];

        if (userInput.length() == 2 &&
                userInput.charAt(0) >= 'a' && userInput.charAt(0) <= 'h' &&
                userInput.charAt(1) >= '1' && userInput.charAt(1) <= '8') {

            move[0] = userInput.charAt(0) - 'a';
            move[1] = Math.abs(userInput.charAt(1) - '8');
        }
        return move;
    }
}
