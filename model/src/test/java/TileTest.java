import enums.Color;
import entity.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {

    @Test
    public void testGetColor() {
        Tile tile = new Tile(Color.WHITE, 0, 0);
        assertEquals(Color.WHITE, tile.getColor());
    }

    @Test
    public void testSetColor() {
        Tile tile = new Tile(Color.WHITE, 0, 0);
        tile.setColor(Color.BLACK);
        assertEquals(Color.BLACK, tile.getColor());
    }

    @Test
    public void testGetX() {
        Tile tile = new Tile(Color.YELLOW, 3, 0);
        assertEquals(3, tile.getX());
    }

    @Test
    public void testGetY() {
        Tile tile = new Tile(Color.YELLOW, 0, 5);
        assertEquals(5, tile.getY());
    }
}