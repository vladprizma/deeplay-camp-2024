package io.deeplay.camp.bot;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;

import java.util.List;

/**
 * Builds the move tree for the Minimax algorithm.
 */
public class MoveTreeBuilder {

    /**
     * Builds the move tree up to a specified depth.
     *
     * @param boardLogic      The board logic to be used for building the move tree.
     * @param currentPlayerId The ID of the current player.
     * @param depth           The depth to build the tree to.
     * @return The root node of the move tree.
     */
    public MoveNode buildMoveTree(BoardService boardLogic, int currentPlayerId, int depth) {
        MoveNode root = new MoveNode(null, boardLogic, currentPlayerId);
        buildMoveTree(root, currentPlayerId, depth, 0);
        return root;
    }

    /**
     * Recursively builds the move tree.
     *
     * @param node            The current node in the move tree.
     * @param currentPlayerId The ID of the current player.
     * @param maxDepth        The maximum depth to build the tree to.
     * @param currentDepth    The current depth in the tree.
     */
    private void buildMoveTree(MoveNode node, int currentPlayerId, int maxDepth, int currentDepth) {
        if (currentDepth >= maxDepth) {
            return;
        }

        BoardService board = node.getBoardService();
        List<Tile> validMoves = board.getAllValidTiles(currentPlayerId);
        
        if (validMoves.isEmpty()) {
            return;
        }

        for (Tile move : validMoves) {
            BoardService newBoard = board.getBoardServiceCopy();
            makeMove(newBoard, move, currentPlayerId);
            int nextPlayer = (currentPlayerId == 1) ? 2 : 1;
            MoveNode childNode = new MoveNode(move, newBoard, nextPlayer);
            childNode.setParent(node);
            node.addChild(childNode);
            buildMoveTree(childNode, nextPlayer, maxDepth, currentDepth + 1);
        }
    }

    /**
     * Makes a move on the board.
     *
     * @param board         The board to make the move on.
     * @param move          The move to make.
     * @param currentPlayer The ID of the current player.
     */
    private void makeMove(BoardService board, Tile move, int currentPlayer) {
        board.setPiece(move.getX(), move.getY(), currentPlayer);
    }
}