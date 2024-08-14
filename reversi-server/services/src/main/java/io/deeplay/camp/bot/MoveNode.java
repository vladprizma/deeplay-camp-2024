package io.deeplay.camp.bot;

import io.deeplay.camp.board.BoardService;
import io.deeplay.camp.entity.Tile;

import java.util.ArrayList;
import java.util.List;

public class MoveNode {
    Tile move; 
    List<MoveNode> children; 
    int score; 
    BoardService boardService;
    int currentPlayerId;

    public MoveNode(Tile move, BoardService boardService, int currentPlayerId) {
        this.move = move;
        this.children = new ArrayList<>();
        this.boardService = boardService;
        this.currentPlayerId = currentPlayerId;
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

    public void addChild(MoveNode child) {
        children.add(child);
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public int getScore() {
        return score;
    }
    
    public Tile getMove() {
        return move;
    }

    public void setMove(Tile move) {
        this.move = move;
    }
    
    public List<MoveNode> getChildren() {
        return children;
    }

    public int countTerminalNodes() {
        if (children.isEmpty()) {
            return 1;
        }
        int count = 0;
        for (MoveNode child : children) {
            count += child.countTerminalNodes();
        }
        return count;
    }
}
