package io.deeplay.camp.botfactory.service.bot.viola;

import io.deeplay.camp.botfactory.model.Tile;
import io.deeplay.camp.botfactory.service.board.BoardService;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private Tile move; // Ход
    private int player; // Игрок, который сделал ход
    private BoardService boardService; // Текущая доска
    private Move parent; // Родительский узел
    private List<Move> children; // Дочерние узлы
    private int wins; // Количество побед
    private int visits; // Количество посещений

    public Move(Tile move, int player, BoardService boardService, Move parent) {
        this.move = move;
        this.player = player;
        this.boardService = boardService;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.wins = 0; // Изначально количество побед 0
        this.visits = 0; // Изначально количество посещений 0
    }

    public Tile getMove() {
        return move;
    }

    public int getPlayer() {
        return player;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public Move getParent() {
        return parent;
    }

    public List<Move> getChildren() {
        return children;
    }

    public void addChild(Move child) {
        this.children.add(child);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void incrementWins() {
        this.wins++;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public void incrementVisits() {
        this.visits++;
    }
}
