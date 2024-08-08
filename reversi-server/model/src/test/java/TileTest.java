import io.deeplay.camp.entity.Tile;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TileTest {

    @Test
    public void testGetX() {
        Tile tile = new Tile(3, 0);
        assertEquals(3, tile.getX());
    }

    @Test
    public void testGetY() {
        Tile tile = new Tile(0, 5);
        assertEquals(5, tile.getY());
    }
}