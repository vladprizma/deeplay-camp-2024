import entity.Player;
import enums.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {
    @Test
    public void testPlayerConstructorAndGetters() {
        String expectedId = "1";
        Color expectedColor = Color.BLACK;

        Player player = new Player(Integer.parseInt(expectedId), expectedColor, "", "");

        assertEquals(expectedId, player.getId());
        assertEquals(expectedColor, player.getColor());
    }

    @Test
    public void testPlayerWithDifferentId() {
        String expectedId1 = "1";
        String expectedId2 = "2";
        Color expectedColor = Color.WHITE;

        Player player1 = new Player(Integer.parseInt(expectedId1), expectedColor, "", "");
        Player player2 = new Player(Integer.parseInt(expectedId2), expectedColor, "", "");

        assertEquals(expectedId1, player1.getId());
        assertEquals(expectedId2, player2.getId());
    }

    @Test
    public void testPlayerWithDifferentColor() {
        String expectedId = "1";
        Color expectedColor1 = Color.WHITE;
        Color expectedColor2 = Color.BLACK;

        Player player1 = new Player(Integer.parseInt(expectedId), expectedColor1, "", "");
        Player player2 = new Player(Integer.parseInt(expectedId), expectedColor2, "", "");

        assertEquals(expectedColor1, player1.getColor());
        assertEquals(expectedColor2, player2.getColor());
    }
}
