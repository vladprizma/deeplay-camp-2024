package io.deeplay.camp.botfactory.service.bot.darling;


// создание бота в отдельном сервисе
// микросервис для метрик (окружение)
// схемку архитектуры

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;
import io.deeplay.camp.botfactory.service.bot.BotStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a bot that uses the Minimax algorithm with alpha-beta pruning to play the game.
 */
@Component
@Qualifier("darlingBotStrategy")
public class DarlingBotStrategy extends BotStrategy {
    private final int depthTree;
    private final EvaluationStrategy evaluationStrategy;

    /**
     * Constructs a new DarlingBot.
     *
     */
    public DarlingBotStrategy() {
        super(1, "Darling");
        this.depthTree = 3;
        this.evaluationStrategy = new HeuristicEvaluatorStrategy();
    }

    /**
     * Gets the best move for the bot using iterative deepening.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardLogic      The board logic to be used for making the move.
     * @return The best move for the bot.
     */
    @Override
    public Tile getMove(int currentPlayerId, BoardService boardLogic) {
        return iterativeDeepening(boardLogic, currentPlayerId, depthTree);
    }

    @Override
    public List<Tile> getAllValidMoves(int currentPlayerId, BoardService boardLogic) {
        return List.of();
    }

    /**
     * Performs iterative deepening to find the best move.
     *
     * @param board          The current board state.
     * @param currentPlayerId The ID of the current player.
     * @param depth          The depth to search to.
     * @return The best move found.
     */
    private Tile iterativeDeepening(BoardService board, int currentPlayerId, int depth) {
        Map<Tile, Double> utilityMap = new HashMap<>();
        var validMoves = board.getAllValidTiles(currentPlayerId);

        for (Tile move : validMoves) {
            var boardServiceCopy = board.getBoardServiceCopy();
            boardServiceCopy.makeMove(currentPlayerId, move);
            double value = minimax(boardServiceCopy, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            utilityMap.put(move, value);
        }

        return utilityMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Performs the Minimax algorithm with alpha-beta pruning.
     *
     * @param depth           The depth to search to.
     * @param maximizingPlayer Whether the current player is the maximizing player.
     * @param currentPlayerId The ID of the current player.
     * @param alpha           The alpha value for alpha-beta pruning.
     * @param beta            The beta value for alpha-beta pruning.
     * @return The evaluation score of the node.
     */
    private double minimax(BoardService boardService, int depth, boolean maximizingPlayer, int currentPlayerId, double alpha, double beta) {
        if (depth == 0 || boardService.isGameOver()) {
            return evaluationStrategy.evaluate(boardService, currentPlayerId);
        }

        var nextPlayer = currentPlayerId == 1 ? 2 : 1;

        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            var validMoves = boardService.getAllValidTiles(currentPlayerId);

            for (Tile move : validMoves) {
                var boardServiceCopy = boardService.getBoardServiceCopy();
                boardServiceCopy.makeMove(currentPlayerId, move);

                double eval = minimax(boardServiceCopy, depth - 1, false, currentPlayerId, alpha, beta);

                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);

                if (beta <= alpha) {
                    break;
                }
            }

            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            var validMoves = boardService.getAllValidTiles(nextPlayer);

            for (Tile move : validMoves) {
                var boardServiceCopy = boardService.getBoardServiceCopy();
                boardServiceCopy.makeMove(nextPlayer, move);

                double eval = minimax(boardServiceCopy, depth - 1, true, currentPlayerId, alpha, beta);

                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);

                if (beta <= alpha) {
                    break;
                }
            }

            return minEval;
        }
    }


}