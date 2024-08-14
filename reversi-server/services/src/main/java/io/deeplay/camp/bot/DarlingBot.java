package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DarlingBot extends BotStrategy {
    private int depthTree;
    private int depthMinimax;
    
    public DarlingBot(int id, String name, int depthTree, int depthMinimax) {
        super(id, name);
        this.depthTree = depthTree;
        this.depthMinimax = depthMinimax;
    }

    @Override
    public Tile getMakeMove(int currentPlayerId, @NotNull BoardService boardLogic) {
        MoveNode root = buildMoveTree(boardLogic, currentPlayerId, depthTree);
        minimax(root, depthMinimax, true, Integer.MIN_VALUE, Integer.MAX_VALUE);

        Tile bestMove = null;
        int bestScore = -1;

        for (MoveNode child : root.getChildren()) {
            if (child.getScore() > bestScore) {
                bestScore = child.getScore();
                bestMove = child.getMove();
            }
        }

        return bestMove;
    }

    @Override
    List<Tile> getAllValidMoves(int currentPlayerId, @NotNull BoardService boardLogic) {
        return boardLogic.getAllValidTiles(currentPlayerId);
    }

    private MoveNode buildMoveTree(BoardService boardLogic, int currentPlayerId, int depth) {
        if (depth == 0 || boardLogic.checkForWin().isGameFinished()) {
            int score = boardLogic.getChips(currentPlayerId).size();
            MoveNode leaf = new MoveNode(null, boardLogic, currentPlayerId);
            leaf.setScore(score);
            return leaf;
        }

        List<Tile> validMoves = boardLogic.getAllValidTiles(currentPlayerId);
        MoveNode root = new MoveNode(null, boardLogic, currentPlayerId);

        for (Tile move : validMoves) {
            BoardService boardCopy = boardLogic.getBoardServiceCopy();
            boardCopy.setPiece(move.getX(), move.getY(), currentPlayerId);

            MoveNode childNode = buildMoveTree(boardCopy, 3 - currentPlayerId, depth - 1);
            childNode.setMove(move);
            childNode.setBoardService(boardCopy);
            root.addChild(childNode);
        }

        return root;
    }

    private int minimax(MoveNode node, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        if (depth == 0 || node.getChildren().isEmpty()) {
            return node.getScore();
        }

        if (isMaximizingPlayer) {
            int maxEval = -1;
            for (MoveNode child : node.getChildren()) {
                int eval = minimax(child, depth - 1, false, alpha, beta);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            node.setScore(maxEval);
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (MoveNode child : node.getChildren()) {
                int eval = minimax(child, depth - 1, true, alpha, beta);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            node.setScore(minEval);
            return minEval;
        }
    }
}
