package io.deeplay.camp.entity;

/**
 * Represents the game board.
 * <p>
 * This class holds the state of the game board, including the positions of black and white chips.
 * It provides methods to get and set the positions of the chips.
 * </p>
 */
public class Board {
    private long blackChips;
    private long whiteChips;

    /**
     * Initializes a new Board with the default starting positions.
     * <p>
     * The initial positions are set with black chips at 0x0000000810000000L and white chips at 0x0000001008000000L.
     * </p>
     */
    public Board() {
        blackChips = 0x0000000810000000L;
        whiteChips = 0x0000001008000000L;
    }

    /**
     * Sets the positions of the black chips.
     *
     * @param blackChips The new positions of the black chips.
     */
    public void setBlackChips(long blackChips) {
        this.blackChips = blackChips;
    }

    /**
     * Sets the positions of the white chips.
     *
     * @param whiteChips The new positions of the white chips.
     */
    public void setWhiteChips(long whiteChips) {
        this.whiteChips = whiteChips;
    }

    /**
     * Gets the positions of the black chips.
     *
     * @return The positions of the black chips.
     */
    public long getBlackChips() {
        return blackChips;
    }

    /**
     * Gets the positions of the white chips.
     *
     * @return The positions of the white chips.
     */
    public long getWhiteChips() {
        return whiteChips;
    }
}