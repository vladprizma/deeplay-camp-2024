package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a bot that uses the Minimax algorithm with alpha-beta pruning to play the game.
 */
public class DarlingBotStrategy extends BotStrategy {
    private int depthTree;
    private final EvaluationStrategy evaluationStrategy;

    /**
     * Constructs a new DarlingBot.
     *
     * @param id           The ID of the bot.
     * @param name         The name of the bot.
     * @param depth   The depth of the move tree and min max.
     */
    public DarlingBotStrategy(int id, String name, int depth, EvaluationStrategy evaluationStrategy) {
        super(id, name);
        this.depthTree = depth;
        this.evaluationStrategy = evaluationStrategy;
    }

    /**
     * Gets the best move for the bot using iterative deepening.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardLogic      The board logic to be used for making the move.
     * @return The best move for the bot.
     */
    @Override
    public Tile getMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        return iterativeDeepening(boardLogic, currentPlayerId, depthTree);
    }

    @Override
    List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
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
            double nodeValue = minimax(boardServiceCopy, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            utilityMap.put(move, nodeValue);
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
        if (depth == 0 || boardService.checkForWin().isGameFinished()) {
            return evaluationStrategy.evaluate(boardService, currentPlayerId);
        }
        
        //TODO перенести в board
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