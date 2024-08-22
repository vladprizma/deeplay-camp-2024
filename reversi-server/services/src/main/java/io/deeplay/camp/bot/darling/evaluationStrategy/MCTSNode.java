package io.deeplay.camp.bot.darling.evaluationStrategy;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MCTSNode {
    private final BoardService boardService;
    private final int currentPlayerId;
    private final MCTSNode parent;
    private final List<MCTSNode> children;
    private int visitCount;
    private double winScore;

    public MCTSNode(BoardService boardService, int currentPlayerId, MCTSNode parent) {
        this.boardService = boardService.getBoardServiceCopy();
        this.currentPlayerId = currentPlayerId;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.visitCount = 0;
        this.winScore = 0.0;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public MCTSNode getParent() {
        return parent;
    }

    public List<MCTSNode> getChildren() {
        return children;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public boolean isTerminal() {
        return boardService.isGameOver();
    }

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

    public MCTSNode getRandomChild() {
        if (children.isEmpty()) {
            throw new IllegalStateException("No children available to select from.");
        }
        Random random = new Random();
        return children.get(random.nextInt(children.size()));
    }

    public double getUCTValue(double explorationParameter) {
        if (visitCount == 0) {
            return Double.POSITIVE_INFINITY;
        }

        return (winScore / visitCount) + explorationParameter * Math.sqrt(Math.log(parent.visitCount) / visitCount);
    }

    public void update(double reward) {
        visitCount++;
        winScore += reward;
    }

    public double getWinRate() {
        return visitCount == 0 ? 0.0 : winScore / visitCount;
    }

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