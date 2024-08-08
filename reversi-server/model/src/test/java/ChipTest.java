import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.deeplay.camp.entity.Chip;
import io.deeplay.camp.entity.Tile;

public class ChipTest {

    private Chip chip;
    private Tile startingPosition;
    private Tile destination;

    @Test
    public void testGetPosition() {
        startingPosition = new Tile(1, 1);
        chip = new Chip(startingPosition);
        assertEquals(startingPosition, chip.getPosition());
    }

    @Test
    public void testMoveTo() {
        startingPosition = new Tile(1, 1);
        chip = new Chip(startingPosition);
        destination = new Tile(2, 2);
        chip.moveTo(destination);
        assertEquals(destination, chip.getPosition());
    }
}