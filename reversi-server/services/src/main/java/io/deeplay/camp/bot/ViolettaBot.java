package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViolettaBot extends BotStrategy {

    private final int depth;
    private final RandomUtilityFunction utilityFunction;

    public ViolettaBot(int id, String name, int depth) {
        super(id, name);
        this.depth = depth;
        this.utilityFunction = new RandomUtilityFunction();
    }

    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        List<Tile> allTiles = boardLogic.getAllValidTiles(currentPlayerId);
        if (allTiles.isEmpty()) {
            return null;
        }

        Tile bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Tile tile : allTiles) {
            // Клонируем доску
            BoardService clonedBoard = boardLogic.getCopy();
            clonedBoard.makeMove(currentPlayerId, tile); // Совершаем ход

            // Оценка хода с помощью метода минимакс
            double score = minimax(clonedBoard, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (score > bestScore) {
                bestScore = score;
                bestMove = tile;
            }
        }

        return bestMove;
    }

    private double minimax(BoardService board, int depth, boolean isMaximizing, int currentPlayerId, double alpha, double beta) {
        int opponentPlayerId = (currentPlayerId == 1) ? 2 : 1;

        // Проверяем состояние игры на каждом уровне
        if (depth == 0 || board.checkForWin().isGameFinished()) {
            return utilityFunction.evaluate(board, currentPlayerId);
        }

        List<Tile> validMoves = board.getAllValidTiles(isMaximizing ? currentPlayerId : opponentPlayerId);
        if (validMoves.isEmpty()) {
            return minimax(board, depth - 1, !isMaximizing, currentPlayerId, alpha, beta); // Пропускаем ход
        }

        double bestScore = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (Tile move : validMoves) {
            BoardService clonedBoard = board.getCopy();
            clonedBoard.makeMove(isMaximizing ? currentPlayerId : opponentPlayerId, move); // Совершаем ход

            double score = minimax(clonedBoard, depth - 1, !isMaximizing, currentPlayerId, alpha, beta);

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }

            // Альфа-бета отсечение
            if (beta <= alpha) {
                break; // Выход из цикла, если отсечение произошло
            }
        }

        return bestScore;
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}