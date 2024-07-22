import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Chip;
import enums.Color;
import entity.Tile;

public class ChipTest {

    @Test
    public void testGetPosition() {
        Tile startingPosition = new Tile(1, 1);
        Chip chip = new Chip(startingPosition);
        assertEquals(startingPosition, chip.getPosition());
    }

    @Test
    public void testMoveTo() {
        Tile startingPosition = new Tile(1, 1);
        Chip chip = new Chip(startingPosition);
        Tile destination = new Tile(2, 2);
        chip.moveTo(destination);
        assertEquals(destination, chip.getPosition());
    }
}