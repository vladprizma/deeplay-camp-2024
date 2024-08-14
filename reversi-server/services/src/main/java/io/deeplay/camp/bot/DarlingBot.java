package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a bot that uses the Minimax algorithm with alpha-beta pruning to play the game.
 */
public class DarlingBot extends BotStrategy {
    private int depthTree;
    private int depthMinimax;
    private final Map<Long, Double> transpositionTable;
    private final MoveTreeBuilder moveTreeBuilder;
    private final HeuristicEvaluator heuristicEvaluator;

    /**
     * Constructs a new DarlingBot.
     *
     * @param id           The ID of the bot.
     * @param name         The name of the bot.
     * @param depth   The depth of the move tree and min max.
     */
    public DarlingBot(int id, String name, int depth) {
        super(id, name);
        this.depthTree = depth;
        this.transpositionTable = new ConcurrentHashMap<>();
        this.moveTreeBuilder = new MoveTreeBuilder();
        this.heuristicEvaluator = new HeuristicEvaluator();
    }

    /**
     * Gets the best move for the bot using iterative deepening.
     *
     * @param currentPlayerId The ID of the current player.
     * @param boardLogic      The board logic to be used for making the move.
     * @return The best move for the bot.
     */
    @Override
    public Tile getMakeMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        BoardService boardCopy = boardLogic.getBoardServiceCopy();
        Tile bestMove = null;

        for (int depth = 1; depth <= depthTree; depth++) {
            bestMove = iterativeDeepening(boardCopy, currentPlayerId, depth);
        }

        return bestMove;
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
        MoveNode root = moveTreeBuilder.buildMoveTree(board, currentPlayerId, depth);

        double bestValue = Double.NEGATIVE_INFINITY;
        Tile bestMove = null;

        for (MoveNode child : root.getChildren()) {
            double nodeValue = minimax(child, depth - 1, false, currentPlayerId, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (nodeValue > bestValue) {
                bestValue = nodeValue;
                bestMove = child.getMove();
            }
        }

        return bestMove;
    }

    /**
     * Performs the Minimax algorithm with alpha-beta pruning.
     *
     * @param node            The current node in the move tree.
     * @param depth           The depth to search to.
     * @param maximizingPlayer Whether the current player is the maximizing player.
     * @param currentPlayerId The ID of the current player.
     * @param alpha           The alpha value for alpha-beta pruning.
     * @param beta            The beta value for alpha-beta pruning.
     * @return The evaluation score of the node.
     */
    private double minimax(MoveNode node, int depth, boolean maximizingPlayer, int currentPlayerId, double alpha, double beta) {
        long boardHash = node.getBoardService().hashCode();
        if (transpositionTable.containsKey(boardHash)) {
            return transpositionTable.get(boardHash);
        }

        if (depth == 0 || node.getChildren().isEmpty()) {
            MoveNode parent = node.getParent();
            BoardService boardBefore = (parent != null) ? parent.getBoardService() : node.getBoardService();
            double evaluation = heuristicEvaluator.heuristic(boardBefore, node.getBoardService(), currentPlayerId);
            transpositionTable.put(boardHash, evaluation);
            return evaluation;
        }

        if (maximizingPlayer) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (MoveNode child : node.getChildren()) {
                double eval = minimax(child, depth - 1, false, currentPlayerId, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }

            transpositionTable.put(boardHash, maxEval);
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (MoveNode child : node.getChildren()) {
                double eval = minimax(child, depth - 1, true, currentPlayerId, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }

            transpositionTable.put(boardHash, minEval);
            return minEval;
        }
    }
}