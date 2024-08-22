package io.deeplay.camp.bot.darling.botStrategy;

import io.deeplay.camp.entity.Tile;
import io.deeplay.camp.board.BoardService;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node in the move tree.
 */
public class MoveNode {
    private Tile move;
    private BoardService boardService;
    private int currentPlayerId;
    private MoveNode parent;
    private List<MoveNode> children;

    /**
     * Constructs a new MoveNode.
     *
     * @param move           The move associated with this node.
     * @param boardService   The board state associated with this node.
     * @param currentPlayerId The ID of the current player.
     */
    public MoveNode(Tile move, BoardService boardService, int currentPlayerId) {
        this.move = move;
        this.boardService = boardService;
        this.currentPlayerId = currentPlayerId;
        this.children = new ArrayList<>();
    }

    public Tile getMove() {
        return move;
    }

    public void setMove(Tile move) {
        this.move = move;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public MoveNode getParent() {
        return parent;
    }

    public void setParent(MoveNode parent) {
        this.parent = parent;
    }

    public List<MoveNode> getChildren() {
        return children;
    }

    public void addChild(MoveNode child) {
        this.children.add(child);
    }
}