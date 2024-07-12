package entity;

import enums.Color;

public class Tile {
    private Color color;
    private int x;
    private int y;

    public Tile(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public enums.Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}