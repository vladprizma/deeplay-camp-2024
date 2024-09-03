package io.deeplay.camp.botfactory.service.bot.viola;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExpectiMaxBot extends BotStrategy {

    private final int depth;
    private final ClassicUtilityFunction utilityFunction;
    private List<Tile> moves; // Список для хранения ходов
    private Random random;

    public ExpectiMaxBot(int id, String name, int depth) {
        super(id, name);
        this.depth = depth;
        this.utilityFunction = new ClassicUtilityFunction();
        this.moves = new ArrayList<>();
        this.random = new Random();
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

            // Оценка хода с помощью метода ExpectiMax
            List<Tile> moves = new ArrayList<>();
            double score = expectiMax(clonedBoard, moves, depth - 1, false, currentPlayerId);
            if (score > bestScore) {
                bestScore = score;
                bestMove = tile;
            }
        }

        moves.add(bestMove); // Добавляем ход в список
        BoardService clonedBoard = boardLogic.getCopy();
        clonedBoard.makeMove(currentPlayerId, bestMove);

        return bestMove;
    }

    private double expectiMax(BoardService board, List<Tile> moves, int depth, boolean isMaximizing, int currentPlayerId) {
        int opponentPlayerId = (currentPlayerId == 1) ? 2 : 1;

        // Проверяем состояние игры на каждом уровне
        if (depth == 0 || board.checkForWin().isGameFinished()) {
            return utilityFunction.evaluate(board, currentPlayerId);
        }

        List<Tile> validMoves = board.getAllValidTiles(isMaximizing ? currentPlayerId : opponentPlayerId);
        if (validMoves.isEmpty()) {
            return expectiMax(board, moves, depth - 1, !isMaximizing, currentPlayerId); // Пропускаем ход
        }

        if (isMaximizing) {
            double bestScore = Double.NEGATIVE_INFINITY;
            for (Tile move : validMoves) {
                BoardService clonedBoard = board.getCopy();
                clonedBoard.makeMove(currentPlayerId, move); // Совершаем ход
                moves.add(move);
                double score = expectiMax(clonedBoard, moves, depth - 1, false, currentPlayerId);
                bestScore = Math.max(bestScore, score);
                moves.remove(moves.size() - 1); // Удаляем последний ход
            }
            return bestScore;
        } else {
            double totalScore = 0;
            for (Tile move : validMoves) {
                BoardService clonedBoard = board.getCopy();
                clonedBoard.makeMove(opponentPlayerId, move); // Совершаем ход противника
                moves.add(move);
                double score = expectiMax(clonedBoard, moves, depth - 1, true, currentPlayerId);
                totalScore += score;
                moves.remove(moves.size() - 1); // Удаляем последний ход
            }
            return totalScore / validMoves.size(); // Возвращаем средний балл
        }
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }
}