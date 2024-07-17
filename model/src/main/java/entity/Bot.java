package entity;

import enums.Color;

public class Bot extends Player {
    private String id;
    private Color color;

    public Bot(String id, Color color) {
        super(id, color);
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

}
