package io.deeplay.camp.bot.darling.evaluationStrategy;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;

import java.util.ArrayList;
import java.util.List;

public class MonteCarloNode {
    private final BoardService boardService;
    private final int currentPlayerId;
    private final MonteCarloNode parent;
    private final List<MonteCarloNode> children;
    private final Tile move;
    private int wins;
    private int visits;

    public MonteCarloNode(BoardService boardService, int currentPlayerId, MonteCarloNode parent, Tile move) {
        this.boardService = boardService;
        this.currentPlayerId = currentPlayerId;
        this.parent = parent;
        this.move = move;
        this.children = new ArrayList<>();
        this.wins = 0;
        this.visits = 0;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public MonteCarloNode getParent() {
        return parent;
    }

    public List<MonteCarloNode> getChildren() {
        return children;
    }

    public Tile getMove() {
        return move;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        this.wins++;
    }

    public int getVisits() {
        return visits;
    }

    public void incrementVisits() {
        this.visits++;
    }

    public void addChild(MonteCarloNode child) {
        this.children.add(child);
    }
}
