package io.deeplay.camp.botfactory.model;

import io.deeplay.camp.botfactory.service.board.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a node in the Monte Carlo Tree Search (MCTS) algorithm.
 */
public class MCTSNode {
    private final BoardService boardService;
    private final int currentPlayerId;
    private final MCTSNode parent;
    private final List<MCTSNode> children;
    private int visitCount;
    private double winScore;

    /**
     * Constructs a new MCTSNode with the specified board service, current player ID, and parent node.
     *
     * @param boardService The board service representing the current state of the game.
     * @param currentPlayerId The ID of the current player.
     * @param parent The parent node of this node.
     */
    public MCTSNode(BoardService boardService, int currentPlayerId, MCTSNode parent) {
        this.boardService = boardService.getBoardServiceCopy();
        this.currentPlayerId = currentPlayerId;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.visitCount = 0;
        this.winScore = 0.0;
    }

    /**
     * Gets the board service representing the current state of the game.
     *
     * @return The board service.
     */
    public BoardService getBoardService() {
        return boardService;
    }

    /**
     * Gets the ID of the current player.
     *
     * @return The ID of the current player.
     */
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * Gets the parent node of this node.
     *
     * @return The parent node.
     */
    public MCTSNode getParent() {
        return parent;
    }

    /**
     * Gets the list of child nodes of this node.
     *
     * @return The list of child nodes.
     */
    public List<MCTSNode> getChildren() {
        return children;
    }

    /**
     * Checks if this node is a leaf node (i.e., has no children).
     *
     * @return true if this node is a leaf node, false otherwise.
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * Checks if this node is a terminal node (i.e., the game is over).
     *
     * @return true if this node is a terminal node, false otherwise.
     */
    public boolean isTerminal() {
        return boardService.checkForWin().isGameFinished();
    }

    /**
     * Expands this node by generating all possible child nodes based on legal moves.
     */
    public void expand() {
        List<Tile> legalMoves = boardService.getAllValidTiles(currentPlayerId);
        if (legalMoves.isEmpty()) {
            return;
        }

        for (Tile move : legalMoves) {
            BoardService newBoard = boardService.getBoardServiceCopy();
            newBoard.makeMove(currentPlayerId, move);
            children.add(new MCTSNode(newBoard, 3 - currentPlayerId, this));
        }
    }

    /**
     * Gets the best child node based on the Upper Confidence Bound for Trees (UCT) value.
     *
     * @param explorationParameter The exploration parameter used in the UCT calculation.
     * @return The best child node based on the UCT value.
     */
    public MCTSNode getBestUCTChild(double explorationParameter) {
        MCTSNode bestChild = null;
        double bestUCTValue = Double.NEGATIVE_INFINITY;

        for (MCTSNode child : children) {
            double uctValue = child.getUCTValue(explorationParameter);
            if (uctValue > bestUCTValue) {
                bestUCTValue = uctValue;
                bestChild = child;
            }
        }

        return bestChild;
    }

    /**
     * Gets a random child node.
     *
     * @return A random child node.
     * @throws IllegalStateException if there are no children available.
     */
    public MCTSNode getRandomChild() {
        if (children.isEmpty()) {
            throw new IllegalStateException("No children available to select from.");
        }
        Random random = new Random();
        return children.get(random.nextInt(children.size()));
    }

    /**
     * Gets the Upper Confidence Bound for Trees (UCT) value for this node.
     *
     * @param explorationParameter The exploration parameter used in the UCT calculation.
     * @return The UCT value for this node.
     */
    public double getUCTValue(double explorationParameter) {
        if (visitCount == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return (winScore / visitCount) + explorationParameter * Math.sqrt(Math.log(parent.visitCount) / visitCount);
    }

    /**
     * Updates the visit count and win score for this node based on the given reward.
     *
     * @param reward The reward to update the win score with.
     */
    public void update(double reward) {
        visitCount++;
        winScore += reward;
    }

    /**
     * Gets the win rate for this node.
     *
     * @return The win rate for this node.
     */
    public double getWinRate() {
        return visitCount == 0 ? 0.0 : winScore / visitCount;
    }

    /**
     * Gets the best child node based on the win rate.
     *
     * @return The best child node based on the win rate, or null if there are no children.
     */
    public MCTSNode getBestChild() {
        if (children.isEmpty()) {
            return null;
        }

        MCTSNode bestChild = null;
        double bestWinRate = Double.NEGATIVE_INFINITY;

        for (MCTSNode child : children) {
            double winRate = child.getWinRate();
            if (winRate > bestWinRate) {
                bestWinRate = winRate;
                bestChild = child;
            }
        }

        return bestChild;
    }
}