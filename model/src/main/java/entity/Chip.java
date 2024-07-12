package entity;

import enums.Color;

public class Chip {
    private Tile position;

    public Chip(Color color, Tile position) {
        this.position = position;
    }

    public void moveTo(Tile destination) {
        this.position = destination;
    }

    public Color getColor() {
        return position.getColor();
    }

    public Tile getPosition() {
        return position;
    }
}