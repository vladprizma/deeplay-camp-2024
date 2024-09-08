package io.deeplay.camp.botfactory.service.bot.andrey;


import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameStateNode {
    private final BoardService board;
    private final Tile move;
    private final int currentPlayer;
    private final GameStateNode parent;
    private final Map<Tile, GameStateNode> children;
    private final AtomicInteger visits;
    private final AtomicInteger wins;

    /**
     * Constructs a new {@code GameStateNode} with the specified board, move, current player,
     * and parent node. Initializes the children map to be empty and sets visit and win counts
     * to zero.
     *
     * @param board the current board state
     * @param move the move leading to this board state
     * @param currentPlayer the player whose move led to this state
     * @param parent the parent node preceding this state in the game tree
     */
    public GameStateNode(BoardService board, Tile move, int currentPlayer, GameStateNode parent) {
        this.board = board;
        this.move = move;
        this.currentPlayer = currentPlayer;
        this.parent = parent;
        this.children = new ConcurrentHashMap<>();
        this.visits = new AtomicInteger(0);
        this.wins = new AtomicInteger(0);
    }

    public BoardService getBoard() {
        return board;
    }

    public Tile getMove() {
        return move;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public GameStateNode getParent() {
        return parent;
    }

    public Collection<GameStateNode> getChildren() {
        return children.values();
    }

    public void addChild(GameStateNode child) {
        children.put(child.getMove(), child);
    }

    public void incrementVisits() {
        visits.incrementAndGet();
    }

    public void incrementWins() {
        wins.incrementAndGet();
    }

    public int getVisits() {
        return visits.get();
    }

    public int getWins() {
        return wins.get();
    }
}
