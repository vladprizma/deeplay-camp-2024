package bot;

import entity.Board;
import entity.Bot;
import entity.Player;
import entity.Tile;
import enums.Color;
import enums.PlayerType;

import java.util.Map;

public class BotServices {

    public void addBot(Map<String, Player> players, String id, Color color) {
        if (!players.containsKey(id)) {
            Player player = new Bot(id, color);
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
        move = getBotMove(board, currentPlayerId);

        if (board.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId))) {
            return new int[]{move[0], move[1]};
        } else {
            System.out.println("Данных ход невозможен, попробуйте еще раз.");
            getCurrentPlayerMove(board, currentPlayerId);
        }
        return new int[]{move[0], move[1]};
    }

    public int[] getBotMove(Board board, String currentPlayerId) {
        Tile[] tiles = new Tile[64];
        short tileCount = 0;
        long blackValidMoves = board.getBlackValidMoves();
        long whiteValidMoves = board.getWhiteValidMoves();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                long validMoves = (Integer.parseInt(currentPlayerId) == 1) ? blackValidMoves : whiteValidMoves;
                long mask = 1L << (x + 8 * y);
                if ((validMoves & mask) != 0) {
                    tiles[tileCount] = new Tile(x, y);
                    tileCount++;
                }
            }
        }

        Tile selectedTile = tiles[(int) (Math.random() * tileCount)];

        return new int[]{selectedTile.getX(), selectedTile.getY()};
    }
}
