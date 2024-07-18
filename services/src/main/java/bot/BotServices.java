package bot;

import board.BoardLogic;
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

    public boolean makeMove(Board board, String currentPlayerId, BoardLogic boardLogic) {
        int[] move = getCurrentPlayerMove(board, currentPlayerId, boardLogic);
        if (move != null) {
            int x = move[0];
            int y = move[1];
            boardLogic.setPiece(x, y, Integer.parseInt(currentPlayerId));
            return true;
        } else {
            return false;
        }
    }

    public int[] getCurrentPlayerMove(Board board, String currentPlayerId, BoardLogic boardLogic) {
        int[] move;
        move = getBotMove(currentPlayerId, boardLogic);

        if (move != null) {
            if (boardLogic.isValidMove(move[0], move[1], Integer.parseInt(currentPlayerId))) {
                return new int[]{move[0], move[1]};
            } else {
                System.out.println("Данных ход невозможен, попробуйте еще раз.");
                getCurrentPlayerMove(board, currentPlayerId, boardLogic);
            }
            return new int[]{move[0], move[1]};
        } else {
            return null;
        }
    }

    public int[] getBotMove(String currentPlayerId, BoardLogic boardLogic) {
        Tile[] tiles = new Tile[64];
        short tileCount = 0;
        long blackValidMoves = boardLogic.getBlackValidMoves();
        long whiteValidMoves = boardLogic.getWhiteValidMoves();

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
        if (selectedTile != null) {
            return new int[]{selectedTile.getX(), selectedTile.getY()};
        } else {
            return null;
        }
    }
}
