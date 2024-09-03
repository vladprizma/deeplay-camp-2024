package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class MonteCarloBot extends BotStrategy {

    private final int simulations;
    private final Random random;

    public MonteCarloBot(int id, String name, int simulations) {
        super(id, name);
        this.simulations = simulations;
        this.random = new Random();
    }

    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);
        if (allTiles.isEmpty()) {
            return null;
        }

        Move root = new Move(null, currentPlayerId, boardLogic.getCopy(), null);

        for (Tile tile : allTiles) {
            BoardService clonedBoard = boardLogic.getCopy();
            clonedBoard.makeMove(currentPlayerId, tile);
            Move child = new Move(tile, currentPlayerId, clonedBoard, root);
            root.addChild(child);
            runSimulations(child, currentPlayerId);
        }

        Move bestMove = selectBestMove(root);
        return bestMove.getMove();
    }

    private void runSimulations(Move move, int currentPlayerId) {
        for (int i = 0; i < simulations; i++) {
            BoardService simulatedBoard = move.getBoardService().getCopy();
            int player = currentPlayerId;

            // Играть случайные ходы до конца игры
            while (!simulatedBoard.checkForWin().isGameFinished()) {
                List<Tile> validMoves = simulatedBoard.getAllValidTiles(player);
                if (validMoves.isEmpty()) break; // Если нет допустимых ходов, завершить

                Tile randomMove = validMoves.get(random.nextInt(validMoves.size()));
                simulatedBoard.makeMove(player, randomMove);
                player = (player == 1) ? 2 : 1; // Смена игрока
            }

            // Оценка результата
            int winnerId = simulatedBoard.checkForWin().getUserIdWinner();
            updateResults(move, winnerId, currentPlayerId);
        }
    }

    private void updateResults(Move move, int winnerId, int currentPlayerId) {
        // Обновление статистики для узла
        if (winnerId == currentPlayerId) {
            move.incrementWins(); // Увеличиваем количество побед
        }
        move.incrementVisits(); // Увеличиваем количество посещений
    }

    private Move selectBestMove(Move root) {
        Move bestMove = null;
        double bestWinRate = Double.NEGATIVE_INFINITY;

        for (Move child : root.getChildren()) {
            double winRate = (double) child.getWins() / child.getVisits();
            if (child.getVisits() > 0 && winRate > bestWinRate) {
                bestWinRate = winRate;
                bestMove = child;
            }
        }
        return bestMove;
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}