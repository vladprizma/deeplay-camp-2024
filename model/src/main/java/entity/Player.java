package entity;

import enums.Color;
import enums.PlayerType;

public class Player {
    private String id;
    private Color color;
    private PlayerType isAI;

    // Конструктор для создания игрока
    public Player(String id, Color color, PlayerType isAI) {
        this.id = id;
        this.color = color;
        this.isAI = isAI;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public PlayerType isAI() {
        return isAI;
    }

    public void setAI(PlayerType isAI) {
        this.isAI = isAI;
    }
}
