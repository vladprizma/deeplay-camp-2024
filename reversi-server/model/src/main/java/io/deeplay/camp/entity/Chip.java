package io.deeplay.camp.entity;

/**
 * Represents a chip on the game board.
 * <p>
 * This class holds the position of a chip and provides methods to move the chip and get its current position.
 * </p>
 */
public class Chip {
    private Tile position;

    /**
     * Initializes a new Chip with the specified position.
     *
     * @param position The initial position of the chip.
     */
    public Chip(Tile position) {
        this.position = position;
    }

    /**
     * Moves the chip to the specified destination.
     *
     * @param destination The new position of the chip.
     */
    public void moveTo(Tile destination) {
        this.position = destination;
    }

    /**
     * Gets the current position of the chip.
     *
     * @return The current position of the chip.
     */
    public Tile getPosition() {
        return position;
    }
}