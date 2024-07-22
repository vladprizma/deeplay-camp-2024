package entity;

public class Chip {
    private Tile position;

    public Chip(Tile position) {
        this.position = position;
    }

    public void moveTo(Tile destination) {
        this.position = destination;
    }

    public Tile getPosition() {
        return position;
    }
}