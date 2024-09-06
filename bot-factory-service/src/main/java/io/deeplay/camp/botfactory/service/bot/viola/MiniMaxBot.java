package io.deeplay.camp.botfactory.service.bot.viola;


import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotStrategy;

import java.util.ArrayList;
import java.util.List;

public class MiniMaxBot extends BotStrategy {

    private final int depth;
    private final ClassicUtilityFunction utilityFunction;
    private List<Tile> moves; // Список для хранения ходов
    private List<Integer> rounds;

    public MiniMaxBot(int id, String name, int depth) {
        super(id, name);
        this.depth = depth;
        this.utilityFunction = new ClassicUtilityFunction();
        this.moves = new ArrayList<>();
        this.rounds = new ArrayList<>();
    }

    @Override
    public Tile getMove(int currentPlayerId, BoardService boardLogic) {
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
            List<Tile> moves = new ArrayList<>();
            double score = minimax(clonedBoard, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (score > bestScore) {
                bestScore = score;
                bestMove = tile;
            }
        }
        rounds.add(boardLogic.getRound());
        moves.add(bestMove); // Добавляем ход в список
        BoardService clonedBoard = boardLogic.getCopy();
        clonedBoard.makeMove(currentPlayerId, bestMove);
        return bestMove;
    }

    private double minimax(BoardService board, int depth, boolean isMaximizing, int currentPlayerId, double alpha, double beta) {

        int opponentPlayerId = (currentPlayerId == 1) ? 2 : 1;
        // Проверяем состояние игры на каждом уровне
        if (depth == 0 || board.checkForWin().isGameFinished()) {
            double score = utilityFunction.evaluate(board, currentPlayerId);

            return score;
        }

        List<Tile> validMoves = board.getAllValidTiles(isMaximizing ? currentPlayerId : opponentPlayerId);
        if (validMoves.isEmpty()) {
            return minimax(board, depth - 1, !isMaximizing, currentPlayerId, alpha, beta); // Пропускаем ход
        }

        double bestScore = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (Tile move : validMoves) {
            BoardService clonedBoard = board.getCopy();
            clonedBoard.makeMove(isMaximizing ? currentPlayerId : opponentPlayerId, move); // Совершаем ход
            moves.add(move);
            double score = minimax(clonedBoard, depth - 1, !isMaximizing, currentPlayerId, alpha, beta);

            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }
            moves.removeLast();
            // Альфа-бета отсечение
            if (beta <= alpha) {
                break; // Выход из цикла, если отсечение произошло
            }
        }
        return bestScore;
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}