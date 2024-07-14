package entity;

import enums.PlayerType;

public class Player {
    private String id;
    private String color;
    private PlayerType isAI;

    // Конструктор для создания игрока
    public Player(String id, String color, PlayerType isAI) {
        this.id = id;
        this.color = color;
        this.isAI = isAI;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public PlayerType isAI() {
        return isAI;
    }

    public void setAI(PlayerType isAI) {
        this.isAI = isAI;
    }
}
